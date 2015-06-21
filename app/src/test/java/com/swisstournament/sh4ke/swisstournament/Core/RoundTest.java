package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.BuildConfig;
import com.swisstournament.sh4ke.swisstournament.Core.Game;
import com.swisstournament.sh4ke.swisstournament.Core.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Round;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RoundTest {
    private List<Player> players;
    private List<Round> oldRounds;
    private Round r;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {

    }

    private void createSimpleRoundWithPlayers(int n) {
        players = new ArrayList();
        for (int i = 0; i < n; i++) {
            players.add(new Player("p" + (i + 1)));
        }
        assertTrue(players.size() == n);
        r = new Round(players);
        assertTrue(r.canStart());
        r.start();
        assertTrue(r.isStarted());
        assertFalse(r.isFinished());
    }

    private boolean playerIsInGame(Player p, Game g){
        return p.equals(g.getP1()) || p.equals(g.getP2());
    }

    @Test
    public void canNotStartRoundWithLessThanTwoPlayers() {
        players = new ArrayList();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot create new round without players!");
        r = new Round(players);
        assertFalse(r.canStart());

        players.add(new Player("p0"));

        thrown.expect(IllegalStateException.class);
        r = new Round(players);
        assertFalse(r.canStart());

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot start round with less than 2 players!");
        r.start();
        assertFalse(r.isStarted());
    }

    @Test
    public void canNotGenerateRoundWithZeroPlayers() {
        players = new ArrayList();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot create new round without players!");
        r = new Round(players);
        assertFalse(r.canStart());
    }

    @Test
    public void canNotGenerateRoundWithOnePlayer() {
        players = new ArrayList();
        players.add(new Player("p0"));
        thrown.expect(IllegalStateException.class);
        r = new Round(players);
        assertFalse(r.canStart());
    }

    @Test
    public void canGenerateSimpleRoundWithTwoPlayers() {
        createSimpleRoundWithPlayers(2);
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        List<Game> games = r.getAllUnfinishedGames();
        assertEquals(1, games.size());
        assertFalse(games.get(0).isFinished());
        assertTrue(playerIsInGame(p1, games.get(0)));
        assertTrue(playerIsInGame(p2, games.get(0)));
    }

    @Test
    public void canFinishSimpleRoundWithTwoPlayers() {
        createSimpleRoundWithPlayers(2);
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        Game g = r.getNextUnfinishedGame();
        assertFalse(g.isFinished());
        assertTrue(playerIsInGame(p1, g));
        assertTrue(playerIsInGame(p2, g));

        g.enterResult(3, 2);
        assertEquals(null, r.getNextUnfinishedGame());
        assertTrue(g.isFinished());
        assertTrue(r.isFinished());
    }

    @Test
    public void canNotPlayTwoRoundsWithTwoPlayers() {
        createSimpleRoundWithPlayers(2);
        Game g = r.getNextUnfinishedGame();
        g.enterResult(3, 2);

        List<Round> oldRounds = new ArrayList();
        oldRounds.add(r);

        thrown.expect(IllegalStateException.class);
        r = new Round(players, oldRounds);
        assertEquals(null, r.getNextUnfinishedGame());
    }

}