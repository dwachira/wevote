package wevote_app;


import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Used to store global variables about screen in use.
 * 
 * @author hollgam
 */
public class ScreenParameters {
    static private Toolkit toolkit =  Toolkit.getDefaultToolkit ();
    static private Dimension dim = toolkit.getScreenSize();
    static int screenWidth = dim.width;
    static int screenHeight = dim.height;
    static int screenDiagonal = screenWidth * screenHeight / 2;


    /**
     *
     */
    public ScreenParameters() {
    }

}
