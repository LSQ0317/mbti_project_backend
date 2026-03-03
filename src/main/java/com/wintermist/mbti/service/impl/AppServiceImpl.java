package com.wintermist.mbti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wintermist.mbti.model.entity.App;
import com.wintermist.mbti.service.AppService;
import com.wintermist.mbti.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
* @author wintermist
* @description 针对表【app(应用)】的数据库操作Service实现
* @createDate 2026-03-03 15:13:31
*/
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
    implements AppService{

}




