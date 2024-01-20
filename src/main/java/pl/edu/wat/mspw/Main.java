package pl.edu.wat.mspw;

import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.enums.MovementDirection;
import pl.edu.wat.mspw.event.MovementEvent;
import pl.edu.wat.mspw.model.CombatUnit;


import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws SimControlException {
        ArrayList<CombatUnit> blueTeam = new ArrayList<>();
        ArrayList<CombatUnit> redTeam = new ArrayList<>();
        ArrayList<MovementDirection> redMovementDirections = new ArrayList<>(Arrays.asList(
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH,
                MovementDirection.SOUTH
        ));
        ArrayList<MovementDirection> blueMovementDirections = new ArrayList<>(Arrays.asList(
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH,
                MovementDirection.NORTH
        ));

        CombatSystem combatSystem = CombatSystem.builder()
                .sideSquare(20)
                .combatUnitsBlue(blueTeam)
                .combatUnitsRed(redTeam)
                .build();

        blueTeam.add(
                CombatUnit.builder()
                        .id("A")
                        .x(0)
                        .y(0)
                        .range(3000)
                        .fireRate(5)
                        .equipmentQuantity(20)
                        .velocity(40)
                        .spread(50)
                        .detectionRate(0.6)
                        .power(1)
                        .route(blueMovementDirections)
                        .conflictSide(ConflictSide.BLUE)
                        .combatSystem(combatSystem)
                        .build()
        );

        redTeam.add(
                CombatUnit.builder()
                        .id("B")
                        .x(0)
                        .y(20)
                        .range(5000)
                        .fireRate(5)
                        .equipmentQuantity(20)
                        .velocity(40)
                        .spread(1000)
                        .detectionRate(0.6)
                        .power(2)
                        .route(redMovementDirections)
                        .conflictSide(ConflictSide.RED)
                        .combatSystem(combatSystem)
                        .build()
        );


        SimManager sm = SimManager.getInstance();
        for (CombatUnit combatUnit : redTeam) {
            new MovementEvent(combatUnit, combatUnit.computeTimeToMove());
        }

        for (CombatUnit combatUnit : blueTeam) {
            new MovementEvent(combatUnit, combatUnit.computeTimeToMove());
        }


        sm.setEndSimTime(100);

        sm.startSimulation();


    }
}