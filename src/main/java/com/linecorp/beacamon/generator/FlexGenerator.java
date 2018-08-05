package com.linecorp.beacamon.generator;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.*;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static java.util.Arrays.asList;

@Component
public class FlexGenerator implements Supplier<FlexMessage>{


    public FlexMessage getFlexMessage(
            String imageUrl,
            String imageLabel,
            String footerLabel,
            String footerUrl,
            String bodyText,
            String firstBoxTitle,
            String firstBoxText,
            String secondBoxtTitle,
            String secondBoxText,
            String altText) {
        final Image heroBlock =
                Image.builder()
                        .url(imageUrl)
                        .size(Image.ImageSize.FULL_WIDTH)
                        .aspectRatio(Image.ImageAspectRatio.R20TO13)
                        .aspectMode(Image.ImageAspectMode.Cover)
                        .action(new URIAction(imageLabel, imageUrl))
                        .build();

        final Box bodyBlock = createBodyBlock(bodyText,firstBoxTitle, firstBoxText, secondBoxtTitle, secondBoxText);
        final Box footerBlock = createFooterBlock(footerLabel, footerUrl);
        final Bubble bubble =
                Bubble.builder()
                        .hero(heroBlock)
                        .body(bodyBlock)
                        .footer(footerBlock)
                        .build();

        return new FlexMessage(altText, bubble);
    }

    public FlexMessage getFlexMessage(
            String imageUrl,
            String imageLabel,
            String footerLabel,
            String footerUrl,
            String bodyText,
            String altText) {
        final Image heroBlock =
                Image.builder()
                        .url(imageUrl)
                        .size(Image.ImageSize.FULL_WIDTH)
                        .aspectRatio(Image.ImageAspectRatio.R20TO13)
                        .aspectMode(Image.ImageAspectMode.Cover)
                        .action(new URIAction(imageLabel, imageUrl))
                        .build();

        final Box bodyBlock = createBodyBlock(bodyText);
        final Box footerBlock = createFooterBlock(footerLabel, footerUrl);
        final Bubble bubble =
                Bubble.builder()
                        .hero(heroBlock)
                        .body(bodyBlock)
                        .footer(footerBlock)
                        .build();

        return new FlexMessage(altText, bubble);
    }

    private Box createFooterBlock(String footerLabel, String footerUrl) {
        final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
//        final Button callAction = Button
//                .builder()
//                .style(Button.ButtonStyle.LINK)
//                .height(Button.ButtonHeight.SMALL)
//                .action(new URIAction("CALL", "tel:000000"))
//                .build();
//        final Separator separator = Separator.builder().build();
        final Button websiteAction =
                Button.builder()
                        .style(Button.ButtonStyle.LINK)
                        .height(Button.ButtonHeight.SMALL)
                        .action(new URIAction(footerLabel, footerUrl))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.SM)
                .contents(asList(spacer, websiteAction))
                .build();
    }

    private Box createBodyBlock(
            String bodyText,
            String firstBoxTitle,
            String firstBoxText,
            String secondBoxtTitle,
            String secondBoxText
    ) {
        final Text title =
                Text.builder()
                        .text(bodyText)
                        .weight(Text.TextWeight.BOLD)
                        .size(FlexFontSize.XL)
                        .build();

        //final Box review = createReviewBox();

        final Box info = createInfoBox(firstBoxTitle, firstBoxText, secondBoxtTitle, secondBoxText);

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(asList(title, info))
                .build();
    }


    private Box createBodyBlock(
            String bodyText
    ) {
        final Text title =
                Text.builder()
                        .text(bodyText)
                        .weight(Text.TextWeight.BOLD)
                        .size(FlexFontSize.XL)
                        .build();

        //final Box review = createReviewBox();


        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(asList(title))
                .build();
    }

    private Box createInfoBox(
            String firstBoxTitle,
            String firstBoxText,
            String secondBoxtTitle,
            String secondBoxText
    ) {
        final Box firstBox = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                                .text(firstBoxTitle)
                                .color("#aaaaaa")
                                .size(FlexFontSize.SM)
                                .flex(1)
                                .build(),
                        Text.builder()
                                .text(firstBoxText)
                                .wrap(true)
                                .color("#666666")
                                .size(FlexFontSize.SM)
                                .flex(5)
                                .build()
                ))
                .build();
        final Box sencondBox =
                Box.builder()
                        .layout(FlexLayout.BASELINE)
                        .spacing(FlexMarginSize.SM)
                        .contents(asList(
                                Text.builder()
                                        .text(secondBoxtTitle)
                                        .color("#aaaaaa")
                                        .size(FlexFontSize.SM)
                                        .flex(1)
                                        .build(),
                                Text.builder()
                                        .text(secondBoxText)
                                        .wrap(true)
                                        .color("#666666")
                                        .size(FlexFontSize.SM)
                                        .flex(5)
                                        .build()
                        ))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .margin(FlexMarginSize.LG)
                .spacing(FlexMarginSize.SM)
                .contents(asList(firstBox, sencondBox))
                .build();
    }

    private Box createReviewBox() {
        final Icon goldStar =
                Icon.builder().size(FlexFontSize.SM).url("https://example.com/gold_star.png").build();
        final Icon grayStar =
                Icon.builder().size(FlexFontSize.SM).url("https://example.com/gray_star.png").build();
        final Text point =
                Text.builder()
                        .text("4.0")
                        .size(FlexFontSize.SM)
                        .color("#999999")
                        .margin(FlexMarginSize.MD)
                        .flex(0)
                        .build();

        return Box.builder()
                .layout(FlexLayout.BASELINE)
                .margin(FlexMarginSize.MD)
                .contents(asList(goldStar, goldStar, goldStar, goldStar, grayStar, point))
                .build();
    }

    @Override
    public FlexMessage get() {
        return null;
    }
}
