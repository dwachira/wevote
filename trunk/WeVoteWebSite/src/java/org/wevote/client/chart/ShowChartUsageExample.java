package org.wevote.client.chart;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.wevote.client.MainEntryPoint;
import org.wevote.client.chart.model.Content;

/**
 * Uses as central panel.
 * Ablility to show Error, System or Question tab.
 *
 * Init state is System tab.
 *
 * If tab is already opened, no need to request it via ajax.
 *
 * @author NorthernDemon
 */

public class ShowChartUsageExample {

    final static AsyncCallback<String> callback = new AsyncCallback<String>() {
        @Override
        public void onSuccess(String result) {
            Content.addQuestion(result);
            MainEntryPoint.theTabPanel.addTab(Content.questions, result, true);
        }

        @Override
        public void onFailure(Throwable caught) {
            MainEntryPoint.theTabPanel.addErrorTab();
        }
    };

    public ShowChartUsageExample() {
        MainEntryPoint.theTabPanel.addSystemTab();
    }

    public static void openTab() {
        MainEntryPoint.theTabPanel.showTab();
    }

    public static void openTab(int id) {
        if (!MainEntryPoint.theTabPanel.isExists(id)) {
            getService().myMethod(id, ShowChartUsageExample.callback);
        }
    }

    public static ShowChartAsync getService() {
        return GWT.create(ShowChart.class);
    }

}