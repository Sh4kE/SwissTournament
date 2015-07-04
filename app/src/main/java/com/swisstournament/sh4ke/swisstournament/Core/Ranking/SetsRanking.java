package com.swisstournament.sh4ke.swisstournament.Core.Ranking;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Round;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by sh4ke on 04.07.15.
 */
public class SetsRanking extends Ranking {

    List<Player> ladder;

    public SetsRanking(Round round, List<Round> formerRounds) {
        super(round, formerRounds);
        ladder = createLadder();
    }

    private List<Player> createLadder(){
        List<Player> players = round.getPlayers();

        Map<Player, Integer> player_wins = new HashMap();


        for(Player p : players) {
            player_wins.put(p, round.getWithdrawnSets(p));
        }

        ValueComparator bvc =  new ValueComparator(player_wins);
        Map<Player, Integer> sorted_player_wins = new TreeMap(bvc);
        sorted_player_wins.putAll(player_wins);
        return new ArrayList<>(sorted_player_wins.keySet());
    }

    @Override
    public Player getRank(int rank) {
        return ladder.get(rank-1);
    }

    @Override
    public List<Player> getLadder() {
        return ladder;
    }

    private class ValueComparator implements Comparator<Player> {

        Map<Player, Integer> player_wins;
        public ValueComparator(Map<Player, Integer> player_wins) {
            this.player_wins = player_wins;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(Player a, Player b) {
            if (player_wins.get(a) >= player_wins.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
