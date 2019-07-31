package com.jupiter.web.manager.bus.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public abstract class BaseResponse implements Serializable {

    private static final long serialVersionUID = 6962383580825250177L;

    protected String code;
    protected String msg;

}
