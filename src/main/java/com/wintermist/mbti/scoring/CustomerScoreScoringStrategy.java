package com.wintermist.mbti.scoring;

import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.UserAnswer;

import java.util.List;

/**
 * 自定义得分系统评分
 */
public class CustomerScoreScoringStrategy implements ScoringStrategyExecutor{
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 1. 根据id 查询到题目和题目结果信息

        // 2. 根据题目结果信息得到

        return null;
    }
}
