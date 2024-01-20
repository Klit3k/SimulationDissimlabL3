package pl.edu.wat.mspw.ui;

import pl.edu.wat.mspw.enums.ConflictSide;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Battlefield extends JPanel {
    private final int startGridSize;
    private int gridSize; // Rozmiar siatki, np. 10x10
    private Color[][] gridColors; // Kolory kwadratów na siatce
    private List<SquareUnit> squareUnits; // Lista jednostek wojskowych na mapie
    private int squareSideLength; // Długość boku jednego pola siatki w metrach
    private Color[][] trailColors; // Kolory śladów na siatce
    private DefaultTableModel redTeamTableModel;
    private DefaultTableModel blueTeamTableModel;
    private JTable redTeamTable;
    private JTable blueTeamTable;
    JPanel buttonPanel;
    JButton zoomInButton;
    JButton zoomOutButton;

    public Battlefield(int gridSize, int squareSideLength) {
        this.gridSize = gridSize;
        this.startGridSize = gridSize;
        this.gridColors = new Color[gridSize][gridSize];
        this.squareUnits = new ArrayList<>();
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.squareSideLength = squareSideLength; // Inicjalizacja długości boku pola
        this.trailColors = new Color[gridSize][gridSize]; // Inicjalizacja siatki śladów

        setLayout(new BorderLayout());


        DefaultTableModel nonEditableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Equipment", "Power", "Range"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Zwraca false, co oznacza, że żadna komórka nie może być edytowana
                return false;
            }
        };
        DefaultTableModel nonEditableModel2 = new DefaultTableModel(new Object[]{"ID", "Name", "Equipment", "Power", "Range"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Zwraca false, co oznacza, że żadna komórka nie może być edytowana
                return false;
            }
        };
        // Inicjalizacja modeli tabeli dla obu drużyn
        redTeamTableModel = nonEditableModel2;
        blueTeamTableModel = nonEditableModel;

        // Tworzenie tabel na podstawie modeli
        redTeamTable = new JTable(redTeamTableModel);
        blueTeamTable = new JTable(blueTeamTableModel);

        TableColumnModel tableColumnRed = redTeamTable.getColumnModel();
        tableColumnRed.removeColumn(tableColumnRed.getColumn(0));

        TableColumnModel tableColumnBlue = blueTeamTable.getColumnModel();
        tableColumnBlue.removeColumn(tableColumnBlue.getColumn(0));

        // Tworzenie przewijanych paneli dla tabel
        JScrollPane redScrollPane = new JScrollPane(redTeamTable);
        JScrollPane blueScrollPane = new JScrollPane(blueTeamTable);

        // Panel boczny na tabele
        JPanel sidePanel = new JPanel(new GridLayout(2, 1)); // 2 wiersze, 1 kolumna
        sidePanel.add(redScrollPane);
        sidePanel.add(blueScrollPane);

        // Ustawienie preferowanego rozmiaru dla panelu bocznego
        sidePanel.setPreferredSize(new Dimension(300, 0)); // szerokość 200 pikseli


        // Dodanie panelu bocznego do głównego panelu
        add(sidePanel, BorderLayout.WEST);

        // Panel na przyciski
        buttonPanel = new JPanel(new GridLayout(2, 1));

        zoomInButton = new JButton("+");
        zoomOutButton = new JButton("-");

        int buttonWidth = 50;
        int buttonHeight = 50;
        int gap = 10; // Odstęp między przyciskami
        // Ustawienie rozmiaru i pozycji przycisków
        // Pobieranie rozmiaru ekranu
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;

        zoomInButton.setBounds(screenWidth - buttonWidth - gap, gap, buttonWidth, buttonHeight);
        zoomOutButton.setBounds(screenWidth - buttonWidth - gap, 2 * gap + buttonHeight, buttonWidth, buttonHeight);

        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);

        // Dodajemy akcje dla przycisków
        zoomInButton.addActionListener(e -> this.changeGridSize(-1));
        zoomOutButton.addActionListener(e -> this.changeGridSize(1));

        // Dodanie panelu bocznego i przycisków do głównego panelu
        add(sidePanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.EAST);
    }

    public void addUnitToTeam(SquareUnit unit) {
        DefaultTableModel model = unit.getSide() == ConflictSide.RED ? redTeamTableModel : blueTeamTableModel;

        model.addRow(new Object[]{unit.getId(), unit.getName(), unit.getEquipmentCount(), unit.getPower(), unit.getStartRange()});
    }

    public void changeGridSize(int change) {
        int newGridSize = gridSize + change;

        // Sprawdzamy, czy nowy rozmiar siatki jest akceptowalny
        if (newGridSize > 0 && newGridSize <= startGridSize) {

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

        // Rysuj ślady
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (trailColors[i][j] != null) {
                    int x = marginX + i * squareSize;
                    int y = marginY + j * squareSize;
                    g.setColor(trailColors[i][j]);
                    g.fillRect(x, y, squareSize, squareSize);
                }
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


        // Ustaw kolor wypełnienia dla okręgu zasięgu (delikatniejszy kolor)
        Color fillColor = new Color(
                unit.getSide() == ConflictSide.RED ? Color.RED.getRGB() : Color.BLUE.getRGB(),
                true // przezroczysty
        );

        g2d.setColor(fillColor);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f)); // ustaw niższą przezroczystość dla wypełnienia

        if (unit.getEquipmentCount() != 0) {
            // Oblicz promień okręgu zasięgu
            int rangeRadius = (unit.getRange() / squareSideLength);

            // Oblicz środek kwadratu jednostki
            int centerX = marginX + unit.getX() * squareSize + squareSize / 2;
            int centerY = marginY + unit.getY() * squareSize + squareSize / 2;

            // Rysuj wypełniony okrąg zasięgu
            g2d.fillOval(centerX - rangeRadius, centerY - rangeRadius, 2 * rangeRadius, 2 * rangeRadius);
        }

        //Rysuj jednostek
        int x = marginX + unit.getX() * squareSize;
        int y = marginY + unit.getY() * squareSize;

        g.setColor(Color.getHSBColor(0.28f, 0.54f, 0.56f));
        FontMetrics metrics = g.getFontMetrics();
        String unitText = unit.getName() + " (" + unit.getEquipmentCount() + ")";
        int textWidth = metrics.stringWidth(unitText);
        int textHeight = metrics.getHeight();

        // Rysowanie nazwy jednostki wyśrodkowanej w kwadracie
        if (unit.getSide().equals(ConflictSide.RED))
            drawCenteredString(g, unitText, new Rectangle(
                            x + (squareSize - textWidth) / 2,
                            y + (squareSize - textHeight) / 2 + metrics.getAscent(),
                            textWidth,
                            squareSize - textHeight),
                    new Font("Arial", Font.BOLD, 12), Color.WHITE, Color.RED);
        else
            drawCenteredString(g, unitText, new Rectangle(
                            x + (squareSize - textWidth) / 2,
                            y + (squareSize - textHeight) / 2 + metrics.getAscent(),
                            textWidth,
                            squareSize - textHeight),
                    new Font("Arial", Font.BOLD, 12), Color.WHITE, Color.BLUE);
        g2d.dispose(); // Zwalnia zasoby Graphics2D

    }


    public void moveUnit(int unitId, int newX, int newY) {
        for (SquareUnit unit : squareUnits) {
            if (unit.getId() == unitId) {
                int oldX = unit.getX();
                int oldY = unit.getY();

                // Zaktualizuj pozycję jednostki
                unit.setX(newX);
                unit.setY(newY);


                // Jeśli jednostka się przemieściła, ustaw ślad
                if ((oldX != newX || oldY != newY) && !isOtherUnitOnPosition(unit.getId(), newX, newY)) {

                    Color baseColor = unit.getSide() == ConflictSide.RED ? Color.RED : Color.BLUE;
                    Color trailColor = makeTrailColor(baseColor); // Metoda do tworzenia koloru śladu
                    trailColors[oldX][oldY] = trailColor;

                }

                // Usuń ślad z nowej pozycji (jeśli istnieje) i ustaw kolor jednostki
                setColorAt(newX, newY, unit.getSide() == ConflictSide.RED ? Color.RED : Color.BLUE);

                repaint();
                break; // Zakładamy, że ID są unikalne, więc można przerwać pętlę
            }
        }
    }

    private boolean isOtherUnitOnPosition(int id, int x, int y) {
        for (SquareUnit unit : squareUnits) {
            if (unit.getX() == x && unit.getY() == y && unit.getId() != id) {
                return true; // Inna jednostka zajmuje tę pozycję
            }
        }
        return false; // Brak innych jednostek na tej pozycji
    }

    public void setColorAt(int x, int y, Color color) {
        if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
            gridColors[x][y] = color; // Ustaw kolor jednostki
            trailColors[x][y] = null; // Usuń ślad pod jednostką (jeśli istnieje)
            repaint(); // Przerysuj komponent po zmianie koloru
        }
    }

    // Metoda do tworzenia delikatniejszego odcienia koloru dla śladu
    private Color makeTrailColor(Color baseColor) {
        float[] hsbVals = Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), null);
        // Zwiększ jasność koloru i zastosuj niską przezroczystość
        return new Color(Color.HSBtoRGB(hsbVals[0], hsbVals[1] * 0.5f, Math.min(1.0f, hsbVals[2] * 1.2f)), true);
    }


    public void updateTable() {
        // Załóżmy, że mamy tabelę i model zainicjalizowane wcześniej
        DefaultTableModel modelRed = redTeamTableModel;
        DefaultTableModel modelBlue = blueTeamTableModel;

        for(int i = 0; i < modelRed.getRowCount(); i++) {
            for (SquareUnit unit:
                 squareUnits) {
                if(modelRed.getValueAt(i, 0).toString().equals(String.valueOf(unit.getId()))) {
                    modelRed.setValueAt(unit.getEquipmentCount(), i, 2);
                }
            }
        }
        for(int i = 0; i < modelBlue.getRowCount(); i++) {
            for (SquareUnit unit:
                 squareUnits) {
                if(modelBlue.getValueAt(i, 0).toString().equals(String.valueOf(unit.getId()))) {
                    modelBlue.setValueAt(unit.getEquipmentCount(), i, 2);
                }
            }
        }


    }

    public void hit(int unitId, int power) {
        for (SquareUnit unit : squareUnits) {
            if (unit.getId() == unitId && unit.getEquipmentCount() > 0) {

                unit.setEquipmentCount(
                        unit.getEquipmentCount() - power
                );
                updateTable();
                repaint();
            }
        }
    }

    // Metoda do dodawania jednostek wojskowych
    public void addMilitaryUnit(SquareUnit unit) {
        if (unit != null) {
            addUnitToTeam(unit);
            squareUnits.add(unit);
            setColorAt(unit.getX(), unit.getY(), unit.getSide() == ConflictSide.RED ? Color.RED : Color.BLUE);
            repaint();
        }
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font, Color textColor, Color shadowColor) {
        // Ustawienie czcionki i koloru
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        // Obliczanie pozycji tekstu, aby był wyśrodkowany w prostokącie
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        // Rysowanie cienia tekstu
        g.setColor(shadowColor);
        g.drawString(text, x + 1, y + 1); // Ustawienie lekkiego przesunięcia dla cienia

        // Rysowanie tekstu
        g.setColor(textColor);
        g.drawString(text, x, y);
    }

    public Battlefield initialize() {
        JFrame frame = new JFrame();
        Battlefield battlefield = new Battlefield(gridSize, squareSideLength);

        frame.add(battlefield);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Pełny ekran
        frame.setVisible(true);

        return battlefield;
    }
}

