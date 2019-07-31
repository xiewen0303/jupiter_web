package com.jupiter.web.manager.bus.label.controller;

import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.jupiter.web.manager.bus.label.service.CategoryLabelService;
import com.jupiter.web.manager.bus.label.service.LabelService;
import com.jupiter.web.manager.common.entity.CategoryLabelEntity;
import com.jupiter.web.manager.common.entity.LabelEntity;
import com.jupiter.web.manager.bus.label.dto.LabelInfoReq;
import com.jupiter.web.manager.utils.GenIdTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("label")
public class LabelController {

    @Resource
    private LabelService labelService;

    @Resource
    private CategoryLabelService categoryLabelService;

    @RequestMapping(value = "/labels")
    public String getCategoryInfos(){
        return "label/labelList";
    }

    @ResponseBody
    @RequestMapping("/searchLabels")
    public CommonResponse<PageInfo<LabelEntity>> searchLabels(CategoryDto categoryDto) {
        CommonResponse<PageInfo<LabelEntity>> result = new CommonResponse<>();
        List<LabelEntity> list = labelService.getLabelPageList(categoryDto);
        PageInfo<LabelEntity> pageInfo = new PageInfo<>(list);

        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("查询成功");

        return result;
    }

    @RequestMapping("/addLabel")
    public String transfer() {
        return "label/addLabel";
    }


    @ResponseBody
    @RequestMapping("/submitAddLabel")
    public CommonResponse submitAddLabel(LabelInfoReq labelInfoReq) {
        CommonResponse<PageInfo<LabelEntity>> result = new CommonResponse<>();

        String categoryId = labelInfoReq.getCategoryId();
        String labelName = labelInfoReq.getName();

        long nowTime = System.currentTimeMillis();
        LabelEntity labelEntity = new LabelEntity();
        labelEntity.setName(labelName);
        labelEntity.setAddTime(nowTime);
        labelEntity.setUpdateTime(nowTime);
        labelEntity.setId(GenIdTools.getUUId());
        labelEntity.setStauts(1);

        String labelId = labelService.insertLabel(labelEntity);

        CategoryLabelEntity categoryLabelEntity = new CategoryLabelEntity();
        categoryLabelEntity.setCategoryId(categoryId);
        categoryLabelEntity.setLabelId(labelId);
        categoryLabelEntity.setId(GenIdTools.getUUId());
        categoryLabelService.insertCategoryLabel(categoryLabelEntity);

        result.setCode("200");
        result.setMsg("提交成功");
        return result;
    }

}
