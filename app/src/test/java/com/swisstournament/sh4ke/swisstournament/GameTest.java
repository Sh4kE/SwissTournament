package com.swisstournament.sh4ke.swisstournament;

import com.swisstournament.sh4ke.swisstournament.Core.Game;
import com.swisstournament.sh4ke.swisstournament.Core.Player;
import com.swisstournament.sh4ke.swisstournament.Core.Round;

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
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GameTest {
    private Player p1, p2;
    private Game g;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        p1 = new Player("p1");
        p2 = new Player("p2");
        g = new Game(p1, p2);
    }

    @Test
    public void canNotEnterNegativeResultsTest(){
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be negative!");

        g.enterResult(-1, 2);
    }

    @Test
    public void canNotEnterResultsGreaterThanThreeTest(){
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be greater than 3!");

        g.enterResult(4, 2);
    }

    @Test
    public void canNotEnterSameResultsTwiceTest(){
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be equal to each other!");

        g.enterResult(2, 2);
    }

    @Test
    public void getCorrectWinner1Test(){
        g.enterResult(3, 2);
        try {
            assertEquals(p1, g.getWinner());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectWinner2Test(){
        g.enterResult(2, 3);
        try {
            assertEquals(p2, g.getWinner());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLoser1Test(){
        g.enterResult(2, 3);
        try {
            assertEquals(p1, g.getLoser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLoser2Test(){
        g.enterResult(3, 2);
        try {
            assertEquals(p2, g.getLoser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLosersWonGames1Test(){
        g.enterResult(2, 3);
        try {
            assertEquals(2, g.getLosersWonGames());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLosersWonGames2Test(){
        g.enterResult(3, 2);
        try {
            assertEquals(2, g.getLosersWonGames());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canNotGetWinnerIfUnfinishedTest(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getWinner();
    }

    @Test
    public void canNotGetLoserIfUnfinishedTest(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getLoser();
    }

    @Test
    public void canNotGetLosersWonGamesIfUnfinishedTest(){
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getLosersWonGames();
    }
}