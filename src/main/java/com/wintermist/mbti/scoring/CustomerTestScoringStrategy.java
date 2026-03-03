package com.wintermist.mbti.scoring;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.Question;
import com.wintermist.mbti.model.entity.ScoringResult;
import com.wintermist.mbti.model.entity.UserAnswer;
import com.wintermist.mbti.service.QuestionService;
import com.wintermist.mbti.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义测评类评分
 */
public class CustomerTestScoringStrategy implements ScoringStrategyExecutor{

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appid = app.getId();

        // 1. 根据id 查询到题目和题目结果信息
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getId, appid)
        );

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appid)
        );

        // 2. 根据题目结果信息得到对应属性个数 例如 I = 10个， E = 5个


        // 3. 遍历返回值，计算评分


        // 4. 构造返回值
        return null;
    }
}
