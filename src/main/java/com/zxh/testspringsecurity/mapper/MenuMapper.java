package com.zxh.testspringsecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxh.testspringsecurity.domain.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}