package com.tz.version.controller;

import com.tz.core.exception.ApiException;
import com.tz.version.domain.ApplicationType;
import com.tz.version.domain.param.CheckParam;
import com.tz.version.domain.po.Version;
import com.tz.version.service.CheckVersion;
import com.tz.version.service.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@Api(tags = "版本控制")
@RequestMapping("version")
public class VersionController {

    @Autowired
    VersionService versionService;
    @Autowired
    CheckVersion checkVersion;

    @ApiOperation(value = "验证版本号", notes = "验证版本号")
    @PostMapping("/checkVersion")
    public ResponseEntity<Version> checkVersion(@RequestBody @Valid CheckParam checkParam) {
        return ResponseEntity.ok(checkVersion.checkVersion(checkParam));
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationName", paramType = "form", value = "项目标识", required = true, dataType = "String"),
            @ApiImplicitParam(name = "applicationType", paramType = "form", value = "项目类型", required = true, dataType = "ApplicationType"),
            @ApiImplicitParam(name = "applicationVersion", paramType = "form", value = "项目版本", required = true, dataType = "String"),
            @ApiImplicitParam(name = "applicationArea", paramType = "form", value = "适用范围", dataType = "String"),
            @ApiImplicitParam(name = "applicationForce", paramType = "form", value = "是否强制升级", required = true, dataType = "boolean", defaultValue = "false"),
            @ApiImplicitParam(name = "remark", paramType = "form", value = "版本内容", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "file", value = "文件对象", required = true, dataType = "__file"),
    })
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @PostMapping("/uploadFile")
    public ResponseEntity<Version> uploadFile(@Param("applicationName") String applicationName, @Param("applicationType") ApplicationType applicationType, @Param("applicationVersion") String applicationVersion, @Param("applicationArea") String applicationArea, @Param("applicationForce") boolean applicationForce, @Param("remark") String remark, @RequestParam("file") MultipartFile file) throws ApiException {
        return ResponseEntity.ok(versionService.uploadFile(applicationName, applicationType, applicationVersion, applicationArea, applicationForce, remark, file));
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiOperation(value = "1.下载文件", notes = "下载文件")
    @GetMapping("/downLoadFileResponse")
    public HttpServletResponse downLoadFileResponse(String id, HttpServletRequest request, HttpServletResponse response) throws ApiException {
        return versionService.downLoadFileResponse(id, request, response);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiImplicitParam(paramType = "query", name = "id", value = "文件ID", required = true)
    @ApiOperation(value = "2.下载文件", notes = "下载文件")
    @GetMapping("/downLoadFileResponseByte")
    public ResponseEntity<byte[]> downLoadFileResponseByte(String id) throws ApiException {
        return versionService.downLoadFileResponseByte(id);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiImplicitParam(paramType = "query", name = "id", value = "文件ID", required = true)
    @ApiOperation(value = "3.下载文件", notes = "下载文件")
    @GetMapping("/downLoadFileResponseIsr")
    public ResponseEntity<InputStreamResource> downLoadFileResponseIsr(String id) throws ApiException {
        return versionService.downLoadFileResponseIsr(id);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @ApiImplicitParam(paramType = "query", name = "id", value = "文件ID", required = true)
    @ApiOperation(value = "4.下载文件", notes = "下载文件")
    @GetMapping("/downLoadFileByte")
    public byte[] downLoadFileByte(String id) throws ApiException {
        return versionService.downLoadFileByte(id);
    }

}
