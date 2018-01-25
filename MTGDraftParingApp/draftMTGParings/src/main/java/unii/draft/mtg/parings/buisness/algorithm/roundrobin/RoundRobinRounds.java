package unii.draft.mtg.parings.buisness.algorithm.roundrobin;


import unii.draft.mtg.parings.buisness.algorithm.base.TournamentRounds;

public class RoundRobinRounds implements TournamentRounds {

    public RoundRobinRounds() {

    }

    @Override
    public int getMaxRound(int players) {
        return players - 1;
    }


}
