package com.bolt.slack;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.ReactionAddedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;


@Configuration
public class SlackApp {
    @Bean
    public App initSlackApp() {
        App app = new App();

       app.command("/hello", (req, ctx) -> {
            return ctx.ack(asBlocks(
                    section(section -> section.text(markdownText(":wave: pong"))),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("ping-again").text(plainText(pt -> pt.text("Ping"))).value("ping"))
                            ))
                    )
            ));
        });
        app.command("/cardservice", (req, ctx) -> {
            return ctx.ack(asBlocks(
                    section(section -> section.text(markdownText("Hello :wave: Please click on the below tabs for additional information."))),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("Jenkins Job").text(plainText(pt -> pt.text("Jenkins Job"))).value("jenkins")),
                                    button(b -> b.actionId("SOAP Services").text(plainText(pt -> pt.text("SOAP Services"))).value("soap")),
                                    button(b -> b.actionId("Rest Services").text(plainText(pt -> pt.text("Rest Services"))).value("rest"))
                            ))
                    )
            ));
        });

        app.event(ReactionAddedEvent.class, (payload, ctx) -> {
            ReactionAddedEvent event = payload.getEvent();
            if (event.getReaction().equals("white_check_mark")) {
                ChatPostMessageResponse message = ctx.client().chatPostMessage(r -> r
                        .channel(event.getItem().getChannel())
                        .threadTs(event.getItem().getTs())
                        .text("<@" + event.getUser() + "> Thank you! We greatly appreciate your efforts :two_hearts:"));
                if (!message.isOk()) {
                    ctx.logger.error("chat.postMessage failed: {}", message.getError());
                }
            }
            return ctx.ack();
        });

        app.message(":wave:", (payload, ctx) -> {
            ctx.say("Hello, <@" + payload.getEvent().getUser() + ">");
            return ctx.ack();
        });

        return app;
    }
}


