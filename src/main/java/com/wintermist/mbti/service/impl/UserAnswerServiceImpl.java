package com.wintermist.mbti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wintermist.mbti.model.entity.UserAnswer;
import com.wintermist.mbti.service.UserAnswerService;
import com.wintermist.mbti.mapper.UserAnswerMapper;
import org.springframework.stereotype.Service;

/**
* @author wintermist
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service实现
* @createDate 2026-03-03 15:13:31
*/
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
    implements UserAnswerService{

}




