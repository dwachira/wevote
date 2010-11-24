package org.wevote.client.chart.tab;

import org.wevote.client.chart.chart.theChartByRating;
import org.wevote.client.chart.chart.theChartByGender;
import org.wevote.client.chart.chart.theChartByAge;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabItem;

import org.wevote.client.chart.model.Content;

import org.wevote.client.icons.Icons;

/**
 * Layout for actual tab with question info and 3 supporting tabs
 * with flash charts (pie and bar) for statistics
 *
 * @author NorthernDemon
 */

public class Overview extends LayoutContainer {
    
    public Overview(String json) {
        addText("<div class='title'>&bull; " + Content.questions.getPool() + " / " + Content.questions.getTitle() + "</div>");
        addText("<div class='date'>Date: " + Content.questions.getDate() + "</div>");
        addText("<div class='text'>" + Content.questions.getQuestion() + "</div>");
        addText("<div class='text'>" + json + "</div>");

        TabPanel tabPanelChart = new TabPanel();
        tabPanelChart.setAutoHeight(true);

        TabItem tabItemRating = new TabItem("By Rating");
        tabItemRating.setIcon(Icons.Images.rating());
        tabItemRating.add(new theChartByRating());
        tabPanelChart.add(tabItemRating);

        TabItem tabItemGender = new TabItem("By Gender");
        tabItemGender.setIcon(Icons.Images.gender());
        tabItemGender.add(new theChartByGender());
        tabPanelChart.add(tabItemGender);

        TabItem tabItemAge = new TabItem("By Age");
        tabItemAge.setIcon(Icons.Images.age());
        tabItemAge.add(new theChartByAge());
        tabPanelChart.add(tabItemAge);

        add(tabPanelChart);
    }

}