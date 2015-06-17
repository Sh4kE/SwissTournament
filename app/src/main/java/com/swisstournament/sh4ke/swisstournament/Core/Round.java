package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by sh4ke on 17.06.15.
 */
public class Round {
    private List<Player> players;
    private List<Round> finishedRounds;
    private List<Game> games;
    private boolean started;

    private Random r;

    public Round(List<Player> players) {
        this.finishedRounds = new ArrayList<Round>();
        init(players);
    }

    public Round(List<Player> players, List<Round> finishedRounds) {
        this.finishedRounds = finishedRounds;
        init(players);
    }

    private void init(List<Player> players) {
        this.players = players;
        this.r = new Random();
        this.games = createGames();
    }

    private List<Game> createGames() {
        List<Game> games = new ArrayList<Game>();

        List<Player> playerQueue = new ArrayList<Player>();
        Collections.copy(playerQueue, players);

        while (!playerQueue.isEmpty()) {
            Player p = playerQueue.remove(0);
            games.add(createNewGame(p));
        }

        return games;
    }

    /**
     * @param p the player
     * @return a new Game with a random player, which p1 has not yet played against.
     */
    private Game createNewGame(Player p) {
        Game game;

        List<Player> possiblePlayers = new ArrayList<Player>();
        Collections.copy(possiblePlayers, players);

        possiblePlayers.removeAll(getAllFormerOpponents(p));
        Player opponent = chooseRandomPlayer(possiblePlayers);
        game = new Game(p, opponent);

        return game;
    }

    private List<Player> getAllFormerOpponents(Player p) {
        List<Player> opponents = new ArrayList<Player>();

        for (Round r : finishedRounds) {
            try {
                Player opponent = r.getOpponent(p);
                opponents.add(opponent);
            } catch (PlayerNotFoundException e) {
                e.printStackTrace();
            }
        }
        return opponents;
    }

    private Player getOpponent(Player p) throws PlayerNotFoundException {
        Player opponent;

        for (Game game : games) {
            if (game.getP1().equals(p)) {
                return game.getP2();
            }
            if (game.getP2().equals(p)) {
                return game.getP1();
            }
        }
        throw new PlayerNotFoundException(String.format("Player %s not found", p.getName()));
    }

    private Player chooseRandomPlayer(List<Player> players) {
        return players.get(r.nextInt(players.size()));
    }

    public void enterResult(Game result) {

    }

    public boolean isStarted() {
        return this.started;
    }

    public void start() {
        this.started = true;
    }

    public boolean isFinished() {
        for (Game game : games) {
            if (!game.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public Game getGame(Player p1, Player p2) throws PlayerNotFoundException {
        for (Game game : games) {
            if ((game.getP1().equals(p1) && game.getP2().equals(p2)) || (game.getP1().equals(p2) && game.getP2().equals(p1))) {
                return game;
            }
        }
        throw new PlayerNotFoundException(String.format("Could not find game with players: (%s, %s)", p1, p2));
    }
}
