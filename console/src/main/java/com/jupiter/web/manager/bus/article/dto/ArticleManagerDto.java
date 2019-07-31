package com.jupiter.web.manager.bus.article.dto;

import com.jupiter.web.manager.bus.admin.dto.BaseDto;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ArticleManagerDto  extends BaseDto {
    private String articleId; // 文章id，新建的文章没有这个字段
    private String title; // 文章标题，新建的文章没有这个字段
    private String categoryId;

    private Integer status;
    private String beginTimeStr;
    private String endTimeStr;
    private Long beginTime;
    private Long endTime;
    private String rejectMsg;
    private Integer isOffer; //0：表示非官方，1：官方


    public Long getBeginTime(){
        return StringUtils.isNotBlank(beginTimeStr)? DateUtil.formatTimeMillis(beginTimeStr,"yyyy-MM-dd"):null;
    }

    public Long getEndTime(){
        return StringUtils.isNotBlank(endTimeStr)? DateUtil.formatTimeMillis(endTimeStr,"yyyy-MM-dd"):null;
    }

}
