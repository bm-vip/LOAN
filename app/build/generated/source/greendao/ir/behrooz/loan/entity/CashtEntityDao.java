package ir.behrooz.loan.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Cash".
*/
public class CashtEntityDao extends AbstractDao<CashtEntity, Long> {

    public static final String TABLENAME = "Cash";

    /**
     * Properties of entity CashtEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property CurrencyType = new Property(2, String.class, "currencyType", false, "CURRENCY_TYPE");
        public final static Property WithDeposit = new Property(3, boolean.class, "withDeposit", false, "WITH_DEPOSIT");
        public final static Property CheckCashRemain = new Property(4, boolean.class, "checkCashRemain", false, "CHECK_CASH_REMAIN");
        public final static Property AffectNext = new Property(5, boolean.class, "affectNext", false, "AFFECT_NEXT");
        public final static Property NotifyDayOfLoan = new Property(6, boolean.class, "notifyDayOfLoan", false, "NOTIFY_DAY_OF_LOAN");
    }


    public CashtEntityDao(DaoConfig config) {
        super(config);
    }
    
    public CashtEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"Cash\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"CURRENCY_TYPE\" TEXT," + // 2: currencyType
                "\"WITH_DEPOSIT\" INTEGER NOT NULL ," + // 3: withDeposit
                "\"CHECK_CASH_REMAIN\" INTEGER NOT NULL ," + // 4: checkCashRemain
                "\"AFFECT_NEXT\" INTEGER NOT NULL ," + // 5: affectNext
                "\"NOTIFY_DAY_OF_LOAN\" INTEGER NOT NULL );"); // 6: notifyDayOfLoan
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"Cash\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CashtEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String currencyType = entity.getCurrencyType();
        if (currencyType != null) {
            stmt.bindString(3, currencyType);
        }
        stmt.bindLong(4, entity.getWithDeposit() ? 1L: 0L);
        stmt.bindLong(5, entity.getCheckCashRemain() ? 1L: 0L);
        stmt.bindLong(6, entity.getAffectNext() ? 1L: 0L);
        stmt.bindLong(7, entity.getNotifyDayOfLoan() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CashtEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String currencyType = entity.getCurrencyType();
        if (currencyType != null) {
            stmt.bindString(3, currencyType);
        }
        stmt.bindLong(4, entity.getWithDeposit() ? 1L: 0L);
        stmt.bindLong(5, entity.getCheckCashRemain() ? 1L: 0L);
        stmt.bindLong(6, entity.getAffectNext() ? 1L: 0L);
        stmt.bindLong(7, entity.getNotifyDayOfLoan() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CashtEntity readEntity(Cursor cursor, int offset) {
        CashtEntity entity = new CashtEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // currencyType
            cursor.getShort(offset + 3) != 0, // withDeposit
            cursor.getShort(offset + 4) != 0, // checkCashRemain
            cursor.getShort(offset + 5) != 0, // affectNext
            cursor.getShort(offset + 6) != 0 // notifyDayOfLoan
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CashtEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCurrencyType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWithDeposit(cursor.getShort(offset + 3) != 0);
        entity.setCheckCashRemain(cursor.getShort(offset + 4) != 0);
        entity.setAffectNext(cursor.getShort(offset + 5) != 0);
        entity.setNotifyDayOfLoan(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CashtEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CashtEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CashtEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
