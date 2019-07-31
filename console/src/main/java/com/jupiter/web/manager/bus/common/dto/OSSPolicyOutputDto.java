package com.jupiter.web.manager.bus.common.dto;

import lombok.Data;

/**
 * @author fuyuling
 * @created 2019/7/19 12:33
 */
@Data
public class OSSPolicyOutputDto {

    private String accessKeyId;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private Long expire;

}
