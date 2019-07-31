package com.jupiter.web.manager.bus.label.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用于页面录入新增的标签信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelInfoReq  implements Serializable{

    //标签名字
    private String  name;
    //TODO 备用字段 标签 0:无效，1:生效，2:删除
    private Integer stauts;
    //描述
    private String describe;
    //类目Id,表示这个标签属于哪个类目
    private String categoryId;
}
