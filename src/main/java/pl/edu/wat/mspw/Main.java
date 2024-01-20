package pl.edu.wat.mspw;

import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.enums.MovementDirection;
import pl.edu.wat.mspw.event.MovementEvent;
import pl.edu.wat.mspw.model.CombatUnit;
import pl.edu.wat.mspw.ui.Battlefield;
import pl.edu.wat.mspw.ui.SquareUnit;


import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static ArrayList<MovementDirection> blueMovementDirections = new ArrayList<>(Arrays.asList(
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
    static ArrayList<MovementDirection> redMovementDirections = new ArrayList<>(Arrays.asList(
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
    static CombatSystem combatSystem;
    static Battlefield battlefield;
    public static void main(String[] args) throws SimControlException {
        SimManager sm = SimManager.getInstance();
        ArrayList<CombatUnit> blueTeam = new ArrayList<>();
        ArrayList<CombatUnit> redTeam = new ArrayList<>();



        combatSystem = CombatSystem.builder()
                .sideSquare(20)
                .combatUnitsBlue(blueTeam)
                .combatUnitsRed(redTeam)
                .build();

         battlefield = new Battlefield(20, combatSystem.getSideSquare())
                .initialize();

        blueTeam.add(
                unitBuilder("Braun", 20, 20, 2000, 5, 20, 40, 50, 0.6, 1, 0.6, blueMovementDirections, ConflictSide.BLUE)
        );

        redTeam.add(

                unitBuilder("Swiecznik", 0, 20, 2000, 5, 20, 40, 1000, 0.6, 2, 0.6, redMovementDirections, ConflictSide.RED)
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

    public static CombatUnit unitBuilder(String name, int x, int y, int range, double fireRate, int equipmentQuantity,
                                         double velocity, double spread, double detectionRate, int power,
                                         double propabilityOfDesctruction, ArrayList<MovementDirection> movementDirections,
                                         ConflictSide side) {
        return CombatUnit.builder()
                .id(name)
                .x(x)
                .y(y)
                .range(range)
                .fireRate(fireRate)
                .equipmentQuantity(equipmentQuantity)
                .velocity(velocity)
                .spread(spread)
                .detectionRate(detectionRate)
                .power(power)
                .propabilityOfDesctruction(propabilityOfDesctruction)
                .route(movementDirections)
                .conflictSide(side)
                .combatSystem(combatSystem)
                .battlefield(battlefield)
                .build();
    }
}
