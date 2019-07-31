package com.jupiter.web.manager.common.base;

import lombok.Data;

import java.util.List;

/**
 * @author fuyuling
 * @created 2019/7/25 17:31
 */
@Data
public class PageOutputDto<E> {

    private List<E> data;
    private Integer currentPage;
    private Integer pageCount;
    private Integer pageSize;
    private Integer recordsTotal;

}
