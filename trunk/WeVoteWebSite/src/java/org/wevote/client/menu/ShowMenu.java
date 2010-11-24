package org.wevote.client.menu;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author NorthernDemon
 */

@RemoteServiceRelativePath("showmenu")
public interface ShowMenu extends RemoteService {

    /**
     * Load menu items
     */
    public String myMethod();
}