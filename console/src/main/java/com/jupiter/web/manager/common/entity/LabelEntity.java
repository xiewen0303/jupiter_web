package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.utils.DateUtil;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "label")
public class LabelEntity extends BaseEntity<String> {

    //标签名字
    private String  name;
    //标签 0:无效，1:生效，2:删除
    private Integer stauts;
    //描述
    private String describe;
    //创建时间
    private Long addTime;
    //最后修改时间
    private Long updateTime;

    @Transient
    protected String categoryName;//类目名

    public String getCreateTimeFormate(){
        return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }
}
