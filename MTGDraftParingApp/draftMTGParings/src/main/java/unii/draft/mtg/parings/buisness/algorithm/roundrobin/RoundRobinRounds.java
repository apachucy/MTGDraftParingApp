package unii.draft.mtg.parings.buisness.algorithm.roundrobin;


import unii.draft.mtg.parings.buisness.algorithm.base.TournamentRounds;

public class RoundRobinRounds implements TournamentRounds {

    public RoundRobinRounds() {

    }

    @Override
    public int getMaxRound(int players) {
        if (areOdd(players)) {
            return players * (players - 1) / 2;
        }
        return players / 2;
    }

    private boolean areOdd(int players) {
        return !(players % 2 == 0);
    }


}
