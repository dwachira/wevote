package org.wevote.client.chart.tab;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.Style.Scroll;

import com.google.gwt.user.client.History;

import org.wevote.client.icons.Icons;

/**
 * Provides items for theTabPanel.java
 *
 * @author NorthernDemon
 */

public class theTabItem extends TabItem {

    /**
     * Creates System or Error tabs
     *
     * @param title name of the tab
     * @param closable defines wether tab can be closed or not (System tab is permanent, Error tab is temporary)
     */
    public theTabItem(String title, Boolean closable) {
        setText(title);
        setClosable(closable);
        setScrollMode(Scroll.AUTO);
        addStyleName("center");

        if (title.equals("Communication Error")) { // Error Tab
            setItemId("-1");
            add(new Error());
            setIcon(Icons.Images.error());
        } else if (title.equals("System Overview")) { // System Tab
            setItemId("0");
            add(new System());
            setIcon(Icons.Images.system());
        }
    }
    
    /**
     * Creates Question tab
     *
     * @param title name of the tab
     * @param closable defines wether tab can be closed or not (System tab is permanent, Error tab is temporary)
     * @param id number of question for RPC
     */
    public theTabItem(String title, String text, Boolean closable, int id) {
        this(title, closable);
        
        setItemId(String.valueOf(id));
        add(new Overview(text));
        setIcon(Icons.Images.tab());
    }

    @Override
    protected void onShow() {
        super.onShow();
        if (getItemId().equals("0")) {
            History.newItem("/system/");
        } else if (!getItemId().equals("-1")) {
            History.newItem("/question/" + getItemId());
        }
    }

    @Override
    protected void onRemove(Component item) {
        super.onRemove(item);
        History.back();
    }

}