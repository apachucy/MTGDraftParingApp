package unii.draft.mtg.parings.database.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import unii.draft.mtg.parings.database.model.Draft;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DRAFT".
*/
public class DraftDao extends AbstractDao<Draft, Long> {

    public static final String TABLENAME = "DRAFT";

    /**
     * Properties of entity Draft.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DraftName = new Property(1, String.class, "DraftName", false, "DRAFT_NAME");
        public final static Property DraftDate = new Property(2, String.class, "DraftDate", false, "DRAFT_DATE");
        public final static Property DraftRounds = new Property(3, Integer.class, "DraftRounds", false, "DRAFT_ROUNDS");
        public final static Property NumberOfPlayers = new Property(4, Integer.class, "NumberOfPlayers", false, "NUMBER_OF_PLAYERS");
    };

    private DaoSession daoSession;


    public DraftDao(DaoConfig config) {
        super(config);
    }
    
    public DraftDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DRAFT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DRAFT_NAME\" TEXT," + // 1: DraftName
                "\"DRAFT_DATE\" TEXT," + // 2: DraftDate
                "\"DRAFT_ROUNDS\" INTEGER," + // 3: DraftRounds
                "\"NUMBER_OF_PLAYERS\" INTEGER);"); // 4: NumberOfPlayers
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DRAFT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Draft entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String DraftName = entity.getDraftName();
        if (DraftName != null) {
            stmt.bindString(2, DraftName);
        }
 
        String DraftDate = entity.getDraftDate();
        if (DraftDate != null) {
            stmt.bindString(3, DraftDate);
        }
 
        Integer DraftRounds = entity.getDraftRounds();
        if (DraftRounds != null) {
            stmt.bindLong(4, DraftRounds);
        }
 
        Integer NumberOfPlayers = entity.getNumberOfPlayers();
        if (NumberOfPlayers != null) {
            stmt.bindLong(5, NumberOfPlayers);
        }
    }

    @Override
    protected void attachEntity(Draft entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Draft readEntity(Cursor cursor, int offset) {
        Draft entity = new Draft( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // DraftName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // DraftDate
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // DraftRounds
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // NumberOfPlayers
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Draft entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDraftName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDraftDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDraftRounds(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setNumberOfPlayers(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Draft entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Draft entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }

}
