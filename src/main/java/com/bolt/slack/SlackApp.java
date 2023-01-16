package com.bolt.slack;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.element.StaticSelectElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
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
           if (req.getPayload().getResponseUrl() != null) {
               View view = getModalView();
               app.client().viewsOpen(r -> r
                       .triggerId(req.getPayload().getTriggerId())
                       .view(view));
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

       app.blockAction("Card Service SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getSelectedOption().getValue();
           if (req.getPayload().getResponseUrl() != null) {
               switch (value) {
                   case "Endpoint URL SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the endpoint URL for Card Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));

                       break;
                   case "Jenkins Job SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Jenkins job for Member Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));

                       break;
                   case "Github URL SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Github links for Member Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));

                       break;
                   case "Confluence link SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Confluence links for Member Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));

                       break;
                   case "KT Recordings SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the KT Recordings for Member Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));

                       break;
                   case "Swagger Links SOAP":
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("Please find the Swagger links for Member Service SOAP" + "\n" + value))),
                                       divider(),
                                       section(section -> section.text(markdownText("Do you want to know anything else about this service" + "\n"))),
                                       divider(),
                                       getSoapActions()
                               )));
                       break;
                   default:
                       ctx.respond("invalid selection");
               }
           }

           return ctx.ack();
       });

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

        /*Pattern pattern1 = Pattern.compile("[a-zA-Z ]*", Pattern.CASE_INSENSITIVE);
       app.message(pattern1, (payload, ctx) -> {
           StaticSelectElement select = StaticSelectElement.builder()
                   .placeholder(plainText("Select an option"))
                   .actionId("select_option")
                   .options(Arrays.asList(
                           option(plainText("Member Service REST"), "Member Service REST"),
                           option(plainText("Card Service SOAP"), "Card Service SOAP")
                   )).build();
           ctx.say(asBlocks(
                   section(section -> section.text(markdownText("Hello! :wave: I am your virtual assistant thanks for reaching out to me. I got all you need about Digital Enterprise Service. Please Select the one you want to inquire for."))),
                   divider(),
                   actions(actions -> actions
                           .elements(asElements(select))
                   ),
                   section(section -> section.text(markdownText("To Onboard new Service please click on below button :point_down:"))),
                   divider(),
                   actions(actions -> actions
                           .elements(asElements(
                                   button(b -> b.actionId("Onboard").style("primary").text(plainText(pt -> pt.text("Onboard"))).value("onboard"))

                           ))
                   )
           ));

           return ctx.ack();
       });
*/
     /*  app.blockAction("select_option", (req, ctx) -> {
           String selectedOption = req.getPayload().getActions().get(0).getSelectedOption().getValue();
           if (req.getPayload().getResponseUrl() != null) {
               switch (selectedOption) {
                   case "Member Service REST":
                       StaticSelectElement selectREST = StaticSelectElement.builder()
                               .placeholder(plainText("Select an option"))
                               .actionId("Member Service REST")
                               .options(Arrays.asList(
                                       option(plainText("Endpoint URL"), "Endpoint URL"),
                                       option(plainText("Jenkins Job"), "Jenkins Job"),
                                       option(plainText("Github URL"), "Github URL"),
                                       option(plainText("Confluence link"), "Confluence link"),
                                       option(plainText("KT Recordings"), "KT Recordings"),
                                       option(plainText("Swagger Links"), "Swagger Links")
                               )).build();
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("What do you want to know about Member Service REST. Please select from one of the options" + "\n"))),
                                       divider(),
                                       actions(actions -> actions
                                               .elements(asElements(selectREST))
                                       )
                               )));
                   break;
                   case "Card Service SOAP":
                       StaticSelectElement selectSOAP = StaticSelectElement.builder()
                               .placeholder(plainText("Select an option"))
                               .actionId("Card Service SOAP")
                               .options(Arrays.asList(
                                       option(plainText("Endpoint URL"), "Endpoint URL SOAP"),
                                       option(plainText("Jenkins Job"), "Jenkins Job SOAP"),
                                       option(plainText("Github URL"), "Github URL SOAP"),
                                       option(plainText("Confluence link"), "Confluence link SOAP"),
                                       option(plainText("KT Recordings"), "KT Recordings SOAP"),
                                       option(plainText("Swagger Links"), "Swagger Links SOAP")
                               )).build();
                       ctx.respond(res -> res
                               .responseType("in_channel")
                               .blocks(asBlocks(
                                       section(section -> section.text(markdownText("What do you want to know about Card Service SOAP. Please select from one of the options" + "\n"))),
                                       divider(),
                                       actions(actions -> actions
                                               .elements(asElements(selectSOAP))
                                       )
                               )));
                       break;
                   default:
                       ctx.respond("invalid selection");
               }
           }

           return ctx.ack();
       });
*/

       /*app.blockAction("Card Service SOAP", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                       section(section -> section.text(markdownText("What do you want to know about Card Service SOAP. Please select from one of the options" + "\n"))),
                       divider(),
                       actions(actions -> actions
                               .elements(asElements(
                                       button(b -> b.actionId("Endpoint URL SOAP").style("primary").text(plainText(pt -> pt.text("Endpoint URL"))).value("\bEndpoint URL: " + "https://endpointurl1.com/CS" + "\n" + "\b Endpoint URL2: " + "https://endpointurl2.com/CS" + "\n" + "https://endpointurl3.com/CS" + "\n" + "https://endpointurl4.com/CS")),
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
       });*/

     /*  app.blockAction("Endpoint URL SOAP", (req, ctx) -> {
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
       });*/


       /*app.blockAction("NO", (req, ctx) -> {
           String value = req.getPayload().getActions().get(0).getValue();
           if (req.getPayload().getResponseUrl() != null) {
               StaticSelectElement select = StaticSelectElement.builder()
                       .placeholder(plainText("Select an option"))
                       .actionId("select_option")
                       .options(Arrays.asList(
                               option(plainText("Member Service REST"), "Member Service REST"),
                               option(plainText("Card Service SOAP"), "Card Service SOAP")
                       )).build();
               ctx.respond(res -> res
                       .responseType("in_channel")
                       .blocks(asBlocks(
                               section(section -> section.text(markdownText("Goodbye !! :wave: If you wish to revisit services please select from the dropdown below :"))),
                               divider(),
                               actions(actions -> actions
                                       .elements(asElements(select))
                               )
                       ))

               );
           }
           return ctx.ack();
       });*/

       /*app.blockAction("YES_SOAP", (req, ctx) -> {
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
*/

       new SocketModeApp(app).start();
        return app;
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
                        option(plainText("Jenkins Job"), "Jenkins Job"),
                        option(plainText("Github URL"), "Github URL"),
                        option(plainText("Confluence link"), "Confluence link"),
                        option(plainText("KT Recordings"), "KT Recordings")
                )).build();
        return selectREST;
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






