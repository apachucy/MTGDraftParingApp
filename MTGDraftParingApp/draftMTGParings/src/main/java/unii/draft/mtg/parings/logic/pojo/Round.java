package unii.draft.mtg.parings.logic.pojo;


import java.util.List;

public class Round {
    private final int number;
    private final List<Game> gameList;

    public Round(int number, List<Game> gameList) {
        this.number = number;
        this.gameList = gameList;
    }

    public int getNumber() {
        return number;
    }

    public List<Game> getGameList() {
        return gameList;
    }
}
