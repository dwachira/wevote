package org.wevote.client.chart;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author NorthernDemon
 */

@RemoteServiceRelativePath("showchart")
public interface ShowChart extends RemoteService {
    /**
     * Request for tab, containing question
     *
     * @param id number of question
     */
    public String myMethod(int id);
}