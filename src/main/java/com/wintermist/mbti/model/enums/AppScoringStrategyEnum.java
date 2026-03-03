package com.wintermist.mbti.model.enums;

import java.util.Arrays;

public enum AppScoringStrategyEnum {

    // AI评分
    AI_SCORING("AI评分", 0),

    // 自定义评分
    CUSTOM_SCORING("自定义评分", 1);

    private final String text;

    private final int value;

    AppScoringStrategyEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    
    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    // 根据 value 获取枚举
    public static AppScoringStrategyEnum getEnumByValue(int value) {

        // 校验 value 是否在枚举值范围内
        if (!Arrays.stream(getValues()).anyMatch(v -> v == value)) {
            return null;
        }
        
        for (AppScoringStrategyEnum anEnum : AppScoringStrategyEnum.values()) {
            if (anEnum.getValue() == value) {
                return anEnum;
            }
        }
        return null;
    }

    // 获取所有 value 数组
    public static int[] getValues() {
        return Arrays.stream(values()).mapToInt(AppScoringStrategyEnum::getValue).toArray();
    }
}
