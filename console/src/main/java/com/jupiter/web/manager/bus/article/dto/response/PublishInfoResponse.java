package com.jupiter.web.manager.bus.article.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/24.
 */
@Data
public class PublishInfoResponse {
    private List<PublishInfoDetail> detail;
    private Long timestamp;

    @Data
    public static class  PublishInfoDetail{
        private String article_id;
        private Integer article_status;
        private String icon; // 头像
        private String name; // 显示名
        private String title;
        private String head_pic;
    }
}
