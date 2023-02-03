package com.tz.version.service.impl;

import com.tz.core.exception.ApiException;
import com.tz.version.domain.ApplicationType;
import com.tz.version.domain.param.CheckParam;
import com.tz.version.domain.po.Version;
import com.tz.version.repository.VersionRepository;
import com.tz.version.service.CheckVersion;
import com.tz.version.service.VersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService, CheckVersion {

    @Resource
    VersionRepository versionRepository;

    @Override
    public Version uploadFile(String applicationName, ApplicationType applicationType, String applicationVersion, String applicationArea, boolean applicationForce, String remark, MultipartFile file) throws ApiException {
        Version version = new Version();
        // 判断文件类型
        Set<String> set = new HashSet<>();
        set.add("exe");
        set.add("tar");
        set.add("mp4");
        //取出文件类型
        String fileType = Objects.requireNonNull(file.getResource().getFilename()).substring(file.getResource().getFilename().lastIndexOf(".") + 1);
        //文件类型
        if (!set.contains(fileType)) {
            throw new ApiException("上传的文件类型错误,只能上传exe,tar类型的文件");
        }
        // 分割版本号
        String[] versionSplit = applicationVersion.split("\\.");
        /*if (versionSplit.length == 1) {
            version.setApplicationVersion1(Integer.parseInt(applicationVersion));
        } else if (versionSplit.length == 2) {
            version.setApplicationVersion1(Integer.parseInt(versionSplit[0]))
                    .setApplicationVersion2(Integer.parseInt(versionSplit[1]));
        } else*/
        if (versionSplit.length == 3) {
            version.setApplicationVersion1(Integer.parseInt(versionSplit[0]))
                    .setApplicationVersion2(Integer.parseInt(versionSplit[1]))
                    .setApplicationVersion3(Integer.parseInt(versionSplit[2]));
        } else {
            throw new ApiException("上传文件版本号不正确");
        }
        //日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(new Date());
        // 创建保存路径
        //String savePath = System.getProperty("user.home") + File.separator + "files" + File.separator + fileType + File.separator + format;
        String savePath = "E:" + File.separator + "files" + File.separator + fileType + File.separator + format;
        // 保存文件的文件夹
        File folder = new File(savePath);
        // 判断路径是否存在,不存在则自动创建
        if (!folder.exists()) {
            boolean mkdirs = folder.mkdirs();
            if (!mkdirs) {
                log.error("文件夹创建失败:" + savePath);
            }
        }
        try {
            file.transferTo(new File(folder, Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
        String filePath = savePath + File.separator + file.getOriginalFilename();
        version.setApplicationName(applicationName)
                .setApplicationType(applicationType)
                .setApplicationArea(applicationArea)
                .setApplicationForce(applicationForce)
                .setRemark(remark)
                .setFileName(file.getOriginalFilename())
                .setFilePath(filePath)
                .setValid(true)
                .setDeleted(false);
        return versionRepository.saveAndFlush(version);
    }

    @Override
    public HttpServletResponse downLoadFileResponse(String id, HttpServletRequest request, HttpServletResponse response) throws ApiException {
        Version version = versionRepository.findById(id).orElseThrow(() -> new ApiException("文件未找到"));
        try {
            // path是指欲下载的文件的路径。
            File file = new File(version.getFilePath());
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(Files.newInputStream(Paths.get(version.getFilePath())));
            byte[] buffer = new byte[fis.available()];
            int read = fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Length", "" + file.length());
            //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(version.getFileName(),"utf-8")+"\"");//new String(version.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            String agent = request.getHeader("USER-AGENT"); //获取浏览器的信息
            if (agent != null && agent.toLowerCase().indexOf("firefox") > 0) {
                //火狐浏览器自己会对URL进行一次URL转码所以区别处理
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + new String(file.getName().getBytes("GB2312"), StandardCharsets.ISO_8859_1) + "\"");
            } else if (agent != null && agent.toLowerCase().indexOf("safari") > 0) {
                //苹果浏览器需要用ISO 而且文件名得用UTF-8
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
            } else {
                //其他的浏览器
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + java.net.URLEncoder.encode(file.getName(), "UTF-8") + "\"");
            }

            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception ex) {
            throw new ApiException(ex.getMessage());
        }
        return response;
    }

    /**
     * @param id 需要下载的文件
     * @return 文件流
     * @throws ApiException 下载文件时的异常说明
     */
    @Override
    public ResponseEntity<byte[]> downLoadFileResponseByte(String id) throws ApiException {
        Version version = versionRepository.findById(id).orElseThrow(() -> new ApiException("文件未找到"));
        File file = new File(version.getFilePath());
        if (file.exists()) {
            try {
                byte[] fileBytes = FileUtils.readFileToByteArray(file);
                if (fileBytes == null) {
                    log.error("下载文件失败");
                    throw new ApiException(500, "下载文件失败!");
                }
                log.info("-----------------------------下载成功{}", version.getFilePath());
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE));
                httpHeaders.add("Content-Disposition", "attachment; filename=\"" + new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
                httpHeaders.setContentLength(file.length());
                return new ResponseEntity<>(fileBytes, httpHeaders, HttpStatus.OK);
            } catch (Exception e) {
                log.error("-------------下载文件失败" + e);
                return null;
            }
        } else {
            log.error("-------------下载文件不存在");
            return null;
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downLoadFileResponseIsr(String id) throws ApiException {
        Version version = versionRepository.findById(id).orElseThrow(() -> new ApiException("文件未找到"));
        File file = new File(version.getFilePath());
        if (file.exists()) {
            try {
                byte[] fileBytes = FileUtils.readFileToByteArray(file);
                log.info("----------------文件下载成功");
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE));
                httpHeaders.setContentLength(file.length());
                httpHeaders.add("Content-Disposition", "attachment; filename=\"" + new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
                InputStream inputStream = new ByteArrayInputStream(fileBytes);
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
            } catch (Exception e) {
                log.error("下载文件失败" + e);
                throw new ApiException(500, "下载文件失败!");
            }
        } else {
            log.error("-------------下载文件不存在");
            return null;
        }
    }

    @Override
    public byte[] downLoadFileByte(String id) throws ApiException {
        Version version = versionRepository.findById(id).orElseThrow(() -> new ApiException("文件未找到"));
        File file = new File(version.getFilePath());
        if (file.exists()) {
            byte[] fileBytes = new byte[2048];
            try {
                if (file.exists()) {
                    fileBytes = FileUtils.readFileToByteArray(file);
                    log.info("-----------------------------下载成功{}", version.getFilePath());
                }
            } catch (Exception e) {
                log.error("-----------------文件下载失败" + e);
                throw new ApiException(500, "文件下载失败");
            }
            return fileBytes;
        } else {
            log.error("-------------下载文件不存在");
            return null;
        }
    }

    @Override
    public Version checkVersion(CheckParam checkParam) {
        List<Version> versions;
        try {
            versions = versionRepository.findAllSortByApplicationVersion(checkParam.getApplicationName(), checkParam.getApplicationType().ordinal());
        } catch (Exception e) {
            log.info("版本查询失败");
            return null;
        }
        if (CollectionUtils.isNotEmpty(versions)) {
            // 比对版本号
            String[] versionSplit = checkParam.getApplicationVersion().split("\\.");
            /*if (versionSplit.length == 1) {
                // 一位版本号,且大于现有版本
                list = versions.stream()
                        .filter(version -> version.getApplicationVersion2() == null)
                        .filter(version -> version.getApplicationVersion3() == null)
                        .filter(version -> version.getApplicationVersion1() > Integer.parseInt(checkParam.getApplicationVersion()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(list)) {
                    // 是否有强制升级版本
                    for (Version version : list) {
                        if (version.isApplicationForce()) {
                            return version.getId();
                        }
                    }
                    // 将最大版本返回
                    current.set(list.get(0));
                } else {
                    throw new ApiException("无");
                }
            } else if (versionSplit.length == 2) {
                // 一位版本号,且大于现有版本
                List<Version> part1 = versions.stream()
                        .filter(version -> version.getApplicationVersion2() != null)
                        .filter(version -> version.getApplicationVersion3() == null)
                        .filter(version -> version.getApplicationVersion1() > Integer.parseInt(versionSplit[0]))
                        .collect(Collectors.toList());
                List<Version> part2 = versions.stream()
                        .filter(version -> version.getApplicationVersion2() != null)
                        .filter(version -> version.getApplicationVersion3() == null)
                        .filter(version -> version.getApplicationVersion1() == Integer.parseInt(versionSplit[0]))
                        .filter(version -> version.getApplicationVersion2() > Integer.parseInt(versionSplit[1]))
                        .collect(Collectors.toList());
                Collection union = CollectionUtils.union(part1, part2);
                if (CollectionUtils.isNotEmpty(union)) {
                    for (Object o : union) {
                        Version v = (Version) o;
                        if (v.isApplicationForce()) {
                            return v.getId();
                        }
                    }
                    // 将最大版本返回
                    current.set((Version) union.toArray()[0]);
                } else {
                    throw new ApiException("无");
                }
            } else */
            // 暂时只考虑3位
            if (versionSplit.length == 3) {
                List<Version> tribunalList = versions.stream()
                        .filter(version -> {
                            String[] split = version.getApplicationArea().split(",");
                            int i = 0;
                            for (String s : split) {
                                if (s.contains("_") && s.contains(checkParam.getTribunalId())) {
                                    log.info("contains:" + s + " , " + checkParam.getTribunalId());
                                    i++;
                                } else if (!s.contains("_") && checkParam.getTribunalId().contains(s)) {
                                    log.info("!contains:" + s + " , " + checkParam.getTribunalId());
                                    i++;
                                }
                            }
                            return i > 0;
                        }).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(tribunalList)) {
                    log.info("未找到匹配的法庭适用范围");
                    return null;
                }
                // 过滤第一位最大元素
                List<Version> part1 = tribunalList.stream()
                        .filter(version -> version.getApplicationVersion1() > Integer.parseInt(versionSplit[0]))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(part1)) {
                    // 是否强制升级
                    for (Version v : part1) {
                        if (v.isApplicationForce()) {
                            return v;
                        }
                    }
                    // 不强制升级,将最大版本返回
                    return part1.get(0);
                }
                List<Version> part2 = tribunalList.stream()
                        .filter(version -> version.getApplicationVersion1() == Integer.parseInt(versionSplit[0]))
                        .filter(version -> version.getApplicationVersion2() > Integer.parseInt(versionSplit[1]))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(part2)) {
                    for (Version v : part2) {
                        if (v.isApplicationForce()) {
                            return v;
                        }
                    }
                    // 将最大版本返回
                    return part2.get(0);
                }
                List<Version> part3 = tribunalList.stream()
                        .filter(version -> version.getApplicationVersion1() == Integer.parseInt(versionSplit[0]))
                        .filter(version -> version.getApplicationVersion2() == Integer.parseInt(versionSplit[1]))
                        .filter(version -> version.getApplicationVersion3() > Integer.parseInt(versionSplit[2]))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(part3)) {
                    for (Version v : part3) {
                        if (v.isApplicationForce()) {
                            return v;
                        }
                    }
                    return part3.get(0);
                }
                log.info("没有新版本");
                return null;
            } else {
                log.info("版本号不正确");
                return null;
            }
        }
        log.info("没有版本发布");
        return null;
    }

}
