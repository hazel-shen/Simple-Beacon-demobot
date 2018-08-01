package com.linecorp.spoon.controller;

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
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.spoon.Generator.ImageCarouselGenerator;
import com.linecorp.spoon.Generator.PokemonGenerator;
import com.linecorp.spoon.dto.UserInfoDto;
import com.linecorp.spoon.service.UserInfoService;
import com.linecorp.spoon.utils.RedisConnection;
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
    PokemonGenerator pokemonGenerator;

    @Autowired
    ImageCarouselGenerator imageCarouselGenerator;

    @Autowired
    UserInfoService userInfoService;

    @Value("${NO_POKE}")
    String NO_POKE;

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


        String poke = pokemonGenerator.getPokemon();

        TextMessage textMessage = new TextMessage(NO_POKE);
        logger.info("poke generated: " + poke);
        TemplateMessage pokeMessage = imageCarouselGenerator.getTemplate(poke);

        if(poke.equals(NO_POKE))
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
        logger.info(event.getBeacon().getDeviceMessageAsHex());
        if (hwid.equals(HARDWARE_ID) && beaconMessage.equals(BEACON_MESSAGE)) {
            Message message = userInfoService.getTemplateForUser(event.getSource().getUserId());
            pushMessage(event.getSource().getSenderId() ,message);
        } else {
            logger.info(HARDWARE_ID);
            logger.info(BEACON_MESSAGE);
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
