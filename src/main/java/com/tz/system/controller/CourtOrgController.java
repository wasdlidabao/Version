package com.tz.system.controller;

import com.tz.core.exception.ApiException;
import com.tz.system.domain.form.CourtOrgFrom;
import com.tz.system.domain.po.CourtOrg;
import com.tz.system.service.CourtOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courtOrg")
@Api(tags = "法院相关")
public class CourtOrgController {

    CourtOrgService courtOrgService;

    @Autowired
    public void setCourtOrgService(CourtOrgService courtOrgService) {
        this.courtOrgService = courtOrgService;
    }

    @ApiOperation("获取已开启智慧庭审列表")
    @GetMapping("/listByStartAits")
    public ResponseEntity<List<CourtOrg>> listByStartAits() throws ApiException {
        return ResponseEntity.ok(courtOrgService.listByStartAits());
    }

    @ApiOperation("根据法院编码更新智慧庭审开启状态")
    @PutMapping("/updateStart")
    public ResponseEntity<HttpStatus> updateStart(String courtNo, boolean startAits) throws ApiException {
        courtOrgService.updateStart(courtNo, startAits);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "新增法院", notes = "新增法院")
    @PostMapping("/save")
    public ResponseEntity<CourtOrg> save(@Validated @RequestBody CourtOrg courtOrg) {
        return ResponseEntity.ok(courtOrgService.save(courtOrg));
    }

    @ApiOperation(value = "删除法院", notes = "删除法院")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "Integer")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id) {
        courtOrgService.delete(id);
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "查询法院详情", notes = "查询法院详情")
    @ApiImplicitParam(paramType = "path", name = "id", value = "编号", required = true, dataType = "Integer")
    @GetMapping("/{id}")
    public ResponseEntity<CourtOrg> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(courtOrgService.findById(id));
    }


    @ApiOperation(value = "1. 分页查询所有法院", notes = "1. 分页查询所有法院")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query", required = true, value = "当前页"),
            @ApiImplicitParam(name = "size", paramType = "query", required = true, value = "每页数")
    })
    @PostMapping("/page")
    public ResponseEntity<Page<CourtOrg>> pageList(@Validated @RequestBody CourtOrgFrom courtOrgFrom, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(courtOrgService.pageList(courtOrgFrom, pageable));
    }


    @ApiOperation(value = "查询所有法院", notes = "查询所有法院")
    @PostMapping("/list")
    public ResponseEntity<List<CourtOrg>> list(@Validated @RequestBody CourtOrgFrom courtOrgFrom) {
        return ResponseEntity.ok(courtOrgService.list(courtOrgFrom));
    }

}