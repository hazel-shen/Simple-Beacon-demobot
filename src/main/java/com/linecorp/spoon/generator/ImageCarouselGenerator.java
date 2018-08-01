package com.linecorp.spoon.generator;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;

@Component
public class ImageCarouselGenerator {

    @Value("${LIFF_URL}")
    String LIFF_URL;

    public TemplateMessage getTemplate (String imageUrl) {
        ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(
                Arrays.asList(
                        new ImageCarouselColumn(imageUrl,
                                new URIAction("Let's battle",
                                        LIFF_URL)
                        )
                ));
        TemplateMessage templateMessage = new TemplateMessage("You have a new BeacaMon message!",
                imageCarouselTemplate);
        return templateMessage;
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }


}
