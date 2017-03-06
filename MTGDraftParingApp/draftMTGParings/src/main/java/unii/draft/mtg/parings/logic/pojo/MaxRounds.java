package unii.draft.mtg.parings.logic.pojo;

import lombok.Builder;
import lombok.Getter;

@Builder
public class MaxRounds {
    @Getter
    private int rounds;
    @Getter
    private int players;
}
