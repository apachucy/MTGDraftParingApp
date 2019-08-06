package unii.draft.mtg.parings.buisness.algorithm.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static unii.draft.mtg.parings.buisness.sittings.SittingsMode.NO_SITTINGS;
import static unii.draft.mtg.parings.buisness.sittings.SittingsMode.SITTINGS_RANDOM;
import static unii.draft.mtg.parings.buisness.sittings.SittingsMode.SITTINGS_TOURNAMENT;

@IntDef({NO_SITTINGS, SITTINGS_RANDOM, SITTINGS_TOURNAMENT})
@Retention(RetentionPolicy.SOURCE)
public @interface SittingsModeNotation {
}
