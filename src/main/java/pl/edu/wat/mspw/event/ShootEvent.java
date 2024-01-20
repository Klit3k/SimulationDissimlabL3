package pl.edu.wat.mspw.event;

import dissimlab.random.RNGenerator;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.edu.wat.mspw.CombatSystem;
import pl.edu.wat.mspw.Main;
import pl.edu.wat.mspw.model.CombatUnit;
import pl.edu.wat.mspw.util.Helper;


public class ShootEvent extends BasicSimEvent<CombatUnit, CombatSystem> {
    Helper helper;
    CombatUnit combatUnit;
    CombatSystem combatSystem;
    CombatUnit enemyUnit;
    RNGenerator rnGenerator;


    public ShootEvent(CombatUnit combatUnit, CombatSystem combatSystem, double period) throws SimControlException {
        super(combatUnit, combatSystem, period);
        this.helper = Helper.getInstance();
        this.combatUnit = combatUnit;
        this.combatSystem = combatSystem;
        this.enemyUnit = combatUnit.getFocusedUnit();
        this.rnGenerator = new RNGenerator();

    }

    public void simulateAttack(double distance) throws SimControlException {
        shotLog();
        if (simulateShot(
                combatSystem.getSideSquare(),
                0,
                0,
                0,
                0,
                combatUnit.getSpread(),
                distance
        )) {
            if (combatUnit.getPropabilityOfDesctruction() >= rnGenerator.nextDouble()) {
                if (enemyUnit.getEquipmentQuantity() >= combatUnit.getPower())
                    enemyUnit.setEquipmentQuantity(enemyUnit.getEquipmentQuantity() - combatUnit.getPower());
                else
                    enemyUnit.setEquipmentQuantity(0);


                //Wyniszczenie
                if (enemyUnit.getEquipmentQuantity() == 0)
                    enemyUnit.setAlive(false);
                hitLog();


            } else {
                nonHitLog();
            }
        } else nonHitLog();
    }

    private void drawHitGraphics() {
        try {
            Thread.sleep(Main.PAUSE_TIME);

                combatUnit.getBattlefield()
                        .hit(
                                enemyUnit.getId().hashCode(),
                                combatUnit.getPower()
                        );

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void stateChange() throws SimControlException {
        if (enemyUnit != null && enemyUnit.isAlive()) {
            if (helper.isInRange(combatUnit, combatUnit.getFocusedUnit())) {
                if (combatUnit.isAlive() && combatUnit.getEquipmentQuantity() > 0) {
                    simulateAttack(helper.computeDistance(combatUnit, enemyUnit));
                }
            } else {
                combatUnit.setFocusedUnit(null);
                combatUnit.setDetected(false);
            }
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    public CombatSystem getEventParams() {
        return this.eventParams;
    }

    /**
     * targetX, tragetY - punkty celowania;
     * center1X, center1Y - środek celu
     */
    public boolean simulateShot(double squareSize,
                                       double center1X, double center1Y,
                                       double targetX, double targetY,
                                       double sigma, double distance) {

        double dx = generateRandomNormalX(sigma, distance);
        double dy = generateRandomNormalY(sigma, distance);

        double hitX = targetX + dx;
        double hitY = targetY + dy;

        return isInsideSquare(hitX, hitY, squareSize, squareSize, center1X, center1Y);
    }

    private boolean isInsideSquare(double x, double y, double h, double w, double centerX, double centerY) {
        double halfH = h / 2.0;
        double halfW = w / 2.0;
        double minX = centerX - halfW;
        double maxX = centerX + halfW;
        double minY = centerY - halfH;
        double maxY = centerY + halfH;

        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    private double generateRandomNormalX(double sigma, double distance) {
        double u1 = rnGenerator.nextDouble();
        double u2 = rnGenerator.nextDouble();

        return Math.sqrt(-2 * Math.log(u1)) * Math.sin(2 * Math.PI * u2) * distance / sigma;
    }

    private double generateRandomNormalY(double sigma, double distance) {
        double u1 = rnGenerator.nextDouble();
        double u2 = rnGenerator.nextDouble();

        return Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2) * distance / sigma;
    }

    private void shotLog() {
        helper.getLog(combatUnit, combatUnit.simTimeFormatted(), String.format(
                "shoots to id=%s side=%s eq=%d at (%d,%d)",
                combatUnit.getFocusedUnit().getId(),
                combatUnit.getFocusedUnit().getConflictSide(),
                combatUnit.getFocusedUnit().getEquipmentQuantity(),
                combatUnit.getFocusedUnit().getX(),
                combatUnit.getFocusedUnit().getY()
        ));
    }

    private void hitLog() {
        drawHitGraphics();

        helper.getLog(combatUnit, combatUnit.simTimeFormatted(), String.format(
                "hit the shot to id=%s side=%s eq=%d at (%d,%d) [X]",
                combatUnit.getFocusedUnit().getId(),
                combatUnit.getFocusedUnit().getConflictSide(),
                combatUnit.getFocusedUnit().getEquipmentQuantity(),
                combatUnit.getFocusedUnit().getX(),
                combatUnit.getFocusedUnit().getY()
        ));
    }

    private void nonHitLog() {
        helper.getLog(combatUnit, combatUnit.simTimeFormatted(), String.format(
                "missed shot to id=%s side=%s eq=%d at (%d,%d) [-]",
                combatUnit.getFocusedUnit().getId(),
                combatUnit.getFocusedUnit().getConflictSide(),
                combatUnit.getFocusedUnit().getEquipmentQuantity(),
                combatUnit.getFocusedUnit().getX(),
                combatUnit.getFocusedUnit().getY()
        ));
    }
}
