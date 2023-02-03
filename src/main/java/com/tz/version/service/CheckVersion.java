package com.tz.version.service;

import com.tz.version.domain.param.CheckParam;
import com.tz.version.domain.po.Version;

public interface CheckVersion {

    /**
     * 版本验证
     *
     * @param checkParam 参数
     * @return com.tz.version.domain.po.Version 版本信息
     */
    Version checkVersion(CheckParam checkParam);

}
