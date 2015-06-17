package com.swisstournament.sh4ke.swisstournament;

import com.swisstournament.sh4ke.swisstournament.Core.Player;
import com.swisstournament.sh4ke.swisstournament.Core.SwissTournament;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Vector;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TournamentTest {
    private SwissTournament t;
    Vector<Player> players;

    @Before
    public void setup() throws Exception {
        t = new SwissTournament();
        assertNotNull("Tournament could not be created", t);
        players = new Vector<Player>();
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
        t.addPlayer(players.elementAt((0)));
        assertFalse(t.canStartTournament());
    }

    @Test
    public void canNotStartTournamentWithLessThanTwoPlayersTest() throws Exception {
        assertFalse(t.canStartTournament());
        t.addPlayer(players.elementAt((0)));
        assertFalse(t.canStartTournament());
    }

    @Test
    public void canStartTournamentWithTwoOrMorePlayersTest() throws Exception {
        t.addPlayer(players.elementAt((0)));
        for(int i = 1; i < 4; i++){
            t.addPlayer(players.elementAt((i)));
            assertTrue(t.canStartTournament());
        }
    }

    @Test
    public void canNotAddAPlayerTwiceTest(){
        t.addPlayer(players.elementAt(0));
        t.addPlayer(players.elementAt(0));
        assertEquals(1, t.registeredPlayerCount());
    }

    @Test
    public void canPlayRoundWithTwoPlayersTest() throws Exception {
        for(int i = 0; i < 2; i++){
            t.addPlayer(players.elementAt(i));
        }
        assertTrue(t.canStartNextRound());
        t.startNextRound();
        t.enterResult(players.elementAt(0), 3, players.elementAt(1), 2);
        assertTrue(t.canStartNextRound());
    }
}