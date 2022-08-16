package com.bolt.slack;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
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
    public App initSlackApp() throws Exception {
        App app = new App();

       app.command("/hello", (req, ctx) -> {
           return ctx.ack(res -> res.responseType("in_channel").text(":wave: Hello there!!"));
       });

        app.command("/cardservice", (req, ctx) -> {
            return ctx.ack(asBlocks(
                    section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + " :wave: Please click on the below tabs for additional information."))),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("Card Service REST").text(plainText(pt -> pt.text("REST Services"))).value("rest")),
                                    button(b -> b.actionId("Card Service SOAP").text(plainText(pt -> pt.text("SOAP Services"))).value("soap"))

                            ))

                    )
            ));
        });

        app.blockAction("Card Service SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(asBlocks(
                       section(section -> section.text(markdownText("What do you want to know about Card Service SOAP. Please select from below options" + "\n"))),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Github URL SOAP").text(plainText(pt -> pt.text("Github URL"))).value("https://github.com" + "\n" + "http://memberservice.com" + "\n" + "http://cardservice.com")),
                                       button(b -> b.actionId("Endpoint URL SOAP").text(plainText(pt -> pt.text("Endpoint URL"))).value("soap")),
                                       button(b -> b.actionId("Jenkins Job SOAP").text(plainText(pt -> pt.text("jenkins"))).value("rest")),
                                       button(b -> b.actionId("Confluence link SOAP").text(plainText(pt -> pt.text("confluence link"))).value("rest")),
                                       button(b -> b.actionId("ECP Project details SOAP").text(plainText(pt -> pt.text("ecp project details"))).value("rest")),
                                       button(b -> b.actionId("KT Recordings SOAP").text(plainText(pt -> pt.text("KT Recording"))).value("rest")),
                                       button(b -> b.actionId("Swagger Links SOAP").text(plainText(pt -> pt.text("Swagger Links"))).value("rest"))
                               ))

                       )
               ));
           }
           return ctx.ack();
       });

       app.blockAction("Card Service REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(asBlocks(
                       section(section -> section.text(markdownText("What do you want to know about Card Service REST. Please select from below options" + "\n"))),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Github URL REST").text(plainText(pt -> pt.text("Github URL"))).value("http://jenkins.com" + "\n" + "http://memberservice.com" + "\n" + "http://cardservice.com")),
                                       button(b -> b.actionId("Endpoint URL REST").text(plainText(pt -> pt.text("Endpoint URL"))).value("soap")),
                                       button(b -> b.actionId("Jenkins Job SOAP REST").text(plainText(pt -> pt.text("jenkins"))).value("rest")),
                                       button(b -> b.actionId("Confluence link REST").text(plainText(pt -> pt.text("confluence link"))).value("rest")),
                                       button(b -> b.actionId("ECP Project details REST").text(plainText(pt -> pt.text("ecp project details"))).value("rest")),
                                       button(b -> b.actionId("KT Recordings REST").text(plainText(pt -> pt.text("KT Recording"))).value("rest")),
                                       button(b -> b.actionId("Swagger Links REST").text(plainText(pt -> pt.text("Swagger Links"))).value("rest"))
                               ))

                       )
               ));
           }
           return ctx.ack();
       });

       app.blockAction("Github URL SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond("Please find the Github links for Card Service SOAP" + "\n" + value);
               ctx.respond(asBlocks(
                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("YES").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                       button(b -> b.actionId("NO").text(plainText(pt -> pt.text("NO"))).value("NO"))

                               ))

                       )
               ));
           }
           return ctx.ack();
       });

       app.blockAction("NO", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond("GoodBye :wave: Hope you have a wonderful day!!");
           }
           return ctx.ack();
       });

       app.blockAction("YES", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(asBlocks(
                       section(section -> section.text(markdownText("Click on below to know more !!" + "\n"))),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Github URL SOAP").text(plainText(pt -> pt.text("Github URL"))).value("https://github.com" + "\n" + "http://memberservice.com" + "\n" + "http://cardservice.com")),
                                       button(b -> b.actionId("Endpoint URL SOAP").text(plainText(pt -> pt.text("Endpoint URL"))).value("soap")),
                                       button(b -> b.actionId("Jenkins Job SOAP").text(plainText(pt -> pt.text("jenkins"))).value("rest")),
                                       button(b -> b.actionId("Confluence link SOAP").text(plainText(pt -> pt.text("confluence link"))).value("rest")),
                                       button(b -> b.actionId("ECP Project details SOAP").text(plainText(pt -> pt.text("ecp project details"))).value("rest")),
                                       button(b -> b.actionId("KT Recordings SOAP").text(plainText(pt -> pt.text("KT Recording"))).value("rest")),
                                       button(b -> b.actionId("Swagger Links SOAP").text(plainText(pt -> pt.text("Swagger Links"))).value("rest"))
                               ))

                       )
               ));
           }
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

        app.message("hi", (payload, ctx) -> {
            ctx.say("Hello, <@" + payload.getEvent().getUser() + ">");
            return ctx.ack();
        });

       new SocketModeApp(app).start();
        return app;
    }

}




