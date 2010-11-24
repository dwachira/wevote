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
 * Inner tab for question. Displays 6 pie charts by age groups.
 *
 * @author NorthernDemon
 */

public class theChartByAge extends LayoutContainer {
    
    public theChartByAge() {
        Chart chart1 = new Chart("images/gxt/open-flash-chart.swf");
        chart1.setChartModel(getChartModel(0, "+17"));
        chart1.setSize(400, 400);

        Chart chart2 = new Chart("images/gxt/open-flash-chart.swf");
        chart2.setChartModel(getChartModel(1, "18..24"));
        chart2.setSize(400, 400);
        
        Chart chart3 = new Chart("images/gxt/open-flash-chart.swf");
        chart3.setChartModel(getChartModel(2, "25..34"));
        chart3.setSize(400, 400);

        Chart chart4 = new Chart("images/gxt/open-flash-chart.swf");
        chart4.setChartModel(getChartModel(3, "35..44"));
        chart4.setSize(400, 400);

        Chart chart5 = new Chart("images/gxt/open-flash-chart.swf");
        chart5.setChartModel(getChartModel(4, "45..54"));
        chart5.setSize(400, 400);

        Chart chart6 = new Chart("images/gxt/open-flash-chart.swf");
        chart6.setChartModel(getChartModel(5, "55+"));
        chart6.setSize(400, 400);

        FlexTable table = new FlexTable();
        table.setWidth("100%");

        table.setWidget(1, 0, chart1);
        table.setWidget(1, 1, chart2);

        table.setWidget(2, 0, chart3);
        table.setWidget(2, 1, chart4);

        table.setWidget(3, 0, chart5);
        table.setWidget(3, 1, chart6);

        add(table);
    }


    /**
     * Gender groups:
     *
     * 0 — +17
     * 1 — 18..24
     * 2 — 25..34
     * 3 — 35..44
     * 4 — 45..54
     * 5 — 55+
     *
     * @param age_group_int number of age group
     * @param age_group_str name of age group
     *
     * @return ChartModel
     */
    private ChartModel getChartModel(int age_group_int, String age_group_str) {
        ChartModel cm = new ChartModel("Age group: " + age_group_str, "font-size: 14px;");
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
            if (!Content.questions.getAnswers().get(i).getAnswerByAge().get(age_group_int).equals(0)) {
                pie.addSlices(new PieChart.Slice(Content.questions.getAnswers().get(i).getAnswerByAge().get(age_group_int), "A" + String.valueOf(i+1), Content.questions.getAnswers().get(i).getAnswer()));
            }
        }

        cm.addChartConfig(pie);

        return cm;
   }

}