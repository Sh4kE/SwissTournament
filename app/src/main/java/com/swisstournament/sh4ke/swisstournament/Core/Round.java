package com.swisstournament.sh4ke.swisstournament.Core;

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
        List<Player> playerQueue = new ArrayList<Player>(players);

        while (!playerQueue.isEmpty()) {
            Player p = playerQueue.remove(0);
            Game g = createNewGame(p, playerQueue);

            // remove the opponent from the queue as well
            playerQueue.remove(g.getP2());

            games.add(g);
        }

        return games;
    }

    /**
     * @param p the player
     * @return a new Game with a random player, which p1 has not yet played against.
     */
    private Game createNewGame(Player p, List<Player> playerQueue) {
        Game game;

        List<Player> possiblePlayers = new ArrayList<Player>(playerQueue);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return opponents;
    }

    private Player getOpponent(Player p) throws Exception {
        Player opponent;

        for (Game game : games) {
            if (game.getP1().equals(p)) {
                return game.getP2();
            }
            if (game.getP2().equals(p)) {
                return game.getP1();
            }
        }
        throw new Exception(String.format("Player %s not found", p.getName()));
    }

    private Player chooseRandomPlayer(List<Player> players) {
        return players.get(r.nextInt(players.size()));
    }

    public void enterResult(Player p1, int won_p1, Player p2, int won_p2) throws Exception {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if ((game.getP1().equals(p1) && game.getP2().equals(p2)) || (game.getP1().equals(p2) && game.getP2().equals(p1))) {
                game.enterResult(won_p1, won_p2);
                games.set(i, game);
                return;
            }
        }
        throw new Exception(String.format("Could not find game with players: (%s, %s)", p1, p2));
    }

    /*private Game getGame(Player p1, Player p2) throws Exception {
        for (Game game : games) {
            if ((game.getP1().equals(p1) && game.getP2().equals(p2)) || (game.getP1().equals(p2) && game.getP2().equals(p1))) {
                return game;
            }
        }
        throw new Exception(String.format("Could not find game with players: (%s, %s)", p1, p2));
    }*/

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
}
