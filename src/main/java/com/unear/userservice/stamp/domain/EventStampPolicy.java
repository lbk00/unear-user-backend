package com.unear.userservice.stamp.domain;


import com.unear.userservice.common.enums.EventType;

public class EventStampPolicy {

    public boolean isSatisfiedBy(StampCollection collection) {
        long require = collection.getStamps().stream()
                .filter(s -> s.getEventPlace().getEventCode() == EventType.REQUIRE)
                .count();

        long general = collection.getStamps().stream()
                .filter(s -> s.getEventPlace().getEventCode() == EventType.GENERAL)
                .count();

        return require >= 1 && general >= 3;
    }
}