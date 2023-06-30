package com.bolt.slack;


import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.context.builtin.ActionContext;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.bolt.request.builtin.BlockActionRequest;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.element.StaticSelectElement;
import com.slack.api.model.view.ViewState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static com.slack.api.model.view.Views.*;
import com.slack.api.model.view.View;

@Configuration
public class SlackApp {
   @Bean
    public App initSlackApp() throws Exception {
       AppConfig appConfig = new AppConfig();
       appConfig.setRequestVerificationEnabled(false);
       appConfig.setSslCheckEnabled(false);
       App app = new App(appConfig);


       app.command("/service", (req, ctx) -> {
           ctx.respond(res -> res
                   .responseType("in_channel")
                   .blocks(asBlocks(
                           section(section -> section.text(markdownText("Hello " + req.getPayload().getUserName() + " :wave: Thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                           divider(),
                           actions(actions -> actions
                                   .elements(asElements(getStaticSelectElementService()))
                           )
                   )));
           return ctx.ack();
       });

       Pattern pattern = Pattern.compile("[a-zA-Z ]*", Pattern.CASE_INSENSITIVE);
        app.message(pattern, (payload, ctx) -> {
                    ctx.say(asBlocks(
                            section(section -> section.text(markdownText("Hello! :wave: I am your virtual assistant thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                            divider(),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.actionId("REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("Member Service REST")),
                                            button(b -> b.actionId("SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("Card Service SOAP"))

                                    ))
                            ),
                            section(section -> section.text(markdownText("To Onboard new Service please click on below button :point_down:"))),
                            divider(),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.actionId("Onboard").style("primary").text(plainText(pt -> pt.text("Onboard"))).value("Onboard"))

                                    ))
                            )
                    ));

                    return ctx.ack();
        });

       app.blockAction("Onboard", (req, ctx) -> {
           String responseUrl = req.getPayload().getResponseUrl();
           if(responseUrl != null) {
               String triggerId = req.getPayload().getTriggerId();
               View view = getModalView();
               app.client().viewsOpen(r -> r
                       .triggerId(triggerId)
                       .view(view));
           }
           return ctx.ack();
       });

       app.viewSubmission("new-Onboarding", (req, ctx) -> {
           String channel = "D03T16EMH2M";
           Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();
           String serviceName = stateValues.get("Service-Name").get("Service-Name").getValue();
           String endpointURL = stateValues.get("Endpoint-URL").get("Endpoint-URL").getValue();
           String jenkinsJob = stateValues.get("Jenkins-Job").get("Jenkins-Job").getValue();
           String githubURL = stateValues.get("Github-URL").get("Github-URL").getValue();
           String confluenceLinks = stateValues.get("Confluence-links").get("Confluence-links").getValue();
           String ktRecordings = stateValues.get("KT-Recordings").get("KT-Recordings").getValue();
           Map<String, String> errors = new HashMap<>();
           if (serviceName.length() <= 1) {
               errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
           }
           if (!errors.isEmpty()) {
               return ctx.ack(r -> r.responseAction("errors").errors(errors));
           } else {
               //TODO
               ChatPostMessageResponse postMessageResponse = ctx.client().chatPostMessage(r -> r
                       .channel(channel)
                       .text("Your service has been successfully onboarded! The service name is: " + serviceName)
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Your service has been successfully onboarded! The service name is: " + serviceName))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("Member Service REST")),
                                               button(b -> b.actionId("SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("Card Service SOAP")),
                                               button(b -> b.actionId(serviceName).style("primary").text(plainText(pt -> pt.text(serviceName))).value(serviceName))
                                       ))
                               ))));
               if (postMessageResponse.isOk()) {
                   System.out.println("Successfully sent message to channel: " + channel);
               } else {
                   System.out.println("Error sending message to channel: " + postMessageResponse.getError());
               }
           }

           return ctx.ack();

       });

       app.blockAction("REST", (req, ctx) -> {
           if (req.getPayload().getResponseUrl() != null) {
               StaticSelectElement selectREST = getStaticSelectElementREST();

               ctx.respond(res -> res
                           .responseType("in_channel")
                           .blocks(asBlocks(
                                   section(section -> section.text(markdownText("What do you want to know about Member Service REST. Please select from one of the options" + "\n"))),
                                   divider(),
                                   actions(actions -> actions
                                           .elements(asElements(selectREST))
                                   )
                           )));

               }
           return ctx.ack();
       });

       app.blockAction("SOAP", (req, ctx) -> {

           if (req.getPayload().getResponseUrl() != null) {
               StaticSelectElement selectSOAP = getStaticSelectElementSOAP();
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("What do you want to know about Member Service SOAP. Please select from one of the options" + "\n"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(selectSOAP))
                               )
                       )));

           }
           return ctx.ack();
       });


       app.blockAction("Member Service REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getSelectedOption().getValue();
           if (req.getPayload().getResponseUrl() != null) {
               switch (value) {
                   case "Endpoint URL":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the endpoint URL for Card Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));


                       break;
                   case "Jenkins Job":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Jenkins job for Member Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));

                       break;
                   case "Github URL":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Github links for Member Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));

                       break;
                   case "Confluence link":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Confluence links for Member Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));

                       break;
                   case "KT Recordings":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the KT Recordings for Member Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));

                       break;
                   case "Swagger Links":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Swagger links for Member Service REST" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getRestActions()
                               )));
                       break;
                   default:
                       ctx.respond("invalid selection");
               }
           }

           return ctx.ack();
       });

       AtomicReference<String> string = new AtomicReference<>("");

       app.blockAction("Member Service", (req, ctx) -> {
           string.set("teSt");
           System.out.println(string.toString().substring(string.toString().length() - 2));
           String value = req.getPayload().getActions().get(0).getSelectedOption().getValue();
           blockActionFun(req, ctx, value);
           return ctx.ack();
       });
       System.out.println(string);

       app.blockAction("NO", (req, ctx) -> {
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Goodbye !! :wave: If you wish to revisit services please click on below button :"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(
                                               button(b -> b.actionId("REST").style("primary").text(plainText(pt -> pt.text("Member Service REST"))).value("Member Service REST")),
                                               button(b -> b.actionId("SOAP").style("primary").text(plainText(pt -> pt.text("Card Service SOAP"))).value("Card Service SOAP"))
                                       ))
                               )
                       ))

               );
           }
           return ctx.ack();
       });

       app.blockAction("YES_SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               if (value.equals("YES")) {
                   StaticSelectElement selectSOAP = getStaticSelectElementSOAP();
                   ctx.respond(res -> res
                           .responseType("in_channel")
                           .blocks(asBlocks(
                                   section(section -> section.text(markdownText("What do you want to know about Card Service SOAP. Please select from one of the options" + "\n"))),
                                   divider(),
                                   actions(actions -> actions
                                           .elements(asElements(selectSOAP))
                                   )
                           )));
               }
           }
           return ctx.ack();
       });

       app.blockAction("YES_REST", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               if (value.equals("YES")) {
                   StaticSelectElement selectREST = getStaticSelectElementREST();
                   ctx.respond(res -> res
                           .responseType("in_channel")
                           .blocks(asBlocks(
                                   section(section -> section.text(markdownText("What do you want to know about Member Service REST. Please select from one of the options" + "\n"))),
                                   divider(),
                                   actions(actions -> actions
                                           .elements(asElements(selectREST))
                                   )
                           )));
               }
           }
           return ctx.ack();
       });

       new SlackAppServer(app).start();
        return app;
    }

    private void blockActionFun(BlockActionRequest req, ActionContext ctx, String value) throws IOException {
        if (req.getPayload().getResponseUrl() != null) {
            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText("Please find the endpoint URL for "+value+"\n"))),
                            divider(),
                            section(section -> section.text(markdownText("Do you want to know anything else about this service\n"))),
                            divider(),
                            getSoapActions()
                    )));
        }
    }


    private StaticSelectElement getStaticSelectElementSOAP() {
        StaticSelectElement selectSOAP = StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Card Service SOAP")
                .options(Arrays.asList(
                        option(plainText("Endpoint URL"), "Endpoint URL SOAP"),
                        option(plainText("Jenkins Job"), "Jenkins Job SOAP"),
                        option(plainText("Github URL"), "Github URL SOAP"),
                        option(plainText("Confluence link"), "Confluence link SOAP"),
                        option(plainText("KT Recordings"), "KT Recordings SOAP")
                )).build();
        return selectSOAP;
    }

    private StaticSelectElement getStaticSelectElementREST() {
        StaticSelectElement selectREST = StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Member Service REST")
                .options(Arrays.asList(
                        option(plainText("Endpoint URL"), "Endpoint URL"),
                        option(plainText("Jenkins URL"), "Jenkins Job"),
                        option(plainText("Github URL"), "Github URL"),
                        option(plainText("Confluence link"), "Confluence link"),
                        option(plainText("KT Recordings"), "KT Recordings")
                )).build();
        return selectREST;
    }

    private StaticSelectElement getStaticSelectElementService() {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Member Service")
                .options(Arrays.asList(
                        option(plainText("Member Service REST"), "Member Service REST"),
                        option(plainText("Card Service SOAP"), "Card Service SOAP")
                )).build();
    }

    private ActionsBlock getSoapActions() {
        return actions(actions -> actions
                .elements(asElements(
                        button(b -> b.actionId("YES_SOAP").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                        button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                ))
        );
    }

    private ActionsBlock getRestActions() {
        return actions(actions -> actions
                .elements(asElements(
                        button(b -> b.actionId("YES_REST").style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                        button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))

                ))
        );
    }

    private View getModalView() {
        View view = View.builder()
                .callbackId("new-Onboarding")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Onboarding New Service").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                .blocks(asBlocks(
                        input(input -> input
                                .blockId("Service-Name")

                                .element(plainTextInput(pti -> pti.actionId("Service-Name").multiline(true)))
                                .label(plainText(pt -> pt.text("Service Name : ").emoji(true)))
                        ),
                        input(input -> input
                                .blockId("Endpoint-URL")
                                .element(plainTextInput(pti -> pti.actionId("Endpoint-URL").multiline(true)))
                                .label(plainText(pt -> pt.text("Enter Endpoint URL's : ").emoji(true)))
                        ),
                        input(input -> input
                                .blockId("Jenkins-Job")
                                .element(plainTextInput(pti -> pti.actionId("Jenkins-Job").multiline(true)))
                                .label(plainText(pt -> pt.text("Enter Jenkins Job details : ").emoji(true)))
                        ),
                        input(input -> input
                                .blockId("Github-URL")
                                .element(plainTextInput(pti -> pti.actionId("Github-URL").multiline(true)))
                                .label(plainText(pt -> pt.text("Enter Github URL's : ").emoji(true)))
                        ),
                        input(input -> input
                                .blockId("Confluence-links")
                                .element(plainTextInput(pti -> pti.actionId("Confluence-links").multiline(true)))
                                .label(plainText(pt -> pt.text("Enter Confluence pages links : ").emoji(true)))
                        ),
                        input(input -> input
                                .blockId("KT-Recordings")
                                .element(plainTextInput(pti -> pti.actionId("KT-Recordings").multiline(true)))
                                .label(plainText(pt -> pt.text("Enter KT Recordings links : ").emoji(true)))
                        )
                )).build();
        return view;
    }

}










