package com.bolt.slack;

import com.bolt.slack.entities.Categories;
import com.bolt.slack.entities.ConfluencePage;
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
    private final String[] services = {
        "Member Service REST",
        "Member Service SOAP",
        "Other"
    };

    private final String[] categories = {
        "Endpoint URL",
        "Jenkins Job",
        "Github URL",
        "Confluence Link",
        "KT Recordings",
        "Swagger Link"
    };

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
            for (String s : value) {
                System.out.println(s);
            }
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
//        categoryList.add(option(plainText("Jenkins Job"), pageId));
//        categoryList.add(option(plainText("GitHub URL"), pageId));
//        categoryList.add(option(plainText("Confluence Links"), pageId));
//        categoryList.add(option(plainText("KT Recordings"), pageId));
//        categoryList.add(option(plainText("Swagger Link"), pageId));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getEndpointURL()), confluencePageHashMap.get(pageId).getEndpointURL()));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getJenkinsJob()), confluencePageHashMap.get(pageId).getJenkinsJob()));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getGithubURL()), confluencePageHashMap.get(pageId).getGithubURL()));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getConfluenceLinks()), confluencePageHashMap.get(pageId).getConfluenceLinks()));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getKtRecordings()), confluencePageHashMap.get(pageId).getKtRecordings()));
//        categoryList.add(option(plainText(confluencePageHashMap.get(pageId).getSwaggerLink()), confluencePageHashMap.get(pageId).getSwaggerLink()));
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