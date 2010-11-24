package org.wevote.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.wevote.client.chart.ShowChart;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Vector;

/**
 * Retrives Chart info from database using MySQL.java
 *
 * @author NorthernDemon
 */

public class ShowChartImpl extends RemoteServiceServlet implements ShowChart {
    
    /**
     * @see http://code.google.com/p/wevote/wiki/JSONChart
     *
     * @return root JSON string with Chart info
     */
    @Override
    public String myMethod(int id) {
        MySQL question = new MySQL();
        JSONObject root = new JSONObject();
        root.put("id", id);

        Vector<Vector> resultQuestion = question.Select("SELECT title, question, pool_id FROM question WHERE id = " + id + " LIMIT 1");
        root.put("title", resultQuestion.get(0).get(0));
        root.put("question", resultQuestion.get(0).get(1));

        Vector<Vector> resultPool = question.Select("SELECT title, date FROM pool WHERE id = " + resultQuestion.get(0).get(2) + " LIMIT 1");
        root.put("pool", resultPool.get(0).get(0));
        root.put("date", resultPool.get(0).get(1));

        Vector<Vector> resultPoolAnswer = question.Select("SELECT id, answer FROM pool_answer WHERE question_id = " + id);
        JSONArray answers = new JSONArray();
        for(int i = 0; i < resultPoolAnswer.size(); i++) {
            JSONObject answer = new JSONObject();
            Vector<Vector> resultUserAnswer = question.Select("SELECT count(id), user_id FROM user_answer WHERE answer_id = " + resultPoolAnswer.get(i).get(0));
            answer.put("answer", resultPoolAnswer.get(i).get(1));
            answer.put("rating", Integer.parseInt(resultUserAnswer.get(0).get(0).toString()));

            resultUserAnswer = question.Select("SELECT user_id FROM user_answer WHERE answer_id = " + resultPoolAnswer.get(i).get(0));

            int[] gender = new int[2];
            for(int j = 0; j < resultUserAnswer.size(); j++) {
                Vector<Vector> resultUser1 = question.Select("SELECT count(id) FROM user WHERE gender = 'male' AND id = " + resultUserAnswer.get(j).get(0) + " LIMIT 1");
                Vector<Vector> resultUser2 = question.Select("SELECT count(id) FROM user WHERE gender = 'female' AND id = " + resultUserAnswer.get(j).get(0) + " LIMIT 1");
                
                gender[0] += Integer.parseInt(resultUser1.get(0).get(0).toString());
                gender[1] += Integer.parseInt(resultUser2.get(0).get(0).toString());
            }
            JSONArray gender_group = new JSONArray();
            gender_group.add(gender[0]);
            gender_group.add(gender[1]);
            answer.put("gender", gender_group);

            int[] age = new int[6];
            for(int j = 0; j < resultUserAnswer.size(); j++) {
                //Counts presented age of the time of question
                //Time of pool - Time of birthday
                //resultPool.get(0).get(1) - resultUser.get(l).get(0)

                Vector<Vector> resultUser = question.Select("SELECT age FROM user WHERE id = " + resultUserAnswer.get(j).get(0));

                int pool_year = Integer.parseInt(resultPool.get(0).get(1).toString().substring(0, 4));
                int user_year = Integer.parseInt(resultUser.get(0).get(0).toString().substring(0, 4));

                int age_result = pool_year - user_year;

                if (age_result <= 17) {
                    age[0] += 1;
                } else if (age_result <= 24 && age_result >= 18) {
                    age[1] += 1;
                } else if (age_result <= 34 && age_result >= 25) {
                    age[2] += 1;
                } else if (age_result <= 44 && age_result >= 35) {
                    age[3] += 1;
                } else if (age_result <= 55 && age_result >= 45) {
                    age[4] += 1;
                } else if (age_result > 55) {
                    age[5] += 1;
                }
            }
            JSONArray age_group = new JSONArray();
            age_group.add(age[0]);
            age_group.add(age[1]);
            age_group.add(age[2]);
            age_group.add(age[3]);
            age_group.add(age[4]);
            age_group.add(age[5]);
            answer.put("age", age_group);

            answers.add(answer);
        }

        root.put("answers", answers);

        return root.toJSONString();
    }

}