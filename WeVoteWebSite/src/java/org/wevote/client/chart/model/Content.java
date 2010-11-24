package org.wevote.client.chart.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

/**
 * Fills Answer.java and Question.java form RPC
 * Provides access for Answer.java and Question.java
 *
 * @see http://code.google.com/p/wevote/wiki/JSONChart
 *
 * @author NorthernDemon
 */

public class Content {

    public static Question questions = new Question();

    /**
     * @param result JSON string from RPC with question info
     */
    public static void addQuestion(String result) {
        JSONValue jsonValue = JSONParser.parse(result);

        Question question = new Question();
        question.setId((int) jsonValue.isObject().get("id").isNumber().doubleValue());
        question.setTitle(jsonValue.isObject().get("title").isString().stringValue());
        question.setPool(jsonValue.isObject().get("pool").isString().stringValue());
        question.setQuestion(jsonValue.isObject().get("question").isString().stringValue());
        question.setDate(jsonValue.isObject().get("date").isString().stringValue());

        JSONArray jsonArray = jsonValue.isObject().get("answers").isArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            Answer answer = new Answer();

            answer.setAnswer(jsonArray.get(i).isObject().get("answer").isString().stringValue());
            answer.setRating((int) jsonArray.get(i).isObject().get("rating").isNumber().doubleValue());

            for (int j = 0; j < jsonArray.get(i).isObject().get("gender").isArray().size(); j++)
                answer.addAnswerByGender((int) jsonArray.get(i).isObject().get("gender").isArray().get(j).isNumber().doubleValue());

            for (int j = 0; j < jsonArray.get(i).isObject().get("age").isArray().size(); j++)
                answer.addAnswerByAge((int) jsonArray.get(i).isObject().get("age").isArray().get(j).isNumber().doubleValue());

            question.addAnswers(answer);
        }

        questions = question;
    }

}