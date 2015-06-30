package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by sh4ke on 17.06.15.
 */
public class Round {
    private List<Player> players;
    private List<Round> finishedRounds;
    private List<Game> games;
    private boolean started;

    public Round(List<Player> players) {
        this.finishedRounds = new ArrayList();
        init(players);
    }

    public Round(List<Player> players, List<Round> finishedRounds) {
        this.finishedRounds = finishedRounds;
        init(players);
    }

    private void init(List<Player> players) {
        if (players.isEmpty()) {
            throw new IllegalStateException("Cannot create new round without players!");
        }
        this.players = players;
        this.games = createGames();
    }

    private List<Game> createGames() {
        List<Game> games = new ArrayList();
        List<Player> playerQueue = new ArrayList(players);

        while (!playerQueue.isEmpty()) {
            Player p = playerQueue.remove(0);
            Game g = createGame(p, playerQueue);

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
    private Game createGame(Player p, List<Player> playerQueue) {
        List<Player> possiblePlayers = new ArrayList(playerQueue);

        possiblePlayers.removeAll(getAllFormerOpponents(p));
        Player opponent = choosePlayer(possiblePlayers);
        if (opponent == null) {
            throw new IllegalStateException("Can't find opponent for player " + p);
        }
        return new Game(p, opponent);
    }

    private List<Player> getAllFormerOpponents(Player p) throws NoSuchElementException {
        List<Player> opponents = new ArrayList();

        for (Round r : finishedRounds) {
            Player opponent = r.getOpponent(p);
            players.add(opponent);
            opponents.add(opponent);
        }
        return opponents;
    }

    public Player getOpponent(Player p) throws NoSuchElementException {
        for (Game game : games) {
            if (game.getP1().equals(p)) {
                return game.getP2();
            }
            if (game.getP2().equals(p)) {
                return game.getP1();
            }
        }
        throw new NoSuchElementException(String.format("Player %s not found", p.getName()));
    }

    private Player choosePlayer(List<Player> players) {
        if (players.size() > 0) {
            return players.get(0);
        }
        return null;
    }

    public void enterResult(Player p1, int won_p1, Player p2, int won_p2) throws InvalidParameterException {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (gameHasPlayers(game, p1, p2)) {
                game.enterResult(won_p1, won_p2);
                games.set(i, game);
                return;
            }
        }
        throw new InvalidParameterException(String.format("Could not find game with players: (%s, %s)", p1, p2));
    }

    public boolean gameHasPlayers(Game game, Player p1, Player p2) {
        return (game.getP1().equals(p1) && game.getP2().equals(p2)) || (game.getP1().equals(p2) && game.getP2().equals(p1));
    }

    public boolean canStart() {
        return players.size() >= 2;
    }

    public void start() {
        if (canStart()) {
            this.started = true;
        } else {
            throw new IllegalStateException("Cannot start round with less than 2 players!");
        }
    }

    public boolean isStarted() {
        return this.started;
    }


    public boolean isFinished() {
        for (Game game : games) {
            if (!game.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public Game getNextUnfinishedGame() {
        for (Game game : games) {
            if (!game.isFinished()) {
                return game;
            }
        }
        return null;
    }

    public List<Game> getAllUnfinishedGames() {
        List<Game> unfinishedGames = new ArrayList();
        for (Game game : games) {
            if (!game.isFinished()) {
                unfinishedGames.add(game);
            }
        }
        return unfinishedGames;
    }
}
