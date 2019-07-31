package com.jupiter.web.manager.common.entity;

import com.jupiter.web.manager.utils.DateUtil;
import com.tron.common.mysql.mybatis.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Table(name = "jpt_goods_category")
public class CategoryEntity extends BaseEntity<String> {

    private String code;
    private String shortname;
    //类目名称
    private String  name;
    //父类 0为一级类目
    private String parentCode;
    //父类编码链，从父到己, 用/分隔
    private String codeChain;
    //顺序，从小到大
    private Integer seq;
    //顺序，从小到大
    private Integer level;
    private Integer deleteFlag; //是否删除，1：已删除，0：未删除
    private Integer enabled; //是否可用,1:是, 0:否

    protected String  updateUser; //更新用户ID
    protected String  addUser; //添加用户ID

    @Transient
    private List<String> mappings;
    @Transient
    private List<String> origin;
    @Transient
    private List<String> mappingIds;

    public String getCreateTimeFormate(){
       return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }
    @Transient
    private List<CategoryMappingEntity> categoryMappings;
}
