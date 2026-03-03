package com.wintermist.mbti.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wintermist.mbti.model.dto.userAnswer.UserAnswerQueryRequest;
import com.wintermist.mbti.model.entity.UserAnswer;
import com.wintermist.mbti.model.vo.UserAnswerVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户答题服务
 *

 */
public interface UserAnswerService extends IService<UserAnswer> {

    /**
     * 校验用户答案
     *
     * @param userAnswer
     * @param add
     */
    void validUserAnswer(UserAnswer userAnswer, boolean add);

    /**
     * 获取查询条件
     *
     * @param userAnswerQueryRequest
     * @return
     */
    QueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest);
    
    /**
     * 获取用户答题封装
     *
     * @param userAnswer
     * @param request
     * @return
     */
    UserAnswerVO getUserAnswerVO(UserAnswer userAnswer, HttpServletRequest request);

    /**
     * 分页获取用户答题封装
     *
     * @param userAnswerPage
     * @param request
     * @return
     */
    Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage, HttpServletRequest request);
}
