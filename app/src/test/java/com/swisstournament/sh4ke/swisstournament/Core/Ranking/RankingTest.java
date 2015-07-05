package com.swisstournament.sh4ke.swisstournament.Core.Ranking;

import com.swisstournament.sh4ke.swisstournament.BuildConfig;
import com.swisstournament.sh4ke.swisstournament.Core.Game;
import com.swisstournament.sh4ke.swisstournament.Core.Player.HumanPlayer;
import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Ranking.Ranking;
import com.swisstournament.sh4ke.swisstournament.Core.Ranking.RankingType;
import com.swisstournament.sh4ke.swisstournament.Core.Round;
import com.swisstournament.sh4ke.swisstournament.Core.SwissTournament;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by sh4ke on 05.07.15.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RankingTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private SwissTournament t;
    private List<HumanPlayer> players;

    @Before
    public void setup() {
        t = new SwissTournament();
        assertNotNull("Tournament could not be created", t);
        players = new ArrayList();
        for (int i = 0; i < 32; i++) {
            players.add(new HumanPlayer("p" + (i + 1)));
        }
        assertTrue(players.size() == 32);
    }

    private void startTournamentWithPlayers(int number_of_players) {
        t = new SwissTournament();
        for (int i = 0; i < number_of_players; i++) {
            t.addPlayer(players.get(i));
        }
        assertTrue(t.canStartTournament());
        t.startTournament();
        assertTrue(t.isStarted());

        assertTrue(t.canStartNextRound());
    }

    private void startRoundWithPlayers(int number_of_players) {
        startTournamentWithPlayers(number_of_players);
        t.startTournament();
        assertTrue(t.isStarted());

        assertTrue(t.canStartNextRound());
        t.startNextRound();
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void getCurrentSetsRankingWithNoRoundFinishedFailsTest() {
        startRoundWithPlayers(2);

        Ranking r = t.getCurrentRanking(RankingType.SETS);
        assertEquals(null, r);
    }

    @Test
    public void getCurrentSetsRankingWith2PlayersPlayer1WinsTest() {
        startRoundWithPlayers(2);
        Round r = t.getCurrentRound();
        Game g = r.getNextUnfinishedGame();

        g.enterResult(3,2);
        Player winner = g.getWinner();
        Player loser = g.getLoser();

        t.endCurrentRound();

        Ranking ranking = t.getCurrentRanking(RankingType.SETS);
        assertNotNull(ranking);

        assertEquals(winner, ranking.getRank(1));
        assertEquals(loser, ranking.getRank(2));
    }

    @Test
    public void getCurrentSetsRankingWith2PlayersPlayer2WinsTest() {
        startRoundWithPlayers(2);
        Round r = t.getCurrentRound();
        Game g = r.getNextUnfinishedGame();

        g.enterResult(2,3);
        Player winner = g.getWinner();
        Player loser = g.getLoser();

        t.endCurrentRound();

        Ranking ranking = t.getCurrentRanking(RankingType.SETS);
        assertNotNull(ranking);

        assertEquals(winner, ranking.getRank(1));
        assertEquals(loser, ranking.getRank(2));
    }
}
