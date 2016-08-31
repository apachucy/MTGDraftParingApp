package unii.draft.mtg.parings.buisness.sittings;

import java.util.List;


public interface ISittingGenerator {
    List<String> generateSittings(List<String> playerNameList);
}
