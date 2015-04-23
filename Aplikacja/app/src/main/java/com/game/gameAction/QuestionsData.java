package com.game.gameAction;

import android.content.Context;
import android.database.Cursor;

import com.game.milionerki.R;
import com.game.sql.sqlAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import menu.ObjectDrawerItem;

@SuppressWarnings("unused") public class QuestionsData {

	private Map<String, String> question = new HashMap<String, String>();
	private sqlAdapter sqlAdapter;

    public QuestionsData(Context context) {
        sqlAdapter = new sqlAdapter(context);
    }

	public Map<String, String> downloadQuestions(int questionVal, int[] oldQuestion) {
        sqlAdapter.open();

        String[] kolumny = { "Nr_Pytania", "Pytanie", "Poprawna_odp", "Zla_odp1", "Zla_odp2", "Zla_odp3" };
        Cursor data = sqlAdapter.getColumn(kolumny, sqlAdapter.DB_QUESTION_TABLE, "Poziom_trudnosci = " + questionVal);

        if (data.getCount() > 0) {
            int total = data.getCount() - 1;
            int chooseQuestion = 0;
            boolean error = false;

            do {
                error = false;
                Random r = new Random();
                chooseQuestion = r.nextInt(total + 1);

                for (int i = 0; i < oldQuestion.length; i++) {
                    if (oldQuestion[i] == chooseQuestion) {
                        error = true;
                    }
                }
            } while (error);

            data.moveToPosition(chooseQuestion);

            String id = data.getString(0);
            String questionTxt = data.getString(1);

            String get_odp[] = new String[4];

            get_odp[0] = data.getString(2);
            get_odp[1] = data.getString(3);
            get_odp[2] = data.getString(4);
            get_odp[3] = data.getString(5);

            int min = 1;
            int max = 4;

            String odp[] = new String[4];
            int bylo[] = new int[4];
            int count = 0;
            int poprawna = 0;
            while (count < 4) {
                Random r = new Random();
                int rand = r.nextInt(max - min + 1) + min;
                error = false;

                for (int i = 0; i < bylo.length; i++) {
                    if (bylo[i] == rand)
                        error = true;
                }

                if (!error) {
                    if (rand == 1)
                        poprawna = count;
                    bylo[count] = rand;
                    odp[count] = get_odp[rand - 1];
                    count++;
                }
            }

            for (int i = 0; i < 4; i++) {
                question.put("odp_" + i, odp[i]);
            }
            question.put("poprawna", "odp_" + poprawna);
            question.put("pytanie", questionTxt);
            question.put("id", id);

        } else {
            question.put("error", "Nie ma pytań..");
        }

        sqlAdapter.close();
        return question;
	}
	
	public boolean checkAnswer(Map<String, String> questionData, int answer) {
		String poprawna = questionData.get("poprawna");

        if (poprawna.equalsIgnoreCase("odp_" + answer))
            return true;

        return false;
	}
	
}
