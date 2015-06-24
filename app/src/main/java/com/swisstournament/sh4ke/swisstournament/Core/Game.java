package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;

import java.security.InvalidParameterException;

/**
 * Created by sh4ke on 17.06.15.
 */
public class Game {

    private Player p1, p2;
    private int won_p1, won_p2;
    private boolean finished;

    public Game(Player p1, Player p2) {
        if (p1 == null || p2 == null) {
            throw new IllegalArgumentException("player can't be null");
        }
        this.p1 = p1;
        this.p2 = p2;
        this.finished = false;
    }

    public void enterResult(int won_p1, int won_p2) throws InvalidParameterException {
        if (won_p1 < 0 || won_p2 < 0)
            throw new InvalidParameterException("results cannot be negative!");
        if (won_p1 > 3 || won_p2 > 3)
            throw new InvalidParameterException("results cannot be greater than 3!");
        if (won_p1 == won_p2)
            throw new InvalidParameterException("results cannot be equal to each other!");
        this.won_p1 = won_p1;
        this.won_p2 = won_p2;
        this.finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() throws IllegalStateException {
        if (isFinished()) {
            return (won_p1 > won_p2) ? p1 : p2;
        }
        throw new IllegalStateException("this game is not played yet");
    }

    public Player getLoser() throws IllegalStateException {
        if (isFinished()) {
            return (won_p1 < won_p2) ? p1 : p2;
        }
        throw new IllegalStateException("this game is not played yet");
    }

    public int getLosersWonGames() throws IllegalStateException {
        if (isFinished()) {
            return (won_p1 < won_p2) ? won_p1 : won_p2;
        }
        throw new IllegalStateException("this game is not played yet");
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }
}
