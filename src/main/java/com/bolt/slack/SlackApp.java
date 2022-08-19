package com.bolt.slack;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.ReactionAddedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;

@Configuration
public class SlackApp {
   @Bean
    public App initSlackApp() throws Exception {
       AppConfig appConfig = new AppConfig();
       appConfig.setRequestVerificationEnabled(false);
       appConfig.setSslCheckEnabled(false);
       App app = new App(appConfig);


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

       Pattern pattern = Pattern.compile("\\bhello\\b|\\bhi\\b|\\bhey\\b", Pattern.CASE_INSENSITIVE);
         app.message(pattern, (payload, ctx) -> {
                ctx.say(asBlocks(
                         section(section -> section.text(markdownText("Hello! :wave: I am your virtual assistant thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                         divider(),
                         actions(actions -> actions
                                 .elements(asElements(
                                         button(b -> b.actionId("Member Service REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("rest")),
                                         button(b -> b.actionId("Card Service SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("soap"))

                                 ))
                         )
                 ));
                
                 return ctx.ack();

       });

       app.command("/hi",(req,ctx) -> {
            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + " :wave: Thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                            divider(),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.actionId("Member Service REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("rest")),
                                            button(b -> b.actionId("Card Service SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("soap"))

                                    ))
                            )
                    )));
            return ctx.ack();
        });

       app.command("/hello", (req, ctx) -> {
           ctx.respond(res -> res
                   .responseType("in_channel")
                   .blocks(asBlocks(
                           section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + " :wave: Thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                           divider(),
                           actions(actions -> actions
                                   .elements(asElements(
                                           button(b -> b.actionId("Member Service REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("rest")),
                                           button(b -> b.actionId("Card Service SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("soap"))

                                   ))
                           )
                   )));
           return ctx.ack();
       });

       app.command("/cardservice", (req, ctx) -> {
            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(
                    section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + " :wave: Thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                    divider(),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("Member Service REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("rest")),
                                    button(b -> b.actionId("Card Service SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("soap"))

                            ))
                    )
            )));
            return ctx.ack();
        });

        app.blockAction("Card Service SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                       section(section -> section.text(markdownText("What do you want to know about Card Service SOAP. Please select from one of the options" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Endpoint URL SOAP").style("primary").text(plainText(pt -> pt.text("Endpoint URL"))).value("https://endpointurl1.com/CS" + "\n" + "https://endpointurl2.com/CS" + "\n" + "https://endpointurl3.com/CS" + "\n" + "https://endpointurl4.com/CS")),
                                       button(b -> b.actionId("Jenkins Job SOAP").style("primary").text(plainText(pt -> pt.text("Jenkins"))).value("http://jenkinsjob1.com/CS" + "\n" + "http://jenkinsjob2.com/CS" + "\n" + "http://jenkinsjob3.com/CS" + "\n" + "http://jenkinsjob4.com/CS")),
                                       button(b -> b.actionId("Github URL SOAP").style("primary").text(plainText(pt -> pt.text("Github URL"))).value("https://githuburl1.com/CS" + "\n" + "https://githuburl2.com/CS" + "\n" + "https://githuburl3.com/CS" + "\n" + "https://githuburl4.com/CS")),
                                       button(b -> b.actionId("Confluence link SOAP").style("primary").text(plainText(pt -> pt.text("Confluence Link"))).value("https://confluence1.com/CS" + "\n" + "https://confluence2.com/CS" + "\n" + "https://confluence3.com/CS" + "\n" + "https://confluence4.com/CS")),
                                       button(b -> b.actionId("KT Recordings SOAP").style("primary").text(plainText(pt -> pt.text("KT Recordings"))).value("https://ktrecording1.com/CS" + "\n" + "https://ktrecording2.com/CS" + "\n" + "https://ktrecording3.com/CS" + "\n" + "https://ktrecording4.com/CS")),
                                       button(b -> b.actionId("Swagger Links SOAP").style("primary").text(plainText(pt -> pt.text("Swagger Links"))).value("https://swaggerlink1.com/CS" + "\n" + "https://swaggerlink2.com/CS" + "\n" + "https://swaggerlink3.com/CS" + "\n" + "https://swaggerlink4.com/CS"))
                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("Member Service REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                       section(section -> section.text(markdownText("What do you want to know about Member Service REST. Please select from one of the options" + "\n"))),
                               divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Endpoint URL REST").style("primary").text(plainText(pt -> pt.text("Endpoint URL"))).value("https://endpointurl1.com/MS" + "\n" + "https://endpointurl2.com/MS" + "\n" + "https://endpointurl3.com/MS" + "\n" + "https://endpointurl4.com/MS")),
                                       button(b -> b.actionId("Jenkins Job REST").style("primary").text(plainText(pt -> pt.text("Jenkins"))).value("http://jenkinsjob1.com/MS" + "\n" + "http://jenkinsjob2.com/MS" + "\n" + "http://jenkinsjob3.com/MS" + "\n" + "http://jenkinsjob3.com/MS")),
                                       button(b -> b.actionId("Github URL REST").style("primary").text(plainText(pt -> pt.text("Github URL"))).value("https://githuburl1.com/MS" + "\n" + "https://githuburl2.com/MS" + "\n" + "https://githuburl3.com/MS" + "\n" + "https://githuburl4.com/MS")),
                                       button(b -> b.actionId("Confluence link REST").style("primary").text(plainText(pt -> pt.text("Confluence Link"))).value("https://confluence1.com/MS" + "\n" + "https://confluence2.com/MS" + "\n" + "https://confluence3.com/MS" + "\n" + "https://confluence4.com/MS")),
                                       button(b -> b.actionId("KT Recordings REST").style("primary").text(plainText(pt -> pt.text("KT Recordings"))).value("https://ktrecording1.com/MS" + "\n" + "https://ktrecording2.com/MS" + "\n" + "https://ktrecording3.com/MS" + "\n" + "https://ktrecording4.com/MS")),
                                       button(b -> b.actionId("Swagger Links REST").style("primary").text(plainText(pt -> pt.text("Swagger Links"))).value("https://swaggerlink1.com/MS" + "\n" + "https://swaggerlink2.com/MS" + "\n" + "https://swaggerlink3.com/MS" + "\n" + "https://swaggerlink4.com/MS"))
                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("Endpoint URL SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the endpoint URL for Card Service SOAP" + "\n" + value))),
                       divider(),
                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                       button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("Endpoint URL REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the endpoint URL for Member Service REST" + "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("Github URL SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the Github links for Card Service SOAP" + "\n" + value))),
                       divider(),
                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                       button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("Github URL REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the Github links for Member Service REST" + "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("Jenkins Job SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Jenkins Job for Card Service SOAP"+ "\n" + value))),
                       divider(),
                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                       button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("Jenkins Job REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Jenkins Job for Member Service REST"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("Confluence link SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Confluence Links for Card Service SOAP"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("Confluence link REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Confluence Links for Member Service REST"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("KT Recordings SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below KT Recordings for Card Service SOAP"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("KT Recordings REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below KT Recordings for Member Service REST"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });


       app.blockAction("Swagger Links SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Swagger Links for Card Service SOAP"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       app.blockAction("Swagger Links REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Please find the below Swagger Links for Member Service REST"+ "\n" + value))),
                               divider(),
                               section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                               button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });


       app.blockAction("NO", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("GoodBye :wave: Hope you have a wonderful day!!")))
                       ))
               );
           }
           return ctx.ack();
       });

       app.blockAction("YES_SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                       section(section -> section.text(markdownText("Click on below to know more !!" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Endpoint URL SOAP").style("primary").text(plainText(pt -> pt.text("Endpoint URL"))).value("https://endpointurl1.com/CS" + "\n" + "https://endpointurl2.com/CS" + "\n" + "https://endpointurl3.com/CS" + "\n" + "https://endpointurl4.com/CS")),
                                       button(b -> b.actionId("Jenkins Job SOAP").style("primary").text(plainText(pt -> pt.text("Jenkins"))).value("http://jenkinsjob1.com/CS" + "\n" + "http://jenkinsjob2.com/CS" + "\n" + "http://jenkinsjob3.com/CS" + "\n" + "http://jenkinsjob4.com/CS")),
                                       button(b -> b.actionId("Github URL SOAP").style("primary").text(plainText(pt -> pt.text("Github URL"))).value("https://githuburl1.com/CS" + "\n" + "https://githuburl2.com/CS" + "\n" + "https://githuburl3.com/CS" + "\n" + "https://githuburl4.com/CS")),
                                       button(b -> b.actionId("Confluence link SOAP").style("primary").text(plainText(pt -> pt.text("Confluence Link"))).value("https://confluence1.com/CS" + "\n" + "https://confluence2.com/CS" + "\n" + "https://confluence3.com/CS" + "\n" + "https://confluence4.com/CS")),
                                       button(b -> b.actionId("KT Recordings SOAP").style("primary").text(plainText(pt -> pt.text("KT Recordings"))).value("https://ktrecording1.com/CS" + "\n" + "https://ktrecording2.com/CS" + "\n" + "https://ktrecording3.com/CS" + "\n" + "https://ktrecording4.com/CS")),
                                       button(b -> b.actionId("Swagger Links SOAP").style("primary").text(plainText(pt -> pt.text("Swagger Links"))).value("https://swaggerlink1.com/CS" + "\n" + "https://swaggerlink2.com/CS" + "\n" + "https://swaggerlink3.com/CS" + "\n" + "https://swaggerlink4.com/CS")),
                                       button(b -> b.actionId("Member Service REST").text(plainText(pt -> pt.text("Member Service REST"))).value("rest"))
                               ))
                       )
               )));
           }
           return ctx.ack();
       });

       app.blockAction("YES_REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Click on below to know more !!" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("Endpoint URL REST").style("primary").text(plainText(pt -> pt.text("Endpoint URL"))).value("https://endpointurl1.com/MS" + "\n" + "https://endpointurl2.com/MS" + "\n" + "https://endpointurl3.com/MS" + "\n" + "https://endpointurl4.com/MS")),
                                               button(b -> b.actionId("Jenkins Job REST").style("primary").text(plainText(pt -> pt.text("Jenkins"))).value("http://jenkinsjob1.com/MS" + "\n" + "http://jenkinsjob2.com/MS" + "\n" + "http://jenkinsjob3.com/MS" + "\n" + "http://jenkinsjob3.com/MS")),
                                               button(b -> b.actionId("Github URL REST").style("primary").text(plainText(pt -> pt.text("Github URL"))).value("https://githuburl1.com/MS" + "\n" + "https://githuburl2.com/MS" + "\n" + "https://githuburl3.com/MS" + "\n" + "https://githuburl4.com/MS")),
                                               button(b -> b.actionId("Confluence link REST").style("primary").text(plainText(pt -> pt.text("Confluence Link"))).value("https://confluence1.com/MS" + "\n" + "https://confluence2.com/MS" + "\n" + "https://confluence3.com/MS" + "\n" + "https://confluence4.com/MS")),
                                               button(b -> b.actionId("KT Recordings REST").style("primary").text(plainText(pt -> pt.text("KT Recordings"))).value("https://ktrecording1.com/MS" + "\n" + "https://ktrecording2.com/MS" + "\n" + "https://ktrecording3.com/MS" + "\n" + "https://ktrecording4.com/MS")),
                                               button(b -> b.actionId("Swagger Links REST").style("primary").text(plainText(pt -> pt.text("Swagger Links"))).value("https://swaggerlink1.com/MS" + "\n" + "https://swaggerlink2.com/MS" + "\n" + "https://swaggerlink3.com/MS" + "\n" + "https://swaggerlink4.com/MS")),
                                               button(b -> b.actionId("Card Service SOAP").text(plainText(pt -> pt.text("Card Service SOAP"))).value("soap"))
                                       ))
                               )
                       )));
           }
           return ctx.ack();
       });

       new SocketModeApp(app).start();
        return app;
    }

}




