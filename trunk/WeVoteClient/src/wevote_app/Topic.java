package wevote_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Classname Topic (set of polls/questions) stands for Poll
 *
 * @author hollgam
 */
public class Topic implements Serializable, Comparable<Topic> {

    private ArrayList<Poll> pollArrayList = new ArrayList<Poll>();
    private String title;
    private Date date;
    private int topicID;

    private ArrayList<Integer> answerID = new ArrayList<Integer>(); // Reference to the answer_id in user_answer table
    private ArrayList<Integer> questionID = new ArrayList<Integer>(); // Reference to the question_id in question table
    private ArrayList<Integer> poolID = new ArrayList<Integer>(); // Reference to the pool_id in pool table

    /**
     * 
     */
    public Topic() {}
    /**
     *
     * @param Title
     * @param date
     */
    public Topic(String Title, Date date) {
        this.title = Title;
        this.date = date;
        //System.out.println(Main.topicArray.size());
        this.topicID = Main.topicArray.size()+1; //not static to be able to save in session
    }

    /**
     *
     * @return
     */
    public int getTopicID() {
        return topicID;
    }

    /**
     *
     * @param topicID
     */
    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param Title
     */
    public void setTitle(String Title) {
        this.title = Title;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public ArrayList<Poll> getPollArrayList() {
        return pollArrayList;
    }

    /**
     *
     * @param pollArrayList
     */
    public void setPollArrayList(ArrayList<Poll> pollArrayList) {
        this.pollArrayList = pollArrayList;
    }

    /**
     *
     * @param poll
     */
    public void addPoll(Poll poll) {
        pollArrayList.add(poll);
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Topic o) {
        return title.compareTo(o.getTitle());
    }

    /**
     * Uploads Poll, questions and predefined answers to database
     *
     * @see http://code.google.com/p/wevote/wiki/Database — pool, question, pool_answer table
     */
    public void uploadPoll() {
        MySQL foo = new MySQL();

        // if topic presentet - not sent it again
        ArrayList<ArrayList> is_topic_presented = foo.Select("SELECT id FROM `pool` WHERE id = " + this.getPoolID(MainFrame.currentTopicID) + " LIMIT 1");

        try {
            if (Integer.parseInt(is_topic_presented.get(0).get(0).toString()) > 0) {
                ArrayList<ArrayList> pool_id = foo.Select("SELECT MAX(id) FROM `pool` LIMIT 1");
                
                for (int i = 0; i < this.getPollArrayList().size(); i++) {
                    ArrayList<ArrayList> is_question_presented = foo.Select("SELECT id FROM `question` WHERE id = " + this.getQuestionID(i) + " LIMIT 1");

                    try {
                        Integer.parseInt(is_question_presented.get(0).get(0).toString());
                    } catch (Exception ex) {
                        foo.Update("INSERT INTO `question` (pool_id, title, question) VALUE ("
                            + pool_id.get(0).get(0) + ", '"
                            + this.getPollArrayList().get(i).getQuestion() + "', '"
                            + this.getPollArrayList().get(i).getQuestionString() + "');");

                        // Insert answers into pool_answer table
                        ArrayList<ArrayList> question_id = foo.Select("SELECT MAX(id) FROM `question` LIMIT 1");
                        this.setQuestionID(Integer.parseInt(String.valueOf(question_id.get(0).get(0))));

                        for (int j = 0; j < this.getPollArrayList().get(i).getAnswers().size(); j++) {
                            foo.Update("INSERT INTO `pool_answer` (question_id, answer) VALUE ("
                                + question_id.get(0).get(0) + ", '"
                                + this.getPollArrayList().get(i).getAnswers().get(j) + "');");

                            ArrayList<ArrayList> answer_id = foo.Select("SELECT MAX(id) FROM `pool_answer` LIMIT 1");
                            this.setAnswerID(Integer.parseInt(String.valueOf(answer_id.get(0).get(0))));
                            this.pollArrayList.get(i).setAnswerID(Integer.parseInt(String.valueOf(answer_id.get(0).get(0))));
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Insert into pool table
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);

            foo.Update("INSERT INTO `pool` (title, date) VALUE ('"
                + this.getTitle() + "', '"
                + year + "-"
                + month + "-"
                + day + " " + hour + ":" + min +  ":" + sec + "');");

            // Insert questions into question table
            ArrayList<ArrayList> pool_id = foo.Select("SELECT MAX(id) FROM `pool` LIMIT 1");
            this.setPoolID(Integer.parseInt(String.valueOf(pool_id.get(0).get(0))));

            for (int i = 0; i < this.getPollArrayList().size(); i++) {
                ArrayList<ArrayList> is_question_presented = foo.Select("SELECT id FROM `question` WHERE id = " + this.getQuestionID(i) + " LIMIT 1");

                try {
                    Integer.parseInt(is_question_presented.get(0).get(0).toString());
                } catch (Exception ex) {
                    foo.Update("INSERT INTO `question` (pool_id, title, question) VALUE ("
                        + pool_id.get(0).get(0) + ", '"
                        + this.getPollArrayList().get(i).getQuestion() + "', '"
                        + this.getPollArrayList().get(i).getQuestionString() + "');");

                    // Insert answers into pool_answer table
                    ArrayList<ArrayList> question_id = foo.Select("SELECT MAX(id) FROM `question` LIMIT 1");
                    this.setQuestionID(Integer.parseInt(String.valueOf(question_id.get(0).get(0))));

                    for (int j = 0; j < this.getPollArrayList().get(i).getAnswers().size(); j++) {
                        foo.Update("INSERT INTO `pool_answer` (question_id, answer) VALUE ("
                            + question_id.get(0).get(0) + ", '"
                            + this.getPollArrayList().get(i).getAnswers().get(j) + "');");

                        ArrayList<ArrayList> answer_id = foo.Select("SELECT MAX(id) FROM `pool_answer` LIMIT 1");
                        this.setAnswerID(Integer.parseInt(String.valueOf(answer_id.get(0).get(0))));
                        this.pollArrayList.get(i).setAnswerID(Integer.parseInt(String.valueOf(answer_id.get(0).get(0))));
                    }
                }
            }
        }
    }

    /**
     * Sets id for answer to connect pool_answer & user_answer tables
     *
     * @param answerID id of answer in pool_answer table
     */
    public void setAnswerID(int answerID) {
        this.answerID.add(answerID);
    }

    /**
     * Returns id of asked answer from pool_answer table
     *
     * @param i - 0..3 Answer ID
     * @return id in pool_answer table
     */
    public Integer getAnswerID(int i) {
        try {
            return this.answerID.get(i);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Sets id for question to connect question & pool_answer tables
     *
     * @param questionID id of question in question table
     */
    public void setQuestionID(int questionID) {
        this.questionID.add(questionID);
    }

    /**
     * Returns id of asked question from question table
     *
     * @param i - 0..N Question ID
     * @return id in question table
     */
    public Integer getQuestionID(int i) {
        try {
            return this.questionID.get(i);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Sets id for pool to connect pool & question tables
     *
     * @param poolID id of pool in pool table
     */
    public void setPoolID(int poolID) {
        this.poolID.add(poolID);
    }

    /**
     * Returns id of asked pool from pool table
     *
     * @param i - 0..N Pool ID
     * @return id in pool table
     */
    public Integer getPoolID(int i) {
        try {
            return this.poolID.get(i);
        } catch (Exception e) {
            return 0;
        }
    }

}
