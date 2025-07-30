package com.unear.userservice.stamp.service.impl;

import com.unear.userservice.event.entity.UnearEvent;
import com.unear.userservice.event.repository.EventRepository;
import com.unear.userservice.place.entity.EventPlace;
import com.unear.userservice.place.repository.EventPlaceRepository;
import com.unear.userservice.stamp.domain.EventStampPolicy;
import com.unear.userservice.stamp.domain.StampCollection;
import com.unear.userservice.stamp.dto.response.EventStampResponseDto;
import com.unear.userservice.stamp.entity.Stamp;
import com.unear.userservice.stamp.repository.StampRepository;
import com.unear.userservice.stamp.service.StampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampServiceImpl implements StampService {

    private final EventRepository eventRepository;
    private final EventPlaceRepository eventPlaceRepository;
    private final StampRepository stampRepository;

    @Override
    public EventStampResponseDto getMyStampsForEvent(Long userId, Long eventId) {
        UnearEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트가 존재하지 않습니다"));

        List<EventPlace> eventPlaces = eventPlaceRepository.findByEvent_UnearEventId(eventId);
        List<Stamp> userStamps = stampRepository.findByUser_UserIdAndEventPlace_Event_UnearEventId(userId, eventId);

        StampCollection collection = new StampCollection(userStamps);
        EventStampPolicy policy = new EventStampPolicy();
        boolean available = policy.isSatisfiedBy(collection);

        return EventStampResponseDto.of(eventPlaces, userStamps, available);
    }
}