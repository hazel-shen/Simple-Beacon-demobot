package com.linecorp.beacamon.controller;

import com.linecorp.beacamon.dto.BeacamonDto;
import com.linecorp.beacamon.generator.FlexGenerator;
import com.linecorp.beacamon.service.FruitService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
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
    private LineMessagingClient client;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    FruitService fruitService;

    @Autowired
    FlexGenerator flexGenerator;

    @Value("${NO_BEACAMON}")
    String NO_BEACAMON;

    @Value("${HARDWARE_ID}")
    String HARDWARE_ID;

    @Value("${BEACON_MESSAGE}")
    String BEACON_MESSAGE;

    @Value("${FOOD_HARDWARE_ID}")
    String FOOD_HARDWARE_ID;

    @Value("${FOOD_BEACON_MESSAGE}")
    String FOOD_BEACON_MESSAGE;

    @Value("${FOOD_URL}")
    String FOOD_URL;

    @Value("${ADMIN}")
    String ADMIN;


    @EventMapping
    public TextMessage handleJoinEvent(JoinEvent event) {
        return new TextMessage("Welcome to BeacaMon Go!");
    }

    Logger logger = LoggerFactory.getLogger(BotController.class);

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String uuid = event.getSource().getSenderId();
        if (uuid.equals(ADMIN) && event.getMessage().getText().equals("reset food")) {
            fruitService.resetFruitNum();
        }
    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) throws IOException {

        String hwid = event.getBeacon().getHwid();
        String beaconMessage = event.getBeacon().getDeviceMessageAsHex();
        String uuid = event.getSource().getUserId();
        FlexMessage flexMessage;

        if (hwid.equals(HARDWARE_ID) && beaconMessage.equals(BEACON_MESSAGE)) {
            BeacamonDto beacamonDto = userInfoService.getBeacamonName(uuid);
            if(beacamonDto.getBeacamonName().equals(NO_BEACAMON)) {
                 flexMessage = flexGenerator.getFlexMessage(
                        beacamonDto.getPictureUrl(),
                        beacamonDto.getBeacamonName(),
                        "再挑戰一次吧！",
                        "line://app/1521000986-rGoo940l",
                        beacamonDto.getBeacamonName(),
                        "快來看看你抓到什麼神奇寶貝！"

                );
            } else {
                flexMessage = flexGenerator.getFlexMessage(
                        beacamonDto.getPictureUrl(),
                        beacamonDto.getBeacamonName(),
                        "查看情報",
                        "line://app/1521000986-rGoo940l",
                        "恭喜捕獲一隻"+ beacamonDto.getBeacamonName() +"！",
                        "地點",
                        "就在這裡",
                        "數量",
                        "10 個",
                        "快來看看你抓到什麼神奇寶貝！"
                );
            }
            pushMessage(event.getSource().getSenderId(), flexMessage);

        } else if (hwid.equals(FOOD_HARDWARE_ID) && beaconMessage.equals(FOOD_BEACON_MESSAGE)) {
            if (userInfoService.isHasBeacaMon(uuid) && fruitService.getFruitNum() > 0) {
                flexMessage = flexGenerator.getFlexMessage(
                        FOOD_URL,
                        "樹果",
                        "領取樹果",
                        "line://app/1521000986-rGoo940l",
                        "領樹果囉！",
                        "地點",
                        "就在這裡",
                        "數量",
                        fruitService.getFruitNum() + " 個",
                        "快來搶樹果喔！"

                );
                pushMessage(event.getSource().getSenderId(), flexMessage);
            }
        } else {
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
