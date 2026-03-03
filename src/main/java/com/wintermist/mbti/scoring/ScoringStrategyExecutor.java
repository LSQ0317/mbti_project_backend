package com.wintermist.mbti.scoring;

import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.UserAnswer;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 评分策略执行器
 */
@Component
public interface ScoringStrategyExecutor {

    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
