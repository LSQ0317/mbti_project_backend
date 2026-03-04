package com.wintermist.mbti.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.Question;
import com.wintermist.mbti.model.entity.ScoringResult;
import com.wintermist.mbti.model.entity.UserAnswer;
import com.wintermist.mbti.model.dto.question.QuestionContentDTO;
import com.wintermist.mbti.model.enums.AppScoringStrategyEnum;
import com.wintermist.mbti.model.enums.AppTypeEnum;
import com.wintermist.mbti.service.QuestionService;
import com.wintermist.mbti.service.ScoringResultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 自定义得分系统评分
 * 计算总得分  < 60 不及格
 * 总得分 > 60 < 80 良好
 * > 80 优秀
 */
@Service
@ScoringStrategyConfig(
        appType = AppTypeEnum.SCORE_CLASSIFICATION,
        scoringStrategy = AppScoringStrategyEnum.CUSTOM_SCORING
)
public class CustomerScoreScoringStrategy implements ScoringStrategy{

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

        // 2. 根据题目结果信息得到得分
        int totalScore = 0;
        if (question != null && question.getQuestionContent() != null) {
            List<QuestionContentDTO> questionContentDTOList =
                    JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);
            for (int i = 0; i < questionContentDTOList.size() && i < choices.size(); i++) {
                QuestionContentDTO questionContentDTO = questionContentDTOList.get(i);
                String choice = choices.get(i);
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    if (Objects.equals(option.getKey(), choice)) {
                        totalScore += option.getScore();
                        break;
                    }
                }
            }
        }

        // 3. 计算总得分，返回最高的得分
        ScoringResult maxScoringResult = null;
        int maxScoreRange = Integer.MIN_VALUE;
        for (ScoringResult scoringResult : scoringResultList) {
            Integer scoreRange = scoringResult.getResultScoreRange();
            if (scoreRange != null && totalScore >= scoreRange && scoreRange > maxScoreRange) {
                maxScoreRange = scoreRange;
                maxScoringResult = scoringResult;
            }
        }

        // 4. 构建返回参数
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultScore(totalScore);
        if (maxScoringResult != null) {
            userAnswer.setResultId(maxScoringResult.getId());
            userAnswer.setResultName(maxScoringResult.getResultName());
            userAnswer.setResultDesc(maxScoringResult.getResultDesc());
            userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        }
        return userAnswer;
    }
}
