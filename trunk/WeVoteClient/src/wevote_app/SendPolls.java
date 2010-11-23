package wevote_app;

/**
 * Thread for sending the poll to registered mobile phones.
 *
 * @author hollgam
 */
public class SendPolls implements Runnable {
    public static boolean sendPollsVar = true;
    public Thread fetchingAnswersThread;

    public void run() {
        do {
            try {
                Thread.sleep(1000);
                //System.out.println("SENDING POLLS THREAD");

                SendMessage sendMsg = new SendMessage();
                try
                {
                        sendMsg.doIt();
                        sendPollsVar = false;
                        break;
                }
                catch (Exception e)
                {
                        //e.printStackTrace();
                }
                //Thread.sleep(1000); //TODO choose sleep time
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        } while(sendPollsVar);

        //System.out.println("BEFORE FETCHING ANSWERS");

        Runnable threadJob = new FetchAnswers();
        fetchingAnswersThread = new Thread(threadJob);
        fetchingAnswersThread.setName("Fetching answers");
        fetchingAnswersThread.start();
        Main.frame.refreshLog("Fetching answers has been started.");

        //System.out.println("AFTER FETCHIGN ASNWERS");
    }
}
