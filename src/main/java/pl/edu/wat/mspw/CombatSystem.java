package pl.edu.wat.mspw;

import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.simcore.BasicSimObj;
import lombok.*;
import pl.edu.wat.mspw.model.CombatUnit;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CombatSystem extends BasicSimObj {

    /** Stan walczących jednostek strony Blue */
    private ArrayList<CombatUnit> combatUnitsBlue;

    /** Stan walczących jednostek strony Red */
    private ArrayList<CombatUnit> combatUnitsRed;

    /** Rozmiar boku kwadratu w modelu terenu */
    private int sideSquare;

    @Override
    public void reflect(IPublisher iPublisher, INotificationEvent iNotificationEvent) {

    }

    @Override
    public boolean filter(IPublisher iPublisher, INotificationEvent iNotificationEvent) {
        return false;
    }
}
