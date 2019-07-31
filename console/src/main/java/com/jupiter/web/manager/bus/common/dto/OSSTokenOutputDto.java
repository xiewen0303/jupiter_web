package com.jupiter.web.manager.bus.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

/**
 * @author fuyuling
 * @created 2019/7/19 12:33
 */
@Setter
public class OSSTokenOutputDto {

    public static final Integer STATUS_CODE_SUCCESS = 200; // 成功
    public static final Integer STATUS_CODE_FAIL = 500; // 失败

    private Integer StatusCode;
    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private String Expiration;

    private String ErrorCode;
    private String ErrorMessage;

    @JsonProperty(value = "StatusCode")
    public Integer getStatusCode() {
        return this.StatusCode;
    }

    @JsonProperty(value = "AccessKeyId")
    public String getAccessKeyId() {
        return this.AccessKeyId;
    }

    @JsonProperty(value = "AccessKeySecret")
    public String getAccessKeySecret() {
        return this.AccessKeySecret;
    }

    @JsonProperty(value = "SecurityToken")
    public String getSecurityToken() {
        return this.SecurityToken;
    }

    @JsonProperty(value = "Expiration")
    public String getExpiration() {
        return this.Expiration;
    }

    @JsonProperty(value = "ErrorCode")
    public String getErrorCode() {
        return this.ErrorCode;
    }

    @JsonProperty(value = "ErrorMessage")
    public String getErrorMessage() {
        return this.ErrorMessage;
    }

}
