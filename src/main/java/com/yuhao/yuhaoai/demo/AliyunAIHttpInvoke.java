package com.yuhao.yuhaoai.demo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.yuhao.yuhaoai.api.AliyunApiKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用hutool工具类构造Http请求调用
 */
public class AliyunAIHttpInvoke {
    public static void main(String[] args) {
        //构造请求头
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer" + AliyunApiKey.apiKey);
        headers.put("Content-Type","application/json");
        //设置请求体
        JSONObject requestBody = new JSONObject();
        //模型选择
        requestBody.put("model","qwen-plus");
        JSONObject input = new JSONObject();
        JSONObject[] messages = new JSONObject[2];
        //ai角色
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messages[0] = systemMessage;
        //用户信息
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "你是谁？");
        messages[1] = userMessage;

        input.put("messages", messages);
        requestBody.put("input", input);

        //发送请求
        String requestUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
        HttpResponse response = HttpRequest.post(requestUrl)
                .addHeaders(headers)
                .body(requestBody.toString())
                .execute();
        // 处理响应
        if (response.isOk()) {
            System.out.println("请求成功，响应内容：");
            System.out.println(response.body());
        } else {
            System.out.println("请求失败，状态码：" + response.getStatus());
            System.out.println("响应内容：" + response.body());
        }
    }

}
