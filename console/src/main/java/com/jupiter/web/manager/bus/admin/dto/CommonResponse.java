package com.jupiter.web.manager.bus.admin.dto;



public class CommonResponse<T> extends BaseResponse {
    private static final long serialVersionUID = -3531514407985082583L;

    private T data;

    public CommonResponse() {}

    public CommonResponse(T data) {
        this.data = data;
    }

    public static <T> CommonResponse<T> getResult() {
        return new CommonResponse<T>();
    }

    public T getModule() {
        return data;
    }

    public void setModule(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommonResponse{");
        sb.append("code=").append(this.getCode());
        sb.append("msg=").append(this.getMsg());
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
