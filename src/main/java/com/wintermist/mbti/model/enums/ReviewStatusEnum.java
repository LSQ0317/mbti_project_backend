package com.wintermist.mbti.model.enums;

import java.util.Arrays;

public enum ReviewStatusEnum {

    QUESTION_REVIEW("待审核", 0),

    QUESTION_PASS("通过", 1),

    QUESTION_REJECT("拒绝", 2);

    private final String text;

    private final int value;

    ReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    // 根据 value 获取枚举
    public static ReviewStatusEnum getEnumByValue(int value) {

        // 校验 value 是否在枚举值范围内
        if (!Arrays.stream(getValues()).anyMatch(v -> v == value)) {
            return null;
        }

        for (ReviewStatusEnum anEnum : ReviewStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    // 获取值列表
    public static int[] getValues() {
        return Arrays.stream(ReviewStatusEnum.values()).mapToInt(ReviewStatusEnum::getValue).toArray();
    }
}
