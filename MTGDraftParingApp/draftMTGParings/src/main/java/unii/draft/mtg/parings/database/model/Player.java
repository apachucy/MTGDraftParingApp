package unii.draft.mtg.parings.database.model;

import unii.draft.mtg.parings.database.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PLAYER".
 */
public class Player {

    private Long id;
    private String PlayerName;
    private Integer PlayerMatchPoints;
    private Float PlayerMatchOverallWin;
    private Float OponentsMatchOveralWins;
    private Float PlayerGamesOverallWin;
    private Float OponentsGamesOverallWin;
    private Boolean Dropped;
    private Long DraftId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PlayerDao myDao;

    private Draft draft;
    private Long draft__resolvedKey;


    public Player() {
    }

    public Player(Long id) {
        this.id = id;
    }

    public Player(Long id, String PlayerName, Integer PlayerMatchPoints, Float PlayerMatchOverallWin, Float OponentsMatchOveralWins, Float PlayerGamesOverallWin, Float OponentsGamesOverallWin, Boolean Dropped, Long DraftId) {
        this.id = id;
        this.PlayerName = PlayerName;
        this.PlayerMatchPoints = PlayerMatchPoints;
        this.PlayerMatchOverallWin = PlayerMatchOverallWin;
        this.OponentsMatchOveralWins = OponentsMatchOveralWins;
        this.PlayerGamesOverallWin = PlayerGamesOverallWin;
        this.OponentsGamesOverallWin = OponentsGamesOverallWin;
        this.Dropped = Dropped;
        this.DraftId = DraftId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlayerDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String PlayerName) {
        this.PlayerName = PlayerName;
    }

    public Integer getPlayerMatchPoints() {
        return PlayerMatchPoints;
    }

    public void setPlayerMatchPoints(Integer PlayerMatchPoints) {
        this.PlayerMatchPoints = PlayerMatchPoints;
    }

    public Float getPlayerMatchOverallWin() {
        return PlayerMatchOverallWin;
    }

    public void setPlayerMatchOverallWin(Float PlayerMatchOverallWin) {
        this.PlayerMatchOverallWin = PlayerMatchOverallWin;
    }

    public Float getOponentsMatchOveralWins() {
        return OponentsMatchOveralWins;
    }

    public void setOponentsMatchOveralWins(Float OponentsMatchOveralWins) {
        this.OponentsMatchOveralWins = OponentsMatchOveralWins;
    }

    public Float getPlayerGamesOverallWin() {
        return PlayerGamesOverallWin;
    }

    public void setPlayerGamesOverallWin(Float PlayerGamesOverallWin) {
        this.PlayerGamesOverallWin = PlayerGamesOverallWin;
    }

    public Float getOponentsGamesOverallWin() {
        return OponentsGamesOverallWin;
    }

    public void setOponentsGamesOverallWin(Float OponentsGamesOverallWin) {
        this.OponentsGamesOverallWin = OponentsGamesOverallWin;
    }

    public Boolean getDropped() {
        return Dropped;
    }

    public void setDropped(Boolean Dropped) {
        this.Dropped = Dropped;
    }

    public Long getDraftId() {
        return DraftId;
    }

    public void setDraftId(Long DraftId) {
        this.DraftId = DraftId;
    }

    /** To-one relationship, resolved on first access. */
    public Draft getDraft() {
        Long __key = this.DraftId;
        if (draft__resolvedKey == null || !draft__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DraftDao targetDao = daoSession.getDraftDao();
            Draft draftNew = targetDao.load(__key);
            synchronized (this) {
                draft = draftNew;
            	draft__resolvedKey = __key;
            }
        }
        return draft;
    }

    public void setDraft(Draft draft) {
        synchronized (this) {
            this.draft = draft;
            DraftId = draft == null ? null : draft.getId();
            draft__resolvedKey = DraftId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
