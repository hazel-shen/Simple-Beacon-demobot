package com.linecorp.beacamon.controller;

import com.linecorp.beacamon.dto.UserInfoDto;
import com.linecorp.beacamon.service.BeacaMonService;
import com.linecorp.beacamon.service.FruitService;
import com.linecorp.beacamon.utils.RedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "https://6065d4da.ngrok.io")
@RequestMapping("fruit")
public class FruitController {

    @Autowired
    FruitService fruitService;

    @Autowired
    BeacaMonService beacaMonService;

    @Value("${RANKING_FIELD_NAME}")
    String RANKING_FIELD_NAME;

    @GetMapping("/{uuid}")
    public @ResponseBody
    String getFruit(@PathVariable String uuid) {

        if(fruitService.getFruit(uuid)) {
            try {
                UserInfoDto userInfoDto = beacaMonService.upgradeLevel(uuid);
                userInfoDto.setPokeLevel(userInfoDto.getPokeLevel() + 1);

                return "恭喜你得到樹果！\n種類:" + userInfoDto.getPokeName() +
                        "\n等級:" + userInfoDto.getPokeLevel() +
                        "\n排名:" + beacaMonService.getRank(RANKING_FIELD_NAME, uuid);
            } catch (IOException e) {
                e.printStackTrace();
                return "You bad bad";
            }
        }
        return "哎呀！樹果被人搶光了！\n下次請早喔！";
    }

}
