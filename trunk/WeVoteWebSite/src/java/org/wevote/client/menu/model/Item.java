package org.wevote.client.menu.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

/**
 * Contains Items for Folders
 *
 * @author NorthernDemon
 */

public class Item extends BaseTreeModel {

    private static final long serialVersionUID = 1L;

    /**
     * @param title name of the question
     * @param question_id number of the question in database
     */
    public Item(String title, int question_id) {
        set("title", title);
        set("question_id", question_id);
    }

    /**
     * @return title name of the question
     */
    public String getTitle() {
        return (String) get("title");
    }

    /**
     * @return question_id number of the question in database
     */
    public double getQuestionID() {
        Double question_id = (Double) get("question_id");
        return question_id.doubleValue();
    }

}