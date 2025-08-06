package com.unear.userservice.recommend.repository.impl;

import com.unear.userservice.place.dto.response.PlaceResponseDto;
import com.unear.userservice.recommend.dto.request.LocationBasedRecommendRequestDto;

import com.unear.userservice.recommend.dto.response.RecommendPlaceResponseDto;
import com.unear.userservice.recommend.repository.RecommendQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendQueryRepositoryImpl implements RecommendQueryRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<RecommendPlaceResponseDto> recommendPlaces(Long userId, LocationBasedRecommendRequestDto request) {
        String sql = """
            WITH target_user AS (
                SELECT user_id, payment_type_tag, user_embedding
                FROM user_recommendation_profile 
                WHERE user_id = ?1 AND user_embedding IS NOT NULL
            ),
            nearby_places AS (
                SELECT p.place_id, p.place_name, p.latitude, p.longitude, pe.embedding
                FROM places p
                JOIN place_embeddings pe ON p.place_id = pe.place_id
                WHERE pe.embedding IS NOT NULL
                  AND p.is_deleted = false
                  AND ST_DWithin(
                        CAST(ST_Point(p.longitude, p.latitude) AS geography),
                        CAST(ST_Point(?2, ?3) AS geography),
                        ?4 * 1000
                    )
            ),
            similar_persona_visits AS (
                SELECT uh.place_id,
                       SUM(CASE
                               WHEN uh.paid_at >= CURRENT_DATE - INTERVAL '1 month' THEN 3
                               WHEN uh.paid_at >= CURRENT_DATE - INTERVAL '2 months' THEN 2
                               WHEN uh.paid_at >= CURRENT_DATE - INTERVAL '3 months' THEN 1
                               ELSE 0
                           END) as recency_score
                FROM user_histories uh
                JOIN user_recommendation_profile urp ON uh.user_id = urp.user_id
                CROSS JOIN target_user tu
                WHERE urp.payment_type_tag = tu.payment_type_tag
                  AND uh.user_id != tu.user_id
                  AND uh.paid_at >= CURRENT_DATE - INTERVAL '3 months'
                GROUP BY uh.place_id
                HAVING COUNT(DISTINCT uh.user_id) >= 2
            ),
            scored_places AS (
                SELECT 
                    np.place_id,
                    np.place_name,
                    np.latitude,
                    np.longitude,
                    ROUND(
                        CAST(
                            (1 - (tu.user_embedding <=> np.embedding)) * 0.7 +
                            CASE 
                                WHEN spv.recency_score > 0 THEN
                                    LEAST(1.0, (CAST(spv.recency_score AS float) / 10.0)) * 0.3
                                ELSE 0.0
                            END
                        AS numeric), 4
                    ) AS score,
                    ST_DistanceSphere(
                        ST_Point(np.longitude, np.latitude),
                        ST_Point(?2, ?3)
                    ) AS distance
                FROM nearby_places np
                CROSS JOIN target_user tu
                LEFT JOIN similar_persona_visits spv ON np.place_id = spv.place_id
            )
            SELECT * 
            FROM scored_places
            WHERE score >= 0.2
            ORDER BY score DESC 
            LIMIT 5
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, userId);
        query.setParameter(2, request.longitude().doubleValue());
        query.setParameter(3, request.latitude().doubleValue());
        query.setParameter(4, 10.0); // 반경 (km)

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> new RecommendPlaceResponseDto(
                        ((Number) row[0]).longValue(),     // place_id
                        (String) row[1],                   // place_name
                        (BigDecimal) row[2],               // latitude
                        (BigDecimal) row[3],               // longitude
                        ((Number) row[5]).doubleValue(),   // distance
                        (BigDecimal) row[4]                // score
                ))
                .toList();
    }
}
