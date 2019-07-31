package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Table(name = "jpt_goods_category_mapping")
public class CategoryMappingEntity extends BaseEntity<String> {

    private String code;
    private String origin;
    private String  url;
    private Integer  deleteFlag; //1:删除，0:未删除
    protected String  updateUser; //更新用户ID
    protected String  addUser ; //添加用户ID

    public String getCreateTimeFormate(){
       return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }

}
