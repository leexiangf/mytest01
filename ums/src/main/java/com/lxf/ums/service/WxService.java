package com.lxf.ums.service;

import com.lxf.common.result.JsonResult;

/**
 * @ClassName : WxService
 * @Description TODO
 * @Date 2022/4/17 13:00
 * @Created lxf
 */
public interface WxService {

    JsonResult callBack(String code);
}
