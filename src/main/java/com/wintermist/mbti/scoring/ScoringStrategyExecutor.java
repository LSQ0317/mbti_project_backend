package com.wintermist.mbti.scoring;

import com.wintermist.mbti.common.ErrorCode;
import com.wintermist.mbti.exception.BusinessException;
import com.wintermist.mbti.exception.ThrowUtils;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.UserAnswer;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评分策略执行器
 */
@Component
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 校验应用类型是否正确
        Integer appType = app.getAppType();
        if (appType == null) {
            ThrowUtils.throwIf(appType == null, ErrorCode.PARAMS_ERROR, "应用类型不能为空");
        }

        // 校验评分策略是否正确
        Integer scoringStrategy = app.getScoringStrategy();
        if (scoringStrategy == null) {
            ThrowUtils.throwIf(scoringStrategy == null, ErrorCode.PARAMS_ERROR, "评分策略不能为空");
        }

        // 根据注解获取具体的评分实现
        for (ScoringStrategy strategy : scoringStrategyList) {
            Class<?> targetClass = AopUtils.getTargetClass(strategy);
            ScoringStrategyConfig annotation = AnnotationUtils.findAnnotation(targetClass, ScoringStrategyConfig.class);
            if (annotation != null && annotation.appType().getValue() == appType && annotation.scoringStrategy().getValue() == scoringStrategy) {
                return strategy.doScore(choices, app);
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到对应的评分策略实现");
    }
}
