package pl.edu.wat.mspw.ui;

import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.mspw.enums.ConflictSide;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Battlefield extends JPanel {
    private int gridSize; // Rozmiar siatki, np. 10x10
    private Color[][] gridColors; // Kolory kwadratów na siatce
    private List<SquareUnit> squareUnits; // Lista jednostek wojskowych na mapie
    private int squareSideLength; // Długość boku jednego pola siatki w metrach

    public Battlefield(int gridSize, int squareSideLength) {
        this.gridSize = gridSize;
        this.gridColors = new Color[gridSize][gridSize];
        this.squareUnits = new ArrayList<>();
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLayout(null); // Usuwamy layout manager, aby móc umieścić przyciski
        this.squareSideLength = squareSideLength; // Inicjalizacja długości boku pola

    }

    // Metoda do dodawania jednostek wojskowych
    public void addMilitaryUnit(SquareUnit unit) {
        if (unit != null) {
            squareUnits.add(unit);
            setColorAt(unit.getX(), unit.getY(), unit.getSide() == ConflictSide.RED ? Color.RED : Color.BLUE);
            repaint();
        }
    }

    // Metoda do ustawiania koloru kwadratu
    public void setColorAt(int x, int y, Color color) {
        if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
            gridColors[x][y] = color;
            repaint(); // Przerysuj komponent po zmianie koloru
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Oblicz rozmiar kwadratu na podstawie mniejszego wymiaru okna
        int squareSize = Math.min(getWidth(), getHeight()) / gridSize;

        // Oblicz całkowity rozmiar siatki
        int totalGridSize = squareSize * gridSize;

        // Oblicz marginesy, aby wycentrować siatkę
        int marginX = (getWidth() - totalGridSize) / 2;
        int marginY = (getHeight() - totalGridSize) / 2;

        // Wypełnij tło
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Rysuj kwadraty
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridColors[i][j] != null) {
                    g.setColor(gridColors[i][j]);
                } else {
                    g.setColor(Color.WHITE); // Domyślny kolor kwadratu
                }

                int x = marginX + i * squareSize;
                int y = marginY + j * squareSize;

                g.fillRect(x, y, squareSize, squareSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, squareSize, squareSize);
            }
        }

        // Rysowanie jednostek wojskowych
        for (SquareUnit unit : squareUnits) {
            drawUnit(g, unit, squareSize, marginX, marginY);
        }
    }

    // Metoda do rysowania jednostek wojskowych
    private void drawUnit(Graphics g, SquareUnit unit, int squareSize, int marginX, int marginY) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Oblicz środek kwadratu jednostki
        int centerX = marginX + unit.getX() * squareSize + squareSize / 2;
        int centerY = marginY + unit.getY() * squareSize + squareSize / 2;

        // Oblicz promień okręgu zasięgu
        int rangeRadius = (int) ((unit.getRange() / (double) squareSideLength) * squareSize);

        // Ustaw kolor wypełnienia dla okręgu zasięgu (delikatniejszy kolor)
        Color fillColor = new Color(
                unit.getSide() == ConflictSide.RED ? Color.RED.getRGB() : Color.BLUE.getRGB(),
                true // przezroczysty
        );
        g2d.setColor(fillColor);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // ustaw niższą przezroczystość dla wypełnienia

        // Rysuj wypełniony okrąg zasięgu
        g2d.fillOval(centerX - rangeRadius, centerY - rangeRadius, 2 * rangeRadius, 2 * rangeRadius);

        // Ustaw kolor obrysu dla okręgu zasięgu (mocniejszy kolor)
        g2d.setColor(fillColor.darker());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)); // ustaw przezroczystość dla obrysu

        // Rysuj obrys okręgu zasięgu
        g2d.drawOval(centerX - rangeRadius, centerY - rangeRadius, 2 * rangeRadius, 2 * rangeRadius);

        //Rysuj jednostke
        int x = marginX + unit.getX() * squareSize;
        int y = marginY + unit.getY() * squareSize;

        g.setColor(Color.getHSBColor(0.28f, 0.54f, 0.56f));
        FontMetrics metrics = g.getFontMetrics();
        String unitText = unit.getName() + " (" + unit.getEquipmentCount() + ")";
        int textWidth = metrics.stringWidth(unitText);
        int textHeight = metrics.getHeight();

        // Rysowanie nazwy jednostki wyśrodkowanej w kwadracie
        g.drawString(unitText, x + (squareSize - textWidth) / 2, y + (squareSize - textHeight) / 2 + metrics.getAscent());
        g2d.dispose(); // Zwalnia zasoby Graphics2D

    }


    public void changeGridSize(int change) {
        int newGridSize = gridSize + change;

        // Sprawdzamy, czy nowy rozmiar siatki jest akceptowalny
        if (newGridSize > 0) {

            // Obliczamy początkowy indeks dla starej siatki, aby była ona wyśrodkowana na nowej
            int startIndexX = (newGridSize - gridSize) / 2;
            int startIndexY = (newGridSize - gridSize) / 2;

            // Przesuwamy stare kolory do nowej, wyśrodkowanej siatki
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    // Upewniamy się, że nie wychodzimy poza nowe wymiary siatki
                    int newX = i + startIndexX;
                    int newY = j + startIndexY;

                }
            }
            gridSize = newGridSize;
            revalidate();
            repaint();
        }
    }

    public void moveUnit(int unitId, int newX, int newY) {
        for (SquareUnit unit : squareUnits) {
            if (unit.getId() == unitId) {
                // Usuń kolor starej pozycji
                setColorAt(unit.getX(), unit.getY(), Color.WHITE);

                // Zaktualizuj pozycję jednostki
                unit.setX(newX);
                unit.setY(newY);

                // Ustaw kolor nowej pozycji
                setColorAt(newX, newY, unit.getSide() == ConflictSide.RED ? Color.RED : Color.BLUE);

                repaint();
                break; // Zakładamy, że ID są unikalne, więc można przerwać pętlę
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            int gridSize = 50;
            int squareSideLength = 200; // Długość boku pola siatki to 200m
            Battlefield battlefield = new Battlefield(gridSize, squareSideLength);
            // Dodanie jednostek wojskowych z zasięgiem
            battlefield.addMilitaryUnit(new SquareUnit(1, 1, 1, ConflictSide.RED, "Tank Division", 20, 500));
            battlefield.addMilitaryUnit(new SquareUnit(2, 5, 10, ConflictSide.BLUE, "Infantry Brigade", 30, 300));

            // Pobieranie rozmiaru ekranu
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;

            // Przyciski przybliżania i oddalania
            JButton zoomInButton = new JButton("+");
            JButton zoomOutButton = new JButton("-");

            int buttonWidth = 50;
            int buttonHeight = 50;
            int gap = 10; // Odstęp między przyciskami

            // Ustawienie rozmiaru i pozycji przycisków
            zoomInButton.setBounds(screenWidth - buttonWidth - gap, gap, buttonWidth, buttonHeight);
            zoomOutButton.setBounds(screenWidth - buttonWidth - gap, 2 * gap + buttonHeight, buttonWidth, buttonHeight);

            // Dodajemy akcje dla przycisków
            zoomInButton.addActionListener(e -> battlefield.changeGridSize(-1));
            zoomOutButton.addActionListener(e -> battlefield.changeGridSize(1));

            // Dodanie przycisków do panelu
            battlefield.setLayout(null); // Usuwamy layout manager
            battlefield.add(zoomInButton);
            battlefield.add(zoomOutButton);

            frame.add(battlefield);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Pełny ekran
            frame.setVisible(true);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    battlefield.moveUnit(1, 2, 1);
                    battlefield.moveUnit(2, 5, 11);
                }
            }).start();

        });
    }
}

// Wewnętrzna klasa reprezentująca jednostki wojskowe
@Getter @Setter
class SquareUnit {
    private int id; // Dodanie identyfikatora
    private int x, y; // Pozycja na siatce
    private ConflictSide side; // Strona konfliktu
    private String name; // Nazwa jednostki
    private int equipmentCount; // Ilość sprzętu
    private int range;
    public SquareUnit(int id, int x, int y, ConflictSide side, String name, int equipmentCount, int range) {
        this.id = id; // Inicjalizacja identyfikatora
        this.x = x;
        this.y = y;
        this.side = side;
        this.name = name;
        this.equipmentCount = equipmentCount;
        this.range = range;
    }
}
