package com.unear.userservice.stamp.service.impl;

import com.unear.userservice.common.exception.exception.EventNotFoundException;
import com.unear.userservice.event.entity.UnearEvent;
import com.unear.userservice.event.repository.EventRepository;
import com.unear.userservice.place.repository.EventPlaceRepository;
import com.unear.userservice.stamp.domain.EventStampPolicy;
import com.unear.userservice.stamp.domain.StampCollection;
import com.unear.userservice.stamp.dto.response.StampStatusResponseDto;
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
    public StampStatusResponseDto getMyStampsForEvent(Long userId, Long eventId) {
        UnearEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<Stamp> stamps = stampRepository.findByUser_UserIdAndEvent_UnearEventId(userId, eventId);

        EventStampPolicy policy = EventStampPolicy.of(event.getEventPlaces());
        StampCollection collection = StampCollection.of(stamps, policy);

        return collection.toResponseDto();  // 최종 응답
    }
}