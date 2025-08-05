package com.unear.userservice.story.service.impl;

import com.unear.userservice.common.exception.BusinessException;
import com.unear.userservice.common.exception.ErrorCode;
import com.unear.userservice.place.entity.Place;
import com.unear.userservice.place.repository.PlaceRepository;
import com.unear.userservice.story.dto.request.StoryCreateRequestDto;
import com.unear.userservice.story.dto.response.StoryDetailResponseDto;
import com.unear.userservice.story.dto.response.StoryDiagnosisResponseDto;
import com.unear.userservice.story.entity.Story;
import com.unear.userservice.story.entity.StoryRepresentativeLog;
import com.unear.userservice.story.entity.UserRecommendationProfile;
import com.unear.userservice.story.repository.StoryRepository;
import com.unear.userservice.story.repository.StoryRepresentativeLogRepository;
import com.unear.userservice.story.repository.UserRecommendationProfileRepository;
import com.unear.userservice.story.service.StoryService;
import com.unear.userservice.story.util.S3ImageMapper;
import com.unear.userservice.user.entity.UserHistory;
import com.unear.userservice.user.repository.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

import static com.unear.userservice.story.util.StoryCommentGenerator.generateRandomCommentByCategory;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UserRecommendationProfileRepository userRecommendationProfileRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final StoryRepresentativeLogRepository storyRepresentativeLogRepository;
    private final PlaceRepository placeRepository;
    private final S3ImageMapper s3ImageMapper;

    @Override
    public StoryDiagnosisResponseDto getDiagnosis(Long userId) {
        UserRecommendationProfile profile = userRecommendationProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PROFILE));

        boolean hasStory = storyRepository.existsByUserId(userId);

        return StoryDiagnosisResponseDto.builder()
                .type(profile.getPaymentTypeTag())
                .tag(profile.getHashtag())
                .hasStory(hasStory)
                .build();
    }

    @Override
    @Transactional
    public void createStory(Long userId, StoryCreateRequestDto request) {
        String targetMonth = request.targetMonth().minusMonths(1).toString(); // "2025-07"

        if (!storyRepository.findByUserIdAndTargetMonth(userId, targetMonth).isEmpty()) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_STORY);
        }

        List<UserHistory> topHistories = userHistoryRepository
                .findTop4ByUserIdAndMonthOrderByAmountDesc(userId, targetMonth);

        if (topHistories.isEmpty()) {
            createEmptyStory(userId, targetMonth);
            return;
        }

        for (UserHistory history : topHistories) {
            createStoryFromHistory(userId, targetMonth, history);
        }
    }

    private void createEmptyStory(Long userId, String targetMonth) {
        Story summary = storyRepository.save(
                Story.builder()
                        .userId(userId)
                        .targetMonth(targetMonth)
                        .imageUrl("BASIC.png")
                        .comment("이번 달은 결제 내역이 없어")
                        .build()
        );

        storyRepresentativeLogRepository.save(
                StoryRepresentativeLog.builder()
                        .storySummary(summary)
                        .date(null)
                        .storeName("결제 내역 없음")
                        .amount(0)
                        .logoUrl("default_logo.png")
                        .build()
        );
    }

    private void createStoryFromHistory(Long userId, String targetMonth, UserHistory history) {
        Place place = placeRepository.findById(history.getPlaceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PLACE));

        String categoryCode = place.getCategoryCode();
        String imageFileName = S3ImageMapper.getRandomBackgroundImageFileName(categoryCode);
        String comment = generateRandomCommentByCategory(categoryCode);

        Story summary = storyRepository.save(
                Story.builder()
                        .userId(userId)
                        .targetMonth(targetMonth)
                        .imageUrl(imageFileName)
                        .comment(comment)
                        .build()
        );

        storyRepresentativeLogRepository.save(
                StoryRepresentativeLog.builder()
                        .storySummary(summary)
                        .date(history.getPaidAt().toLocalDate())
                        .storeName(place.getPlaceName())
                        .amount(history.getTotalPaymentAmount())
                        .logoUrl(history.getPlaceCategory())
                        .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoryDetailResponseDto> getUserStories(Long userId, String targetMonth) {
        List<Story> stories = storyRepository.findByUserIdAndTargetMonth(userId, targetMonth);
        return stories.stream()
                .map(story -> storyRepresentativeLogRepository.findByStorySummary(story)
                        .map(log -> StoryDetailResponseDto.of(story, log))
                        .orElseGet(() -> StoryDetailResponseDto.ofEmpty(story)))
                .toList();
    }
}
