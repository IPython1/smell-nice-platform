package com.sky.test;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/28 18:03
 * @Description:
 **/
@SpringBootTest
public class HttpClientTest {

    //通过httpclient发送get请求
    @Test
    public void testGet() throws Exception {
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httpget对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        //执行请求 接受响应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应状态码：" + statusCode);
        //获取返回的数据
        HttpEntity entity = response.getEntity();
        String body= EntityUtils.toString(entity);
        System.out.println("服务端返回的数据：" + body);
        //关闭资源
        response.close();
        httpClient.close();

    }
    //通过httpclient发送post请求
    @Test
    public void testPost() throws Exception {
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httppost对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username","admin");
        jsonObject.addProperty("password","123456");

//        jsonObject.put("username","admin");
//        jsonObject.put("password","123456");
        StringEntity entity = new StringEntity(jsonObject.toString());
        //指定请求的编码方式
        entity.setContentEncoding("UTF-8");
        //数据格式
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        //执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应状态码：" + statusCode);
        //获取返回的数据
        HttpEntity entity1 = response.getEntity();
        String body= EntityUtils.toString(entity1);
        System.out.println("服务端返回的数据：" + body);
        //关闭资源
        response.close();
        httpClient.close();
    }
}
