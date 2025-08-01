package com.unear.userservice.stamp.domain;

import com.unear.userservice.stamp.dto.response.StampStatusResponseDto;
import com.unear.userservice.stamp.entity.Stamp;
import lombok.Getter;

import java.util.List;

@Getter
public class StampCollection {

    private final List<Stamp> stamps;
    private final boolean rouletteAvailable;

    public StampCollection(List<Stamp> stamps, boolean rouletteAvailable) {
        this.stamps = stamps;
        this.rouletteAvailable = rouletteAvailable;
    }

    public StampCollection(List<Stamp> stamps) {
        this(stamps, false);
    }

    public static StampCollection of(List<Stamp> stamps, EventStampPolicy policy) {
        boolean isSatisfied = policy.isSatisfiedBy(new StampCollection(stamps));
        return new StampCollection(stamps, isSatisfied);
    }


    public StampStatusResponseDto toResponseDto() {
        return StampStatusResponseDto.of(this.stamps, this.rouletteAvailable);
    }
}
