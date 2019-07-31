package com.jupiter.web.manager.bus.article.dto.request;

import com.jupiter.web.manager.bus.admin.dto.BaseDto;
import lombok.Data;

/**
 * Created by zhangxiqiang on 2019/7/24.
 */
@Data
public class PublishInfoRequest extends BaseDto {
    private Long timestamp;
}
