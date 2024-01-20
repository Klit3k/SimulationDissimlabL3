package pl.edu.wat.mspw.util;


import pl.edu.wat.mspw.model.CombatUnit;

public class Helper {
    private static Helper instance;
    private Helper() {}
    public static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public boolean isInRange(CombatUnit aUnit, CombatUnit bUnit) {
        return (aUnit.getRange() / aUnit.getCombatSystem().getSideSquare())
                >= computeDistance(aUnit, bUnit);
    }
    public double computeDistance(CombatUnit aUnit, CombatUnit bUnit) {
        double x1 = aUnit.getX(), y1 = aUnit.getY(), x2 = bUnit.getX(), y2 = bUnit.getY();
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)) * aUnit.getCombatSystem().getSideSquare();
    }

    public void getLog(CombatUnit unit, String time, String text) {
        System.out.println(
                String.format(
                        "[ %s ] :: id=%s side=%s eq=%s ",
                        time,
                        unit.getId(),
                        unit.getConflictSide(),
                        unit.getEquipmentQuantity()
                )
                        + text
        );
    }
}
