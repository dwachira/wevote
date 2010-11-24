package org.wevote.client.menu.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * Fills Item.java and Folder.java form RPC by parsing JSON string
 *
 * @see http://code.google.com/p/wevote/wiki/JSONMenu
 *
 * @author NorthernDemon
 */

public class Menu {

    public static Folder[] folders;

    /**
     * @return collections of Folders and Items in it
     */
    public static Folder getMenu() {
        Folder root = new Folder("root");
        for (int i = 0; i < folders.length; i++) {
            root.add((Folder) folders[i]);
        }

        return root;
    }

    /**
     * @param result JSON string from RPC with Folders & Items
     */
    public static void setMenu(String result) {
        JSONValue jsonValue = JSONParser.parse(result);
        JSONArray jsonArray = jsonValue.isArray();

        folders = new Folder[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            folders[i] = new Folder(jsonArray.get(i).isObject().get("folder").isString().stringValue());

            for(int j = 0; j < jsonArray.get(i).isObject().get("item").isArray().size(); j++)
                folders[i].addItem(
                    new Item(
                        jsonArray.get(i).isObject().get("item").isArray().get(j).isObject().get("title").isString().stringValue(),
                        (int) jsonArray.get(i).isObject().get("item").isArray().get(j).isObject().get("id").isNumber().doubleValue()
                    )
                );
        }
    }

}  