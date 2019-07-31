package com.jupiter.web.manager.mongo;

import com.jupiter.web.ConsoleStarter;
import com.jupiter.web.manager.common.entity.ArticleContentMongoEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhangxiqiang on 2019/7/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConsoleStarter.class})
public class MongoDaoTest {
    @Autowired
    private MongoDao mongoDao;


    @Test
    public void add() throws Exception {
        ArticleContentMongoEntity reviewMongoEntity = new ArticleContentMongoEntity();
        reviewMongoEntity.setId("5d30535746bf32a9c429999");
        reviewMongoEntity.setData("测试数据信息<card src=\"https://www.baidu.com?aaa=1111&aaa=222\"/>好吧宝贝回家<img src=\"/storage/emulated/0/XRichText/1563517161054-\"/><card src=\"https://www.baidu.com\"/><img src=\"http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg\"/><img src=\"http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg\"/><img src=\"http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg\"/>");
        BaseMongoEntity sss =  mongoDao.add(reviewMongoEntity);
        System.out.println(sss);
    }

    @Test
    public void findById() throws Exception {
    }

    @Test
    public void updateById() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void removeById() throws Exception {
        ArticleContentMongoEntity reviewMongoEntity = new ArticleContentMongoEntity();
        reviewMongoEntity.setId("5d30535746bf32a9c42281cd");
        reviewMongoEntity.setData("432451324");
        System.out.println( mongoDao.removeById(reviewMongoEntity));
    }

}