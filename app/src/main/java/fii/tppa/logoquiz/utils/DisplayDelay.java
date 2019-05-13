package fii.tppa.logoquiz.utils;

import fii.tppa.logoquiz.GameActivity;

public class DisplayDelay implements Runnable {

    public GameActivity game;
    public DisplayDelay(GameActivity game){
        this.game=game;
    }

    @Override
    public void run() {
        game.displayNextLogo();
    }
}
