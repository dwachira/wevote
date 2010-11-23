package wevote_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classname Poll stands for Question
 *
 * @author hollgam & NorthernDemon
 */
public class Poll implements Serializable, Comparable<Poll> {
    private String question;
    private ArrayList<String> answers = new ArrayList<String>();
    private String questionString;

    private ArrayList<Integer> answerID = new ArrayList<Integer>(); // Reference to the answer_id in user_answer table

    /**
     *
     * @return
     */
    public String getQuestionString() {
        return questionString;
    }

    /**
     *
     * @param questionString
     */
    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getAnswers() {
        return answers;
    }

    /**
     *
     * @param answers
     */
    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    /**
     *
     * @return
     */
    public String getQuestion() {
        return question;
    }

    /**
     *
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     *
     * @param question
     * @param answers
     */
    public Poll(String question, ArrayList<String> answers) {
        this.question = question;
        this.answers = answers;
        makeQuestionString();
    }

    /**
     *
     */
    public Poll() {}

    /**
     *
     */
    public void makeQuestionString() {
        this.questionString = this.question + ">";
        for (String answer : this.answers) {
            this.questionString += " ";
            this.questionString += answer;
        }
        //System.out.println(questionString);
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Poll o) {
        return question.compareTo(o.getQuestion());
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
        return this.answerID.get(i);
    }

}
