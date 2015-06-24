package com.swisstournament.sh4ke.swisstournament.Core.Player;

/**
 * Created by sh4ke on 17.06.15.
 */
public class HumanPlayer extends Player {
    private final String name;

    public HumanPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
