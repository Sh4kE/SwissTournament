package com.swisstournament.sh4ke.swisstournament.Core;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh4ke on 17.06.15.
 */
public class SwissTournament {

    private List<Player> players;
    private List<Round> finishedRounds;
    private Round currentRound;
    private boolean started;


    public SwissTournament() {
        players = new ArrayList<Player>();
        finishedRounds = new ArrayList<Round>();
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void addPlayer(Player p) {
        if (this.isStarted()) {
            throw new IllegalStateException("Tournament is already started. Can't add Players any more.");
        }
        if (!players.contains(p)) {
            players.add(p);
        }
    }

    public int registeredPlayerCount() {
        return players.size();
    }

    public boolean canStartTournament() {
        return players.size() >= 2;
    }

    public void startTournament() {
        if (canStartTournament()) {
            started = true;
        } else {
            throw new IllegalStateException("Can't start tournament yet.");
        }
    }

    public boolean canStartNextRound() {
        if (this.isStarted()) {
            if (currentRound != null) {
                return currentRound.isFinished();
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Starts the next round with all currently registered players.
     *
     * @return returns the last finished round
     */
    public Round startNextRound() throws IllegalStateException {
        if (canStartNextRound()) {
            Round oldRound = currentRound;
            currentRound = new Round(players);
            currentRound.start();
            return oldRound;
        }
        throw new IllegalStateException("Round not Finished");
    }

    public boolean enterResult(Player p1, int won_p1, Player p2, int won_p2) throws InvalidParameterException, IllegalStateException {
        if(currentRound != null){
            if (currentRound.isStarted()) {
                currentRound.enterResult(p1, won_p1, p2, won_p2);
                return true;
            }
        }
        throw new IllegalStateException("Round has not started yet. Can't enter resutlts.");
    }


}
