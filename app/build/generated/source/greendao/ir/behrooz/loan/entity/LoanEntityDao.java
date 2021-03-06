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
 * DAO for table "Loan".
*/
public class LoanEntityDao extends AbstractDao<LoanEntity, Long> {

    public static final String TABLENAME = "Loan";

    /**
     * Properties of entity LoanEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property PersonId = new Property(2, Long.class, "personId", false, "PERSON_ID");
        public final static Property DayInMonth = new Property(3, int.class, "dayInMonth", false, "DAY_IN_MONTH");
        public final static Property Date = new Property(4, java.util.Date.class, "date", false, "DATE");
        public final static Property Value = new Property(5, Long.class, "value", false, "VALUE");
        public final static Property Installment = new Property(6, int.class, "installment", false, "INSTALLMENT");
        public final static Property InstallmentAmount = new Property(7, Long.class, "installmentAmount", false, "INSTALLMENT_AMOUNT");
        public final static Property Settled = new Property(8, boolean.class, "settled", false, "SETTLED");
        public final static Property WinDate = new Property(9, java.util.Date.class, "winDate", false, "WIN_DATE");
        public final static Property CashId = new Property(10, Long.class, "cashId", false, "CASH_ID");
    }


    public LoanEntityDao(DaoConfig config) {
        super(config);
    }
    
    public LoanEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"Loan\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"PERSON_ID\" INTEGER," + // 2: personId
                "\"DAY_IN_MONTH\" INTEGER NOT NULL ," + // 3: dayInMonth
                "\"DATE\" INTEGER," + // 4: date
                "\"VALUE\" INTEGER," + // 5: value
                "\"INSTALLMENT\" INTEGER NOT NULL ," + // 6: installment
                "\"INSTALLMENT_AMOUNT\" INTEGER," + // 7: installmentAmount
                "\"SETTLED\" INTEGER NOT NULL ," + // 8: settled
                "\"WIN_DATE\" INTEGER," + // 9: winDate
                "\"CASH_ID\" INTEGER);"); // 10: cashId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"Loan\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LoanEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long personId = entity.getPersonId();
        if (personId != null) {
            stmt.bindLong(3, personId);
        }
        stmt.bindLong(4, entity.getDayInMonth());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(5, date.getTime());
        }
 
        Long value = entity.getValue();
        if (value != null) {
            stmt.bindLong(6, value);
        }
        stmt.bindLong(7, entity.getInstallment());
 
        Long installmentAmount = entity.getInstallmentAmount();
        if (installmentAmount != null) {
            stmt.bindLong(8, installmentAmount);
        }
        stmt.bindLong(9, entity.getSettled() ? 1L: 0L);
 
        java.util.Date winDate = entity.getWinDate();
        if (winDate != null) {
            stmt.bindLong(10, winDate.getTime());
        }
 
        Long cashId = entity.getCashId();
        if (cashId != null) {
            stmt.bindLong(11, cashId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LoanEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long personId = entity.getPersonId();
        if (personId != null) {
            stmt.bindLong(3, personId);
        }
        stmt.bindLong(4, entity.getDayInMonth());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(5, date.getTime());
        }
 
        Long value = entity.getValue();
        if (value != null) {
            stmt.bindLong(6, value);
        }
        stmt.bindLong(7, entity.getInstallment());
 
        Long installmentAmount = entity.getInstallmentAmount();
        if (installmentAmount != null) {
            stmt.bindLong(8, installmentAmount);
        }
        stmt.bindLong(9, entity.getSettled() ? 1L: 0L);
 
        java.util.Date winDate = entity.getWinDate();
        if (winDate != null) {
            stmt.bindLong(10, winDate.getTime());
        }
 
        Long cashId = entity.getCashId();
        if (cashId != null) {
            stmt.bindLong(11, cashId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LoanEntity readEntity(Cursor cursor, int offset) {
        LoanEntity entity = new LoanEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // personId
            cursor.getInt(offset + 3), // dayInMonth
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // date
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // value
            cursor.getInt(offset + 6), // installment
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // installmentAmount
            cursor.getShort(offset + 8) != 0, // settled
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // winDate
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10) // cashId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LoanEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPersonId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setDayInMonth(cursor.getInt(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setValue(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setInstallment(cursor.getInt(offset + 6));
        entity.setInstallmentAmount(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setSettled(cursor.getShort(offset + 8) != 0);
        entity.setWinDate(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setCashId(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LoanEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LoanEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LoanEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
