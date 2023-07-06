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
    private final String[] services = {"REST", "SOAP", "Other"};
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
                            section(section -> section.text(markdownText("Hello "+ req.getPayload().getUserName() + "! :wave: Thanks for reaching out to me. I got all you need about Digital Core Service. Please Select the one you want to inquire for."))),
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

//        app.blockAction ("Onboard", (req, ctx) -> {
//            if (req.getPayLoad().getResponseUrt() != null) {
//                Vlew view = getModalView();
//                app.client().vlewsOpen (r -> r
//                        .triggerId(req.getPayload().getTriggerId())
//                        .view(view));
//            }
//            return ctx.ack();
//        });

        // app.viewSubmission("new-Onboarding", (req, ct) -> {
        // String channel = "DO3SVPONXRR";
        // Map<String, Map<String, ViewState. Value>> stateValues = req. getPayload() . getView() • getState() • getValues();
        // String title = stateValues. get ("Service-Name"). get ('Service-Name"). getValue();
        // String serviceName = stateValues. get ("Service-Name"). get ("Service-Name") •getValue();
        // String endpointURL = stateValues. get ("Endpoint-URL"). get ("Endpoint-URL") .getValue ();
        // String jenkinsJob = stateValues.get ("Jenkins-Job") .get ("Jenkins-Job").getValue();
        // String githubURL = stateValues.get("Github-URL"). get ("Github-URL"). getValue);
        // String confluenceLinks = stateValues. get ("Confluence-Links").get("Confluence-links") •getValue);
        // String ktRecordings = stateValues. get ("KT-Recordings") .get ("KT-Recordings") • getValue);
        // Map<String, String> errors = new HashMap<>0);
        // if (serviceName. length <= 1) {
        // errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
        // A9 A3 43 1
        // }
        // if (lerrors. isEmpty0) {
        // return ctx.ack(r -> r.responseAction("errors") .errors(errors));
        // ] else {
        // //TODO confluenceService.postConfluenceApi(title,serviceName,endpointURL,jenkinsJob,githubURL,confluenceLinks,ktRecordings)
        // ChatPostMessageResponse postMessageResponse = ct.client .chatPostMessage (r -> r
        // •channel (channel.)
        // •text ("Your service has been successfully onboarded! The service name is:
        // " + serviceName)
        // .blocks(asBlocks
        // section (section -> section.text (markdownText ("Your application has been successfully onboarded!
        // :partying_face:"))), dividerO,
        // actions (actions -> actions
        // .elements (asElements
        // button(b -> b.actionId("REST") .style("primary") .textplainText(pt -> pt.text("Member
        // Service REST")))),
        // button(b -> b.actionId("SOAP") .style("primary") .text plainText (pt -> pt.text("Member
        // Service SOAP")))),
        // button(b -> b.actionId (serviceName) .style("primary") .text (plainText(pt -> pt.text (serviceNlame))))
        // ))
        // )));
        // if (postMessageResponse.is0k()) {
        // System.out .printin("Successfully sent message to channel:
        // " + channel);
        // } else {
        // System.out.println("Error sending message to channel:
        // " + postMessageResponse.getError));
        // }
        // 子
        // return ctx.ack0;

        app.blockAction("Member Service", (req, ctx) -> {
            if (req.getPayload().getResponseUrl() != null) {
                String actionId = req.getPayload().getActions().get(0).getSelectedOption().getValue();
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                section(section -> section.text(markdownText("What do you want to know about Member Service " + actionId + "? Please select from one of the options" + "\n"))),
                                divider(),
                                actions (actions -> actions
                                        .elements(asElements(getStaticSelectElementCategory(actionId)))
                                )
                        )));
            }
            return ctx.ack();
        });

        app.blockAction("Get Info", (req, ctx) -> {
            String value = req.getPayload().getActions().get(0).getSelectedOption().getValue();
            String[] services = value.split(" ");
            String service = services[services.length - 1];
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(
                                //FIXME String value and pageContent value do not match
                                section(section -> section.text(markdownText(value + ":\n"))),
                                // Insert info here
                                divider(),
                                section(section -> section.text(markdownText("Do you want to know anything else about this service?" + "\n"))),
                                divider(),
                                actions(actions -> actions
                                        .elements(asElements(
                                                button(b -> b.actionId(service).style("primary").text(plainText(pt -> pt.text("YES"))).value("YES")),
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
                                actions (actions -> actions
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
            categoryList.add(option(plainText(s), s + " " + service));
        }
        return categoryList;
    }

    private StaticSelectElement getStaticSelectElementService() {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Member Service")
                .options(serviceList()).build();
    }

    private StaticSelectElement getStaticSelectElementCategory(String service) {
        return StaticSelectElement.builder()
                .placeholder(plainText("Select an option"))
                .actionId("Get Info")
                .options(categoryList(service)).build();
    }

    // 	private View getModalView()
    // View view = View.builder()
    // .callbackId("new-Onboarding")
    // .type ("modal")
    // .notifyOnClose (true)
    // title (viewTitle(title -> title.type("plain_text"). text ("Onboarding New Service") emoji(true)))
    // .submit (viewSubmit (submit -> submit.type ("plain_text"). text ("Submit").emoji(true)))
    // •cLose (viewCLose(close -> close. type ("plain_text").text ("Cancel").emoji(true)))
    // blocks (asBlocks (
    // input (input -> input
    // .blockId("Service-Name")
    // element (plainTextInput(pti->pti.actionId("Service-Name").multiline(true)))
    // .label (plainText (pt -> pt. text ("Service Name
    // ") ¿emoji(true)))
    // ),
    // input (input -> input
    // .blockId("Endpoint-URL")
    // .element (plainTextInput (pti -> pti.actionId("Endpoint-URL") multiline (true)))
    // •label (plainText(pt -> pt.text("Enter Endpoint URL's : ").emoji(true)))
    // input (input -> input
    // .blockId("Jenkins-Job")
    // •element (plainTextInput(pti-> pti.actionId("Jenkins-Job").multiline(true)))
    // •label(plainText(pt -> pt. text ("Enter Jenkins Job details: ").emoji(true)))
    // );
    // input (input -> input
    // blockId("Github-URL")
    // element (plainTextInput(pti-> pti.actionId("Github-URL") .multiline (true)))
    // •label(plainText(pt -> pt.text("Enter Github URL's : ").emoji(true)))
    // ),
    // input (input -> input
    // .blockId("Confluence-Links")
    // •eLement (plainTextInput (pti -> ptiactionId("Confluence-Links").multiline(true)))
    // •Label (plainText(pt -> pt. text ("Enter Confluence pages Links : ").emoji(true)))
    // ).
    // input (input -> input
    // blockId ("KT-Recordings")
    // .element (plainTextInput (pti -> ptiactionId("KT-Recordings").multiline(true)))
    // .label(plainText(pt -> pt.text("Enter KT Recordings links
    // ").emoji(true)))
    // )
    // ).buildO;
    // return view;
}