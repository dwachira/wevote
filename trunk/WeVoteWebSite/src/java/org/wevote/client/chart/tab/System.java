package org.wevote.client.chart.tab;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

/**
 * Layout for System tab with info about WeVote capability
 *
 * @author NorthernDemon
 */

public class System extends LayoutContainer {

    public System() {
        addText("<div class='title'>&bull; System Overview</div>");
        addText("<div class='text'>" +
                "<div style='font-size: 1.2em;font-weight: bold;'>What does system?</div>" +
                "<div>Mobile Voting System allows to provide voting among multiple users using mobile phone SMS technology and publish outcome to the web-server.</div>" +
                "<div>In order to get system work you need:</div>" +
                    "<div style='padding-left: 1em;'>&bull; GSM modem and driver for it</div>" +
                    "<div style='padding-left: 1em;'>&bull; JDK 6 or latest</div>" +
                    "<div style='padding-left: 1em;'>&bull; WeVote program</div>" +
                "<div>In downloading tab you can find drivers for mc35 GSM modem, which was used as an example, documentations, WeVote program and putty program for testing connections with modem.</div>" +
                "</div>");
        addText("<div class='text'>" +
                "<div style='font-size: 1.2em;font-weight: bold;'>What does system for user?</div>" +
                    "<div style='padding-left: 1em;'>&bull; Users sends sms to GSM modem</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; if not registered &mdash; containing age and gender \"M16\"</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; if registered &mdash; empty</div>" +
                    "<div style='padding-left: 1em;'>&bull; GSM modem veryfies users</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; if not registered & sms is empty &mdash; send request for age & gender</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; if registered &mdash; pick user from database</div>" +
                    "<div style='padding-left: 1em;'>&bull; Admin creates pool and questions &mdash; multiple choice</div>" +
                    "<div style='padding-left: 1em;'>&bull; GSM modem sends question sms to the users</div>" +
                    "<div style='padding-left: 1em;'>&bull; Users replies to questions &mdash; \"1a2abc3c4ca\"</div>" +
                    "<div style='padding-left: 1em;'>&bull; Program collects sms into database</div>" +
                "</div>");
        addText("<div class='text'>" +
                "<div style='font-size: 1.2em;font-weight: bold;'>What does system for admin?</div>" +
                    "<div style='padding-left: 1em;'>&bull; Login</div>" +
                    "<div style='padding-left: 1em;'>&bull; Settings for GSM modem</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; SIM-card code</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; Port number</div>" +
                    "<div style='padding-left: 1em;'>&bull; Collects list of users</div>" +
                    "<div style='padding-left: 1em;'>&bull; Checks amount of money in SIM-card</div>" +
                    "<div style='padding-left: 1em;'>&bull; Creates pool and questions</div>" +
                    "<div style='padding-left: 1em;'>&bull; Received answers</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; correct &mdash; accept & upload into database</div>" +
                        "<div style='padding-left: 1.5em;'>&bull; incorrect &mdash; resubmit question</div>" +
                    "<div style='padding-left: 1em;'>&bull; Logout</div>" +
                "</div>");
        addText("<div class='text'>" +
                "<div style='font-size: 1.2em;font-weight: bold;'>Problems with modem?</div>" +
                "<div>Testing the connections is easy:</div>" +
                    "<div style='padding-left: 1em;'>&bull; plugin <b>\"GSM modem\"</b> and run <b>\"putty\"</b></div>" +
                    "<div style='padding-left: 1em;'>&bull; choose <b>\"Serial\"</b> option and <b>\"COM-port\"</b></div>" +
                    "<div style='padding-left: 1em;'>&bull; in opened terminal type <b>\"AT\"</b></div>" +
                    "<div style='padding-left: 1em;'>&bull; correct respons from modem must be <b>\"OK\"</b></div>" +
                "</div>");
    }

}