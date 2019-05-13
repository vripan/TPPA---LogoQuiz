package fii.tppa.logoquiz;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameSession {
    private List<Logo> logos = null;
    private int score = 0;
    private Logo current;

    public GameSession(@NonNull List<Logo> logos) {
        this.logos = new LinkedList<>(logos);
        Collections.shuffle(this.logos);
    }

    public Logo getLogo() {
        if (logos.size() == 0)
            return null;

        current = this.logos.get(0);
        this.logos.remove(0);
        return current;
    }

    public void increaseScore() {
        score += 1;

    }

    public void decreaseScore() {
        score -= 1;
    }

    public void resetScore() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public boolean answerIsCorrect(String answer) {
        return answer.equals(current.name);
    }

    public Logo getCurrentLogo() {
        return current;
    }
}
