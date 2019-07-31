package com.jupiter.web.manager.bus.article.controller;

import com.jupiter.web.manager.bus.article.dto.request.ArticleInfoRequest;
import com.jupiter.web.manager.bus.article.dto.request.GoodsCardLinkRequest;
import com.tron.common.util.BeanUtils;
import com.tron.common.util.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiqiang on 2019/7/24.
 */
public class ArticleControllerTest {


    @Test
    public void update() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/update";
        ArticleInfoRequest articleInfoRequest = new ArticleInfoRequest();
//        articleInfoRequest.setArticle_id("1fc84e3739be42708e23e3305d69a113");
        articleInfoRequest.setCategory_id("13d3e05e881411e99b44080027e45041");
        articleInfoRequest.setContent("<html></html>");
        articleInfoRequest.setTitle("test title");
        articleInfoRequest.setHead_pic("http://www.test.com/1.jpg");
        articleInfoRequest.setType(1);
        Map params = BeanUtils.request2Map(articleInfoRequest);
        params.put("sid","0037807aad7b4741ba838339bfe79585");
        String s = HttpClientUtils.doPost(url, params);
        System.out.println(s);
    }

    @Test
    public void get() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/get";
        Map<String,String> params = new HashMap<>();
        params.put("article_id","84ea8e4a821b4d2f9954b7d054dbb115");
        params.put("sid","0037807aad7b4741ba838339bfe79585");

        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }

    @Test
    public void remove() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/remove";
        Map<String,String> params = new HashMap<>();
        params.put("article_id","84ea8e4a821b4d2f9954b7d054dbb115");
        params.put("sid","0037807aad7b4741ba838339bfe79585");
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }

    @Test
    public void getCategory() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/category";
        String s = HttpClientUtils.doGet(url, new HashMap<>());
        System.out.println(s);
    }

    @Test
    public void like() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/like";
        Map<String,String> params = new HashMap<>();
        params.put("article_id","84ea8e4a821b4d2f9954b7d054dbb115");
        params.put("sid","0037807aad7b4741ba838339bfe79585");
        params.put("like","1");
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }

    @Test
    public void getAll() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/get_all";
        Map<String,String> params = new HashMap<>();
        params.put("page","1");
        params.put("rows","30");
        params.put("sid","0037807aad7b4741ba838339bfe79585");
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }

    @Test
    public void getPublishInfo() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/get_publish_info";
        Map<String,String> params = new HashMap<>();
        params.put("sid","0037807aad7b4741ba838339bfe79585");
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }
    @Test
    public void getGoodsCardLink() throws Exception {
        String url = "http://localhost:8011/jupiter/api/web/article/get_goods_card_link";
        Map<String,String> params = new HashMap<>();
        params.put("url","https://www.lazada.co.id/products/apple-iphone-8-plus-i556758379-s784754774.html?spm=a2o4j.searchlist.list.1.5c3244336kCIJ5&search=1");
        String s = HttpClientUtils.doGet(url, params);
        System.out.println(s);
    }

}