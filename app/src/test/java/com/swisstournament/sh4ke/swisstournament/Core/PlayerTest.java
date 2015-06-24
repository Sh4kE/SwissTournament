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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class PlayerTest {
    private HumanPlayer p1, p2, p3;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        p1 = new HumanPlayer("p1");
        p2 = new HumanPlayer("p2");
        p3 = new HumanPlayer("p1");
    }

    @Test
    public void playersEqualWithSameNameTest() {
        assertTrue(p1.equals(p3));
    }

    @Test
    public void playersDifferWithDifferentNameTest() {
        assertFalse(p1.equals(p2));
    }

    @Test
    public void correctToStringTest() {
        assertEquals("Player 'p1'", p1.toString());
    }
}