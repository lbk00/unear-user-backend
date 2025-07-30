package com.unear.userservice.stamp.domain;

import com.unear.userservice.stamp.entity.Stamp;
import lombok.Getter;

import java.util.List;

@Getter
public class StampCollection {
    private final List<Stamp> stamps;

    public StampCollection(List<Stamp> stamps) {
        this.stamps = stamps;
    }
}