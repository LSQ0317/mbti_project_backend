package com.wintermist.mbti.scoring;

import com.wintermist.mbti.model.enums.AppScoringStrategyEnum;
import com.wintermist.mbti.model.enums.AppTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScoringStrategyConfig {

    AppTypeEnum appType();

    AppScoringStrategyEnum scoringStrategy();

}
