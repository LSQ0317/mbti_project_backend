package com.wintermist.mbti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wintermist.mbti.model.entity.Question;
import com.wintermist.mbti.service.QuestionService;
import com.wintermist.mbti.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author wintermist
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2026-03-03 15:13:31
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




