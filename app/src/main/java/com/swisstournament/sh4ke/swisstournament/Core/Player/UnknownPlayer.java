package com.swisstournament.sh4ke.swisstournament.Core.Player;

/**
 * Created by sh4ke on 24.06.15.
 */
public class UnknownPlayer extends Player {
    private final String name;

    public UnknownPlayer() {
        this.name = "?";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player otherPlayer = (HumanPlayer) o;
        return !(otherPlayer instanceof UnknownPlayer);
    }
}
