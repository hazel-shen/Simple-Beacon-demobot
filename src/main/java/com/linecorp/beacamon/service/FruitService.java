package com.linecorp.beacamon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FruitService {

    private static Integer fruitNum = 0;

    Logger logger = LoggerFactory.getLogger(FruitService.class);

    public boolean getFruit (String uuid) {
        if(fruitNum != 0) {
            logger.info(uuid + "got fruit! Now the fruits last " + fruitNum);
            fruitNum = fruitNum - 1;
            return true;
        }
        logger.info(uuid + "failed fruit!");
        return false;
    }


    public void resetFruitNum () {
        logger.info("Reset fruit!");
        fruitNum = 10;
    }

    public Integer getFruitNum () {
        return this.fruitNum;
    }

}
