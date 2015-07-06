package com.swisstournament.sh4ke.swisstournament.Core;

import com.swisstournament.sh4ke.swisstournament.Core.Player.Player;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by sh4ke on 17.06.15.
 */
public class Round {

    private static final int WIN_SETS = 3;
    private List<Player> players;
    private List<Round> finishedRounds;
    private List<Game> games;
    private boolean started;
    private boolean finished;

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
        Player opponent = choosePlayerWithSameOrNearlySameWins(possiblePlayers);
        if (opponent == null) {
            throw new IllegalStateException("Can't find opponent for player " + p);
        }
        return new Game(p, opponent);
    }

    private List<Player> getAllFormerOpponents(Player p) throws NoSuchElementException {
        List<Player> opponents = new ArrayList();

        for (Round r : finishedRounds) {
            Player opponent = r.getOpponent(p);
            opponents.add(opponent);
        }
        return opponents;
    }

    private Player choosePlayerWithSameOrNearlySameWins(List<Player> players) {
        players = sortPlayersBasedOnWins(players);
        if (players.size() > 0) {
            return players.get(0);
        }
        return null;
    }

    private List<Player> sortPlayersBasedOnWins(List<Player> players){
        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                int winsP1 = getPlayerWins(p1);
                int winsP2 = getPlayerWins(p2);
                if (winsP1 == winsP2)
                    return 0;
                return winsP1 >= winsP2 ? -1 : 1;
            }
        });
        return players;
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

    public Game getGameWithPlayer(Player p) {
        if(games != null){
            for(Game g: games){
                if(g.getP1().equals(p) || g.getP2().equals(p)){
                    return g;
                }
            }
        }
        return null;
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

    public boolean endRound(){
        if(canBeFinished()){
            finished = true;
            return true;
        }
        return false;
    }
    
    public boolean isFinished(){
        return finished;
    }

    public boolean canBeFinished() {
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

    public List<Game> getAllFinishedGames() {
        List<Game> finishedGames = new ArrayList();
        for (Game game : games) {
            if (game.isFinished()) {
                finishedGames.add(game);
            }
        }
        return finishedGames;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerWins(Player p){
        int wins = 0;

        // current round
        Game g = getGameWithPlayer(p);
        if (checkIfWinner(g, p)){
            wins++;
        }

        // finished rounds
        for(Round r : finishedRounds){
            g = r.getGameWithPlayer(p);
            if (checkIfWinner(g, p)){
                wins++;
            }
        }

        return wins;
    }

    public int getWonSets(Player p){
        int sets = 0;

        // current round
        Game g = getGameWithPlayer(p);
        if (checkIfWinner(g, p)){
            sets += WIN_SETS;
        } else {
            sets += g.getLosersWonSets();
        }

        // finished rounds
        for(Round r : finishedRounds){
            g = r.getGameWithPlayer(p);
            if (checkIfWinner(g, p)){
                sets += 3;
            } else {
                sets += g.getLosersWonSets();
            }
        }

        return sets;
    }

    public int getWithdrawnSets(Player p){
        int sets = 0;

        // current round
        Game g = getGameWithPlayer(p);
        if (checkIfWinner(g, p)){
            sets += WIN_SETS - g.getLosersWonSets();
        } else {
            sets += g.getLosersWonSets() - WIN_SETS;
        }

        // finished rounds
        for(Round r : finishedRounds){
            g = r.getGameWithPlayer(p);
            if (checkIfWinner(g, p)){
                sets += WIN_SETS - g.getLosersWonSets();
            } else {
                sets += g.getLosersWonSets() - WIN_SETS;
            }
        }

        return sets;
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

    private boolean checkIfWinner(Game g, Player p){
        if(g != null && g.isFinished()){
            if(g.getWinner().equals(p)){
                return true;
            }
        }
        return false;
    }
}
