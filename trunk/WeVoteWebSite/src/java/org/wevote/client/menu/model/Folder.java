package org.wevote.client.menu.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

/**
 * Contains Folders with Items in it
 *
 * @author NorthernDemon
 */

public class Folder extends BaseTreeModel {

    private static final long serialVersionUID = 1L;
    private static int ID = 0;

    /**
     * Set id for folder, which is pool id
     */
    public Folder() {
        set("id", ID++);
    }

    /**
     * @param title name of the pool
     */
    public Folder(String title) {
        this();
        set("title", title);
    }

    /**
     * @param children Item
     */
    public void addItem(BaseTreeModel children) {
        add(children);
    }

    /**
     * @return id for folder, which is pool id
     */
    public Integer getId() {
        return (Integer) get("id");
    }

    /**
     * @return title name of the pool
     */
    public String getTitle() {
        return (String) get("title");
    }

}