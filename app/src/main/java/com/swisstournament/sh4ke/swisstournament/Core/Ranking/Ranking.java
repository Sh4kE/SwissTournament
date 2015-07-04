package com.swisstournament.sh4ke.swisstournament.Core.Ranking;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Round;

import java.util.List;

/**
 * Created by sh4ke on 24.06.15.
 */
public abstract class Ranking {

    protected Round round;
    protected List<Round> formerRounds;

    protected Ranking(Round round, List<Round> formerRounds) {
        this.round = round;
        this.formerRounds = formerRounds;
    }

    public abstract Player getRank(int rank);

    public abstract List<Player> getLadder();
}
