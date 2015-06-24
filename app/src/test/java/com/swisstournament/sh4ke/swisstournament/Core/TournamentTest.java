package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.BuildConfig;
import com.swisstournament.sh4ke.swisstournament.Core.Player.HumanPlayer;
import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TournamentTest {
    private SwissTournament t;
    private List<HumanPlayer> players;

    @Test
    public void defaultTest() {

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
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
        try {
            t.startNextRound();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canCreateTournamentTest() throws Exception {
        assertNotNull("Tournament could not be created", t);
    }

    @Test
    public void canAddPlayerTest() throws Exception {
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
    public void canNotStartTournamentWithLessThanTwoPlayersTest() throws Exception {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.get(0));
        assertFalse(t.canStartTournament());
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canNotCallStartTournamentWithLessThanTwoPlayersTest() throws Exception {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.get(0));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Can't start tournament yet.");

        t.startTournament();

        assertFalse(t.canStartTournament());
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canStartTournamentWithTwoOrMorePlayersTest() throws Exception {
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
    public void canPlayOneRoundWithTwoPlayersTest() throws Exception {
        startRoundWithPlayers(2);
        t.enterResult(players.get(0), 3, players.get(1), 2);
        assertTrue(t.canStartNextRound());
    }

    @Test
    public void canPlayOneRoundWithFourPlayersTest() throws Exception {
        startRoundWithPlayers(4);

        while (!t.getCurrentRound().isFinished()) {
            Game g = t.getCurrentRound().getNextUnfinishedGame();
            Player p1 = g.getP1();
            Player p2 = g.getP2();
            t.enterResult(p1, 3, p2, 2);
        }

        assertTrue(t.canStartNextRound());
    }

    @Test
    public void canNotCallStartNextRoundWhenResultsAreMissingTest() throws Exception {
        startRoundWithPlayers(2);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Round not Finished");

        t.startNextRound();
    }

    @Test
    public void canNotEnterResultsWhenRoundIsNotStartedTest() throws Exception {
        startTournamentWithPlayers(2);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Round has not started yet. Can't enter results.");

        t.enterResult(players.get(0), 3, players.get(1), 2);
    }

    @Test
    public void canNotEnterNegativeResultsTest() {
        startRoundWithPlayers(2);

        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be negative!");

        t.enterResult(players.get(0), -1, players.get(1), 2);
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void getMinPossibleRoundsWithPowerZeroTest() {
        assertEquals(0, t.getMinPossibleRounds());
    }

    @Test
    public void getMinPossibleRoundsWithPowerOneTest(){
        for(int i = 2; i <= 2; i++){
            startRoundWithPlayers(i);
            assertEquals(1, t.getMinPossibleRounds());
        }
    }

    @Test
    public void getMinPossibleRoundsWithPowerTwoTest(){
        for(int i = 3; i <= 4; i++){
            startRoundWithPlayers(i);
            assertEquals(2, t.getMinPossibleRounds());
        }
    }

    @Test
    public void getMinPossibleRoundsWithPowerThreeTest(){
        for(int i = 5; i <= 8; i++){
            startRoundWithPlayers(i);
            assertEquals(3, t.getMinPossibleRounds());
        }
    }

    @Test
    public void getMinPossibleRoundsWithPowerFourTest(){
        for(int i = 9; i <= 16; i++){
            startRoundWithPlayers(i);
            assertEquals(4, t.getMinPossibleRounds());
        }
    }

    @Test
    public void getMinPossibleRoundsWithPowerFiveTest(){
        for(int i = 17; i <= 32; i++){
            startRoundWithPlayers(i);
            assertEquals(5, t.getMinPossibleRounds());
        }
    }
}