package com.linecorp.spoon.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Random;

@LineMessageHandler
public class MainController {


    Random rand = new Random();
    private String [] replymessage = new String []{
            "恩恩，合理.",
            "你都不了解我，要不要去看我部落格",
            "我知道大家都想要我離開",
            "娘娘～～～ <3(噁心的少女音)",
            "想跟你生小孩",
            "@@",
            "哎呀好淫蕩",
            "妳今天穿的好性感！",
            "呼⋯一直開會",
            "宅宅想奏奏",
            "對不起....我騙了妳",
            "謝謝妳給我一段開心的時光",
            "我覺得我們沒什麼話聊",
            "為什麼我們只能聊扣",
            "跟我睡同一個房間我又不會對妳怎樣",
            "可以幫我代購清肺湯嗎?",
            "離職是唯一的選項嗎?",
            "她只是我妹妹",
            "這部好像不錯！",
            "最喜歡搶部下功勞",
            "頭好昏",
            "咳咳...(肺癆般的咳嗽聲)"};

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {

        System.out.println("event: " + event);
        final String originalMessageText = event.getMessage().getText();
        int indexOfMessage = rand.nextInt(22);
        final String replyMessageText = replymessage[indexOfMessage];
        return new TextMessage(replyMessageText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

}
