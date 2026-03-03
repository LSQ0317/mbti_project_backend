package com.wintermist.mbti.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wintermist.mbti.common.ErrorCode;
import com.wintermist.mbti.constant.CommonConstant;
import com.wintermist.mbti.exception.BusinessException;
import com.wintermist.mbti.exception.ThrowUtils;
import com.wintermist.mbti.mapper.AppMapper;
import com.wintermist.mbti.model.dto.app.AppQueryRequest;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.model.entity.User;
import com.wintermist.mbti.model.enums.AppScoringStrategyEnum;
import com.wintermist.mbti.model.enums.AppTypeEnum;
import com.wintermist.mbti.model.vo.AppVO;
import com.wintermist.mbti.model.vo.UserVO;
import com.wintermist.mbti.service.AppService;
import com.wintermist.mbti.service.UserService;
import com.wintermist.mbti.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用服务实现
 *

 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Override
    public void validApp(App app, boolean add) {
        if (app == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String appName = app.getAppName();
        String appDesc = app.getAppDesc();
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(appName, appDesc)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (appType == null || scoringStrategy == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 修改时，有值才校验
        if (StringUtils.isNotBlank(appName) && appName.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用名称过长");
        }
        if (StringUtils.isNotBlank(appDesc) && appDesc.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用描述过长");
        }
        if (appType != null) {
            AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
            if (appTypeEnum == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用类别非法");
            }
        }
        if (scoringStrategy != null) {
            AppScoringStrategyEnum scoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(scoringStrategy);
            if (scoringStrategyEnum == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "评分策略非法");
            }
        }
    }

    /**
     * 获取查询条件
     *
     * @param appQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest) {
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if (appQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String appDesc = appQueryRequest.getAppDesc();
        String appIcon = appQueryRequest.getAppIcon();
        Integer appType = appQueryRequest.getAppType();
        Integer scoringStrategy = appQueryRequest.getScoringStrategy();
        Integer reviewStatus = appQueryRequest.getReviewStatus();
        String reviewMessage = appQueryRequest.getReviewMessage();
        Long reviewerId = appQueryRequest.getReviewerId();
        Long userId = appQueryRequest.getUserId();
        Long notId = appQueryRequest.getNotId();
        String searchText = appQueryRequest.getSearchText();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("appName", searchText).or().like("appDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(appName), "appName", appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), "appDesc", appDesc);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "reviewMessage", reviewMessage);
        // 精确查询
        queryWrapper.eq(StringUtils.isNotBlank(appIcon), "appIcon", appIcon);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), "appType", appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoringStrategy", scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "reviewStatus", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewerId", reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取应用封装
     *
     * @param app
     * @param request
     * @return
     */
    @Override
    public AppVO getAppVO(App app, HttpServletRequest request) {
        // 对象转封装类
        AppVO appVO = AppVO.objToVo(app);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = app.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        appVO.setUser(userVO);
        // endregion

        return appVO;
    }

    /**
     * 分页获取应用封装
     *
     * @param appPage
     * @param request
     * @return
     */
    @Override
    public Page<AppVO> getAppVOPage(Page<App> appPage, HttpServletRequest request) {
        List<App> appList = appPage.getRecords();
        Page<AppVO> appVOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        if (CollUtil.isEmpty(appList)) {
            return appVOPage;
        }
        // 对象列表 => 封装对象列表
        List<AppVO> appVOList = appList.stream().map(app -> {
            return AppVO.objToVo(app);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = appList.stream().map(App::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        appVOList.forEach(appVO -> {
            Long userId = appVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            appVO.setUser(userService.getUserVO(user));
        });
        // endregion

        appVOPage.setRecords(appVOList);
        return appVOPage;
    }

}
