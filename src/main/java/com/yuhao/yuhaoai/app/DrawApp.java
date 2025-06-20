package com.yuhao.yuhaoai.app;


import com.yuhao.yuhaoai.advisor.LoggerAdvisor;
import com.yuhao.yuhaoai.chatmemory.BasedFilePersistentChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class DrawApp {
    private  final ChatClient chatClient;

    public DrawApp(ChatModel dashscopeChatModel) {
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        //对话记忆
        //InMemoryChatMemory chatMemory = new InMemoryChatMemory();
        BasedFilePersistentChatMemory chatMemory = new BasedFilePersistentChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem("你是一名绘画家")
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        //自定义Advisor
                        new LoggerAdvisor()
                )
                .build();
    }

    /**
     * Ai对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message,String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String chatResult = chatResponse.getResult().getOutput().getText();
        log.info("chatResult:{}",chatResult);
        return chatResult ;
    }
}
