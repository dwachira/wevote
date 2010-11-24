package org.wevote.client.chart.chart;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.BarStyle;

import com.extjs.gxt.charts.client.model.axis.Label;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

import com.google.gwt.user.client.ui.FlexTable;

import org.wevote.client.chart.model.Content;

/**
 * Inner tab for question. Displays bar chart by rating.
 *
 * @author NorthernDemon
 */

public class theChartByRating extends LayoutContainer {

    public theChartByRating() {
        Chart chart = new Chart("images/gxt/open-flash-chart.swf");
        chart.setChartModel(getChartModel());
        chart.setSize(800, 400);

        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.setWidget(1, 0, chart);
        add(table);
    }

    /**
     * Unordered rating statistics.
     *
     * @return bar ChartModel
     */
    private ChartModel getChartModel() {
        ChartModel cm = new ChartModel();
        cm.setBackgroundColour("#ffffff");

        XAxis xa = new XAxis();
        xa.setGridColour("#DFE8F6");

        YAxis ya = new YAxis();
        ya.setMax(2);
        ya.setGridColour("#DFE8F6");

        BarChart bchart = new BarChart(BarStyle.GLASS);
        bchart.setTooltip("#val# point(s)");
        for (int i = 0; i < Content.questions.getAnswers().size(); i++) {
            bchart.addBars(new BarChart.Bar(Content.questions.getAnswers().get(i).getRating(), "#7CA4D9"));
            
            xa.addLabels(new Label(Content.questions.getAnswers().get(i).getAnswer()));

            // Update ya scale so, that ya = rating + 1
            if (ya.getMax().intValue() <= Content.questions.getAnswers().get(i).getRating()) {
                ya.setMax(Content.questions.getAnswers().get(i).getRating() + 1);
            }
        }

        cm.setXAxis(xa);
        cm.setYAxis(ya);

        cm.addChartConfig(bchart);

        return cm;
   }

}