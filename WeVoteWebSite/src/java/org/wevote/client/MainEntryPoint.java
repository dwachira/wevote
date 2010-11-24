package org.wevote.client;

import com.extjs.gxt.ui.client.util.Margins;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import org.wevote.client.chart.ShowChartUsageExample;
import org.wevote.client.chart.tab.theTabPanel;

import org.wevote.client.menu.ShowMenuUsageExample;

import org.wevote.client.icons.Icons;

/**
 * Entry point for web-site
 *
 * Creates major panels and layouts
 *
 * Supports history token by
 *  — opening proper tab
 *  — expanding proper folder
 *  — back & forward browser buttons
 *  — if requested tab is already opened, no need to load it form server, instead show it & update history
 *
 * @author NorthernDemon
 */

public class MainEntryPoint implements EntryPoint, ValueChangeHandler<String> {

    // Central panel for Charts
    public static theTabPanel theTabPanel = new theTabPanel();
    private boolean notFirstTime;

    @Override
    public void onModuleLoad() {
        Viewport viewport = new Viewport();

        viewport.setLayout(new BorderLayout());

        // West panel for Folders
        ContentPanel west = new ContentPanel();
        west.setHeading("Pools History List");
        west.setScrollMode(Scroll.AUTOY);
        west.setIcon(Icons.Images.history());

        //Settings for panels
        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 125);
        northData.setCollapsible(true);
        northData.setMargins(new Margins(0,0,0,0));

        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 235);
        westData.setCollapsible(true);
        westData.setMargins(new Margins(0,5,0,5));

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, 1);
        centerData.setMargins(new Margins(0,5,0,0));

        BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 40);
        southData.setMargins(new Margins(0,0,0,0));

        //Assign Menu panel
        west.add(new ShowMenuUsageExample());

        //Assign Chart panel
        new ShowChartUsageExample();

        //Assign panels to the web-document
        viewport.add(new Html("<div class='logo'><img src='./images/wevote.png' class='img_logo' /></div>"), northData);
        viewport.add(west, westData);
        viewport.add(theTabPanel, centerData);
        viewport.add(new Html("<div class='footer'>Copyright &copy; 2010 <a href='http://www.mikkeliamk.fi/' target='_blank'>Mikkeli University of Applied Sciences</a></div>"), southData);

        RootPanel.get().add(viewport);

        //History events starts
        History.addValueChangeHandler(this);
        History.fireCurrentHistoryState();

        // Check is it first time in current session
        notFirstTime = true;
        String reference = null;

        if (!History.getToken().isEmpty()) {
            reference = History.getToken();
        }

        History.newItem("/system/");

        if (reference.contains("/question/") && !reference.substring(reference.lastIndexOf("/") + 1).equals("")) {
            ShowMenuUsageExample.openTreeOnLoad(reference.substring(reference.lastIndexOf("/") + 1));
            History.newItem("/question/" + reference.substring(reference.lastIndexOf("/") + 1));
        }
    }

    /**
     * History is separated on following sectors:
     *
     * Question — explains number of question to load
     * System — uses for entry system tab
     *
     * Axaj is handaling RPC in that way:
     *
     * http://wevote.com/#/question/45 — load Question number 45 (if does not exists — load Error tab)
     * http://wevote.com/#/system — load System tab
     *
     * @see http://code.google.com/p/wevote/wiki/HistoryToken
     *
     * @param event URI string
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (notFirstTime && event.getValue().contains("/question/") && !event.getValue().substring(event.getValue().lastIndexOf("/") + 1).equals("")) {
            ShowChartUsageExample.openTab(Integer.parseInt(event.getValue().substring(event.getValue().lastIndexOf("/") + 1)));
            ShowMenuUsageExample.openTree(event.getValue().substring(event.getValue().lastIndexOf("/") + 1));
        } else if (notFirstTime && event.getValue().contains("/system/")) {
            ShowChartUsageExample.openTab();
            ShowMenuUsageExample.openTree();
        }
    }

}