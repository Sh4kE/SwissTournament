package com.swisstournament.sh4ke.swisstournament;

import com.swisstournament.sh4ke.swisstournament.Core.Game;
import com.swisstournament.sh4ke.swisstournament.Core.Player;

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
    private Player p1, p2, p3;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() throws Exception {
        p1 = new Player("p1");
        p2 = new Player("p2");
        p3 = new Player("p1");
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