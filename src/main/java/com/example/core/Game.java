package com.example.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.model.Category;
import com.example.model.GameEvent;
import com.example.model.Player;
import com.example.model.Question;
import com.example.observer.IGameObserver;
import com.example.state.GameOffState;
import com.example.state.IGameState;
import com.example.utilities.ProcessLogger;

public class Game {

    private Map<String, Category> board = new LinkedHashMap<>();
    private final List<Player> players = new ArrayList<>();
    private final List<IGameObserver> observers = new ArrayList<>();
    private final ProcessLogger logger;

    private IGameState state = new GameOffState();
    private int currentPlayerIndex = 0;
    private Question currentQuestion;
    private boolean ended = false;

    public Game(ProcessLogger logger) {
        this.logger = logger;
    }

    /* ================= BASIC GETTERS ================= */

    public ProcessLogger getLogger() {
        return logger;
    }

    public void setBoard(Map<String, Category> board) {
        this.board = board;
    }

    public Map<String, Category> getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players available");
        }
        return players.get(currentPlayerIndex);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public boolean isEnded() {
        return ended;
    }

    /* ================= SETUP ================= */

    public void addPlayer(Player player) {
        players.add(player);
        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Enter Player Name",
                Instant.now(),
                "",
                null,
                player.getId(),
                "N/A",
                player.getScore()
        ));
    }

    public void attachObserver(IGameObserver observer) {
        observers.add(observer);
    }

    public void setState(IGameState state) {
        this.state = state;
    }

    /* ================= GAME FLOW ================= */

    public void startGame() {
        state.start(this); // logging handled inside state
    }

    public void endGame() {
        ended = true;
        state.end(this);
        logger.logSystem("Exit Game");
    }

    public boolean allQuestionsUsed() {
        return board.values().stream().allMatch(Category::allUsed);
    }

    public void advanceTurn() {
        if (!players.isEmpty()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /* ================= STATE-DELEGATED ACTIONS ================= */

    public void processSelection(String category, int value) {
        state.processSelection(this, category, value);
    }

    public void processAnswer(String answer) {
        state.processAnswer(this, answer);
    }

    /* ================= IMPLEMENTATION METHODS ================= */

    public void processSelectionImpl(String categoryName, int value) {
        Category category = board.get(categoryName);
        Player player = getCurrentPlayer();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Category",
                Instant.now(),
                categoryName,
                null,
                "",
                "N/A",
                player.getScore()
        ));

        if (category == null) {
            currentQuestion = null;
            return;
        }

        Optional<Question> found = category.findByValue(value);
        if (found.isEmpty()) {
            currentQuestion = null;
            return;
        }

        currentQuestion = found.get();
        currentQuestion.markUsed();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Question",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
        ));

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Question Selected",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
        ));
    }

    public void processAnswerImpl(String answer) {
        if (currentQuestion == null) {
            return;
        }

        Player player = getCurrentPlayer();
        boolean correct = currentQuestion.checkAnswer(answer);
        int delta = correct ? currentQuestion.getValue() : -currentQuestion.getValue();
        player.addScore(delta);

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
        ));

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
        ));

        currentQuestion = null;
        advanceTurn();
    }

    /* ================= OBSERVER ================= */

    private void notifyObservers(GameEvent event) {
        observers.forEach(o -> o.update(event));
    }
}


/**package com.example.core;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.model.Category;
import com.example.model.GameEvent;
import com.example.model.Player;
import com.example.model.Question;
import com.example.observer.IGameObserver;
import com.example.state.GameOffState;
import com.example.state.IGameState;
import com.example.utilities.ProcessLogger;

public class Game{

    private Map<String, Category> board = new LinkedHashMap<>();
    private final List<Player> players = new ArrayList<>();
    private final List<IGameObserver> observers = new ArrayList<>();
    private final ProcessLogger logger;

    private IGameState state = new GameOffState();
    private int currentPlayerIndex = 0;
    private Question currentQuestion;
    private boolean ended = false;

    public Game(ProcessLogger logger){
        this.logger = logger;
    }

    public ProcessLogger getLogger(){
        return logger;
    }

    public void setBoard(Map<String, Category> board){
        this.board = board;
    }

    public Map<String, Category> getBoard(){
        return board;
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer(){
        
        if(players.isEmpty()){
            throw new IllegalStateException("No players available");
        }

        return players.get(currentPlayerIndex);

    }

    public Question getCurrentQuestion(){
        return currentQuestion;
    }

    public boolean isEnded(){
        return ended;
    }

    public void addPlayer(Player player){
        
        players.add(player);
        
        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Enter Player Name",
                Instant.now(),
                "",
                null,
                player.getId(),
                "N/A",
                player.getScore()
        )
        );
    }

    public void attachObserver(IGameObserver observer){
        observers.add(observer);
    }

    public void setState(IGameState state){
        this.state = state;
    }

    public void startGame(){
        state.start(this); 
    }

    public void endGame(){
        ended = true;
        state.end(this);
        logger.logSystem("Exit Game");
    }

    public boolean allQuestionsUsed(){
        return board.values().stream().allMatch(Category::allUsed);
    }

    public void advanceTurn(){ 
        if (!players.isEmpty()){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    public void processSelection(String category, int value){
        state.processSelection(this, category, value);
    }

    public void processAnswer(String answer){
        state.processAnswer(this, answer);
    }

    public void processSelectionImpl(String categoryName, int value){
        Category category = board.get(categoryName);
        Player player = getCurrentPlayer();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Category",
                Instant.now(),
                categoryName,
                null,
                "",
                "N/A",
                player.getScore()
        ));

        if(category == null){
            currentQuestion = null;
            return;
        }

        Optional<Question> found = category.findByValue(value);
        if(found.isEmpty()){
            currentQuestion = null;
            return;
        }

        currentQuestion = found.get();
        currentQuestion.markUsed();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Question",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
        ));

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Question Selected",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
        ));
    }

    public void processAnswerImpl(String answer){
        if(currentQuestion == null){
            return;
        }

        Player player = getCurrentPlayer();
        boolean correct = currentQuestion.checkAnswer(answer);
        int delta = correct ? currentQuestion.getValue() : -currentQuestion.getValue();
        player.addScore(delta);

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
        ));

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
        ));

        currentQuestion = null;
        advanceTurn();
    }

    private void notifyObservers(GameEvent event){
        observers.forEach(o -> o.update(event));
    }
}
**/

/**package com.example.core;

import com.example.model.Category;
import com.example.model.GameEvent;
import com.example.model.Player;
import com.example.model.Question;
import com.example.observer.IGameObserver;
import com.example.state.GameOffState;
import com.example.state.IGameState;
import com.example.utilities.ProcessLogger;

import java.time.Instant;
import java.util.*;

public class Game {

    private Map<String, Category> board = new LinkedHashMap<>();
    private final List<Player> players = new ArrayList<>();
    private final List<IGameObserver> observers = new ArrayList<>();
    private final ProcessLogger logger;

    private IGameState state = new GameOffState();
    private int currentPlayerIndex = 0;
    private Question currentQuestion;
    private boolean ended = false;

    public Game(ProcessLogger logger) {
        this.logger = logger;
    }

    //basic getters and setters
    public ProcessLogger getLogger() {
        return logger;
    }

    public void setBoard(Map<String, Category> board) {
        this.board = board;
    }

    public Map<String, Category> getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players available");
        }
        return players.get(currentPlayerIndex);
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public boolean isEnded() {
        return ended;
    }

    //setup methods

    public void addPlayer(Player player) {
        players.add(player);
        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Enter Player Name",
                Instant.now(),
                "",
                null,
                player.getId(),
                "N/A",
                player.getScore()
            )
        );
    }

    public void attachObserver(IGameObserver observer) {
        observers.add(observer);
    }

    public void setState(IGameState state) {
        this.state = state;
    }

    //the flow of the game
    public void startGame() {
        state.start(this); 
    }

    public void endGame() {
        ended = true;
        state.end(this);
        logger.logSystem("Exit Game");
    }

    public boolean allQuestionsUsed() {
        return board.values().stream().allMatch(Category::allUsed);
    }

    public void advanceTurn() {
        if (!players.isEmpty()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    //state delegation methods

    public void processSelection(String category, int value) {
        state.processSelection(this, category, value);
    }

    public void processAnswer(String answer) {
        state.processAnswer(this, answer);
    }

    // state implementation methods

    public void processSelectionImpl(String categoryName, int value) {
        Category category = board.get(categoryName);
        Player player = getCurrentPlayer();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Category",
                Instant.now(),
                categoryName,
                null,
                "",
                "N/A",
                player.getScore()
            )
        );

        if (category == null) {
            currentQuestion = null;
            return;
        }

        Optional<Question> found = category.findByValue(value);
        if (found.isEmpty()) {
            currentQuestion = null;
            return;
        }

        currentQuestion = found.get();
        currentQuestion.markUsed();

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Select Question",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
            )
        );

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Question Selected",
                Instant.now(),
                categoryName,
                value,
                "",
                "N/A",
                player.getScore()
            )
        );
    }

    public void processAnswerImpl(String answer) {
        if (currentQuestion == null) {
            return;
        }

        Player player = getCurrentPlayer();
        boolean correct = currentQuestion.checkAnswer(answer);
        int delta = correct ? currentQuestion.getValue() : -currentQuestion.getValue();
        player.addScore(delta);

        logger.log(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
            )
        );

        notifyObservers(new GameEvent(
                logger.getCaseId(),
                player.getId(),
                "Answer Question",
                Instant.now(),
                currentQuestion.getCategory(),
                currentQuestion.getValue(),
                answer,
                correct ? "Correct" : "Incorrect",
                player.getScore()
            )
        );

        currentQuestion = null;
        advanceTurn();
    }

    // notify observers of game events
    private void notifyObservers(GameEvent event) {
        observers.forEach(o -> o.update(event));
    }

}
**/