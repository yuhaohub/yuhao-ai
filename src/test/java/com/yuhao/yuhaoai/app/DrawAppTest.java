package com.yuhao.yuhaoai.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DrawAppTest {

     @Resource
     private DrawApp drawApp;


     @Test
     void testChat(){
      drawApp.doChat("我是yuhao，你好绘画小助手！","1");
      drawApp.doChat("你知道我的名字吗","1");
     }
}