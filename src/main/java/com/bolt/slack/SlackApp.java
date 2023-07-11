package com.bolt.slack;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.element.StaticSelectElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.regex.Pattern;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;

@Configuration
public class SlackApp {
    private final String[] services = {"Member Service REST", "Member Service SOAP", "Other"};
    private final String[] categories = {"Endpoint URL", "Jenkins Job", "Github URL", "Confluence Link", "KT Recordings"};

    @Bean
    public App initStackApp() throws Exception {

        AppConfig appConfig = new AppConfig();
        appConfig.setRequestVerificationEnabled(false);
        appConfig.setSslCheckEnabled(false);
        App app = new App(appConfig);

        app.command("/services", (req, ctx) -> {
            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText("Hello " + req.getPayload().getUserName() + "! :wave: Thanks for reaching out to me. I got all you need about Digital Core Service. Please Select the one you want to inquire for."))),
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
                    section(section -> section.text(markdownText("Hello! :wave: I am your virtual assistant thanks for reaching out to me. I got all you need about Digital Core Service.Please Select the one you want to inquire for."))),
                    divider(),
                    actions(actions -> actions
                            .elements(asElements(getStaticSelectElementService()))
                    ),
                    section(section -> section.text(markdownText("To Onboard new application please click on below button :point_down: "))),
                    divider(),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("Onboard").style("primary").text(plainText(pt -> pt.text("Add New Application"))).value("Onboard"))
                            ))
                    )
            ));
            return ctx.ack();
        });

        app.blockAction("Service Catalog", (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                String service = req.getPayload().getActions().get(0).getSelectedOption().getValue();
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about " + service + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(service)))
                                )
                        )));
            }
            return ctx.ack();
        });

        app.blockAction("Get Info", (req, ctx) -> {
            String[] value = req.getPayload().getActions().get(0).getSelectedOption().getValue().split("::");
//            String service = value.substring(value.lastIndexOf("- ") + 2);
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Here is the " + value[0] + " information for " + value[1] + ":\n(Insert information here)"))),
                                // Insert info here
                                divider(),
                                section(section -> section.text(markdownText("Do you want to know anything else about this service?" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(
                                                button(b -> b.actionId(value[1]).style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
                                                button(b -> b.actionId("NO").style("danger").text(plainText(pt -> pt.text("NO"))).value("NO"))
                                        ))
                                )
                        )));
            }
            return ctx.ack();
        });

        for (String service : services) {
            blockActionService(service, app);
        }

        app.blockAction("NO", (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Goodbye I! :wave: If you wish to revisit services please click on below button :"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementService()))
                                )
                        ))
                );
            }
            return ctx.ack();
        });

        new SlackAppServer(app).start();
        return app;
    }

    private void blockActionService(String service, App app) {
        app.blockAction(service, (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about Member Service " + service + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(service)))
                                )
                        )));
            }
            return ctx.ack();
        });
    }

    private List<OptionObject> serviceList() {
        List<OptionObject> serviceList = new ArrayList<>();
        for (String s : services) {
            serviceList.add(option(plainText(s), s));
        }
        return serviceList;
    }

    private List<OptionObject> categoryList(String service) {
        List<OptionObject> categoryList = new ArrayList<>();
        for (String s : categories) {
            categoryList.add(option(plainText(s), s + "::" + service));
        }
        return categoryList;
    }

    private StaticSelectElement getStaticSelectElementService() {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Service Catalog")
                .options(serviceList()).build();
    }

    private StaticSelectElement getStaticSelectElementCategory(String service) {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Get Info")
                .options(categoryList(service)).build();
    }
}