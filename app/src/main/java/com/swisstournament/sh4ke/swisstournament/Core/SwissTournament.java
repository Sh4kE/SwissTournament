package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Exceptions.PlayerNotFoundException;

import java.util.Vector;

/**
 * Created by sh4ke on 17.06.15.
 */
public class SwissTournament {

    private Vector<Player> players;
    private Vector<Round> finishedRounds;
    private Round currentRound;

    public SwissTournament() {
        players = new Vector<Player>();
        finishedRounds = new Vector<Round>();
        currentRound = new Round(players);
    }

    public void addPlayer(Player p) {
        if (!players.contains(p)) {
            players.add(p);
        }
    }

    public int registeredPlayerCount() {
        return players.size();
    }

    public boolean canStartTournament() {
        return players.size() > 1;
    }

    public boolean canStartNextRound() {
        return currentRound.isFinished();
    }

    public boolean startNextRound() {
        if (!canStartNextRound()) {
            return false;
        }
        return true;
    }

    public boolean enterResult(Player p1, int won_p1, Player p2, int won_p2) {
        try {
            currentRound.getGame(p1, p2).enterResult(won_p1, won_p2);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
