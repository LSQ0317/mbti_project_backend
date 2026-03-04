package com.wintermist.mbti.scoring;

import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.UserAnswer;

import java.util.List;

public interface ScoringStrategy {

    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
