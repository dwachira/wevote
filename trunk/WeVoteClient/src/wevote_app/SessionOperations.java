package wevote_app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Saving, loading, creating sessions.
 *
 * @author hollgam
 */
public class SessionOperations {
    /**
     * Indicates that the session has had changes.
     */
    public static boolean sessionChanged = false; // indication of changes in current session, bug with save, save as sessions methods
    /**
     * Indicates that the session in new.
     */
    public static boolean sessionNew = true;//indication of a state of the program, used for fetcghin mobile numbers and polls

    /**
     *
     * @param nameFile
     */
    public static void saveSession(String nameFile) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(nameFile));
            os.writeObject(Main.topicArray);
            os.writeObject(Main.mobileNumberArray);
//            os.writeObject(Main.answerArray);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        sessionChanged = false;
        Main.frame.refreshLog("Session has been saved.");

    }

    /**
     *
     * @param nameFile
     */
    public static void loadSession(String nameFile) {
        ArrayList<Topic> topicArrayRestore = null;
        ArrayList<MobileNumber> mobileNumberArrayRestore = null;
//        ArrayList<Answer> answerArrayRestore = null;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(nameFile));
            //loading objects to temporary object variables
            topicArrayRestore = (ArrayList<Topic>) is.readObject();
            mobileNumberArrayRestore = (ArrayList<MobileNumber>) is.readObject();
//            answerArrayRestore = (ArrayList<Answer>) is.readObject();

            Main.respondentsRegistered = 0;

            Main.topicArray = topicArrayRestore;
            Main.mobileNumberArray = mobileNumberArrayRestore;
            Main.answerArray = new ArrayList<Answer>();
//            Main.answerArray = answerArrayRestore;

            FetchMobiles.fetchMobileVar = true;
            SendMessage.sendingPollsStopped = false;

            Main.frame.refreshLog("Session has been loaded.");

            if (!Main.mobileNumberArray.isEmpty()) {
                for (MobileNumber mobNum : Main.mobileNumberArray) {
                    Main.frame.refreshLog("Mobile number was loaded: "+mobNum.getPhoneNumber()+" "+ mobNum.getGender() + " " + mobNum.getBirthDateString());
                    Main.respondentsRegistered++;
                }
                Main.frame.setRespondentsRegistered(Integer.toString(Main.respondentsRegistered));
            }

//            if (!Main.answerArray.isEmpty()) {
//                for (Answer answer : Main.answerArray) {
//                    Main.frame.refreshLog("Answer        was loaded: "+answer.getAnswer()+" from " + answer.getMobileNumberSentPhoneNumber());
//                }
//            }
            //Main.frame.


        } catch (Exception ex) {
            //ex.printStackTrace();
        }

        // setting global variables
        sessionChanged = false;
        sessionNew = true;
    }

    /**
     *
     */
    public static void newSession() {
        Main.mobileNumberArray = new ArrayList<MobileNumber>();
        Main.topicArray = new ArrayList<Topic>();
        Main.answerArray = new ArrayList<Answer>();

        Main.respondentsRegistered = 0;
        Main.answersRegistered = 0;

        Main.frame.setAnswersReceived("0");
        Main.frame.setRespondentsRegistered("0");
        //TODO test
        
        Main.frame.lastOpenedSessionFileName = null;
        Main.frame.currentPollID = -1;
        Main.frame.currentTopicID = 0;
        Main.frame.refreshLog("New session has been initialised.");
    }

}
