package com.bolt.slack;

import com.bolt.slack.entities.Categories;
import com.bolt.slack.entities.ConfluencePage;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.element.StaticSelectElement;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;
import static com.slack.api.model.view.Views.*;

@Configuration
public class SlackApp {
    ConfluencePage[] confluencePage = {
        new ConfluencePage(
            "293674599",
            "Card Member Service Soap",
            "<div>SOAP</div>",
            "Member Service Soap",
            "endpoint.aexp.com/membersoap",
            "ci.aexp.com/membersoap",
            "github.aexp.com\ngithub.aexp2.com",
            "qa-confluence.aexp.com",
            "webex.aexp.com/meetinglinksoap",
            "swagger.aexp.com"
        ),
        new ConfluencePage(
            "873945712",
            "Card Member Service Rest",
            "<div>REST</div>",
            "Member Service Rest",
            "endpoint.aexp.com/memberrest",
            "ci.aexp.com/memberrest",
            "github.aexp.com\ngithub.aexp2.com",
            "qa-confluence.aexp.com",
            "webex.aexp.com/meetinglinkrest",
            "swagger.aexp.com"
        ),
        new ConfluencePage(
            "928347596",
            "GSIS Service Rest",
            "<div>GSIS</div>",
            "GSIS Service Rest",
            "endpoint.aexp.com/member2",
            "ci.aexp.com/member2",
            "github.aexp.com2\ngithub.aexp2.com2",
            "qa-confluence.aexp.com2",
            "webex.aexp.com/meetinglink2",
            "swagger.aexp.com2"
        )
    };

    private HashMap<String, ConfluencePage> confluencePageHashMap = new HashMap<>();

    @Bean
    public App initStackApp() throws Exception {
        for (ConfluencePage c : confluencePage)
            confluencePageHashMap.put(c.getPageId(), c);

        AppConfig appConfig = new AppConfig();
        appConfig.setRequestVerificationEnabled(false);
        appConfig.setSslCheckEnabled(false);
        App app = new App(appConfig);

        for (ConfluencePage service : confluencePageHashMap.values()) {
            blockActionService(service.getPageId(), app);
        }

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
                String pageId = req.getPayload().getActions().get(0).getSelectedOption().getValue();
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about " + confluencePageHashMap.get(pageId).getServiceName() + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(pageId)))
                                )
                        )));
            }
            return ctx.ack();
        });

        app.blockAction("Get Info", (req, ctx) -> {
            String[] value = req.getPayload().getActions().get(0).getSelectedOption().getValue().split("::");
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Here is the " + value[0].replace("_", " ") + " information for " + confluencePageHashMap.get(value[1]).getServiceName() + ":\n" + confluencePageHashMap.get(value[1]).getCategoryInfo(value[0])))),
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

    private void blockActionService(String pageId, App app) {
        app.blockAction(pageId, (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about Member Service " + confluencePageHashMap.get(pageId).getServiceName() + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(pageId)))
                                )
                        )));
            }
            return ctx.ack();
        });
    }

    private List<OptionObject> serviceList() {
        List<OptionObject> serviceList = new ArrayList<>();
        for (ConfluencePage s : confluencePageHashMap.values()) {
            serviceList.add(option(plainText(s.getServiceName()), s.getPageId()));
        }
        return serviceList;
    }

    private List<OptionObject> categoryList(String pageId) {
        List<OptionObject> categoryList = new ArrayList<>();
        for (Categories category : Categories.values()) {
            categoryList.add(option(plainText(category.toString().replace("_", " ")), category.toString() + "::" + pageId));
        }
        return categoryList;
    }

    private StaticSelectElement getStaticSelectElementService() {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Service Catalog")
                .options(serviceList()).build();
    }

    private StaticSelectElement getStaticSelectElementCategory(String pageId) {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Get Info")
                .options(categoryList(pageId)).build();
    }
}


    private HashMap<String, ConfluencePage> confluencePageHashMap;
    private static final VaultReader VAULT_READER = VaultReader.getInstance();
    private String BOT_TOKEN = VAULT_READER.get("slack_bot_token");
    private String SIGNING_SECRET = VAULT_READER.get("slack_signing_secret");
    Logger logger = LoggerFactory.getLogger(SlackApp.class);
    @Autowired
    ConfluenceService confluenceService;


    @Bean
    public App initSlackApp() throws Exception {

        AppConfig appConfig = new AppConfig();
        appConfig.setSingleTeamBotToken(BOT_TOKEN);
        appConfig.setSigningSecret(SIGNING_SECRET);
        appConfig.getSlack().getConfig().setProxyUrl("http://PHXAPPGWE2-VIP.phx.aexp.com:9090");
        logger.info(String.format("Bot_token: %s\nSigning_Secret:%s\nProxy:%s",appConfig.getSingleTeamBotToken(),appConfig.getSigningSecret(),appConfig.getSlack().getConfig().getProxyUrl()));

        App app = new App(appConfig);


        app.command("/services", (req, ctx) -> {
            reloadConfluencePages(app);

            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + "! :wave: Thanks for reaching out to me. I got all you need about Digital Core Service. Please Select the one you want to inquire for."))),
                            divider(),
                            actions(actions -> actions
                                    .elements(asElements(getStaticSelectElementService()))
                            ),

                            section(section -> section.text(markdownText("To Onboard new application please click on button below :point_down:"))),
                            divider(),
                            actions(actions -> actions
                                    .elements(asElements(
                                            button(b -> b.actionId("Onboard").style("primary").text(plainText(pt -> pt.text("Add New Application"))).value("Onboard"))

                                    ))
                            )
                    ))
            );
            return ctx.ack();
        });

        Pattern pattern = Pattern.compile("[a-zA-Z ]*", Pattern.CASE_INSENSITIVE);
        app.message(pattern, (payload, ctx) -> {
            reloadConfluencePages(app);
            ctx.say(asBlocks(
                    section(section -> section.text(markdownText("Hello! :wave: I am your virtual assistant thanks for reaching out to me. I got all you need about Digital Core Service. Please Select the one you want to inquire for."))),
                    divider(),
                    actions(actions -> actions
                            .elements(asElements(getStaticSelectElementService()))
                    ),
                    section(section -> section.text(markdownText("To Onboard new application please click on below button :point_down:"))),
                    divider(),
                    actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.actionId("Onboard").style("primary").text(plainText(pt -> pt.text("Add New Application"))).value("Onboard"))

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

        app.viewSubmission("new-Onboarding", (req, ctx) -> {
            String channel = "D03SVP0NXRR";
            Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();

            ConfluencePage confluencePage = new ConfluencePage();
            confluencePage.setTitle(stateValues.get("Service_Name").get("Service_Name").getValue());
            confluencePage.setServiceName(stateValues.get("Service_Name").get("Service_Name").getValue());
            confluencePage.setEndpointURL(stateValues.get(Categories.Endpoint_URL.name()).get(Categories.Endpoint_URL.name()).getValue());
            confluencePage.setJenkinsJob(stateValues.get(Categories.Jenkins_Job.name()).get(Categories.Jenkins_Job.name()).getValue());
            confluencePage.setGithubURL(stateValues.get(Categories.Github_URL.name()).get(Categories.Github_URL.name()).getValue());
            confluencePage.setConfluenceLinks(stateValues.get(Categories.Confluence_Link.name()).get(Categories.Confluence_Link.name()).getValue());
            confluencePage.setKtRecordings(stateValues.get(Categories.KT_Recordings.name()).get(Categories.KT_Recordings.name()).getValue());
            Map<String, String> errors = new HashMap<>();
            if (confluencePage.getServiceName().length() <= 1) {
                errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
            }
            if (!errors.isEmpty()) {
                return ctx.ack(r -> r.responseAction("errors").errors(errors));
            } else {
                //TODO
                confluenceService.postConfluenceApi(confluencePage);
                reloadConfluencePages(app);

                ChatPostMessageResponse postMessageResponse = ctx.client().chatPostMessage(r -> r
                        .channel(channel)
                        .text("Your service has been successfully onboarded! The service name is: " + confluencePage.getServiceName())
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Your application has been successfully onboarded! :partying_face:"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementService()))
                                ))));
                if (postMessageResponse.isOk()) {
                    System.out.println("Successfully sent message to channel: " + channel);
                } else {
                    System.out.println("Error sending message to channel: " + postMessageResponse.getError());
                }
            }

            return ctx.ack();

        });

        app.blockAction("Service Catalog", (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                String pageId = req.getPayload().getActions().get(0).getSelectedOption().getValue();
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about " + confluencePageHashMap.get(pageId).getServiceName() + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(pageId)))
                                )
                        )));
            }
            return ctx.ack();
        });

        app.blockAction("Get Info", (req, ctx) -> {
            String[] value = req.getPayload().getActions().get(0).getSelectedOption().getValue().split("::");
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Here is the " + value[0].replace("_", " ") + " information for " + confluencePageHashMap.get(value[1]).getServiceName() + ":\n" + confluencePageHashMap.get(value[1]).getCategoryInfo(value[0])))),
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

        app.blockAction("NO", (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("Goodbye!! :wave: If you wish to revisit services please select an option below:"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementService()))
                                )
                        ))

                );
            }
            return ctx.ack();
        });

        return app;
    }

    private void reloadConfluencePages(App app) throws IOException {
        ArrayList<ConfluencePage> confluencePages = confluenceService.getPagesByLabel("pitstop");
        confluencePageHashMap = new HashMap<>();
        for (ConfluencePage confluencePage : confluencePages)
            confluencePageHashMap.put(confluencePage.getPageId(), confluencePage);
        for (ConfluencePage service : confluencePageHashMap.values()) {
            blockActionService(service.getPageId(), app);
        }
    }

    private void blockActionService(String pageId, App app) {
        app.blockAction(pageId, (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about " + confluencePageHashMap.get(pageId).getServiceName() + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(pageId)))
                                )
                        )));
            }
            return ctx.ack();
        });
    }

    private List<OptionObject> serviceList() {
        List<OptionObject> serviceList = new ArrayList<>();

        for (ConfluencePage page : confluencePageHashMap.values()) {
            serviceList.add(option(plainText(page.getServiceName()), page.getPageId()));
        }

        return serviceList;
    }

    private List<OptionObject> categoryList(String pageId) {
        List<OptionObject> categoryList = new ArrayList<>();

        for (Categories category : Categories.values()) {
            categoryList.add(option(plainText(category.toString().replace("_", " ")), category.toString() + "::" + pageId));
        }

        return categoryList;
    }

    private StaticSelectElement getStaticSelectElementService() {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Service Catalog")
                .options(serviceList()).build();
    }

    private StaticSelectElement getStaticSelectElementCategory(String pageId) {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Get Info")
                .options(categoryList(pageId)).build();
    }

    private List<LayoutBlock> inputBlockCategory() {
        List<LayoutBlock> blocks = new ArrayList<>();
        blocks.add(input(input -> input
                .blockId("Service_Name")
                .element(plainTextInput(pti -> pti.actionId("Service_Name").multiline(true)))
                .label(plainText(pt -> pt.text("Service Name : ").emoji(true)))
        ));
        for (Categories category : Categories.values()) {
            blocks.add(input(input -> input
                    .blockId(category.name())
                    .element(plainTextInput(pti -> pti.actionId(category.name()).multiline(true)))
                    .label(plainText(pt -> pt.text("Enter " + category.name().replace("_", " ") + " info: ").emoji(true)))
            ));
        }
        return blocks;
    }

    private View getModalView() {
        return View.builder()
                .callbackId("new-Onboarding")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Onboarding New Service").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                .blocks(inputBlockCategory()).build();
    }
}

