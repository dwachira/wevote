package wevote_app;

import java.util.ArrayList;
import org.smslib.InboundMessage;

/**
 * Fetching mobile numbers with information about users that wish to
 * register themselves into the poll.
 * 
 * @author hollgam
 */
public class FetchMobiles implements Runnable {

    public static boolean fetchMobileVar = true;
    public static ReadMessages readMsg;

    public void run() {
        Main.frame.setEnabledMenuItem(7);

        //System.out.println("111");
//        try {
//            DeleteMessages deleteMsg = new DeleteMessages();
//            deleteMsg.doIt(1);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        do {
            try {
                //System.out.println("Fetching mobiles");
                
                readMsg = new ReadMessages();
                ArrayList<InboundMessage> mobPhones = null;
                mobPhones = readMsg.doIt(1);
                Main.addMobileNumbersFromArrayList(mobPhones);
                
                Thread.sleep(5000); 

            } catch (Exception ex) {
               //ex.printStackTrace();
            }
        } while(fetchMobileVar);
        Main.frame.refreshLog("Fetching mobile numbers has been stopped.");
        Main.frame.setEnabledMenuItem(10);
        Main.frame.setEnabledMenuItem(11);

    }
}
