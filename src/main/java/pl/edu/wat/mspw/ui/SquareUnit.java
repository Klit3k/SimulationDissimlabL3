package pl.edu.wat.mspw.ui;

import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.mspw.enums.ConflictSide;

// Wewnętrzna klasa reprezentująca jednostki wojskowe
@Getter @Setter
public class SquareUnit {
    private final int startRange;
    private int power;
    private int id; // Dodanie identyfikatora
    private int x, y; // Pozycja na siatce
    private ConflictSide side; // Strona konfliktu
    private String name; // Nazwa jednostki
    private int equipmentCount; // Ilość sprzętu
    private int range;

    public SquareUnit(int id, int x, int y, ConflictSide side, String name, int equipmentCount, int range, int power) {
        this.id = id; // Inicjalizacja identyfikatora
        this.x = x;
        this.y = y;
        this.side = side;
        this.name = name;
        this.equipmentCount = equipmentCount;
        this.startRange = range;
        this.power = power;
        this.range = (int) (range*0.8);
    }
}
