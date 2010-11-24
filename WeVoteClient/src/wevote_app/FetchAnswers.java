package wevote_app;

import java.util.ArrayList;
import org.smslib.InboundMessage;

/**
 * Fetching Answers after sending a poll.
 * @author hollgam
 */
public class FetchAnswers implements Runnable {

    public static boolean fetchAnswersVar = true;
    public static ReadMessages readMsg;

    public void run() {
        do {
            try {
                //System.out.println("Fetching answers");

                readMsg = new ReadMessages();
                ArrayList<InboundMessage> answers = null;
                answers = readMsg.doIt(2);
                Main.addAnswersFromArrayList(answers);

                if (Main.answerArray.size() == Main.mobileNumberArray.size()) {
                    Main.frame.stopSendPolls();
                    break;
                }

                Thread.sleep(1000); 
            
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        } while(fetchAnswersVar);   
    }
}
