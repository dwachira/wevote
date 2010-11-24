package wevote_app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import org.smslib.InboundMessage;

/**
 *
 * @author hollgam
 *
 * 
 */
public class Main {

    // START OF GLOBAL VARIABLES
    /**
     * GUI
     */
    public static MainFrame frame = new MainFrame();
    /**
     * List of topics (sets of polls)
     */
    public static ArrayList<Topic> topicArray = new ArrayList<Topic>(); // array of created polls
    /**
     * List of mobile numbers that are registered to the system
     */
    public static ArrayList<MobileNumber> mobileNumberArray = new ArrayList<MobileNumber>();
    /**
     * List of answers received from users that are present in ArrayList<MobileNumber> mobileNumberArray
     * other answers are filtered in addAnswersFromArrayList method. It is unique for every poll
     * that is sent.
     */
    public static ArrayList<Answer> answerArray = new ArrayList<Answer>();
    /**
     * Thread for fetching mobile numbers
     */
    public static Thread modemListenerThread;
    //TODO adding mobile numbers that spam to ignore list
    /**
     * Array of numbers that need to be ignored (operator's number, spammers)
     *
     * "18258" - number of service messages received from Finnish mobile operator SaunaLahti
     */
    public static String[] mobileNumbersToIgnore = {
        "18258"
    };
    
    /**
     * Current topic that is being processed.
     */
    public static Topic topicSent = new Topic();
    /**
     * Current poll that is being processed.
     */
    public static Poll pollSent = new Poll();

    /**
     *
     */
    public static String[] topicArrayString;
    /**
     * String representation of a poll (question) to send. Used at the stage
     * of sending SMS messages with the question to registered mobile numbers.
     */
    public static String currentQuestion;
    /**
     * PIN code of a modem in use.
     */
    public static String pinCode = "1234";
    /**
     * PUK code of a modem in use.
     */
    public static String pukCode = "62904554";
    /**
     *
     */
    public static String modemNumber = "+358465985754";//306948494037
    /**
     * Default COM port (it is later changed by calling CommTest that looks
     * for a connected modem.
     */
    public static String commPort = "COM1"; 
    /**
     * Clockrate
     */
    public static int baud = 19700;
    /**
     *
     */
    public static String modemManufactorer = "Siemens";
    /**
     *
     */
    public static String modemModel = "MC35i";
    /**
     * Options that are sent. Used by methods which work with GUI.
     */
    public static ArrayList<String> currentOptions = new ArrayList<String>();
    /**
     * Amount of mobile phones that have registered.
     */
    public static int respondentsRegistered = 0;
    /**
     * Number of answers received back (When it is equal to the number of phones
     * present in the system fetching of answers is stopped)
     */
    public static int answersRegistered = 0;
    /**
     *
     */
    public static boolean deviceDetected = true;

    //END OF GLOBAL VARIABLES

    /**
     * Main function.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //int frameWidth = ScreenParameters.screenWidth / 2;
        int frameWidth = 960;
        //int frameHeight = ScreenParameters.screenHeight / 2;
        int frameHeight = 540;


        //find COM port with a modem
        //UNCOMMENT IT BEFORE TESTING. DOES NOT WORK WITHOUT COMM LIBRARY ON MAC OS. THAT IS WHY IT IS COMMENTED
//        CommTest findPorts = new CommTest();
//        String[] commPortTest = findPorts.doIt();
//        try {
//            if (commPortTest.length != 0) {
//                if (!commPortTest[0].equals(" ")) {
//                    commPort = commPortTest[0];
//                }
//                if (!commPortTest[1].equals(" ")) {
//                    baud = Integer.parseInt(commPortTest[1]);
//                }
//            }
//        } catch (Exception e) {
//        }
        //System.out.println("COM: " + commPort);
        //System.out.println("BAUD: " + baud);
        baud = 57600; //TODO reason why it doesnt work with 19700

        //creating GUI
        frame = new MainFrame();
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setMobileNumber(Main.modemNumber); //show mobile Number in GUI
        frame.refreshLog("COM Port to use: " + commPort + "   Baud to use: " + baud);

        //thread for collecting mobile numbers
        Runnable threadJob = new FetchMobiles();
        modemListenerThread = new Thread(threadJob);
        modemListenerThread.setName("Fetching numbers");
        modemListenerThread.start();
        frame.refreshLog("Fetching mobile numbers has been started.");
    }

    /**
     * Adds topic (set of polls) to Main.topicArray
     *
     * @param title
     */
    public static void addTopic(String title) {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        Date topicDate = Calendar.getInstance().getTime();
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String topicDatweString = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
            topicDate = df.parse(topicDatweString);
        } catch (Exception e) {
            //System.out.println("DATE WRONG");
            //e.printStackTrace();
        }

        Topic topic = new Topic(title, topicDate);
        topicArray.add(topic);

        frame.updateList1();
        frame.refreshLog("Topic \"" + topicArray.get(Main.topicArray.size() - 1).getTitle() + "\" has been added.");

    }

    /**
     * Adds poll (question to ask from registered users) to the selected topic
     * inside of Main.topicArray list of mobile phones that are registered to
     * the system.
     *
     * @param topicID
     * @param question
     * @param answers
     */
    public static void addPoll(int topicID, String question, ArrayList<String> answers) {
        Poll poll = new Poll(question, answers);
        topicArray.get(topicID - 1).getPollArrayList().add(poll);

        frame.updateList2(MainFrame.currentTopicID);
        frame.refreshLog("Poll \"" + question + "\" has been added.");
    }

    /**
     * Adds mobile phone to Main.mobilephoneArray list of mobile phones that are registered to the system
     *
     * @param phoneNumber
     * @param gender
     * @param birthDate
     */
    public static void addMobileNumber(String phoneNumber, char gender, Date birthDate) {
        boolean updated = false;
        for (MobileNumber mobNumToCheck : mobileNumberArray) {
            if (phoneNumber.equals(mobNumToCheck.getPhoneNumber())) {
                //mobileNumberArray.remove(mobNumToCheck);

                mobNumToCheck.setGender(gender);
                if (birthDate != null)
                    mobNumToCheck.setBirthDate(birthDate);

                frame.refreshLog("Mobile number " + phoneNumber + " has been updated. New gender: " + gender + " New birth day: " + birthDate.toString());
                updated = true;
                break;
            }
        }
        if (!updated) {
            MobileNumber mobNum = new MobileNumber(phoneNumber, gender, birthDate);//"m1991-02-17"
            mobileNumberArray.add(mobNum);
            Collections.sort(mobileNumberArray);
            if (mobNum.getBirthDate() != null) {
                frame.refreshLog("Mobile number " + mobNum.getPhoneNumber() + " (gender " + mobNum.getGender() + " bithday: " + mobNum.getBirthDateString() + ") has been added.");
            } else {
                frame.refreshLog("Mobile number " + mobNum.getPhoneNumber() + " has been added.");
            }
            respondentsRegistered = Main.mobileNumberArray.size();
            frame.setRespondentsRegistered(Integer.toString(respondentsRegistered));
        }

    }

    /**
     * Used to filter correct mobile numbers that are received during work of FetchMobiles thread
     * quite complex function. Might have some problems due to its complexity. A lot of testing has
     * done though.
     *
     * @param mobileNumbers
     */
    public synchronized static void addMobileNumbersFromArrayList(ArrayList<InboundMessage> mobileNumbers) {
        try {
            if (!mobileNumbers.isEmpty()) {
                for (InboundMessage mobNum : mobileNumbers) {
//                    for (MobileNumber mobNumToCheck : mobileNumberArray) {
//                        if (mobNum.getOriginator().equals(mobNumToCheck.getPhoneNumber())) {
//                            mobileNumberArray.remove(mobNumToCheck);
//                            frame.refreshLog("Mobile number " + mobNum.getOriginator() + " has been removed since new text was received from it.");
//                            break;
//                        }
//                    }
                    if (!mobNum.getOriginator().isEmpty() || mobNum.getOriginator().contains("+")) {

                        //Checking if mobile number of a swender is in an array of numbers that need to be filtered Main.mobileNumbersToIgnore
                        if (!Arrays.asList(mobileNumbersToIgnore).contains(mobNum.getOriginator())) {
                            //System.out.println("NOT PRESENT : " + mobNum.getText());
                            //System.out.println("ORIGINATOR  : " + mobNum.getOriginator());

                            Date birthDate = null;
                            char gender = 'u';

                            if (mobNum.getText().indexOf('/') > 0) {
                                //message received is in a form of "gdd/MM/yyyy" g - gender (m/f) dd/MM/yyyy - birthday  (e.g. "m17/02/1991") . YYYY can also be YY
                                //System.out.println("birthday is given");

                                if (mobNum.getText().length() == 10) {
                                    //only birthday is given. year is in form of YYYY
                                    //System.out.println("only birthday is given");

                                    try {
                                        //try to filter unwanted messages
                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                        birthDate = df.parse(mobNum.getText());
                                    } catch (Exception e) {
                                        //e.printStackTrace();
                                    }

                                } else if (mobNum.getText().length() == 11) {
                                    //both birthday and gender are given. year is in form of YYYY
                                    //System.out.println("both birthday and gender are given");

                                    if (mobNum.getText().substring(0, 1).equals("m") || mobNum.getText().substring(0, 1).equals("f") || mobNum.getText().substring(0, 1).equals("u") || mobNum.getText().substring(0, 1).equals("M") || mobNum.getText().substring(0, 1).equals("F") || mobNum.getText().substring(0, 1).equals("U")) {
                                        char[] genderArray = mobNum.getText().toCharArray();
                                        gender = genderArray[0];//TODO get first character of the string in one function getChars() ???
                                    }
                                    //System.out.println("GENDER: " + gender);
                                    try {
                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                        birthDate = df.parse(mobNum.getText().substring(1, mobNum.getText().length()));
                                    } catch (Exception e) {
                                        //System.out.println("DATE WRONG");
                                        //e.printStackTrace();
                                    }

                                } else if (mobNum.getText().length() == 8) {
                                    //only birthday is given. year is in form of YYYY
                                    //System.out.println("only birthday is given");

                                    //System.out.println("GENDER: " + gender);
                                    try {
                                        DateFormat df = new SimpleDateFormat("dd/MM/yy");
                                        birthDate = df.parse(mobNum.getText().substring(1, mobNum.getText().length()));
                                    } catch (Exception e) {
                                        //System.out.println("DATE WRONG");
                                        //e.printStackTrace();
                                    }

                                } else if (mobNum.getText().length() == 9) {
                                    //both birthday and gender are given. year is in form of YY
                                    //System.out.println("both birthday and gender are given");

                                    if (mobNum.getText().substring(0, 1).equals("m") || mobNum.getText().substring(0, 1).equals("f") || mobNum.getText().substring(0, 1).equals("u") || mobNum.getText().substring(0, 1).equals("M") || mobNum.getText().substring(0, 1).equals("F") || mobNum.getText().substring(0, 1).equals("U")) {
                                        char[] genderArray = mobNum.getText().toCharArray();
                                        gender = genderArray[0];//TODO get first character of the string in one function getChars() ???
                                    }
                                    //System.out.println("GENDER: " + gender);
                                    try {
                                        DateFormat df = new SimpleDateFormat("dd/MM/yy");
                                        birthDate = df.parse(mobNum.getText().substring(1, mobNum.getText().length()));
                                    } catch (Exception e) {
                                        //System.out.println("DATE WRONG");
                                        //e.printStackTrace();
                                    }

                                }



                            } else if (mobNum.getText().length() == 3) {
                                //message received is in a form of "gaa" g - gender (m/f) aa - age (e.g. "m19" )
                                //System.out.println("age is given");

                                char[] genderArray = mobNum.getText().toCharArray();
                                gender = genderArray[0];//TODO get first character of the string in one function getChars() ???
                                //System.out.println("GENDER: " + gender);

                                Calendar cal = Calendar.getInstance();
                                int day = cal.get(Calendar.DATE);
                                int month = cal.get(Calendar.MONTH) + 1;
                                int year = cal.get(Calendar.YEAR);
                                try {
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    int age = Integer.parseInt(mobNum.getText().substring(1, mobNum.getText().length()));

                                    String birthdayYearsBack = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year - age);
                                    birthDate = df.parse(birthdayYearsBack);

                                    //System.out.println(birthdayYearsBack);
                                } catch (Exception e) {
                                    //System.out.println("DATE WRONG");
                                    //e.printStackTrace();
                                }

                            } else if (mobNum.getText().length() == 1) {
                                //cases: "m", "9"

                                if (mobNum.getText().equals("m") || mobNum.getText().equals("f") || mobNum.getText().equals("u") || mobNum.getText().equals("M") || mobNum.getText().equals("F") || mobNum.getText().equals("U")) {
                                    char[] genderArray = mobNum.getText().toCharArray();
                                    gender = genderArray[0];
                                } else if (Integer.parseInt(mobNum.getText()) > 0 && Integer.parseInt(mobNum.getText()) < 10) {
                                    Calendar cal = Calendar.getInstance();
                                    int day = cal.get(Calendar.DATE);
                                    int month = cal.get(Calendar.MONTH) + 1;
                                    int year = cal.get(Calendar.YEAR);

                                    try {
                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                        int age = Integer.parseInt(mobNum.getText());
                                        String birthdayYearsBack = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year - age);
                                        birthDate = df.parse(birthdayYearsBack);
                                    } catch (Exception e) {
                                        //System.out.println("DATE WRONG");
                                        //e.printStackTrace();
                                    }
                                }

                            } else if (mobNum.getText().length() == 2) {
                                //cases "18", "m4"

                                if (mobNum.getText().substring(0, 1).equals("m") || mobNum.getText().substring(0, 1).equals("f") || mobNum.getText().substring(0, 1).equals("u") || mobNum.getText().substring(0, 1).equals("M") || mobNum.getText().substring(0, 1).equals("F") || mobNum.getText().substring(0, 1).equals("U")) {
                                    if (Integer.parseInt(mobNum.getText().substring(1)) > 0 && Integer.parseInt(mobNum.getText().substring(1)) < 10) {
                                        char[] genderArray = mobNum.getText().toCharArray();
                                        gender = genderArray[0];

                                        Calendar cal = Calendar.getInstance();
                                        int day = cal.get(Calendar.DATE);
                                        int month = cal.get(Calendar.MONTH) + 1;
                                        int year = cal.get(Calendar.YEAR);
                                        try {
                                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                            int age = Integer.parseInt(mobNum.getText().substring(1));
                                            String birthdayYearsBack = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year - age);
                                            birthDate = df.parse(birthdayYearsBack);
                                        } catch (Exception e) {
                                            //System.out.println("DATE WRONG");
                                            //e.printStackTrace();
                                        }

                                    }
                                } else if (Integer.parseInt(mobNum.getText()) > 10 && Integer.parseInt(mobNum.getText()) < 100) {
                                    //age is given (e.g. "34")

                                    Calendar cal = Calendar.getInstance();
                                    int day = cal.get(Calendar.DATE);
                                    int month = cal.get(Calendar.MONTH) + 1;
                                    int year = cal.get(Calendar.YEAR);
                                    try {
                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                        int age = Integer.parseInt(mobNum.getText());
                                        String birthdayYearsBack = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year - age);
                                        birthDate = df.parse(birthdayYearsBack);
                                    } catch (Exception e) {
                                        //System.out.println("DATE WRONG");
                                        //e.printStackTrace();
                                    }

                                }


                            } else {
                                //message received is in a free form. gender = 'u' (unknown), birthday = null
                                
                                //System.out.println("birthday nor age is given");
                                birthDate = null;
                            }

                            //Checking if year received is correctly formatted but from the future (e.g. "2687")
                            if (birthDate != null) {

                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
                                Calendar cal = Calendar.getInstance();
                                int year = cal.get(Calendar.YEAR);

                                if (Integer.parseInt(simpleDateformat.format(birthDate)) > year) {
                                    birthDate = null;
                                }
                            }
                            addMobileNumber(mobNum.getOriginator(), gender, birthDate);
                        } else {
                            
                            // number from ignore list was found
                            frame.refreshLog("Mobile number " + mobNum.getOriginator() + " has been ignored.");
                            //System.out.println("IGNORED :" + mobNum.getOriginator());
                        }

                    } else {
                        //System.out.println("Message was not recognised.");
                        frame.refreshLog("Registration message was not recognised.");
                    }
                }
            }

        } catch (Exception e) {
            //catching unwanted messages and ignoring them
            //.printStackTrace();
        }
    }

    /**
     * Adds answer to Main.answerArray list of answers
     *
     * @param answerString
     * @param mobileNumber
     */
    public static void addAnswer(String answerString, MobileNumber mobileNumber) {

        //System.out.println(answerString);
        //System.out.println(mobileNumber.getPhoneNumber());

        Answer answer = new Answer(answerString, mobileNumber);

        //answer.checkRegistration();
        //answer.addFromThread(answerString, mobileNumber);

        answerArray.add(answer);
        frame.refreshLog("Answer \"" + answerString + "\" from " + mobileNumber.getPhoneNumber() + " has been recorded.");

        answersRegistered = Main.answerArray.size();
        frame.setAnswersReceived(Integer.toString(answersRegistered));
    }

    /**
     * Used to filter correct messages that are received during work of FetchAnswers thread
     *
     * @param answerList - list of objects of type Answer generated in a thread for collecting answers
     */
    static synchronized void addAnswersFromArrayList(ArrayList<InboundMessage> answerList) {
        try {
            if (!answerList.isEmpty()) {

                //check if mobile number of sender was registered before polls were sent
                for (InboundMessage answer : answerList) {
                    if (answer.getText().length() == 1) {

                        //if message received is not among options used
                        //System.out.println(answer.getText());

                        if (!currentOptions.contains(answer.getText().toUpperCase())) {
                            frame.refreshLog("Message received from " + answer.getOriginator() + " contains a wrong symbol.");
                            continue;
                        }

                        //if answer was recorded already from the same number
                        if (answerArray != null) {
                            for (Answer ans : answerArray) {
                                //System.out.println(ans.getAnswer());
                                if (ans.getMobileNumberSent().getPhoneNumber().equals(answer.getOriginator())) {
                                    ans.setAnswer(answer.getText());
                                    frame.refreshLog("Message received from " + answer.getOriginator() + ". There were texts from this number already. Answer was substituted for a new one.");
                                    continue;
                                }
                            }
                        }

                        //get mobile number of a sender
                        MobileNumber mobileNumberOriginator = null;
                        for (MobileNumber mobNumToCheck : mobileNumberArray) {
                            //System.out.println(mobNumToCheck.getPhoneNumber());
                            if (answer.getOriginator().equals(mobNumToCheck.getPhoneNumber())) {
                                mobileNumberOriginator = mobNumToCheck;
                                break;
                            }
                        }


                        //if mobile number has not been found in array of registered phone numbers - ignore
                        if (mobileNumberOriginator != null) {
                            addAnswer(answer.getText(), mobileNumberOriginator);
                        } else {
                            frame.refreshLog("Text message received from unknown number: " + answer.getOriginator());
                        }

                    } else {
                        frame.refreshLog("Text message received from " + answer.getOriginator() + "contains more than one symbol.");
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Outputs a short summary of analysis of answers fetched to the log.
     */
    public static void showSummary() {
        //System.out.println("SHOW SUMMARY");
        try {
            if (!Main.answerArray.isEmpty()) {
                int[] answersReceived = new int[4];
                for (int i = 0; i < Main.currentOptions.size(); i++) {
                    answersReceived[i] = 0;
                }

                for (Answer answer : Main.answerArray) {
                    int optionIndex = 0;
                    for (String option : Main.currentOptions) {
                        if (answer.getAnswer().equals(option)) {
                            answersReceived[optionIndex]++;
                        }
                        optionIndex++;

                    }

                }

                double TotalAmount = 0;
                for (int i : answersReceived) {
                    TotalAmount += i;
                }

                Main.frame.refreshLog("Short summary from the poll (Option - Amount of votes received - Percentage):");
                int j = 0;
                for (int i : answersReceived) {
                    Main.frame.refreshLog("Option " + Main.currentOptions.get(j) + ": " + i + " - " + i / TotalAmount * 100 + "%"); // show amount of votes received
                    j++;
                }
                Main.frame.refreshLog("For more detailed information check the server.");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
