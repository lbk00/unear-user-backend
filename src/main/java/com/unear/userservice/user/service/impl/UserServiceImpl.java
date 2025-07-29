package com.unear.userservice.user.service.impl;

import com.unear.userservice.common.exception.BusinessException;
import com.unear.userservice.common.exception.ErrorCode;
import com.unear.userservice.place.entity.Place;
import com.unear.userservice.place.repository.PlaceRepository;
import com.unear.userservice.user.dto.response.*;
import com.unear.userservice.user.entity.User;
import com.unear.userservice.user.entity.UserHistory;
import com.unear.userservice.user.repository.UserHistoryRepository;
import com.unear.userservice.user.repository.UserRepository;
import com.unear.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final UserHistoryRepository userHistoryRepository;

    @Override
    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserInfoResponseDto.from(user);
    }

    @Override
    public String getMembershipBarcode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return user.getBarcodeNumber();
    }


    @Override
    public MyStatisticsSummaryResponseDto getMySummary(Long userId) {
        YearMonth thisMonth = YearMonth.now();

        List<String> last5Months = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            last5Months.add(thisMonth.minusMonths(i).toString());
        }

        List<UserHistory> last5 = userHistoryRepository.findHistoriesByUserIdAndYearMonths(userId, last5Months);

        int thisMonthTotalDiscount = last5.stream()
                .filter(h -> YearMonth.from(h.getUsedAt()).equals(thisMonth))
                .mapToInt(h -> Optional.ofNullable(h.getTotalDiscountAmount()).orElse(0))
                .sum();

        Map<YearMonth, Integer> monthToDiscountMap = last5.stream()
                .collect(Collectors.groupingBy(
                        h -> YearMonth.from(h.getUsedAt()),
                        Collectors.summingInt(h -> Optional.ofNullable(h.getTotalDiscountAmount()).orElse(0))
                ));

        List<MonthlyDiscountDto> recentDiscounts = last5Months.stream()
                .map(ymStr -> {
                    YearMonth ym = YearMonth.parse(ymStr);
                    int discount = monthToDiscountMap.getOrDefault(ym, 0);
                    return new MonthlyDiscountDto(ymStr, discount);
                })
                .toList();

        return new MyStatisticsSummaryResponseDto(thisMonthTotalDiscount, recentDiscounts);
    }



    @Override
    public Page<UserHistoryResponseDto> getUserUsageHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("usedAt").descending());
        Page<UserHistory> histories = userHistoryRepository.findByUser_UserId(userId, pageable);

        return histories.map(h -> {
            String placeName = placeRepository.findById(h.getPlaceId())
                    .map(Place::getPlaceName)
                    .orElse(null);

            return UserHistoryResponseDto.builder()
                    .placeName(placeName)
                    .usedAt(h.getUsedAt())
                    .originalAmount(h.getOriginalAmount())
                    .totalDiscountAmount(h.getTotalDiscountAmount())
                    .totalPaymentAmount(h.getTotalPaymentAmount())
                    .isCouponUsed(h.getIsCouponUsed())
                    .isMembershipUsed(h.getIsMembershipUsed())
                    .discountCode(h.getDiscountCode())
                    .membershipCode(h.getMembershipCode())
                    .placeCategory(h.getPlaceCategory())
                    .build();
        });
    }

    @Override
    public MyStatisticsDetailResponseDto getMyStatisticsDetail(Long userId, int year, int month) {
        YearMonth thisMonth = YearMonth.of(year, month);
        String thisMonthStr = thisMonth.toString();
        List<UserHistory> thisMonthHistories = userHistoryRepository.findByUserIdAndYearMonth(userId, thisMonthStr);

        int thisSpent = thisMonthHistories.stream().mapToInt(UserHistory::getTotalPaymentAmount).sum();
        int thisDiscount = thisMonthHistories.stream().mapToInt(UserHistory::getTotalDiscountAmount).sum();

        YearMonth lastMonth = thisMonth.minusMonths(1);
        String lastMonthStr = lastMonth.toString();
        List<UserHistory> lastMonthHistories = userHistoryRepository.findByUserIdAndYearMonth(userId, lastMonthStr);

        int lastSpent = lastMonthHistories.stream().mapToInt(UserHistory::getTotalPaymentAmount).sum();
        int lastDiscount = lastMonthHistories.stream().mapToInt(UserHistory::getTotalDiscountAmount).sum();

        double spentChangeRatio = (lastSpent != 0)
                ? Math.round(((thisSpent - lastSpent) * 1000.0 / lastSpent)) / 10.0
                : 0.0;

        double discountChangeRatio = (lastDiscount != 0)
                ? Math.round(((thisDiscount - lastDiscount) * 1000.0 / lastDiscount)) / 10.0
                : 0.0;

        Map<String, Integer> discountPerCategory = new HashMap<>();
        for (UserHistory history : thisMonthHistories) {
            String category = history.getPlaceCategory();
            discountPerCategory.merge(category, history.getTotalDiscountAmount(), Integer::sum);
        }

        Map<String, Double> discountCategoryRatio = new HashMap<>();
        for (Map.Entry<String, Integer> entry : discountPerCategory.entrySet()) {
            double ratio = (thisDiscount > 0)
                    ? Math.round(entry.getValue() * 1000.0 / thisDiscount) / 10.0
                    : 0.0;
            discountCategoryRatio.put(entry.getKey(), ratio);
        }

        return new MyStatisticsDetailResponseDto(
                thisSpent,
                thisDiscount,
                discountPerCategory,
                discountCategoryRatio,
                spentChangeRatio,
                discountChangeRatio
        );
    }



}
