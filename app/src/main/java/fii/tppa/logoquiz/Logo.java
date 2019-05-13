package fii.tppa.logoquiz;

import java.util.ArrayList;
import java.util.Collections;

public class Logo {
    public int id;
    public String name;
    public String variant;
    public String hint;
    public String alternative_1;
    public String alternative_2;
    public String alternative_3;
    public String img_q;
    public String img_a;

    public Logo(int id, String name, String variant, String hint, String alternative_1, String alternative_2, String alternative_3, String img_q, String img_a) {
        this.id = id;
        this.name = name;
        this.variant = variant;
        this.hint = hint;
        this.alternative_1 = alternative_1;
        this.alternative_2 = alternative_2;
        this.alternative_3 = alternative_3;
        this.img_q = img_q;
        this.img_a = img_a;
    }

    public ArrayList<String> getPossibleAnswersRandomized() {
        ArrayList<String> answers = new ArrayList<String>();

        answers.add(name);
        answers.add(alternative_1);
        answers.add(alternative_2);
        answers.add(alternative_3);

        Collections.shuffle(answers);

        return answers;
    }
}
