package pl.edu.wat.mspw.event;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import pl.edu.wat.mspw.model.CombatUnit;
import pl.edu.wat.mspw.util.Helper;
import pl.edu.wat.mspw.util.Statistic;


public class DetectionEvent extends BasicSimEvent<CombatUnit, Object> {
    CombatUnit combatUnit;
    Helper helper;
    public DetectionEvent(CombatUnit entity, double delay) throws SimControlException {
        super(entity, delay);
        this.combatUnit = entity;
        this.helper = Helper.getInstance();
    }

    @Override
    protected void stateChange() throws SimControlException {

        if(combatUnit.isAlive()) {
            CombatUnit enemy = combatUnit.getFocusedUnit();
            if(enemy != null) {
                helper.getLog(getSimObj(), getSimObj().simTimeFormatted(), String.format(
                        "detected id=%s side=%s at (%d,%d)",
                        enemy.getId(),
                        enemy.getConflictSide(),
                        enemy.getX(),
                        enemy.getY()
                ));
                new ShootEvent(getSimObj(), getSimObj().getCombatSystem(), 1/combatUnit.getFireRate());
            }
        }
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    public Object getEventParams() {
        return this.eventParams;
    }

}
