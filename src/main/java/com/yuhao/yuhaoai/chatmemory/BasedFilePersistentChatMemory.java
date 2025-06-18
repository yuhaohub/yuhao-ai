package com.yuhao.yuhaoai.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasedFilePersistentChatMemory implements ChatMemory {

    private final String Base_Path;

    private static final Kryo kryo = new Kryo();
    static {
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }
    public BasedFilePersistentChatMemory(String basePath) {
        this.Base_Path = basePath;
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    @Override
    public void add(String conversationId, Message message) {
        //保存会话消息
        saveConversation(conversationId, List.of(message));
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        messageList.addAll(messages);
        saveConversation(conversationId, messageList);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        return messageList.stream().skip(Math.max(0, messageList.size() - lastN)).toList();
    }

    @Override
    public void clear(String conversationId) {
        // 根据会话id获取文件
        File file = getConversationFile(conversationId);
        // 文件存在
        if (file.exists()) {
            file.delete();
        }
    }

    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                 kryo.readObject(input, ArrayList.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return messages;
    }


    private void saveConversation(String conversationId, List<Message> messages) {
        // 保存
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getConversationFile(String conversationId) {
        // 获取持久化文件
        return new File(Base_Path , conversationId + ".kryo");
    }
}
