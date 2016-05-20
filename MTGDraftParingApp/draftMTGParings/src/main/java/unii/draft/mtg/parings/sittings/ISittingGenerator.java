package unii.draft.mtg.parings.sittings;

import java.util.List;

/**
 * Created by Unii on 2016-05-08.
 */
public interface ISittingGenerator {
    List<String> generateSittings(List<String> playerNameList);
}
