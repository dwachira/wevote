package org.wevote.client.chart.model;

import java.util.Vector;

/**
 * Contains collection of answers for question
 *
 * @author NorthernDemon
 */

public class Answer {

    private int rating;
    private String answer;
    private Vector<Integer> gender = new Vector();
    private Vector<Integer> age = new Vector();
    
    /**
     * @param rating total number of votes for that answer
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @param answer users answer for question
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    /**
     * @param gender rating of answer in gender division
     *
     * 0 — male
     * 1 — female
     */
    public void addAnswerByGender(int gender) {
        this.gender.add(gender);
    }

    /**
     * @param gender rating of answer in age division
     *
     * 0 — +17
     * 1 — 18..24
     * 2 — 25..34
     * 3 — 35..44
     * 4 — 45..54
     * 5 — 55+
     */
    public void addAnswerByAge(int age) {
        this.age.add(age);
    }

    /**
     * @return total number of votes for that answer
     */
    public int getRating() {
        return rating;
    }

    /**
     * @return answer users answer for question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @return collection of ratings in gender division
     */
    public Vector<Integer> getAnswerByGender() {
        return gender;
    }

    /**
     * @return collection of ratings in age division
     */
    public Vector<Integer> getAnswerByAge() {
        return age;
    }

}