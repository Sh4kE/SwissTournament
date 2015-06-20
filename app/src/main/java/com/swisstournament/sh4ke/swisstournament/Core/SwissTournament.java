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
        if (!this.isStarted()) {
            return players.size() >= 2;
        }
        return false;
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
     */
    public void startNextRound() throws Exception {
        if (!canStartNextRound()) {
            throw new Exception("Round not Finished");
        }
        currentRound = new Round(players);
        currentRound.start();
    }

    public boolean enterResult(Player p1, int won_p1, Player p2, int won_p2) throws InvalidParameterException {
        if (currentRound.isStarted()) {
            currentRound.enterResult(p1, won_p1, p2, won_p2);
            return true;
        }
        return false;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public boolean isStarted() {
        return started;
    }
}
