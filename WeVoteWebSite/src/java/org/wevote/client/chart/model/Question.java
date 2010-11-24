package org.wevote.client.chart.model;

import java.util.Vector;

/**
 * Conteins question info and collection of answers for it
 *
 * @author NorthernDemon
 */

public class Question {
    
    private int id;
    private String title, pool, question, date;
    private Vector<Answer> answers = new Vector<Answer>();
    
    /**
     * @param id number of question in database
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param title name of question
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param pool name of pool to which question belong to
     */
    public void setPool(String pool) {
        this.pool = pool;
    }

    /**
     * @param question actual question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @param date of voting
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Adds answer to collection
     *
     * @param answer object of Answer.java, containig one answer unit
     */
    public void addAnswers(Answer answer) {
        this.answers.add(answer);
    }

    /**
     * @return number of question in database
     */
    public int getId() {
        return id;
    }

    /**
     * @return name of question
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return name of pool to which question belong to
     */
    public String getPool() {
        return pool;
    }

    /**
     * @return actual question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return date of voting
     */
    public String getDate() {
        return date;
    }

    /**
     * @return collection of answers
     */
    public Vector<Answer> getAnswers() {
        return answers;
    }

}