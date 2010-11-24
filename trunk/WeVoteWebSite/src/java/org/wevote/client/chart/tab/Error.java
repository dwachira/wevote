package org.wevote.client.chart.tab;

import com.extjs.gxt.ui.client.widget.LayoutContainer;

/**
 * Layout for Error tab in case of incapability of loading tab info
 *
 * @author NorthernDemon
 */

public class Error extends LayoutContainer {

    public Error() {
        addText("<div class='error'>&bull; Communication Error</div>");
        addText("<div class='text'>Possible situations:<br />" +
                "&mdash; database system is not running;<br />" +
                "&mdash; question, you are looking for, does not exist;<br />" +
                "</div>");
        addText("<div class='text'>Possible troubleshooting:<br />" +
                "&mdash; reload page (Ctrl + F5);<br />" +
                "&mdash; try other questions;" +
                "</div>");
    }

}