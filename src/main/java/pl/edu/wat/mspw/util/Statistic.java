package pl.edu.wat.mspw.util;

import dissimlab.monitors.Diagram;
import dissimlab.monitors.MonitoredVar;
import dissimlab.monitors.Statistics;
import pl.edu.wat.mspw.enums.ConflictSide;
import pl.edu.wat.mspw.model.CombatUnit;

import java.awt.*;

public class Statistic {
    public static MonitoredVar iloscSrodkiWalkiRed;
    public static MonitoredVar iloscSrodkiWalkiBlue;
    public static MonitoredVar czasZniszczeniaDlaRed = new MonitoredVar(0);
    public static MonitoredVar czasPomiedzyZniszczeniaDlaRed = new MonitoredVar(0);
    public static MonitoredVar czasZniszczeniaDlaBlue = new MonitoredVar(0);
    public static MonitoredVar czasPomiedzyZniszczeniaDlaBlue = new MonitoredVar(0);

    public static void logUnitsQuantity(CombatUnit unit) {
        int sum = 0;
        if(unit.getConflictSide() == ConflictSide.RED) {
            for (CombatUnit u:
                 unit.getCombatSystem().getCombatUnitsRed()) {
                sum = sum+ u.getEquipmentQuantity();
            }
            iloscSrodkiWalkiRed.setValue(sum);
        } else if (unit.getConflictSide() == ConflictSide.BLUE){
            for (CombatUnit u:
                    unit.getCombatSystem().getCombatUnitsBlue()) {
                sum = sum+ u.getEquipmentQuantity();
            }
            iloscSrodkiWalkiBlue.setValue(sum);
        }
    }

    public static void logDestroyment(CombatUnit unit, double simTime) {

        if(unit.getConflictSide() == ConflictSide.RED) {
            if (czasZniszczeniaDlaRed.getValue() == 0) {
                czasZniszczeniaDlaRed.setValue(simTime);
            }
            if (simTime - czasZniszczeniaDlaRed.getValue() != 0 && (simTime - czasZniszczeniaDlaRed.getValue() != czasPomiedzyZniszczeniaDlaRed.getValue())) {
                czasPomiedzyZniszczeniaDlaRed.setValue(simTime-czasZniszczeniaDlaRed.getValue());
                czasZniszczeniaDlaRed.setValue(simTime);
            }
        } else {
            if (czasZniszczeniaDlaBlue.getValue() == 0) {
                czasZniszczeniaDlaBlue.setValue(simTime);
            }
            if (simTime - czasZniszczeniaDlaBlue.getValue() != 0 && (simTime - czasZniszczeniaDlaBlue.getValue() != czasPomiedzyZniszczeniaDlaBlue.getValue())) {
                czasPomiedzyZniszczeniaDlaBlue.setValue(simTime-czasZniszczeniaDlaBlue.getValue());
                czasZniszczeniaDlaBlue.setValue(simTime);
            }
        }
    }
    public static void summary() {
        System.out.println("====== Podsumowanie ======");
        System.out.println("Srednia liczba srodków walki strony Red: " + Statistics.arithmeticMean(Statistic.iloscSrodkiWalkiRed));
        System.out.println("Srednia liczba srodków walki strony Blue: " + Statistics.arithmeticMean(Statistic.iloscSrodkiWalkiBlue));
        System.out.println("Sredni czas pomiedzy kolejnymi zniszczeniami srodkow walki strony przeciwnej przez srodki walki Red: "
                + Statistics.arithmeticMean(Statistic.czasPomiedzyZniszczeniaDlaRed));
        System.out.println("Sredni czas pomiedzy kolejnymi zniszczeniami srodkow walki strony przeciwnej przez srodki walki Blue: "
                + Statistics.arithmeticMean(Statistic.czasPomiedzyZniszczeniaDlaBlue));


        Diagram d1 = new Diagram(Diagram.DiagramType.TIME, "Zmiana w czasie liczby srodkow walki dla sil");
        d1.add(Statistic.iloscSrodkiWalkiRed, Color.RED, "Sily Red");
        d1.add(Statistic.iloscSrodkiWalkiBlue, Color.BLUE, "Sily Blue");
        d1.show();



        Diagram d3 = new Diagram(Diagram.DiagramType.DISTRIBUTION,
                "Dystrybuanta czasu pomiedzy kolejnymi zniszczeniami srodków walki ");
        d3.add(Statistic.czasPomiedzyZniszczeniaDlaRed, Color.RED, "Sily Red");
        d3.add(Statistic.czasPomiedzyZniszczeniaDlaBlue, Color.BLUE, "Sily Blue");
        d3.show();
    }

    public static void initialize(int iloscSrodkiWalkiRedStart, int iloscSrodkiWalkiBlueStart) {
        iloscSrodkiWalkiRed = new MonitoredVar(iloscSrodkiWalkiRedStart);
        iloscSrodkiWalkiBlue = new MonitoredVar(iloscSrodkiWalkiBlueStart);
    }
}
