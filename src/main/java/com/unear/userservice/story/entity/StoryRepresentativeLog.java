package com.unear.userservice.story.entity;

import com.unear.userservice.story.entity.Story;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "story_representative_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoryRepresentativeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_summary_id", nullable = false)
    private Story storySummary;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "logo_url")
    private String logoUrl;
}
