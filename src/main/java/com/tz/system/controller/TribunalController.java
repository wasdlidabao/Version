package com.tz.system.controller;

import com.tz.system.domain.param.TribunalParam;
import com.tz.system.domain.param.TribunalQueryParam;
import com.tz.system.domain.param.TribunalUpdateStatusParam;
import com.tz.system.domain.po.Tribunal;
import com.tz.system.service.TribunalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "法庭管理")
@RestController
@RequestMapping("/tribunal")
public class TribunalController {

    @Resource
    private TribunalService tribunalService;

    @ApiOperation(value = "分页查询法庭信息", notes = "分页查询法庭信息")
    @GetMapping("/list")
    public ResponseEntity<Page<Tribunal>> list(@Validated TribunalQueryParam param) {
        return ResponseEntity.ok(tribunalService.list(param));
    }

    @ApiOperation(value = "查询法院下所有法庭", notes = "查询法院下所有法庭")
    @GetMapping("/listByCourtNo")
    public ResponseEntity<List<Tribunal>> listByCourtNo(@ApiParam(value = "法院编号", example = "1856", required = true) @RequestParam String courtNo,
                                                        @ApiParam(value = "是否启用", example = "true", required = true) @RequestParam Boolean valid) {
        return ResponseEntity.ok(tribunalService.listByCourtNo(courtNo, valid));
    }

    @ApiOperation(value = "新增法庭", notes = "新增法庭")
    @PostMapping("/addAndUpdate")
    public ResponseEntity<Tribunal> addAndUpdate(@RequestBody @Validated TribunalParam param) {
        return ResponseEntity.ok(tribunalService.addAndUpdate(param));
    }

    @ApiOperation(value = "修改法庭禁用启用状态", notes = "修改法庭禁用启用状态")
    @PostMapping("/updateStatus")
    public ResponseEntity<HttpStatus> updateStatus(@RequestBody @Validated TribunalUpdateStatusParam param) {
        tribunalService.updateStatus(param);
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "获取法庭基本信息", notes = "获取法庭基本信息")
    @GetMapping("/find/{id}")
    public ResponseEntity<Tribunal> findById(@ApiParam(value = "法庭ID", example = "court_1856_abc_011", required = true)
                                             @PathVariable String id) {
        return ResponseEntity.ok(tribunalService.findById(id));
    }

    @ApiOperation(value = "删除法庭信息", notes = "删除法庭信息")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@ApiParam(value = "法庭ID", example = "court_1856_abc_011", required = true)
                                             @PathVariable String id) {
        tribunalService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
