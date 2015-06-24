package com.swisstournament.sh4ke.swisstournament.Core.Player;

/**
 * Created by sh4ke on 17.06.15.
 *
 * Singleton class for a bye player.
 */
public class ByePlayer extends Player {
    private final String name;
    private static final Player bye = new ByePlayer();

    public static Player getInstance(){
        return bye;
    }

    private ByePlayer() {
        this.name = "Bye";
    }

    @Override
    public String getName() {
        return name;
    }
}
