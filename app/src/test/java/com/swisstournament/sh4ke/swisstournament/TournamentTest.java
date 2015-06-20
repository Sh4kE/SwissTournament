package com.swisstournament.sh4ke.swisstournament;

import com.swisstournament.sh4ke.swisstournament.Core.Game;
import com.swisstournament.sh4ke.swisstournament.Core.Player;
import com.swisstournament.sh4ke.swisstournament.Core.SwissTournament;

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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TournamentTest {
    private SwissTournament t;
    List<Player> players;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        t = new SwissTournament();
        assertNotNull("Tournament could not be created", t);
        players = new ArrayList<Player>();
        for (int i = 0; i < 16; i++) {
            players.add(new Player("p" + (i + 1)));
        }
        assertTrue(players.size() == 16);
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
    public void canNotStartTournamentWithLessThanTwoPlayersTest() throws Exception {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.get(0));
        assertFalse(t.canStartTournament());
        assertFalse(t.canStartNextRound());
    }

    @Test
    public void canStartTournamentWithTwoOrMorePlayersTest() throws Exception {
        t.addPlayer(players.get(0));
        for(int i = 1; i < 16; i++){
            t.addPlayer(players.get(i));
            assertTrue(t.canStartTournament());
        }
    }

    @Test
    public void canNotAddAPlayerTwiceTest(){
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

        while(! t.getCurrentRound().isFinished()){
            Game g = t.getCurrentRound().getNextGame();
            Player p1 = g.getP1();
            Player p2 = g.getP2();
            t.enterResult(p1, 3, p2, 2);
        }

        assertTrue(t.canStartNextRound());
    }

    @Test(expected= InvalidParameterException.class)
    public void canNotEnterNegativeResults(){
        startRoundWithPlayers(2);
        t.enterResult(players.get(0), -1, players.get(1), 2);
        //assertFalse(t.canStartNextRound());
    }

    private void startRoundWithPlayers(int number_of_players){
        for(int i = 0; i < number_of_players; i++){
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
}