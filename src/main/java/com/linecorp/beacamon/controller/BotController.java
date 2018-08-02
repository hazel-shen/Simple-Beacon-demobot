package com.linecorp.beacamon.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.beacamon.generator.ImageCarouselGenerator;
import com.linecorp.beacamon.generator.BeacamonGenerator;
import com.linecorp.beacamon.service.UserInfoService;
import com.linecorp.beacamon.utils.RedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@LineMessageHandler
public class BotController {

    @Autowired
    RedisConnection redisConnection;

    @Autowired
    private LineMessagingClient client;

    @Autowired
    BeacamonGenerator beacamonGenerator;

    @Autowired
    ImageCarouselGenerator imageCarouselGenerator;

    @Autowired
    UserInfoService userInfoService;

    @Value("${NO_BEACAMON}")
    String NO_BEACAMON;

    @Value("${HARDWARE_ID}")
    String HARDWARE_ID;

    @Value("${BEACON_MESSAGE}")
    String BEACON_MESSAGE;



    @EventMapping
    public TextMessage handleJoinEvent(JoinEvent event) {
        return new TextMessage("Welcome to BeacaMon Go!");
    }

    Logger logger = LoggerFactory.getLogger(BotController.class);

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {


        String poke = beacamonGenerator.getBeacamon();

        TextMessage textMessage = new TextMessage(NO_BEACAMON);
        logger.info("poke generated: " + poke);
        TemplateMessage pokeMessage = imageCarouselGenerator.getTemplate(poke);

        if(poke.equals(NO_BEACAMON))
            pushMessage(event.getSource().getSenderId() ,textMessage);
        else
            pushMessage(event.getSource().getSenderId() ,pokeMessage);

    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) throws IOException {

//        final TextMessage textMessage = new TextMessage(
//                "Beacon 送來: \n" +
//                "HWID: " + event.getBeacon().getHwid() + "\n" +
//                "Msg Data(Hex): " + event.getBeacon().getDeviceMessageAsHex() + "\n" +
//                "Beacon Event Type: " + event.getBeacon().getType());
//        pushMessage(event.getSource().getSenderId() ,textMessage);

        String hwid = event.getBeacon().getHwid();
        String beaconMessage = event.getBeacon().getDeviceMessageAsHex();
        logger.info("哈為哀低"+ HARDWARE_ID + "hwid:" + hwid);
        logger.info("乜寫居"+BEACON_MESSAGE + "BM" + beaconMessage);
        if (hwid.equals(HARDWARE_ID) && beaconMessage.equals(BEACON_MESSAGE)) {
            logger.info("對了");
            Message message = userInfoService.getTemplateForUser(event.getSource().getUserId());
            pushMessage(event.getSource().getSenderId() ,message);
        } else {
            logger.info("錯了");
            logger.info("Not correct hardware ID");
        }

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
