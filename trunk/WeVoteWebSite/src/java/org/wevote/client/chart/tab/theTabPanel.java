package org.wevote.client.chart.tab;

import org.wevote.client.chart.model.Question;
import com.extjs.gxt.ui.client.widget.TabPanel;

/**
 * Creates main panel, in which theTabItem.java can be placed
 *
 * @author NorthernDemon
 */

public class theTabPanel extends TabPanel {

    public theTabPanel() {
        setAnimScroll(true);
        setTabScroll(true);
        setCloseContextMenu(true);
    }

    public void addErrorTab() {
        theTabItem tab = new theTabItem("Communication Error", true);
        add(tab);
        setSelection(tab);
    }

    public void addSystemTab() {
        theTabItem tab = new theTabItem("System Overview", false);
        add(tab);
        setSelection(tab);
    }

    /**
     * @param question collection of questions and answers for it
     * @param closable defines wether tab can be closed or not
     */
    public void addTab(Question question, String text, Boolean closable) {
        theTabItem tab = new theTabItem(question.getTitle(), text, closable, question.getId());
        add(tab);
        setSelection(tab);
    }

    /**
     * @param pid number of question, if exists — show it, if not — load it
     */
    public boolean isExists(int pid) {
        if (getItemByItemId(String.valueOf(pid)) != null) {
            showTab(String.valueOf(pid));
            return true;
        } else return false;
    }

    /**
     * Always shows System tab, which id is 0
     */
    public void showTab() {
        setSelection(getItemByItemId("0"));
    }

    /**
     * @param pid number of question to show
     */
    public void showTab(String pid) {
        setSelection(getItemByItemId(pid));
    }

}