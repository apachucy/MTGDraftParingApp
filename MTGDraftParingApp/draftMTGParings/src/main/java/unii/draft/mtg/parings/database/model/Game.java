package unii.draft.mtg.parings.database.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "GAME".
 */
public class Game {

    private Long id;
    private String Winner;
    private Integer Games;
    private Integer Round;
    private Integer Draws;
    private Integer PlayerAPoints;
    private Integer PlayerBPoints;
    private Long PlayerAGameJoinTableId;
    private Long PlayerBGameJoinTableId;
    private Long DraftGameJoinTableId;

    public Game() {
    }

    public Game(Long id) {
        this.id = id;
    }

    public Game(Long id, String Winner, Integer Games, Integer Round, Integer Draws, Integer PlayerAPoints, Integer PlayerBPoints, Long PlayerAGameJoinTableId, Long PlayerBGameJoinTableId, Long DraftGameJoinTableId) {
        this.id = id;
        this.Winner = Winner;
        this.Games = Games;
        this.Round = Round;
        this.Draws = Draws;
        this.PlayerAPoints = PlayerAPoints;
        this.PlayerBPoints = PlayerBPoints;
        this.PlayerAGameJoinTableId = PlayerAGameJoinTableId;
        this.PlayerBGameJoinTableId = PlayerBGameJoinTableId;
        this.DraftGameJoinTableId = DraftGameJoinTableId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String Winner) {
        this.Winner = Winner;
    }

    public Integer getGames() {
        return Games;
    }

    public void setGames(Integer Games) {
        this.Games = Games;
    }

    public Integer getRound() {
        return Round;
    }

    public void setRound(Integer Round) {
        this.Round = Round;
    }

    public Integer getDraws() {
        return Draws;
    }

    public void setDraws(Integer Draws) {
        this.Draws = Draws;
    }

    public Integer getPlayerAPoints() {
        return PlayerAPoints;
    }

    public void setPlayerAPoints(Integer PlayerAPoints) {
        this.PlayerAPoints = PlayerAPoints;
    }

    public Integer getPlayerBPoints() {
        return PlayerBPoints;
    }

    public void setPlayerBPoints(Integer PlayerBPoints) {
        this.PlayerBPoints = PlayerBPoints;
    }

    public Long getPlayerAGameJoinTableId() {
        return PlayerAGameJoinTableId;
    }

    public void setPlayerAGameJoinTableId(Long PlayerAGameJoinTableId) {
        this.PlayerAGameJoinTableId = PlayerAGameJoinTableId;
    }

    public Long getPlayerBGameJoinTableId() {
        return PlayerBGameJoinTableId;
    }

    public void setPlayerBGameJoinTableId(Long PlayerBGameJoinTableId) {
        this.PlayerBGameJoinTableId = PlayerBGameJoinTableId;
    }

    public Long getDraftGameJoinTableId() {
        return DraftGameJoinTableId;
    }

    public void setDraftGameJoinTableId(Long DraftGameJoinTableId) {
        this.DraftGameJoinTableId = DraftGameJoinTableId;
    }

}