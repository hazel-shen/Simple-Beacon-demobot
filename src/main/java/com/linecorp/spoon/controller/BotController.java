package com.linecorp.spoon.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.spoon.service.JoinCollectService;
import com.linecorp.spoon.utils.RedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;

@Controller
@LineMessageHandler
public class BotController {

    @Autowired
    JoinCollectService joinCollectService;

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    private LineMessagingClient client;


    @EventMapping
    public TextMessage handleJoinEvent(JoinEvent event) {
        String replyToken = event.getReplyToken();
        return new TextMessage("感謝您邀請＋1蒐集器");
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {

        String originalMessageText = event.getMessage().getText();
        final TextMessage textMessage = new TextMessage("訊息 " + originalMessageText);
        pushMessage(event.getSource().getSenderId() ,textMessage);

    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
        String replyToken = event.getReplyToken();
        final TextMessage textMessage = new TextMessage(
                "Beacon 送來: \n" +
                "HWID: " + event.getBeacon().getHwid() + "\n" +
                "Msg Data(Hex): " + event.getBeacon().getDeviceMessageAsHex() + "\n" +
                "Beacon Event Type: " + event.getBeacon().getType());
        pushMessage(event.getSource().getSenderId() ,textMessage);
    }


    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    public void pushMessage(String senderId ,Message pushMessageContent) {


        final PushMessage pushMessage = new PushMessage(
                senderId,
                pushMessageContent);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(botApiResponse);


    }

}
