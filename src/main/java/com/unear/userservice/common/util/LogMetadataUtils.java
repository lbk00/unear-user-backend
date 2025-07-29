package com.unear.userservice.common.util;

import com.unear.userservice.user.entity.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogMetadataUtils {

    private LogMetadataUtils() {}

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[\\p{Cntrl}]", "");
    }

    public static Map<String, Object> sanitizeMapValues(Map<String, Object> input) {
        if (input == null) return null;

        return input.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Object value = entry.getValue();
                            if (value instanceof String str) {
                                return sanitize(str);
                            }
                            return value;
                        },
                        (a, b) -> b,
                        LinkedHashMap::new
                ));
    }


    public static Map<String, Object> buildUserBaseMetadata(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("gender", user.getGender());
        map.put("ageGroup", getAgeGroup(user.getBirthdate().toLocalDate()));
        map.put("grade", user.getMembershipCode());
        return map;
    }

    public static String getAgeGroup(LocalDate birthdate) {
        if (birthdate == null) return "UNKNOWN";
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age < 10) return "0s";
        if (age < 20) return "10s";
        if (age < 30) return "20s";
        if (age < 40) return "30s";
        if (age < 50) return "40s";
        if (age < 60) return "50s";
        return "60+";
    }


}
