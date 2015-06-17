package com.swisstournament.sh4ke.swisstournament.Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh4ke on 17.06.15.
 */
public class SwissTournament {

    private List<Player> players;
    private List<Round> finishedRounds;
    private Round currentRound;

    public SwissTournament() {
        players = new ArrayList<Player>();
        finishedRounds = new ArrayList<Round>();
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

        if (currentRound != null) {
            return currentRound.isFinished();
        } else if (players.size() >= 2) {
            return true;
        }
        return false;
    }

    /**
     * Starts the next round with all currently registered players.
     *
     * @return true if the creation succeeded.
     */
    public void startNextRound() throws Exception{
        if (!canStartNextRound()) {
            if (players.size() < 2){
                throw new Exception("Not enough players to start tournament");
            }else {
                throw new Exception("Round not Finished");
            }
        }
        currentRound = new Round(players);
        currentRound.start();
    }

    public boolean enterResult(Player p1, int won_p1, Player p2, int won_p2) {
        if (currentRound.isStarted()) {
            try {
                currentRound.enterResult(p1, won_p1, p2, won_p2);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
}
