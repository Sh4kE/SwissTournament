package com.swisstournament.sh4ke.swisstournament;

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
    public void setup() throws Exception {
        players = new ArrayList();
        for (int i = 0; i < 16; i++) {
            players.add(new Player("p" + (i + 1)));
        }
        r = new Round(players);

        assertTrue(players.size() == 16);
    }

    @Test
    public void test(){

    }

}