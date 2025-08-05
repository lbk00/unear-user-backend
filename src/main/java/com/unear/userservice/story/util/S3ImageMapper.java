package com.unear.userservice.story.util;

import com.unear.userservice.common.enums.PlaceCategory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

@Component
public class S3ImageMapper {

    private static final Map<PlaceCategory, Integer> categoryImageCountMap = new EnumMap<>(PlaceCategory.class);
    private static final Random random = new Random();

    static {
        categoryImageCountMap.put(PlaceCategory.FOOD, 5);    
        categoryImageCountMap.put(PlaceCategory.CAFE, 2);    
        categoryImageCountMap.put(PlaceCategory.LIFE, 1);
        categoryImageCountMap.put(PlaceCategory.BEAUTY, 1);
        categoryImageCountMap.put(PlaceCategory.ACTIVITY, 5);
        categoryImageCountMap.put(PlaceCategory.CULTURE, 2);
        categoryImageCountMap.put(PlaceCategory.BAKERY, 1);
        categoryImageCountMap.put(PlaceCategory.SHOPPING, 1);
        // 필요하면 추가
    }

    public static String getRandomBackgroundImageFileName(String categoryCode) {
        try {
            PlaceCategory category = PlaceCategory.fromCode(categoryCode); 
            int maxIndex = categoryImageCountMap.getOrDefault(category, 1);
            int randomIndex = random.nextInt(maxIndex) + 1;
            return String.format("%s%d.png", category.getCode(), randomIndex);
        } catch (IllegalArgumentException e) {
            return "BASIC.png";
        }
    }
}
