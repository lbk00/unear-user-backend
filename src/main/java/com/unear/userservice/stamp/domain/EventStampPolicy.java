package com.unear.userservice.stamp.domain;


import com.unear.userservice.common.enums.EventType;
import com.unear.userservice.place.entity.EventPlace;

import java.util.List;

public class EventStampPolicy {

    private final List<EventPlace> eventPlaces;

    private EventStampPolicy(List<EventPlace> eventPlaces) {
        this.eventPlaces = eventPlaces;
    }

    public static EventStampPolicy of(List<EventPlace> eventPlaces) {
        return new EventStampPolicy(eventPlaces);
    }

    public boolean isSatisfiedBy(StampCollection collection) {
        long require = collection.getStamps().stream()
                .filter(s -> s.getEventPlace().getEventCode() == EventType.REQUIRE)
                .count();

        long general = collection.getStamps().stream()
                .filter(s -> s.getEventPlace().getEventCode() == EventType.GENERAL)
                .count();

        return require >= 1 && general >= 3;
    }

    public List<EventPlace> getEventPlaces() {
        return eventPlaces;
    }
}