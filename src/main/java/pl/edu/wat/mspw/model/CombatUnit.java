package pl.edu.wat.mspw.model;

import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.simcore.BasicSimObj;
import lombok.*;
import pl.edu.wat.mspw.CombatSystem;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.enums.MovementDirection;


import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CombatUnit extends BasicSimObj {
    private final double FIGHTING_VELOCITY = 1.2;

    /** Strona konfliktu */
    private ConflictSide conflictSide;

    /** Położenie: współrzędne sieci kwadratów oraz kierunek przemieszczania */
    private int x, y;

    /** Marszruta: lista kolejnych kierunków zmian położenia */
    private ArrayList<MovementDirection> route;

    /** Prędkość w danym położeniu determinująca czas, po którym nastąpi zmiana położenia */
    private double velocity;

    /** Szybkostrzelność strzelania */
    private double fireRate;

    /** Zasięg rażenia środków walki */
    private float range;

    /** Ilość sprzętu */
    private int equipmentQuantity;

    /** Wartość jednostkowa intensywności wykrywani */
    private double detectionRate;

    /** Rozrzut środka walki */
    private double spread;

    private int power;

    //===========================================
    private String id;

    @Builder.Default
    private boolean isAlive = true;
    @Builder.Default
    private boolean isFighting = false;
    @Builder.Default
    private boolean isDetected = false;

    private CombatUnit focusedUnit;


    private CombatSystem combatSystem;


    @Override
    public void reflect(IPublisher iPublisher, INotificationEvent iNotificationEvent) {

    }

    @Override
    public boolean filter(IPublisher iPublisher, INotificationEvent iNotificationEvent) {
        return false;
    }

    /** Oblicza czas do przesunięcia jednostki */
    public double computeTimeToMove() {
        if(this.isFighting)
            return combatSystem.getSideSquare()/this.getVelocity() * FIGHTING_VELOCITY;
        else
            return combatSystem.getSideSquare()/this.getVelocity();
    }
}
