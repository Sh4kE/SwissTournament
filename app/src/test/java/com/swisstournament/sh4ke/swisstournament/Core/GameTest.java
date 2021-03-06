package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.BuildConfig;
import com.swisstournament.sh4ke.swisstournament.Core.Player.HumanPlayer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.security.InvalidParameterException;

import static junit.framework.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GameTest {
    private HumanPlayer p1, p2;
    private Game g;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        p1 = new HumanPlayer("p1");
        p2 = new HumanPlayer("p2");
        g = new Game(p1, p2);
    }

    @Test
    public void canNotEnterNegativeResultsTest() {
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be negative!");

        g.enterResult(-1, 2);
    }

    @Test
    public void canNotEnterResultsGreaterThanThreeTest() {
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be greater than 3!");

        g.enterResult(4, 2);
    }

    @Test
    public void canNotEnterSameResultsTwiceTest() {
        thrown.expect(InvalidParameterException.class);
        thrown.expectMessage("results cannot be equal to each other!");

        g.enterResult(2, 2);
    }

    @Test
    public void getCorrectP1Test() {
        assertEquals(p1, g.getP1());
    }

    @Test
    public void getCorrectP2Test() {
        assertEquals(p2, g.getP2());
    }

    @Test
    public void getCorrectWinner1Test() {
        g.enterResult(3, 2);
        try {
            assertEquals(p1, g.getWinner());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectWinner2Test() {
        g.enterResult(2, 3);
        try {
            assertEquals(p2, g.getWinner());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLoser1Test() {
        g.enterResult(2, 3);
        try {
            assertEquals(p1, g.getLoser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLoser2Test() {
        g.enterResult(3, 2);
        try {
            assertEquals(p2, g.getLoser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLosersWonGames1Test() {
        g.enterResult(2, 3);
        try {
            assertEquals(2, g.getLosersWonSets());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCorrectLosersWonGames2Test() {
        g.enterResult(3, 2);
        try {
            assertEquals(2, g.getLosersWonSets());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canNotGetWinnerIfUnfinishedTest() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getWinner();
    }

    @Test
    public void canNotGetLoserIfUnfinishedTest() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getLoser();
    }

    @Test
    public void canNotGetLosersWonGamesIfUnfinishedTest() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("this game is not played yet");

        g.getLosersWonSets();
    }
}