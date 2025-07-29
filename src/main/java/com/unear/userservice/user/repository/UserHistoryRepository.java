package com.unear.userservice.user.repository;

import com.unear.userservice.user.entity.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {


    @Query(value = """
        SELECT * FROM user_histories
        WHERE user_id = :userId
        AND TO_CHAR(used_at, 'YYYY-MM') IN (:yearMonths)
        ORDER BY used_at DESC
    """, nativeQuery = true)
    List<UserHistory> findHistoriesByUserIdAndYearMonths(
            @Param("userId") Long userId,
            @Param("yearMonths") List<String> yearMonths);


    Page<UserHistory> findByUser_UserId(Long userId, Pageable pageable);

        @Query(value = """
        SELECT * FROM user_histories
        WHERE user_id = :userId
        AND TO_CHAR(used_at, 'YYYY-MM') = :yearMonth
    """, nativeQuery = true)
    List<UserHistory> findByUserIdAndYearMonth(@Param("userId") Long userId,
                                               @Param("yearMonth") String yearMonth);


}
