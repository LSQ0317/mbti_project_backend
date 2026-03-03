package com.wintermist.mbti.model.enums;

import java.util.Arrays;

public enum AppTypeEnum {

    /**
     * 得分类
     */
    SCORE_CLASSIFICATION("得分类", 0),

    /**
     * 测评类
     */
    ASSESSMENT_CLASSIFICATION("测评类", 1);

    private final String text;

    private final int value;
    
    AppTypeEnum(String text, int value) {
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
    public static AppTypeEnum getEnumByValue(int value) {

        // 校验 value 是否在枚举值范围内
        if (!Arrays.stream(getValues()).anyMatch(v -> v == value)) {
            return null;
        }

        for (AppTypeEnum anEnum : AppTypeEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    // 获取值列表
    public static int[] getValues() {
        return Arrays.stream(AppTypeEnum.values()).mapToInt(AppTypeEnum::getValue).toArray();
    }
}
