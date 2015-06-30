package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.BuildConfig;
import com.swisstournament.sh4ke.swisstournament.Core.Player.HumanPlayer;
import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TournamentTest {
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
        t = new SwissTournament();
        for (int i = 0; i < number_of_players; i++) {
            t.addPlayer(players.get(i));
        }
        assertTrue(t.canStartTournament());
        t.startTournament();
        assertTrue(t.isStarted());

        assertTrue(t.canStartNextRound());
        t.startNextRound();
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canCreateTournamentTest() {
        assertNotNull("Tournament could not be created", t);
    }

    @Test
    public void canAddPlayerTest() {
        t.addPlayer(players.get(0));
        assertFalse(t.canStartTournament());
    }

    @Test
    public void canNotAddPlayersAfterTournamentStartTest() {
        startRoundWithPlayers(2);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Tournament is already started. Can't add Players any more.");

        t.addPlayer(players.get(2));
    }

    @Test
    public void canNotStartTournamentWithLessThanTwoPlayersTest() {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.get(0));
        assertFalse(t.canStartTournament());
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canNotCallStartTournamentWithLessThanTwoPlayersTest() {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.get(0));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Can't start tournament yet.");

        t.startTournament();

        assertFalse(t.canStartTournament());
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canStartTournamentWithTwoOrMorePlayersTest() {
        t.addPlayer(players.get(0));
        for (int i = 1; i < 16; i++) {
            t.addPlayer(players.get(i));
            assertTrue(t.canStartTournament());
        }
    }

    @Test
    public void canNotAddAPlayerTwiceTest() {
        t.addPlayer(players.get(0));
        t.addPlayer(players.get(0));
        assertEquals(1, t.registeredPlayerCount());
    }

    @Test
    public void canPlayOneRoundWithTwoPlayersTest() {
        startRoundWithPlayers(2);
        Round r = t.getCurrentRound();
        r.enterResult(players.get(0), 3, players.get(1), 2);
        assertTrue(t.canStartNextRound());
    }

    @Test
    public void canPlayOneRoundWithFourPlayersTest() {
        startRoundWithPlayers(4);

        while (!t.getCurrentRound().isFinished()) {
            Game g = t.getCurrentRound().getNextUnfinishedGame();
            Player p1 = g.getP1();
            Player p2 = g.getP2();
            Round r = t.getCurrentRound();
            r.enterResult(p1, 3, p2, 2);
        }

        assertTrue(t.canStartNextRound());
    }

    @Test
    public void canNotCallStartNextRoundWhenResultsAreMissingTest() {
        startRoundWithPlayers(2);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Round not Finished");

        t.startNextRound();
    }

    @Test
    public void canNotEnterResultsWhenRoundIsNotStartedTest() {
        startTournamentWithPlayers(2);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Round has not started yet!");

        Round r = t.getCurrentRound();
        r.enterResult(players.get(0), 3, players.get(1), 2);
    }

    @Test
    public void canNotEnterNegativeResultsTest() {
        startRoundWithPlayers(2);

        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be negative!");

        Round r = t.getCurrentRound();
        r.enterResult(players.get(0), -1, players.get(1), 2);
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canStartSecondRoundWithThreePlayersTest() {
        startRoundWithPlayers(3);

        Round currentRound = t.getCurrentRound();
        Game g1 = currentRound.getNextUnfinishedGame();
        g1.enterResult(3, 2);
        assertEquals(null, currentRound.getNextUnfinishedGame());
        assertTrue(t.canStartNextRound());
        t.startNextRound();
        Round nextRound = t.getCurrentRound();
        assertNotEquals(currentRound, nextRound);

        Game g2 = nextRound.getNextUnfinishedGame();
        assertNotEquals(g1, g2);

        g2.enterResult(3, 2);
        assertEquals(null, currentRound.getNextUnfinishedGame());
        assertTrue(t.canStartNextRound());
    }

    @Test
    public void TwoPlayersWithOneWinPlayAgainstEachOtherTest() {
        startRoundWithPlayers(4);
        Player p1, p2, p3, p4;

        Game g1 = t.getCurrentRound().getNextUnfinishedGame();
        p1 = g1.getP1();
        p2 = g1.getP2();
        g1.enterResult(3, 2);

        Game g2 = t.getCurrentRound().getNextUnfinishedGame();
        p3 = g2.getP1();
        p4 = g2.getP2();
        g2.enterResult(2, 3);

        assertEquals(null, t.getCurrentRound().getNextUnfinishedGame());
        assertTrue(t.canStartNextRound());
        t.startNextRound();

        Game g3 = t.getCurrentRound().getNextUnfinishedGame();
        boolean correctPlayers = (g3.getP1().equals(p1) && g3.getP2().equals(p4)) || (g3.getP1().equals(p2) && g3.getP2().equals(p3));
        assertTrue(correctPlayers);
        g3.enterResult(3, 2);

        Game g4 = t.getCurrentRound().getNextUnfinishedGame();
        correctPlayers = (g4.getP1().equals(p1) && g4.getP2().equals(p4)) || (g4.getP1().equals(p2) && g4.getP2().equals(p3));
        assertTrue(correctPlayers);
        g4.enterResult(3, 2);

        assertTrue(t.canStartNextRound());
    }

    @Test
    public void ThreePlayersWithOneWinTest() {
        startRoundWithPlayers(6);

        Game[] games = new Game[3];

        for (int i = 0; i < 3; i++) {
            games[i] = playNextGame();
        }

        assertEquals(null, t.getCurrentRound().getNextUnfinishedGame());
        assertTrue(t.canStartNextRound());
        t.startNextRound();

        Game g = t.getCurrentRound().getNextUnfinishedGame();
        Player p1 = g.getP1();
        Player p2 = g.getP2();
        Assert.assertEquals(1, t.getCurrentRound().getPlayerWins(p1));
        Assert.assertEquals(1, t.getCurrentRound().getPlayerWins(p2));

        playNextGame();

        g = t.getCurrentRound().getNextUnfinishedGame();
        p1 = g.getP1();
        p2 = g.getP2();
        boolean oneHasOneWin = t.getCurrentRound().getPlayerWins(p1) == 1 || t.getCurrentRound().getPlayerWins(p2) == 1;
        boolean oneHasZeroWins = t.getCurrentRound().getPlayerWins(p1) == 0 || t.getCurrentRound().getPlayerWins(p2) == 0;
        Assert.assertTrue(oneHasOneWin);
        Assert.assertTrue(oneHasZeroWins);

        playNextGame();

        g = t.getCurrentRound().getNextUnfinishedGame();
        p1 = g.getP1();
        p2 = g.getP2();
        boolean bothHaveZeroWins = t.getCurrentRound().getPlayerWins(p1) == 0 && t.getCurrentRound().getPlayerWins(p2) == 0;
        Assert.assertTrue(oneHasOneWin);
        Assert.assertTrue(oneHasZeroWins);

        playNextGame();

        assertTrue(t.canStartNextRound());
    }

    @Test
    public void getcurrentRankingWithNoRoundFinishedFailsTest() {
        startRoundWithPlayers(2);

        Ranking r = t.getCurrentRanking();
        assertEquals(null, r);
    }

    @Test
    public void getMinPossibleRoundsWithPowerZeroTest() {
        assertEquals(0, t.getMinPossibleRounds());
    }

    @Test
    public void getMinPossibleRoundsTest() {
        for (int i = 2; i <= 32; i++) {
            startRoundWithPlayers(i);
            int subscript = findMaxPossibleSubscript(i);
            assertEquals(subscript, t.getMinPossibleRounds());
        }
    }

    private int findMaxPossibleSubscript(int p) {
        int i = 0;
        while (Math.pow(2, i) < p) {
            i++;
        }
        return i;
    }

    private Game playNextGame() {
        Game g = t.getCurrentRound().getNextUnfinishedGame();
        g.enterResult(3, 2);
        return g;
    }
}