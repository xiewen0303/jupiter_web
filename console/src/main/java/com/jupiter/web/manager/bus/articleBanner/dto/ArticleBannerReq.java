package com.jupiter.web.manager.bus.articleBanner.dto;

import com.jupiter.web.manager.bus.admin.dto.BaseDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleBannerReq extends BaseDto {

    private MultipartFile bannerFile;
    private String bannerName;
    private String href;
    private String bannerId; //唯一Id
    private Integer seq; //优先级
    private String target; //优先级
}
