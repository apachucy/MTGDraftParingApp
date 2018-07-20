package unii.draft.mtg.parings.buisness.algorithm.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static unii.draft.mtg.parings.buisness.algorithm.base.PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS;
import static unii.draft.mtg.parings.buisness.algorithm.base.PairingMode.PAIRING_MANUAL;
import static unii.draft.mtg.parings.buisness.algorithm.base.PairingMode.PAIRING_ROUND_KNOCK_OUT;
import static unii.draft.mtg.parings.buisness.algorithm.base.PairingMode.PAIRING_ROUND_ROBIN;
import static unii.draft.mtg.parings.buisness.algorithm.base.PairingMode.PAIRING_TOURNAMENT;


@IntDef({PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS, PAIRING_MANUAL, PAIRING_TOURNAMENT, PAIRING_ROUND_ROBIN, PAIRING_ROUND_KNOCK_OUT})
@Retention(RetentionPolicy.SOURCE)
public @interface PairingModeNotation {
}
