package com.swisstournament.sh4ke.swisstournament.Core.Player;

/**
 * Created by sh4ke on 23.06.15.
 */
public abstract class Player {

    public abstract String getName();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player otherPlayer = (HumanPlayer) o;

        return getName().equals(otherPlayer.getName());
    }

    @Override
    public String toString() {
        return String.format("Player '%s'", getName());
    }
}
