package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by zhangxiqiang on 2019/7/23.
 */
@Data
@Table(name = "jpt_article_info")
public class ArticleInfoEntity extends BaseEntity<String> {

    private String categoryId;
    private String userId;
    private String headPic;
    private String title;
    @Column(name = "_status")
    private Integer status;
    private Long publishTime;
    private String contentRefId;
    private Integer isOffer;
    private Integer deleteFlag;
    @Column(name = "_order")
    private Integer order;
    private String addUser;
    private String updateUser;

    public String getCreateTimeFormate(){
        return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }

    @Transient
    private String categoryName;
    @Transient
    private String mobile;
    @Transient
    private String nickname;
}
