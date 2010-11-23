package wevote_app;

import java.util.ArrayList;
import org.smslib.InboundMessage;

/**
 * Thread for deleting all messages. It is needed for removing possibility of messages
 * from previous sessions getting into results / registered users.
 *
 * @author hollgam
 */
public class DeleteMessagesThread implements Runnable {

    public static boolean deleteMessagesVar = true;
    public static DeleteMessages deleteMsg;

    public void run() {
        try {
            deleteMsg = new DeleteMessages();
            deleteMsg.doIt(1);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }

        do {
            try {
                //System.out.println("Deleting messages");

                DeleteMessages deleteMsg = new DeleteMessages();
                deleteMsg.doIt(1);

                deleteMessagesVar = false;

            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        } while(deleteMessagesVar);
        Main.frame.refreshLog("All messages have been deleted.");
        Main.frame.setEnabledMenuItem(9);
    }
}
