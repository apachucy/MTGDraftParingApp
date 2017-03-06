package unii.draft.mtg.parings.buisness.algorithm.tournament;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.logic.pojo.MaxRounds;

public class TournamentRounds {
    private List<MaxRounds> tournamentRounds;
    private int MAX_ROUNDS = 13;

    public TournamentRounds() {
        tournamentRounds = createRounds();
    }

    public int getMaxRound(int players) {

        for (MaxRounds maxRounds : tournamentRounds) {
            if (players <= maxRounds.getPlayers()) {
                return maxRounds.getRounds();
            }
        }
        return MAX_ROUNDS;
    }

    //todo: read from a file
    public List<MaxRounds> createRounds() {
        List<MaxRounds> maxRoundsList = new ArrayList<>();
        //from: http://magic.wizards.com/en/game-info/products/magic-online/swiss-pairings
        MaxRounds maxRounds1 = MaxRounds.builder().rounds(1).players(2).build();
        MaxRounds maxRounds2 = MaxRounds.builder().rounds(2).players(4).build();
        MaxRounds maxRounds3 = MaxRounds.builder().rounds(3).players(8).build();
        MaxRounds maxRounds4 = MaxRounds.builder().rounds(4).players(16).build();
        MaxRounds maxRounds5 = MaxRounds.builder().rounds(5).players(32).build();
        MaxRounds maxRounds6 = MaxRounds.builder().rounds(6).players(64).build();
        MaxRounds maxRounds7 = MaxRounds.builder().rounds(7).players(128).build();
        MaxRounds maxRounds8 = MaxRounds.builder().rounds(8).players(212).build();
        MaxRounds maxRounds9 = MaxRounds.builder().rounds(9).players(384).build();
        MaxRounds maxRounds10 = MaxRounds.builder().rounds(10).players(672).build();
        MaxRounds maxRounds11 = MaxRounds.builder().rounds(11).players(1248).build();
        MaxRounds maxRounds12 = MaxRounds.builder().rounds(12).players(2272).build();
        MaxRounds maxRounds13 = MaxRounds.builder().rounds(13).players(10000).build();
        maxRoundsList.add(maxRounds1);
        maxRoundsList.add(maxRounds2);
        maxRoundsList.add(maxRounds3);
        maxRoundsList.add(maxRounds4);
        maxRoundsList.add(maxRounds5);
        maxRoundsList.add(maxRounds6);
        maxRoundsList.add(maxRounds7);
        maxRoundsList.add(maxRounds8);
        maxRoundsList.add(maxRounds9);
        maxRoundsList.add(maxRounds10);
        maxRoundsList.add(maxRounds11);
        maxRoundsList.add(maxRounds12);
        maxRoundsList.add(maxRounds13);
        return maxRoundsList;
    }
}
