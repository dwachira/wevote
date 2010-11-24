package org.wevote.client.chart.chart;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.charts.PieChart;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

import com.google.gwt.user.client.ui.FlexTable;

import org.wevote.client.chart.model.Content;

/**
 * Inner tab for question. Displays 2 pie charts by gender.
 *
 * @author NorthernDemon
 */

public class theChartByGender extends LayoutContainer {

    public theChartByGender() {
        Chart chartMale = new Chart("images/gxt/open-flash-chart.swf");
        chartMale.setChartModel(getChartModel(0, "Male"));
        chartMale.setSize(400, 400);

        Chart chartFemale = new Chart("images/gxt/open-flash-chart.swf");
        chartFemale.setChartModel(getChartModel(1, "Female"));
        chartFemale.setSize(400, 400);

        FlexTable table = new FlexTable();
        table.setWidth("100%");

        table.setWidget(1, 0, chartMale);
        table.setWidget(1, 1, chartFemale);

        add(table);
    }

    /**
     * Gender groups:
     *
     * 0 — male
     * 1 — female
     *
     * @param gender_int number of gender group
     * @param gender_str name of gender group
     *
     * @return pie ChartModel
     */
    private ChartModel getChartModel(int gender_int, String gender_str) {
        ChartModel cm = new ChartModel(gender_str, "font-size: 14px;");
        cm.setBackgroundColour("#ffffff");

        Legend lg = new Legend(Position.RIGHT, true);
        lg.setPadding(10);
        cm.setLegend(lg);

        PieChart pie = new PieChart();
        pie.setAlpha(0.5f);
        pie.setGradientFill(true);
        pie.setAlphaHighlight(true);
        pie.setTooltip("#label# #val# point(s)<br>#percent#");
        pie.setColours("#ff0000", "#00aa00", "#0000ff", "#ff9900", "#ff00ff", "#00ff00");

        for (int i = 0; i < Content.questions.getAnswers().size(); i++) {
            if (!Content.questions.getAnswers().get(i).getAnswerByGender().get(gender_int).equals(0)) {
                pie.addSlices(new PieChart.Slice(Content.questions.getAnswers().get(i).getAnswerByGender().get(gender_int), "A" + String.valueOf(i+1), Content.questions.getAnswers().get(i).getAnswer()));
            }
        }
        
        cm.addChartConfig(pie);

        return cm;
   }

}