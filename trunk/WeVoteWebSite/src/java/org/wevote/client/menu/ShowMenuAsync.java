package org.wevote.client.menu;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author NorthernDemon
 */

public interface ShowMenuAsync {

    /**
     * Load menu items
     */
    public void myMethod(AsyncCallback<String> callback);
}