import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class TicTacToeApplication extends Application {
    final int size = 3;
    int playerPoints;
    int computerPoints;
    int rng;
    int round;
    boolean playerTurn = true;
    boolean computerTurn;
    Random random = new Random();

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();
        Pane pane3 = new Pane();
        pane1.setMaxSize(200, 200);
        pane2.setMaxSize(200, 200);
        pane3.setMaxSize(200, 200);
        grid.setPrefSize(600, 600);
        grid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Button button[][] = new Button[3][3];
        Button buttonNewGame = new Button("Start new game");
        Button buttonResetPoints = new Button("Reset points");

        buttonResetPoints.setFont(Font.font(14));
        buttonResetPoints.setPrefSize(100, 50);
        buttonResetPoints.setMaxSize(100, 50);
        buttonResetPoints.setPadding(new Insets(5,10,5,14));

        buttonNewGame.setPrefSize(120, 50);
        buttonNewGame.setMaxSize(120, 50);
        buttonNewGame.setPadding(new Insets(5, 10, 5, 7));
        buttonNewGame.setFont(Font.font(14));
        buttonNewGame.setAlignment(Pos.CENTER_RIGHT);
        buttonNewGame.setOnMouseClicked(event -> {
            try {
                newGame(button);
            } catch (Exception e) {
                System.out.println("New game button error");
            }
        });

        Label labelXPoints = new Label("X points = " + playerPoints);
        Label labelOPoints = new Label("O points = " + computerPoints);
        Label labelWhoWins = new Label();
        labelXPoints.setFont(Font.font(20));
        labelOPoints.setFont(Font.font(20));
        labelWhoWins.setFont(Font.font(30));

        buttonResetPoints.setOnMouseClicked(event -> {
            resetPoints();
            labelOPoints.setText("O points = " + computerPoints);
            labelXPoints.setText("X points = " + playerPoints);
        });

        pane1.getChildren().addAll(labelWhoWins);
        pane2.getChildren().addAll(buttonNewGame, buttonResetPoints);
        pane3.getChildren().addAll(labelXPoints, labelOPoints);

        grid.add(pane1, 3, 0);
        grid.add(pane2, 3, 1);
        grid.add(pane3, 3, 2);

        labelWhoWins.setLayoutY(50);
        labelWhoWins.setLayoutX(5);
        buttonNewGame.setLayoutX(40);
        buttonNewGame.setLayoutY(30);
        buttonResetPoints.setLayoutX(49);
        buttonResetPoints.setLayoutY(85);
        labelOPoints.setLayoutX(42);
        labelOPoints.setLayoutY(45);
        labelXPoints.setLayoutX(45);
        labelXPoints.setLayoutY(15);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                button[i][j] = new Button("");
                grid.add(button[i][j], i, j);
                button[i][j].setPrefSize(200, 200);
                button[i][j].setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                button[i][j].setFont(Font.font(72));
                int x = i;
                int y = j;
                button[i][j].setOnMousePressed(event -> {
                    if (button[x][y].getText().equals("") && playerTurn) {
                        button[x][y].setText("X");
                        if(button[x][y].getText().equals("X")) {
                            button[x][y].setTextFill(Color.BLUE);
                        }
                        try {
                            if (checkWinX(button)) {
                                labelWhoWins.setText("      X won\n the last round");
                                playerPoints++;
                                labelXPoints.setText("X points = " + playerPoints);
                                newGame(button);
                            } else if(!checkWinX(button) && !checkWinO(button) && round == 9) {
                                labelWhoWins.setText("   Its draw");
                                newGame(button);
                            }
                        } catch (Exception e) {
                            System.out.println("Check win X error");
                        }
                        computerTurn = true;
                        rng = random.nextInt(3);
                        if (button[x][y].getText().equals("X") && computerTurn) {
                            round++;
                            playerTurn = false;
                            try {
                                if (checkComputerFirstTwoMoves(button) && !isComputerAvailableWin(button) && !isPlayerAvailableWin(button) && computerTurn) {
                                    computerFirstTwoMoves(button);
                                    paintO(button);
                                    computerTurn = false;
                                } else if (isComputerAvailableWin(button) && computerTurn) {
                                    computerWinMove(button);
                                    paintO(button);
                                    computerTurn = false;
                                } else if (isPlayerAvailableWin(button) && !isComputerAvailableWin(button) && computerTurn) {
                                    computerBlock(button);
                                    paintO(button);
                                    computerTurn = false;
                                } else if (!isComputerAvailableWin(button) && !isPlayerAvailableWin(button) && !checkComputerFirstTwoMoves(button) && computerTurn) {
                                    computerFillO(button);
                                    paintO(button);
                                    computerTurn = false;
                                }
                                button[x][y].setOnMouseReleased(event1 -> {
                                    try {
                                        if (checkWinO(button)) {
                                            labelWhoWins.setText("      O won\n the last round");
                                            computerPoints++;
                                            labelOPoints.setText("O points = " + computerPoints);
                                            newGame(button);
                                        }  else if(!checkWinX(button) && !checkWinO(button) && round == 9) {
                                            labelWhoWins.setText("     Its draw");
                                            newGame(button);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("New game error");
                                    }
                                });
                            } catch (Exception e) {
                                System.out.println("Computer input O error");
                            }
                        }
                    }
                });
            }
        }

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void computerFillO(Button button[][]) throws Exception {
        if (!playerTurn) {
            if (button[0][0].getText().equals("")) {
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][0].getText().equals("")) {
                button[1][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("")) {
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][1].getText().equals("")) {
                button[0][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][1].getText().equals("")) {
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][1].getText().equals("")) {
                button[2][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("")) {
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][2].getText().equals("")) {
                button[1][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("")) {
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            }
        }
    }

    public void computerFirstTwoMoves(Button button[][]) throws Exception {
        if (!playerTurn) {
            if (rng == 0) {
                if (button[0][0].getText().equals("")) {
                    button[0][0].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[0][2].getText().equals("")) {
                    button[0][2].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[0][1].getText().equals("")) {
                    button[0][1].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[2][1].getText().equals("")) {
                    button[2][1].setText("O");
                    round++;
                    playerTurn = true;
                }
            } else if (rng == 1) {
                if (button[1][1].getText().equals("")) {
                    button[1][1].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[2][2].getText().equals("")) {
                    button[2][2].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[0][0].getText().equals("")) {
                    button[0][0].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[0][1].getText().equals("")) {
                    button[0][1].setText("O");
                    round++;
                    playerTurn = true;
                }
            } else if (rng == 2) {
                if (button[2][2].getText().equals("")) {
                    button[2][2].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[1][1].getText().equals("")) {
                    button[1][1].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[2][0].getText().equals("")) {
                    button[2][0].setText("O");
                    round++;
                    playerTurn = true;
                } else if (button[2][1].getText().equals("")) {
                    button[2][1].setText("O");
                    round++;
                    playerTurn = true;
                }
            }
        }
    }

    void computerWinMove(Button button[][]) throws Exception {
        if (playerTurn == false) {
            if (button[0][0].getText().equals("O") && button[1][0].getText().equals("O") && button[2][0].getText().equals("")) { //row
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("O") && button[2][0].getText().equals("O") && button[1][0].getText().equals("")) { //row
                button[1][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("O") && button[1][0].getText().equals("O") && button[0][0].getText().equals("")) { //row
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][1].getText().equals("O") && button[1][1].getText().equals("O") && button[2][1].getText().equals("")) { //row
                button[2][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][1].getText().equals("O") && button[2][1].getText().equals("O") && button[1][1].getText().equals("")) { //row
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][1].getText().equals("O") && button[1][1].getText().equals("O") && button[0][1].getText().equals("")) { //row
                button[0][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("O") && button[1][2].getText().equals("O") && button[2][2].getText().equals("")) { //row
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("O") && button[2][2].getText().equals("O") && button[1][2].getText().equals("")) { //row
                button[1][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("O") && button[1][2].getText().equals("O") && button[0][2].getText().equals("")) { //row
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("O") && button[0][1].getText().equals("O") && button[0][2].getText().equals("")) { //column
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("O") && button[0][2].getText().equals("O") && button[0][1].getText().equals("")) { //column
                button[0][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("O") && button[0][1].getText().equals("O") && button[0][0].getText().equals("")) { //column
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][0].getText().equals("O") && button[1][1].getText().equals("O") && button[1][2].getText().equals("")) { //column
                button[1][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][0].getText().equals("O") && button[1][2].getText().equals("O") && button[1][1].getText().equals("")) { //column
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][2].getText().equals("O") && button[1][1].getText().equals("O") && button[1][0].getText().equals("")) { //column
                button[1][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("O") && button[2][1].getText().equals("O") && button[2][2].getText().equals("")) { //column
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("O") && button[2][2].getText().equals("O") && button[2][1].getText().equals("")) { //column
                button[2][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("O") && button[2][1].getText().equals("O") && button[2][0].getText().equals("")) { //column
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("O") && button[2][2].getText().equals("O") && button[1][1].getText().equals("") ||
                    button[2][0].getText().equals("O") && button[0][2].getText().equals("O") && button[1][1].getText().equals("")) { //diagonal
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("O") && button[1][1].getText().equals("O") && button[2][0].getText().equals("")) { //diagonal
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("O") && button[1][1].getText().equals("O") && button[0][2].getText().equals("")) { //diagonal
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("O") && button[1][1].getText().equals("O") && button[2][2].getText().equals("")) { //diagonal
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("O") && button[1][1].getText().equals("O") && button[0][0].getText().equals("")) { //diagonal
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            }
        }
    }

    public void computerBlock(Button button[][]) {
        if (!playerTurn) {
            if (button[0][0].getText().equals("X") && button[1][0].getText().equals("X") && button[2][0].getText().equals("")) { //row
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("X") && button[2][0].getText().equals("X") && button[1][0].getText().equals("")) { //row
                button[1][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("X") && button[1][0].getText().equals("X") && button[0][0].getText().equals("")) { //row
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][1].getText().equals("X") && button[1][1].getText().equals("X") && button[2][1].getText().equals("")) { //row
                button[2][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][1].getText().equals("X") && button[2][1].getText().equals("X") && button[1][1].getText().equals("")) { //row
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][1].getText().equals("X") && button[1][1].getText().equals("X") && button[0][1].getText().equals("")) { //row
                button[0][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("X") && button[1][2].getText().equals("X") && button[2][2].getText().equals("")) { //row
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("X") && button[2][2].getText().equals("X") && button[1][2].getText().equals("")) { //row
                button[1][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("X") && button[1][2].getText().equals("X") && button[0][2].getText().equals("")) { //row
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("X") && button[0][1].getText().equals("X") && button[0][2].getText().equals("")) { //column
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("X") && button[0][2].getText().equals("X") && button[0][1].getText().equals("")) { //column
                button[0][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("X") && button[0][1].getText().equals("X") && button[0][0].getText().equals("")) { //column
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][0].getText().equals("X") && button[1][1].getText().equals("X") && button[1][2].getText().equals("")) { //column
                button[1][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][0].getText().equals("X") && button[1][2].getText().equals("X") && button[1][1].getText().equals("")) { //column
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[1][2].getText().equals("X") && button[1][1].getText().equals("X") && button[1][0].getText().equals("")) { //column
                button[1][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("X") && button[2][1].getText().equals("X") && button[2][2].getText().equals("")) { //column
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("X") && button[2][2].getText().equals("X") && button[2][1].getText().equals("")) { //column
                button[2][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("X") && button[2][1].getText().equals("X") && button[2][0].getText().equals("")) { //column
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("X") && button[2][2].getText().equals("X") && button[1][1].getText().equals("") ||
                    button[2][0].getText().equals("X") && button[0][2].getText().equals("X") && button[1][1].getText().equals("")) { //diagonal
                button[1][1].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][2].getText().equals("X") && button[1][1].getText().equals("X") && button[2][0].getText().equals("")) { //diagonal
                button[2][0].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][0].getText().equals("X") && button[1][1].getText().equals("X") && button[0][2].getText().equals("")) { //diagonal
                button[0][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[0][0].getText().equals("X") && button[1][1].getText().equals("X") && button[2][2].getText().equals("")) { //diagonal
                button[2][2].setText("O");
                round++;
                playerTurn = true;
            } else if (button[2][2].getText().equals("X") && button[1][1].getText().equals("X") && button[0][0].getText().equals("")) { //diagonal
                button[0][0].setText("O");
                round++;
                playerTurn = true;
            }
        }
    }

    public boolean isPlayerAvailableWin(Button button[][]) {
        if (button[0][0].getText().equals("X") && button[1][0].getText().equals("X") && button[2][0].getText().equals("") ||
                button[0][0].getText().equals("X") && button[2][0].getText().equals("X") && button[1][0].getText().equals("") ||
                button[2][0].getText().equals("X") && button[1][0].getText().equals("X") && button[0][0].getText().equals("") ||
                button[0][1].getText().equals("X") && button[1][1].getText().equals("X") && button[2][1].getText().equals("") ||
                button[0][1].getText().equals("X") && button[2][1].getText().equals("X") && button[1][1].getText().equals("") ||
                button[2][1].getText().equals("X") && button[1][1].getText().equals("X") && button[0][1].getText().equals("") ||
                button[0][2].getText().equals("X") && button[1][2].getText().equals("X") && button[2][2].getText().equals("") ||
                button[0][2].getText().equals("X") && button[2][2].getText().equals("X") && button[1][2].getText().equals("") ||
                button[2][2].getText().equals("X") && button[1][2].getText().equals("X") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("X") && button[0][1].getText().equals("X") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("X") && button[0][2].getText().equals("X") && button[0][1].getText().equals("") ||
                button[0][2].getText().equals("X") && button[0][1].getText().equals("X") && button[0][0].getText().equals("") ||
                button[1][0].getText().equals("X") && button[1][1].getText().equals("X") && button[1][2].getText().equals("") ||
                button[1][0].getText().equals("X") && button[1][2].getText().equals("X") && button[1][1].getText().equals("") ||
                button[1][2].getText().equals("X") && button[1][1].getText().equals("X") && button[1][0].getText().equals("") ||
                button[2][0].getText().equals("X") && button[2][1].getText().equals("X") && button[2][2].getText().equals("") ||
                button[2][0].getText().equals("X") && button[2][2].getText().equals("X") && button[2][1].getText().equals("") ||
                button[2][2].getText().equals("X") && button[2][1].getText().equals("X") && button[2][0].getText().equals("") ||
                button[0][0].getText().equals("X") && button[2][2].getText().equals("X") && button[1][1].getText().equals("") ||
                button[2][0].getText().equals("X") && button[0][2].getText().equals("X") && button[1][1].getText().equals("") ||
                button[0][2].getText().equals("X") && button[1][1].getText().equals("X") && button[2][0].getText().equals("") ||
                button[2][0].getText().equals("X") && button[1][1].getText().equals("X") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("X") && button[1][1].getText().equals("X") && button[2][2].getText().equals("") ||
                button[2][2].getText().equals("X") && button[1][1].getText().equals("X") && button[0][0].getText().equals("")) {
            return true;
        }
        return false;
    }

    public boolean isComputerAvailableWin(Button button[][]) {
        if (button[0][0].getText().equals("O") && button[1][0].getText().equals("O") && button[2][0].getText().equals("") ||
                button[0][0].getText().equals("O") && button[2][0].getText().equals("O") && button[1][0].getText().equals("") ||
                button[2][0].getText().equals("O") && button[1][0].getText().equals("O") && button[0][0].getText().equals("") ||
                button[0][1].getText().equals("O") && button[1][1].getText().equals("O") && button[2][1].getText().equals("") ||
                button[0][1].getText().equals("O") && button[2][1].getText().equals("O") && button[1][1].getText().equals("") ||
                button[2][1].getText().equals("O") && button[1][1].getText().equals("O") && button[0][1].getText().equals("") ||
                button[0][2].getText().equals("O") && button[1][2].getText().equals("O") && button[2][2].getText().equals("") ||
                button[0][2].getText().equals("O") && button[2][2].getText().equals("O") && button[1][2].getText().equals("") ||
                button[2][2].getText().equals("O") && button[1][2].getText().equals("O") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("O") && button[0][1].getText().equals("O") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("O") && button[0][2].getText().equals("O") && button[0][1].getText().equals("") ||
                button[0][2].getText().equals("O") && button[0][1].getText().equals("O") && button[0][0].getText().equals("") ||
                button[1][0].getText().equals("O") && button[1][1].getText().equals("O") && button[1][2].getText().equals("") ||
                button[1][0].getText().equals("O") && button[1][2].getText().equals("O") && button[1][1].getText().equals("") ||
                button[1][2].getText().equals("O") && button[1][1].getText().equals("O") && button[1][0].getText().equals("") ||
                button[2][0].getText().equals("O") && button[2][1].getText().equals("O") && button[2][2].getText().equals("") ||
                button[2][0].getText().equals("O") && button[2][2].getText().equals("O") && button[2][1].getText().equals("") ||
                button[2][2].getText().equals("O") && button[2][1].getText().equals("O") && button[2][0].getText().equals("") ||
                button[0][0].getText().equals("O") && button[2][2].getText().equals("O") && button[1][1].getText().equals("") ||
                button[2][0].getText().equals("O") && button[0][2].getText().equals("O") && button[1][1].getText().equals("") ||
                button[0][2].getText().equals("O") && button[1][1].getText().equals("O") && button[2][0].getText().equals("") ||
                button[2][0].getText().equals("O") && button[1][1].getText().equals("O") && button[0][2].getText().equals("") ||
                button[0][0].getText().equals("O") && button[1][1].getText().equals("O") && button[2][2].getText().equals("") ||
                button[2][2].getText().equals("O") && button[1][1].getText().equals("O") && button[0][0].getText().equals("")) {
            return true;
        }
        return false;
    }

    public void newGame(Button button[][]) throws Exception {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                round = 0;
                button[i][j].setText("");
                playerTurn = true;
                computerTurn = false;
            }
        }
    }

    public void resetPoints() {
        computerPoints = 0;
        playerPoints = 0;
    }

    public boolean checkComputerFirstTwoMoves(Button button[][]) throws Exception {
        if (rng == 0) {
            if (button[0][0].getText().equals("") ||
                    button[0][2].getText().equals("") ||
                    button[0][1].getText().equals("") ||
                    button[2][1].getText().equals("")) {
                return true;
            }
        } else if (rng == 1) {
            if (button[1][1].getText().equals("") ||
                    button[2][2].getText().equals("") ||
                    button[0][0].getText().equals("") ||
                    button[0][1].getText().equals("")) {
                return true;
            }
        } else if (rng == 2) {
            if (button[2][2].getText().equals("") ||
                    button[1][1].getText().equals("") ||
                    button[2][0].getText().equals("") ||
                    button[2][1].getText().equals("")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkWinX(Button button[][]) throws Exception {
        if (button[0][0].getText().equals("X") && button[1][0].getText().equals("X") && button[2][0].getText().equals("X") ||
                button[0][1].getText().equals("X") && button[1][1].getText().equals("X") && button[2][1].getText().equals("X") ||
                button[0][2].getText().equals("X") && button[1][2].getText().equals("X") && button[2][2].getText().equals("X") ||
                button[2][0].getText().equals("X") && button[2][1].getText().equals("X") && button[2][2].getText().equals("X") ||
                button[0][0].getText().equals("X") && button[0][1].getText().equals("X") && button[0][2].getText().equals("X") ||
                button[1][0].getText().equals("X") && button[1][1].getText().equals("X") && button[1][2].getText().equals("X") ||
                button[0][0].getText().equals("X") && button[1][1].getText().equals("X") && button[2][2].getText().equals("X") ||
                button[2][0].getText().equals("X") && button[1][1].getText().equals("X") && button[0][2].getText().equals("X")) {
            return true;
        }
        return false;
    }

    public boolean checkWinO(Button button[][]) throws Exception {
        if (button[0][0].getText().equals("O") && button[1][0].getText().equals("O") && button[2][0].getText().equals("O") ||
                button[0][1].getText().equals("O") && button[1][1].getText().equals("O") && button[2][1].getText().equals("O") ||
                button[0][2].getText().equals("O") && button[1][2].getText().equals("O") && button[2][2].getText().equals("O") ||
                button[2][0].getText().equals("O") && button[2][1].getText().equals("O") && button[2][2].getText().equals("O") ||
                button[0][0].getText().equals("O") && button[0][1].getText().equals("O") && button[0][2].getText().equals("O") ||
                button[1][0].getText().equals("O") && button[1][1].getText().equals("O") && button[1][2].getText().equals("O") ||
                button[0][0].getText().equals("O") && button[1][1].getText().equals("O") && button[2][2].getText().equals("O") ||
                button[2][0].getText().equals("O") && button[1][1].getText().equals("O") && button[0][2].getText().equals("O")) {
            return true;
        }
        return false;
    }

    public void paintO(Button button[][]) throws Exception {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(button[i][j].getText().equals("O")) {
                    button[i][j].setTextFill(Color.RED);
                }
            }
        }
    }

    public static void main(String args[]) {
        launch(args);
    }
}