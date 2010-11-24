package org.wevote.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.wevote.client.menu.ShowMenu;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Vector;

/**
 * Retrives Menu info from database using MySQL.java
 * That code is runnign as soon, as web-site was loaded
 * and simply contains all possible folders and items (pools and questions)
 *
 * @author NorthernDemon
 */

public class ShowMenuImpl extends RemoteServiceServlet implements ShowMenu {

    /**
     * @see http://code.google.com/p/wevote/wiki/JSONMenu
     *
     * @return root JSON string with Menu info
     */
    @Override
    public String myMethod() {
        MySQL menu = new MySQL();
        Vector<Vector> resultPool = menu.Select("SELECT id, title FROM pool ORDER BY date");

        JSONArray root = new JSONArray();
        for(int i = 0; i < resultPool.size(); i++) {
            JSONObject pool = new JSONObject();
            Vector<Vector> resultQuestion = menu.Select("SELECT id, title FROM question WHERE pool_id = " + resultPool.get(i).get(0) + " ORDER BY id");

            pool.put("folder", resultPool.get(i).get(1));
            JSONArray questions = new JSONArray();
            for(int j = 0; j < resultQuestion.size(); j++) {
                JSONObject question = new JSONObject();
                question.put("id", Integer.parseInt(resultQuestion.get(j).get(0).toString()));
                question.put("title", resultQuestion.get(j).get(1));
                questions.add(question);
            }
            pool.put("item", questions);
            root.add(pool);
        }

        return root.toJSONString();
    }

}