package org.sxchinacourt.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import org.sxchinacourt.dao.Msgleavedb;
import org.sxchinacourt.dao.Msgmachinedb;

import org.sxchinacourt.dao.MsgleavedbDao;
import org.sxchinacourt.dao.MsgmachinedbDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig msgleavedbDaoConfig;
    private final DaoConfig msgmachinedbDaoConfig;

    private final MsgleavedbDao msgleavedbDao;
    private final MsgmachinedbDao msgmachinedbDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        msgleavedbDaoConfig = daoConfigMap.get(MsgleavedbDao.class).clone();
        msgleavedbDaoConfig.initIdentityScope(type);

        msgmachinedbDaoConfig = daoConfigMap.get(MsgmachinedbDao.class).clone();
        msgmachinedbDaoConfig.initIdentityScope(type);

        msgleavedbDao = new MsgleavedbDao(msgleavedbDaoConfig, this);
        msgmachinedbDao = new MsgmachinedbDao(msgmachinedbDaoConfig, this);

        registerDao(Msgleavedb.class, msgleavedbDao);
        registerDao(Msgmachinedb.class, msgmachinedbDao);
    }
    
    public void clear() {
        msgleavedbDaoConfig.clearIdentityScope();
        msgmachinedbDaoConfig.clearIdentityScope();
    }

    public MsgleavedbDao getMsgleavedbDao() {
        return msgleavedbDao;
    }

    public MsgmachinedbDao getMsgmachinedbDao() {
        return msgmachinedbDao;
    }

}
