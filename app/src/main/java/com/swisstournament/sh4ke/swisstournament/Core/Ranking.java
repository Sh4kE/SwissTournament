package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Player.UnknownPlayer;

import java.util.List;

/**
 * Created by sh4ke on 24.06.15.
 */
public class Ranking {

    private Round round;
    private List<Round> formerRounds;

    public Ranking(Round round, List<Round> formerRounds) {
        this.round = round;
        this.formerRounds = formerRounds;
    }

    private Player getRank(int rank){
        return new UnknownPlayer();
    }
}
