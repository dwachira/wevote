package wevote_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classname Answer stands for everything, but Answer
 *
 * @author hollgam & NorthernDemon
 */
public class Answer implements Serializable {

    private String answer;
    MobileNumber mobileNumberSent;

    /**
     *
     * @return
     */
    public MobileNumber getMobileNumberSent() {
        return mobileNumberSent;
    }

    /**
     *
     * @param mobileNumberSent
     */
    public void setMobileNumberSent(MobileNumber mobileNumberSent) {
        this.mobileNumberSent = mobileNumberSent;
    }
    Poll pollSent;

    /**
     *
     * @param answer
     * @param mobileNumberSent
     */
    public Answer(String answer, MobileNumber mobileNumberSent) {
        this.answer = answer;
        this.mobileNumberSent = mobileNumberSent;
    }

    /**
     * Add Answer from the thread. Security issue is handled by this.
     * @param answer
     * @param mobileNumberSent
     */
    public void addFromThread(String answer, MobileNumber mobileNumberSent) {
        //TODO check if it is needed
        setAnswer(answer);
        this.mobileNumberSent = mobileNumberSent;
    }

    /**
     *
     * @return
     */
    public String getAnswer() {
        return answer;
    }

    /**
     *
     * @param answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Uploads Answer to database
     *
     * Uses mobileNumberSent.getPhoneNumber() as reference for used_id
     *
     * @see http://code.google.com/p/wevote/wiki/Database — user_answer table
     */
    public void uploadAnswer() {
        MySQL foo = new MySQL();

        //System.out.println("mobileNumberSent.getPhoneNumber()"+mobileNumberSent.getPhoneNumber());

        ArrayList<ArrayList> result = foo.Select("SELECT id FROM `user` WHERE mobile_number = '" + mobileNumberSent.getPhoneNumber() + "' LIMIT 1");

        //System.out.println("result.get(0).get(0): "+result.get(0).get(0).toString());

        for (int i = 0; i < Main.currentOptions.size(); i++) {
            if (this.getAnswer().equals(Main.currentOptions.get(i))) {

                //System.out.println("Main.pollSent.getAnswerID(i)" + Main.pollSent.getAnswerID(i));

                foo.Update("INSERT INTO `user_answer` (user_id, answer_id) VALUE ("
                    + result.get(0).get(0).toString() + ","
                    + Main.pollSent.getAnswerID(i) + ");");
            }
        }
    }
    /**
     * Check if mobile phone registered in system or not.
     *
     */
    public String getMobileNumberSentPhoneNumber() {
        if (mobileNumberSent != null)
            return mobileNumberSent.getPhoneNumber();
        else
            return " ";
    }

    /**
     * Check registration of the number on the server.
     *
     */
    public void checkRegistration() {
        MySQL foo = new MySQL();
        //System.out.println("1");
        ArrayList<ArrayList> result = foo.Select("SELECT count(id) FROM `user` WHERE mobile_number = '" + mobileNumberSent.getPhoneNumber() + "' LIMIT 1");
//System.out.println("2: "+result.get(0).get(0));
        if (result.get(0).get(0).equals("0")) {
            String gender = "unknown";
            if (mobileNumberSent.getGender() == 'm' || mobileNumberSent.getGender() == 'M')
                gender = "male";
            else if (mobileNumberSent.getGender() == 'f' || mobileNumberSent.getGender() == 'F')
                gender = "female";

            foo.Update("INSERT INTO `user` (mobile_number, age, gender) VALUE ("
                + "'" + mobileNumberSent.getPhoneNumber() + "',"
                + "'" + mobileNumberSent.getBirthDate().getYear() + "-" + (mobileNumberSent.getBirthDate().getMonth()+1) + "-" + mobileNumberSent.getBirthDate().getDate() + "', "
                + "'" + gender + "');");
        }
        
        this.uploadAnswer();
    }
}
