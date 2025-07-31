package com.unear.userservice.stamp.repository;

import com.unear.userservice.stamp.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByUser_UserIdAndEvent_UnearEventId(Long userId, Long eventId);

//    List<Stamp> findByUserIdAndEvent_UnearEventId(Long userId, Long eventId);

}