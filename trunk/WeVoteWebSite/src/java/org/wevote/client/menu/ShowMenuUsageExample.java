package org.wevote.client.menu;

import java.util.List;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.data.LoadEvent;

import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionListener;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.GWT;

import org.wevote.client.menu.model.Folder;
import org.wevote.client.menu.model.Menu;
import org.wevote.client.icons.Icons;

/**
 * Provides functionality for opening/loading tabs, displaying menu and filtering items
 *
 * @author NorthernDemon
 */

public class ShowMenuUsageExample extends VerticalPanel {

    private static String treeItemId;
    private static final TreeLoader<ModelData> loader = new BaseTreeLoader<ModelData>(new TreeModelReader<List<ModelData>>());
    private static final TreeStore<ModelData> store = new TreeStore<ModelData>(loader);
    private static final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);

    StoreFilterField<ModelData> filter = new StoreFilterField<ModelData>() {
        private int skipItems = 0; // uses to filter folder is string matches folder name

        @Override
        protected boolean doSelect(Store<ModelData> Store, ModelData parent, ModelData record, String property, String filter) {
            if (skipItems == 0) { // Item
                String title = record.get("title").toString().trim().toLowerCase();

                if (title.contains(filter.trim().toLowerCase()) && !(record instanceof Folder)) {
                    return true;
                }

                if (title.contains(filter.trim().toLowerCase()) && record instanceof Folder) {
                    skipItems = store.getChildCount(record);
                    return true;
                }

                return false;
            } else { // Folder
                skipItems--;
                return true;
            }
        }
    };
    
    public ShowMenuUsageExample() {
        final ToggleButton toggleBtn = new ToggleButton();
        final FlexTable table = new FlexTable();

        tree.setAutoLoad(true);
        tree.addStyleName("menu");
        tree.setDisplayProperty("title");
        tree.setIconProvider(new ModelIconProvider<ModelData>() {
            @Override
            public AbstractImagePrototype getIcon(ModelData model) {
                if (((TreeModel) model).isLeaf()) {
                    return Icons.Images.question();
                } else {
                    return Icons.Images.pool();
                }
            }
        });

        /**
         * Listener for adding new question number to HistoryToken, which will open new tab for it
         */
        tree.addListener(Events.OnMouseDown, new Listener<ComponentEvent>() {
            @Override
            public void handleEvent(ComponentEvent be) {
                History.newItem("/question/" + tree.getSelectionModel().getSelectedItem().get("question_id").toString());
            }
        });

        /**
         * Load menu or show error
         */
        final AsyncCallback<String> callback = new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Menu.setMenu(result);
                loader.load(Menu.getMenu());
            }

            @Override
            public void onFailure(Throwable caught) {
                add(new Html("<div class='error'>Communication Error</div>"));
            }
        };

        /**
         * Toggle Button for collapsing/expanding all folders in menu
         */
        toggleBtn.setIcon(Icons.Images.expand());
        toggleBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (toggleBtn.isPressed()) {
                    toggleBtn.setIcon(Icons.Images.collapse());
                    tree.expandAll();
                } else {
                    toggleBtn.setIcon(Icons.Images.expand());
                    tree.collapseAll();
                }
            }
        });

        filter.bind(store);
        filter.focus();
        filter.setWidth(184);

        table.setWidget(1, 0, toggleBtn);
        table.setWidget(1, 1, filter);
        
        add(table);
        add(tree);
        setSpacing(8);
        addStyleName("west");

        getService().myMethod(callback);
    }

    public static void openTree() {
        tree.getSelectionModel().deselectAll();
    }

    /**
     * Uses when web-site is already running and user just clicking around
     * Expand tree, which contains opened question number
     *
     * @param id question number to expand tree
     */
    public static void openTree(String id) {
        tree.setExpanded(store.findModel("question_id", Integer.parseInt(id)), true);
        tree.getSelectionModel().setSelection(store.findModels("question_id", Integer.parseInt(id)));
    }

    /**
     * Uses when web-site is reopened with Ctrl+F5 or from scretch
     * Expand tree, which contains opened question number
     *
     * @param id question number to expand tree
     */
    public static void openTreeOnLoad(String id) {
        treeItemId = id;

        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                openTree(treeItemId);
            }
        });
    }

    public static ShowMenuAsync getService() {
        return GWT.create(ShowMenu.class);
    }

}