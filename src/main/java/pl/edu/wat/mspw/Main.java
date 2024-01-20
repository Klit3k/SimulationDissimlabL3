package pl.edu.wat.mspw;

import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.enums.MovementDirection;
import pl.edu.wat.mspw.event.MovementEvent;
import pl.edu.wat.mspw.model.CombatUnit;
import pl.edu.wat.mspw.ui.Battlefield;
import pl.edu.wat.mspw.ui.SquareUnit;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SimControlException {
        SimManager sm = SimManager.getInstance();
        ArrayList<CombatUnit> blueTeam = new ArrayList<>();
        ArrayList<CombatUnit> redTeam = new ArrayList<>();
        ArrayList<MovementDirection> blueMovementDirections = new ArrayList<>(Arrays.asList(
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST,
                MovementDirection.WEST
        ));
        ArrayList<MovementDirection> redMovementDirections = new ArrayList<>(Arrays.asList(
                MovementDirection.EAST,
                MovementDirection.EAST,
                MovementDirection.EAST,
                MovementDirection.EAST,
                MovementDirection.EAST,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.EAST,
                MovementDirection.EAST,
                MovementDirection.EAST

        ));


        CombatSystem combatSystem = CombatSystem.builder()
                .sideSquare(20)
                .combatUnitsBlue(blueTeam)
                .combatUnitsRed(redTeam)
                .build();

        Battlefield battlefield = new Battlefield(20, combatSystem.getSideSquare())
                .initialize();

        blueTeam.add(
                CombatUnit.builder()
                        .id("Braun")
                        .x(20)
                        .y(20)
                        .range(2000)
                        .fireRate(5)
                        .equipmentQuantity(20)
                        .velocity(40)
                        .spread(50)
                        .detectionRate(0.6)
                        .power(1)
                        .propabilityOfDesctruction(0.6)
                        .route(blueMovementDirections)
                        .conflictSide(ConflictSide.BLUE)
                        .combatSystem(combatSystem)
                        .battlefield(battlefield)
                        .build()
        );

        redTeam.add(
                CombatUnit.builder()
                        .id("Swiecznik")
                        .x(0)
                        .y(20)
                        .range(2000)
                        .fireRate(5)
                        .equipmentQuantity(20)
                        .velocity(40)
                        .spread(1000)
                        .detectionRate(0.6)
                        .power(2)
                        .propabilityOfDesctruction(0.6)
                        .route(redMovementDirections)
                        .conflictSide(ConflictSide.RED)
                        .combatSystem(combatSystem)
                        .battlefield(battlefield)
                        .build()
        );

        for (CombatUnit combatUnit : redTeam) {
            battlefield.addMilitaryUnit(new SquareUnit(combatUnit.getId().hashCode(),
                    combatUnit.getX(),
                    combatUnit.getY(),
                    ConflictSide.RED,
                    combatUnit.getId(),
                    combatUnit.getEquipmentQuantity(),
                    combatUnit.getRange()
            ));
            new MovementEvent(combatUnit, combatUnit.computeTimeToMove());
        }

        for (CombatUnit combatUnit : blueTeam) {
            battlefield.addMilitaryUnit(new SquareUnit(combatUnit.getId().hashCode(),
                    combatUnit.getX(),
                    combatUnit.getY(),
                    ConflictSide.BLUE,
                    combatUnit.getId(),
                    combatUnit.getEquipmentQuantity(),
                    combatUnit.getRange()
            ));
            new MovementEvent(combatUnit, combatUnit.computeTimeToMove());
        }
        sm.setEndSimTime(20_000);
        sm.startSimulation();
    }


}
