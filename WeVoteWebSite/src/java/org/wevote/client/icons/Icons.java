package org.wevote.client.icons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

import com.google.gwt.core.client.GWT;

/**
 * Provide access from other classes to icons
 *
 * @author NorthernDemon
 */

public interface Icons extends ClientBundle {

    Icons ICONS = GWT.create(Icons.class);

    public class Images {

        /**
         * @return gender icon for supporting tab (theChartByGender) inside question tab
         */
        public static AbstractImagePrototype gender() {
            return AbstractImagePrototype.create(ICONS.gender());
        }
        
        /**
         * @return rating icon for supporting tab (theChartByRating) inside question tab
         */
        public static AbstractImagePrototype rating() {
            return AbstractImagePrototype.create(ICONS.rating());
        }

        /**
         * @return age icon for supporting tab (theChartByAge) inside question tab
         */
        public static AbstractImagePrototype age() {
            return AbstractImagePrototype.create(ICONS.age());
        }

        /**
         * @return tab icon for question tab
         */
        public static AbstractImagePrototype tab() {
            return AbstractImagePrototype.create(ICONS.tab());
        }

        /**
         * @return system icon for system tab
         */
        public static AbstractImagePrototype system() {
            return AbstractImagePrototype.create(ICONS.system());
        }

        /**
         * @return expand icon for expanding menu items
         */
        public static AbstractImagePrototype expand() {
            return AbstractImagePrototype.create(ICONS.expand());
        }

        /**
         * @return collapse icon for collapsing menu items
         */
        public static AbstractImagePrototype collapse() {
            return AbstractImagePrototype.create(ICONS.collapse());
        }

        /**
         * @return question icon for questions in western panel
         */
        public static AbstractImagePrototype question() {
            return AbstractImagePrototype.create(ICONS.question());
        }

        /**
         * @return pool icon for folders in western panel
         */
        public static AbstractImagePrototype pool() {
            return AbstractImagePrototype.create(ICONS.pool());
        }

        /**
         * @return history icon western panel
         */
        public static AbstractImagePrototype history() {
            return AbstractImagePrototype.create(ICONS.history());
        }

        /**
         * @return error icon for error tab
         */
        public static AbstractImagePrototype error() {
            return AbstractImagePrototype.create(ICONS.error());
        }

    }

    @Source("gender.png")
    ImageResource gender();

    @Source("rating.png")
    ImageResource rating();

    @Source("age.png")
    ImageResource age();

    @Source("tab.png")
    ImageResource tab();

    @Source("system.png")
    ImageResource system();

    @Source("expand.png")
    ImageResource expand();

    @Source("collapse.png")
    ImageResource collapse();

    @Source("question.png")
    ImageResource question();

    @Source("pool.png")
    ImageResource pool();

    @Source("history.png")
    ImageResource history();

    @Source("error.png")
    ImageResource error();
}