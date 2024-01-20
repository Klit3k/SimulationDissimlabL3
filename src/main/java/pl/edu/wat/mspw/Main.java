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
    public static final int PAUSE_TIME = 250;
    static ArrayList<MovementDirection> blueMovementDirectionsArtillery = new ArrayList<>();
    static ArrayList<MovementDirection> blueMovementDirectionsInfantry = new ArrayList<>();
    static ArrayList<MovementDirection> blueMovementDirectionsTank = new ArrayList<>();


    static ArrayList<MovementDirection> redMovementDirectionsTank = new ArrayList<>();
    static ArrayList<MovementDirection> redMovementDirectionsArtillery = new ArrayList<>();
    static ArrayList<MovementDirection> redMovementDirectionsInfantry = new ArrayList<>();

    static CombatSystem combatSystem;
    static Battlefield battlefield;

    public static void main(String[] args) throws SimControlException {
        SimManager sm = SimManager.getInstance();
        ArrayList<CombatUnit> blueTeam = new ArrayList<>();
        ArrayList<CombatUnit> redTeam = new ArrayList<>();

        //Movements

        //Artillery RED
        for(int i = 0; i<=50; i++) {
            if(i<10) {
                redMovementDirectionsArtillery.add(MovementDirection.NORTH);
            }
        }
        //Infantry RED
        for(int i = 0; i<=50; i++) {
            if(i<3) {
                redMovementDirectionsInfantry.add(MovementDirection.WEST);
                redMovementDirectionsInfantry.add(MovementDirection.NORTH);
            }
        }
        //Tank RED
        for(int i = 0; i<=50; i++) {
            if(i<15) {
                redMovementDirectionsTank.add(MovementDirection.NORTH);
                redMovementDirectionsTank.add(MovementDirection.WEST);
            }
        }
        //Artillery BLUE
        for(int i = 0; i<=50; i++) {
            if(i<5) {
                blueMovementDirectionsArtillery.add(MovementDirection.WEST);
                blueMovementDirectionsArtillery.add(MovementDirection.WEST);
                blueMovementDirectionsArtillery.add(MovementDirection.WEST);
                blueMovementDirectionsArtillery.add(MovementDirection.SOUTH);
            }
            if(i<10) {
                blueMovementDirectionsArtillery.add(MovementDirection.SOUTH);
            }
            if(i<4) {
                blueMovementDirectionsArtillery.add(MovementDirection.WEST);
            }
        }

        //Infantry BLUE
        for(int i = 0; i<=50; i++) {
            if(i<10) {
                blueMovementDirectionsInfantry.add(MovementDirection.SOUTH);
                blueMovementDirectionsInfantry.add(MovementDirection.EAST);
            }
            if(i<5)
                blueMovementDirectionsInfantry.add(MovementDirection.EAST);
        }
        //Tank BLUE
        for(int i = 0; i<=50; i++) {
            if(i<20) {
                blueMovementDirectionsTank.add(MovementDirection.SOUTH);
            }
            if(i<5)
                blueMovementDirectionsTank.add(MovementDirection.WEST);
        }
        combatSystem = CombatSystem.builder()
                .sideSquare(20)
                .combatUnitsBlue(blueTeam)
                .combatUnitsRed(redTeam)
                .build();

        //grid size np. 10x10
        //square side length dlugosc boku
        battlefield = new Battlefield(50, combatSystem.getSideSquare())
                .initialize();
        //==============================================================================================================
        //                                          BLUE TEAM
        //==============================================================================================================

        blueTeam.add(
                unitBuilder("Tank", 10, 30, 4_000, 5, 20, 40, 50,
                        0.6, 1, 0.6, blueMovementDirectionsTank, ConflictSide.BLUE)
        );

        blueTeam.add(
                unitBuilder("Infantry", 25, 20, 1_000, 5, 20, 40, 50,
                        0.6, 1, 0.6, blueMovementDirectionsInfantry, ConflictSide.BLUE)
        );

        blueTeam.add(
                unitBuilder("Artillery", 25, 40, 10_000, 5, 20, 40, 50,
                        0.6, 1, 0.6, blueMovementDirectionsArtillery, ConflictSide.BLUE)
        );
        //==============================================================================================================
        //                                          RED TEAM
        //==============================================================================================================
        redTeam.add(
                unitBuilder("Tank", 25, 5, 4_000, 5, 20, 40, 1000,
                        0.6, 2, 0.6, redMovementDirectionsTank, ConflictSide.RED)
        );

        redTeam.add(
                unitBuilder("Infantry", 5, 5, 1_000, 5, 20, 40, 1000,
                        0.6, 2, 0.6, redMovementDirectionsInfantry, ConflictSide.RED)
        );

        redTeam.add(
                unitBuilder("Artillery", 35, 5, 10_000, 5, 20, 40, 1000,
                        1, 2, 1, redMovementDirectionsArtillery, ConflictSide.RED)
        );


        for (CombatUnit combatUnit : redTeam) {
            battlefield.addMilitaryUnit(new SquareUnit(combatUnit.getId().hashCode(),
                    combatUnit.getX(),
                    combatUnit.getY(),
                    ConflictSide.RED,
                    combatUnit.getId(),
                    combatUnit.getEquipmentQuantity(),
                    combatUnit.getRange(),
                    combatUnit.getPower()
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
                    combatUnit.getRange(),
                    combatUnit.getPower()
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
                .id(String.format("%s-%s", name, side))
                .x(x)
                .y(y)
                .range(range/combatSystem.getSideSquare()*10)
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
