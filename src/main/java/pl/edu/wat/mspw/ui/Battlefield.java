package pl.edu.wat.mspw.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Battlefield extends Application {
    private static final int SIZE = 10; // Rozmiar siatki
    private static final int CELL_SIZE = 30; // Wielkość pojedynczej komórki

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK); // Kolor obramowania komórki

                // Tutaj można określić, czy komórka ma zawierać jednostkę red team czy blue team, lub być pusta.
                // Np.:
                // if (warunki dla red team) {
                //     cell.setFill(Color.RED);
                // } else if (warunki dla blue team) {
                //     cell.setFill(Color.BLUE);
                // } else {
                //     cell.setFill(Color.WHITE);
                // }

                grid.add(cell, x, y);
            }
        }

        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
