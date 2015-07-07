package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Player.ByePlayer;
import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Ranking.Ranking;
import com.swisstournament.sh4ke.swisstournament.Core.Ranking.RankingType;
import com.swisstournament.sh4ke.swisstournament.Core.Ranking.WinsAndSetsRanking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh4ke on 17.06.15.
 */
public class SwissTournament {

    private List<Player> players;
    private List<Round> finishedRounds;
    private List<Ranking> rankings;
    private Round currentRound;
    private boolean started;


    public SwissTournament() {
        players = new ArrayList<>();
        finishedRounds = new ArrayList<>();
        rankings = new ArrayList<>();
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public Round getCurrentRound() {
        if (currentRound != null && currentRound.isStarted()) {
            return currentRound;
        } else {
            throw new IllegalStateException("Round has not started yet!");
        }
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
            if (players.size() % 2 == 1) {
                addPlayer(ByePlayer.getInstance());
            }
            started = true;
        } else {
            throw new IllegalStateException("Can't start tournament yet.");
        }
    }

    public boolean canStartNextRound() {
        if (this.isStarted()) {
            return currentRound == null || currentRound.canBeFinished();
        }
        return false;
    }

    /**
     * Starts the next round with all currently registered players.
     *
     * @return returns the last finished round
     */
    public void startNextRound() throws IllegalStateException {
        currentRound = new Round(players, finishedRounds);
        currentRound.start();
    }

    public void endCurrentRound() {
        if (canStartNextRound()) {
            Round oldRound = currentRound;

            if(oldRound != null) {
                finishedRounds.add(oldRound);
                Ranking ranking = createRanking(oldRound);
                rankings.add(ranking);
            }
        }else{
            throw new IllegalStateException("Round not Finished");
        }
    }

    private Ranking createRanking(Round round) {
        return new WinsAndSetsRanking(round, finishedRounds);
    }

    public int getMinPossibleRounds() {
        if (isStarted()) {
            double i = 0;
            double power = 0;
            for (; power < registeredPlayerCount(); i++) {
                power = Math.pow(2.0, i);
            }
            return (int) i-1;
        }
        return 0;
    }

    public int getMaxPossibleRounds() {
        if (isStarted()) {
            return registeredPlayerCount() - 1;
        }
        return 0;
    }

    public Ranking getCurrentRanking(RankingType type) {
        if (!rankings.isEmpty()){
            return rankings.get(rankings.size()-1);
        }
        return null;
    }


}
