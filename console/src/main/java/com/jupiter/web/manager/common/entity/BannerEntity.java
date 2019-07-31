package com.jupiter.web.manager.common.entity;

import com.tron.common.mysql.mybatis.entity.BaseEntity;
import com.jupiter.web.manager.utils.DateUtil;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Table(name = "jpt_banner")
public class BannerEntity extends BaseEntity<String> {

    private String marketChannel;  //推广市场渠道，支持通配符%(代替0或多个字符)
    private String devicePlatform; //设备平台:android/ios/wpm/pc, 支持通配符%(代替0或多个字符)
    private String productVersion; //适用的app版本号，支持通配符%(代替0或多个字符)，如：1.0.0/1.0.%
    private String addProduct; //产品，支持通配符%(代替0或多个字符)
    private String addChannel; //支持通配符%(代替0或多个字符)
    private String bannerName; //banner名称
    private String srcId; //轮播图片的id
    private String src;  // 图片地址
    private Integer seq;  // 顺序，从小大排列
    private String target;  // self:  app内部打开, blank: 浏览器打开
    private String href; //跳转地址，如果需要登录，跳转时请加sid参数
    private String type; //取值： img: 仅图片展示；href: 带超链接的图片, 超链接由字段href定义，repeatedMsg:反复显示的文本消息，oneTimeMsg:一次性消息
    private Integer needLogin; //跳转链接页是否需要登录，如果需要，建议app先引导登录
    private String location; //页面位置：支持通配符%(代替0或多个字符):start:启动页(广告);guide:引导页;homepage_top:首页顶部;home_profile:首页异型图;choose_recommend:精选推荐;mine:我的
    private Integer deleteFlag; //是否删除，1：已删除，0：未删除
    private Integer enabled; //是否可用,1:是, 0:否
    private String extension;//弹窗扩展信息
    private String description;//描述，可以是html片段
    protected String  updateUser; //更新用户ID
    protected String  addUser; //添加用户ID

    public String getCreateTimeFormate(){
       return DateUtil.formatDate(this.addTime,"yyyy-MM-dd  HH:mm:ss");
    }

    public String getLastModifyTimeFormate(){
        return DateUtil.formatDate(this.updateTime,"yyyy-MM-dd  HH:mm:ss");
    }
}
