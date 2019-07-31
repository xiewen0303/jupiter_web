package com.jupiter.web.manager.bus.admin.dto;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class BaseDto implements Serializable {

    private static final long serialVersionUID = -6671463602563775275L;

    private Long id;

    private Integer page = 1;

    private Integer rows = 8;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        //防止前端传空字符串
        if(StringUtils.isEmpty(page)){
            return ;
        }

        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    //将记录条数限制为最大100条
    public void setRows(Integer rows) {
        //防止前端传空字符串
        if(StringUtils.isEmpty(rows)){
            return ;
        }

        if(rows > 100){
            this.rows = 100;
            return ;
        }

        this.rows = rows;
    }
}
