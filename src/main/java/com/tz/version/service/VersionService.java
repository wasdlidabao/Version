package com.tz.version.service;

import com.tz.core.exception.ApiException;
import com.tz.version.domain.ApplicationType;
import com.tz.version.domain.po.Version;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VersionService {

    /**
     * 上传版本文件和信息
     *
     * @param applicationName    项目标识
     * @param applicationType    项目类型
     * @param applicationVersion 版本号
     * @param applicationArea    适用范围
     * @param applicationForce   是否强制升级
     * @param remark             升级内容
     * @param file               上传文件
     * @return com.tz.version.domain.po.Version 版本信息
     */
    Version uploadFile(String applicationName, ApplicationType applicationType, String applicationVersion, String applicationArea, boolean applicationForce, String remark, MultipartFile file) throws ApiException;

    /**
     * 根据编号下载文件
     *
     * @param id       版本编号
     * @param request  请求
     * @param response 返回
     * @return javax.servlet.http.HttpServletResponse 返回
     */
    HttpServletResponse downLoadFileResponse(String id, HttpServletRequest request, HttpServletResponse response) throws ApiException;

    /**
     * 根据编号下载文件字节数组
     *
     * @param id 版本编号
     * @return org.springframework.http.ResponseEntity<byte [ ]> 字节
     */
    ResponseEntity<byte[]> downLoadFileResponseByte(String id) throws ApiException;

    /**
     * 根据编号下载文件流
     *
     * @param id 版本编号
     * @return org.springframework.http.ResponseEntity<org.springframework.core.io.InputStreamResource> 输入流
     */
    ResponseEntity<InputStreamResource> downLoadFileResponseIsr(String id) throws ApiException;

    /**
     * 根据编号下载文件字节数组
     *
     * @param id 版本编号
     * @return byte[] 字节
     */
    byte[] downLoadFileByte(String id) throws ApiException;

}
