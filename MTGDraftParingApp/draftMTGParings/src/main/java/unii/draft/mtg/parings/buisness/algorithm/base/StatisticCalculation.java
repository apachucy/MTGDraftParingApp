package unii.draft.mtg.parings.buisness.algorithm.base;

import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.IStatisticCalculation;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;

/**
 * TODO: change list to hash map?
 *
 * @author Arkadiusz Pachucy
 */
public class StatisticCalculation implements IStatisticCalculation {

    private IParingAlgorithm mParingAlgorithm;
    private List<Player> mPlayerList;

    public StatisticCalculation(IParingAlgorithm algorithm) {
        mParingAlgorithm = algorithm;
        mPlayerList = mParingAlgorithm.getSortedPlayerList();
    }

    @Override
    public void calculatePMW() {
        for (Player p : mPlayerList) {
            calculatePMW(p);
        }

    }

    @Override
    public void calculateOMW() {
        for (Player p : mPlayerList) {
            calculateOMW(p);
        }
    }

    @Override
    public void calculatePGW() {
        for (Player p : mPlayerList) {
            calculatePGW(p);
        }

    }

    @Override
    public void calculateOGW() {
        for (Player p : mPlayerList) {
            calculateOGW(p);
        }

    }

    @Override
    public void calculateAll() {
        for (Player player : mPlayerList) {
            calculatePMW(player);
            calculatePGW(player);
        }

        for (Player player : mPlayerList) {
            calculateOMW(player);
            calculateOGW(player);
        }
    }

    /**
     * Calculate Player overall match % win
     */
    private void calculatePMW(Player player) {
        float pmw = player.getMatchPoints()
                / (float) (mParingAlgorithm.getCurrentRound() * BaseConfig.MAX_MATCH);

        if (pmw < BaseConfig.MIN_OVERALL_VALUE) {
            pmw = BaseConfig.MIN_OVERALL_VALUE;
        } else if (pmw > BaseConfig.MAX_OVERALL_VALUE) {
            pmw = BaseConfig.MAX_OVERALL_VALUE;
        }
        player.setPlayerMatchOverallWin(pmw);
    }

    /**
     * Calculate Oponents overall match % win based on:
     * https://www.wizards.com/dci/downloads/tiebreakers.pdf
     *
     * @param player
     */
    private void calculateOMW(Player player) {
        float omw = 0f;
        for (Game g : player.getPlayedGame()) {
            if (g.getPlayerNameA().equals(player.getPlayerName())) {
                omw += findPlayer(g.getPlayerNameB())
                        .getPlayerMatchOverallWin();
            } else {
                omw += findPlayer(g.getPlayerNameA())
                        .getPlayerMatchOverallWin();

            }

        }
        // in case of bye
        if (omw != 0) {
            omw /= (float) player.getPlayedGame().size();
        }

        player.setOponentsMatchOveralWins(omw);
    }

    private void calculatePGW(Player player) {
        float pgw = 0f;
        int gamesPlayed = gamesPlayed(player);
        // in case of bye
        if (!(gamesPlayed == 0)) {
            pgw = player.getGamePoints()
                    / ((float) (gamesPlayed * BaseConfig.MAX_MATCH));
            if (pgw < BaseConfig.MIN_OVERALL_VALUE) {
                pgw = BaseConfig.MIN_OVERALL_VALUE;
            } else if (pgw > BaseConfig.MAX_OVERALL_VALUE) {
                pgw = BaseConfig.MAX_OVERALL_VALUE;
            }
        }
        player.setPlayerGamesOverallWin(pgw);
    }

    private void calculateOGW(Player player) {
        float ogw = 0f;
        for (Game g : player.getPlayedGame()) {
            if (!g.getPlayerNameA().equals(player.getPlayerName())) {
                ogw += findPlayer(g.getPlayerNameA())
                        .getPlayerGamesOverallWin();
            } else {
                ogw += findPlayer(g.getPlayerNameB())
                        .getPlayerGamesOverallWin();
            }

        }
        // in case of bye
        if (ogw != 0) {
            ogw /= (float) player.getPlayedGame().size();
        }
        player.setOponentsGamesOverallWin(ogw);
    }

    /**
     * Find player by name
     *
     * @param name
     * @return
     */
    private Player findPlayer(String name) {
        for (Player p : mPlayerList) {
            if (p.getPlayerName().equals(name)) {
                return p;
            }
        }
        return null;

    }

    private int gamesPlayed(Player player) {
        int gamesPlayed = 0;
        for (Game g : player.getPlayedGame()) {
            gamesPlayed += g.getGamesPlayed();
        }
        return gamesPlayed;
    }


}
