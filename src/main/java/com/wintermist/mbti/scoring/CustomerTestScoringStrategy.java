package com.wintermist.mbti.scoring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wintermist.mbti.model.dto.question.QuestionContentDTO;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.Question;
import com.wintermist.mbti.model.entity.ScoringResult;
import com.wintermist.mbti.model.entity.UserAnswer;
import com.wintermist.mbti.service.QuestionService;
import com.wintermist.mbti.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Long appId = app.getId();

        // 1. 根据id 查询到题目和题目结果信息
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId)
        );

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
        );

        // 2. 根据题目结果信息得到对应属性个数 例如 I = 10个， E = 5个
        Map<String, Integer> optionCount = new HashMap<>();

        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);

        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionContentDTO questionContentDTO = questionContentDTOList.get(i);
            String choice = choices.get(i);
            for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                if (option.getKey().equals(choice)) {
                    String result = option.getResult();
                    if (StrUtil.isNotBlank(result)) {
                        optionCount.put(result, optionCount.getOrDefault(result, 0) + 1);
                    }
                }
            }
        }

        // 3. 遍历返回值，计算评分
        ScoringResult maxScoringResult = null;
        int maxScore = 0;

        if (!scoringResultList.isEmpty()) {
            maxScoringResult = scoringResultList.get(0);
            for (ScoringResult scoringResult : scoringResultList) {
                List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
                int score = resultProp.stream()
                        .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                        .sum();
                if (score > maxScore) {
                    maxScore = score;
                    maxScoringResult = scoringResult;
                }
            }
        }

        // 4. 构造返回值
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        if (maxScoringResult != null) {
            userAnswer.setResultId(maxScoringResult.getId());
            userAnswer.setResultName(maxScoringResult.getResultName());
            userAnswer.setResultDesc(maxScoringResult.getResultDesc());
            userAnswer.setResultPicture(maxScoringResult.getResultPicture());
            userAnswer.setResultScore(maxScore);
        }
        return userAnswer;
    }
}
