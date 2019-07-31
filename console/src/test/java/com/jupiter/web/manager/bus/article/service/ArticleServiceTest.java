package com.jupiter.web.manager.bus.article.service;

import com.alibaba.fastjson.JSON;
import com.jupiter.web.ConsoleStarter;
import com.jupiter.web.manager.bus.article.dto.ArticleContentDto;
import com.jupiter.web.manager.common.entity.ArticleContentMongoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Source;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConsoleStarter.class})
public class ArticleServiceTest {

    @Autowired
    private ArticleService reviewService;


    @Test
    public void save2mongo() throws Exception {
        ArticleContentDto reviewInfoDto = new ArticleContentDto();
        reviewInfoDto.setData("aaaaa");
        String str = reviewService.save2mongo(reviewInfoDto);
        System.out.println(str);
    }

    @Test
    public void getById() throws Exception {
        ArticleContentMongoEntity reviewMongoEntity = reviewService.getArticleContentById("5d38379b46bf328750db3185");
        System.out.println(JSON.toJSONString(reviewMongoEntity));
    }
//
    @Test
    public void updateById() throws Exception {
        ArticleContentMongoEntity reviewMongoEntity = new ArticleContentMongoEntity();
        reviewMongoEntity.setData("还记得技能等级对你的<card src=\"https://www.jianshu.com/p/31f163f5c9d9\"/>好吧宝贝回家<img src=\"/storage/emulated/0/XRichText/1563517161054-\"/><img src=\"http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg\"/>");
        reviewMongoEntity.setId("5d38379b46bf328750db3185");
        long count = reviewService.updateArticleContentById(reviewMongoEntity);
        System.out.println(count);

    }
//    @Test
//    public void removeById() throws Exception {
//        long count = reviewService.removeById("5d30535446bf32b154ce5437");
//        System.out.println(count);
//    }

}