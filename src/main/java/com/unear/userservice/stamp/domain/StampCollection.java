package com.unear.userservice.stamp.domain;

import com.unear.userservice.stamp.dto.response.StampStatusResponseDto;
import com.unear.userservice.stamp.entity.Stamp;
import lombok.Getter;

import java.util.List;

@Getter
public class StampCollection {

    private final List<Stamp> stamps;
    private final boolean rouletteEnabled;

    public StampCollection(List<Stamp> stamps, boolean rouletteEnabled) {
        this.stamps = stamps;
        this.rouletteEnabled = rouletteEnabled;
    }

    public StampCollection(List<Stamp> stamps) {
        this(stamps, false);
    }

    public static StampCollection of(List<Stamp> stamps, EventStampPolicy policy) {
        boolean isSatisfied = policy.isSatisfiedBy(new StampCollection(stamps));
        return new StampCollection(stamps, isSatisfied);
    }


    public StampStatusResponseDto toResponseDto() {
        return StampStatusResponseDto.of(this.stamps, this.rouletteEnabled);
    }
}
