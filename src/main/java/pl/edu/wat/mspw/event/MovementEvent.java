package pl.edu.wat.mspw.event;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.enums.MovementDirection;
import pl.edu.wat.mspw.model.CombatUnit;
import pl.edu.wat.mspw.util.Helper;


import java.util.List;

public class MovementEvent extends BasicSimEvent<CombatUnit, Object> {
    CombatUnit combatUnit;
    Helper helper;

    public MovementEvent(CombatUnit entity, double delay) throws SimControlException {
        super(entity, delay);
        combatUnit = entity;
        helper = Helper.getInstance();
    }

    private void makePause(long milis)  {
        try {
//            SimManager.getInstance().pauseSimulation();
            Thread.sleep(milis);
//            SimManager.getInstance().resumeSimulation();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void stateChange() throws SimControlException {
//        makePause(250);

        if (combatUnit.isAlive() && !combatUnit.getRoute().isEmpty()) {
            this.updateLocation();
            this.findEnemy();
            new MovementEvent(combatUnit, combatUnit.computeTimeToMove());
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    public Object getEventParams() {
        return null;
    }


    public void findEnemy() throws SimControlException {
        List<CombatUnit> enemies;

        if (combatUnit.getConflictSide().equals(ConflictSide.BLUE))
            enemies = combatUnit.getCombatSystem().getCombatUnitsRed();
        else
            enemies = combatUnit.getCombatSystem().getCombatUnitsBlue();

        List<CombatUnit> localEnemies = enemies.stream()
                .filter(enemy -> enemy.isAlive() && helper.isInRange(combatUnit, enemy))
                .toList();

        if(!localEnemies.isEmpty() && !combatUnit.isDetected()) {
            combatUnit.setFocusedUnit(localEnemies.getFirst());
            combatUnit.setDetected(true);

            new DetectionEvent(
                    combatUnit,
                    helper.computeDistance(
                            combatUnit,
                            localEnemies.getFirst())/combatUnit.getRange()/combatUnit.getDetectionRate()
            );
        }
    }

    public void updateLocation() throws SimControlException {
        if (!combatUnit.getRoute().isEmpty()) {
            int startX = combatUnit.getX(), startY = combatUnit.getY();
            MovementDirection nextDirection = combatUnit.getRoute().getFirst();
            combatUnit.getRoute().removeFirst();

            switch (nextDirection) {
                case NORTH -> {
                    combatUnit.setY(combatUnit.getY() + 1);
                }
                case NORTH_EAST -> {
                    combatUnit.setY(combatUnit.getY() + 1);
                    combatUnit.setX(combatUnit.getX() + 1);
                }
                case NORTH_WEST -> {
                    combatUnit.setY(combatUnit.getY() + 1);
                    combatUnit.setX(combatUnit.getX() - 1);
                }
                case SOUTH -> combatUnit.setY(combatUnit.getY() - 1);

                case SOUTH_EAST -> {
                    combatUnit.setY(combatUnit.getY() - 1);
                    combatUnit.setX(combatUnit.getX() + 1);
                }
                case SOUTH_WEST -> {
                    combatUnit.setY(combatUnit.getY() - 1);
                    combatUnit.setX(combatUnit.getX() - 1);
                }
                case EAST -> combatUnit.setX(combatUnit.getX() + 1);
                case WEST -> combatUnit.setX(combatUnit.getX() - 1);
            }
            helper.getLog(getSimObj(), getSimObj().simTimeFormatted(), String.format(
                    "move %s (%d,%d) -> (%d,%d)",
                    nextDirection,
                    startX,
                    startY,
                    combatUnit.getX(),
                    combatUnit.getY()
            ));
        } else if (combatUnit.getCombatSystem().getCombatUnitsRed().isEmpty() &&
                combatUnit.getCombatSystem().getCombatUnitsBlue().isEmpty()) {
            this.stopSimulation();
        }

    }


}
