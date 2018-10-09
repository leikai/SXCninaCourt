package org.sxchinacourt.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MSGLEAVEDB".
*/
public class MsgleavedbDao extends AbstractDao<Msgleavedb, Long> {

    public static final String TABLENAME = "MSGLEAVEDB";

    /**
     * Properties of entity Msgleavedb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Applicant = new Property(1, String.class, "applicant", false, "APPLICANT");
        public final static Property ApplicationData = new Property(2, String.class, "applicationData", false, "APPLICATIONDATA");
        public final static Property LeaveType = new Property(3, String.class, "leaveType", false, "LEAVETYPE");
        public final static Property LeaveData = new Property(4, String.class, "leaveData", false, "LEAVEDATA");
        public final static Property LeaveDays = new Property(5, String.class, "leaveDays", false, "LEAVEDAYS");
        public final static Property LeaveReasons = new Property(6, String.class, "leaveReasons", false, "LEAVEREASONS");
    }


    public MsgleavedbDao(DaoConfig config) {
        super(config);
    }
    
    public MsgleavedbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MSGLEAVEDB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"APPLICANT\" TEXT," + // 1: applicant
                "\"APPLICATIONDATA\" TEXT," + // 2: applicationData
                "\"LEAVETYPE\" TEXT," + // 3: leaveType
                "\"LEAVEDATA\" TEXT," + // 4: leaveData
                "\"LEAVEDAYS\" TEXT," + // 5: leaveDays
                "\"LEAVEREASONS\" TEXT);"); // 6: leaveReasons
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MSGLEAVEDB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Msgleavedb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String applicant = entity.getApplicant();
        if (applicant != null) {
            stmt.bindString(2, applicant);
        }
 
        String applicationData = entity.getApplicationData();
        if (applicationData != null) {
            stmt.bindString(3, applicationData);
        }
 
        String leaveType = entity.getLeaveType();
        if (leaveType != null) {
            stmt.bindString(4, leaveType);
        }
 
        String leaveData = entity.getLeaveData();
        if (leaveData != null) {
            stmt.bindString(5, leaveData);
        }
 
        String leaveDays = entity.getLeaveDays();
        if (leaveDays != null) {
            stmt.bindString(6, leaveDays);
        }
 
        String leaveReasons = entity.getLeaveReasons();
        if (leaveReasons != null) {
            stmt.bindString(7, leaveReasons);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Msgleavedb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String applicant = entity.getApplicant();
        if (applicant != null) {
            stmt.bindString(2, applicant);
        }
 
        String applicationData = entity.getApplicationData();
        if (applicationData != null) {
            stmt.bindString(3, applicationData);
        }
 
        String leaveType = entity.getLeaveType();
        if (leaveType != null) {
            stmt.bindString(4, leaveType);
        }
 
        String leaveData = entity.getLeaveData();
        if (leaveData != null) {
            stmt.bindString(5, leaveData);
        }
 
        String leaveDays = entity.getLeaveDays();
        if (leaveDays != null) {
            stmt.bindString(6, leaveDays);
        }
 
        String leaveReasons = entity.getLeaveReasons();
        if (leaveReasons != null) {
            stmt.bindString(7, leaveReasons);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Msgleavedb readEntity(Cursor cursor, int offset) {
        Msgleavedb entity = new Msgleavedb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // applicant
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // applicationData
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // leaveType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // leaveData
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // leaveDays
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // leaveReasons
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Msgleavedb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setApplicant(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setApplicationData(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLeaveType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLeaveData(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLeaveDays(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLeaveReasons(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Msgleavedb entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Msgleavedb entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Msgleavedb entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
