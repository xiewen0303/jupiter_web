package com.jupiter.web.manager.common.base;

import lombok.Data;

/**
 * @author fuyuling
 * @created 2019/7/25 17:31
 */
@Data
public class PageInputDto {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    private Integer page;

    private Integer rows;

    public PageInputDto () {}

    public PageInputDto(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

}
