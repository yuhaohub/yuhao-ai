package com.yuhao.yuhaoai.demo;

import com.yuhao.yuhaoai.api.AliyunApiKey;
import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class LangChainAIInvoke {
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(AliyunApiKey.apiKey)
                .modelName("qwen-max")
                .build();
        String answer = qwenChatModel.chat("I am yuhao");
        System.out.println(answer);
    }
}
