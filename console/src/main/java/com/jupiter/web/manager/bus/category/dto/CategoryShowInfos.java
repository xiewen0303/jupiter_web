package com.jupiter.web.manager.bus.category.dto;

import lombok.Data;
import lombok.experimental.Builder;

import java.util.List;
import java.util.Map;

@Data
public class CategoryShowInfos{
    private List<CategoryShowInfo> data;

    @Builder
    @Data
    public final static class CategoryShowInfo{
        private String id;
        private String parentId;
        private String text;
        private Map<String,Object> state;
        private String icon;
        private List<CategoryShowInfo> children;
    }
}

