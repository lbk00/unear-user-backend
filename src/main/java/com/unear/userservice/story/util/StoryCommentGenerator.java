package com.unear.userservice.story.util;

import java.util.List;
import java.util.Map;

public class StoryCommentGenerator {

    private static final Map<String, List<String[]>> CATEGORY_COMMENTS = Map.of(
            "FOOD", List.of(
                    new String[]{"맛있는 음식 도착 소식에,", "행복해지는 순간이 찾아왔어."},
                    new String[]{"든든하게 배를 채우고,", "에너지가 충전됐어요!"},
                    new String[]{"오늘은 무얼 먹을까 고민하다가,", "최고의 선택을 했어요!"}
            ),
            "LIFE", List.of(
                    new String[]{"바쁜 일상 속에서,", "잠시의 여유를 찾았어요."},
                    new String[]{"편리함이 가득한 하루에,", "기분 좋은 미소가 번졌어요."},
                    new String[]{"생활 속 소소한 만족에,", "마음이 따뜻해졌어요."}
            ),
            "BEAUTY", List.of(
                    new String[]{"피부에 양보하는 순간,", "자신감도 함께 빛났어요."},
                    new String[]{"아름다움에 투자한 오늘,", "기분 좋은 변화가 시작돼요."},
                    new String[]{"스스로를 가꾸는 시간,", "행복한 하루가 완성됐어요."}
            ),
            "ACTIVITY", List.of(
                    new String[]{"땀 흘린 그 순간에도,", "웃음이 떠나지 않았어요."},
                    new String[]{"몸도 마음도 가벼워지는,", "활동적인 하루였어요."},
                    new String[]{"에너지 넘치는 하루에,", "기분 좋은 피로가 남았어요."}
            ),
            "CULTURE", List.of(
                    new String[]{"문화가 주는 여유에,", "마음이 한결 넉넉해졌어요."},
                    new String[]{"새로운 경험과 함께,", "생각의 폭이 넓어졌어요."},
                    new String[]{"여유로운 오후, 문화와 함께,", "감성이 충전됐어요."}
            ),
            "BAKERY", List.of(
                    new String[]{"빵 냄새에 이끌려 들어간 그곳,", "달콤한 순간이 기다리고 있었어요."},
                    new String[]{"따끈한 빵 한 조각에,", "마음도 따뜻해졌어요."},
                    new String[]{"고소한 향기로 가득 찬 공간에서,", "작은 행복을 느꼈어요."}
            ),
            "SHOPPING", List.of(
                    new String[]{"원하던 걸 찾았을 때,", "기쁨이 두 배가 됐어요."},
                    new String[]{"쇼핑의 설렘과 함께,", "기분 좋은 하루를 보냈어요."},
                    new String[]{"나를 위한 소비에,", "하루가 더 특별해졌어요."}
            ),
            "CAFE", List.of(
                    new String[]{"따뜻한 커피 한 잔과 함께,", "여유로운 시간을 가졌어요."},
                    new String[]{"카페 창가 자리에서,", "생각 정리를 해봤어요."},
                    new String[]{"잔잔한 음악과 향긋한 커피,", "기분 좋은 힐링이 되었어요."}
            )
    );

    public static String generateRandomCommentByCategory(String categoryCode) {
        List<String[]> commentPairs = CATEGORY_COMMENTS.get(categoryCode);
        if (commentPairs == null || commentPairs.isEmpty()) {
            return "이번 달의 소비를 확인해보세요!\n마음에 드는 소비가 있었나요?";
        }
        int index = (int) (Math.random() * commentPairs.size());
        String[] selected = commentPairs.get(index);
        return selected[0] + "\n" + selected[1];
    }
}
