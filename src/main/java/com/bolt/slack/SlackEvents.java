package com.bolt.slack;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.ReactionAddedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class SlackEvents {

    @Bean
    public App initSlackEventApp() throws Exception {

        App app = new App();
        app.message(":wave:", (payload, ctx) -> {
            ctx.say("Hello, <@" + payload.getEvent().getUser() + ">");
            return ctx.ack();
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

       // Pattern sdk = Pattern.compile(".*[(Java SDK)|(Bolt)|(Hi)|Hello|(slack\\-java\\-sdk)].*", Pattern.CASE_INSENSITIVE);
        app.message("hi", (payload, ctx) -> {
            ctx.say("Hello, <@" + payload.getEvent().getUser() + ">");
            return ctx.ack();
        });


        new SocketModeApp(app).start();
        return app;
    }
}