package com.tinytinybites.android.kopiaddict.dao;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.model.ConcentrationLevel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bundee on 11/2/16.
 */
public class ConcentrationLevelDao implements Dao{
    //Tag
    protected final static String TAG = ConcentrationLevelDao.class.getSimpleName();

    //Variables
    private Realm mRealm;
    public static ConcentrationLevelDao _instance;

    /**
     * Constructor
     */
    protected ConcentrationLevelDao(){
        this.mRealm = Realm.getDefaultInstance();
    }

    /**
     * Get instance
     * @return
     */
    public static ConcentrationLevelDao getInstance(){
        if(_instance == null){
            _instance = new ConcentrationLevelDao();
        }
        return _instance;
    }

    /**
     * Save single concentrationlevel object in async mode
     * @param concentrationLevel
     */
    public void save(final ConcentrationLevel concentrationLevel){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(concentrationLevel);
            }
        });
    }

    /**
     * Save a list of concentrationlevel objects in async mode
     * @param concentrationLevels
     * @param asyncMode
     */
    public void save(final List<ConcentrationLevel> concentrationLevels, boolean asyncMode){
        if(asyncMode) {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(concentrationLevels);
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(concentrationLevels);
                }
            });
        }
    }

    /**
     * Load all concentrationLevel in sync mode
     * @return
     */
    public RealmResults<ConcentrationLevel> loadAll() {
        return mRealm.where(ConcentrationLevel.class).findAllSorted(ConcentrationLevel.KEY_ID);
    }

    /**
     * Load all concentrationLevel in async mode
     * @return
     */
    public RealmResults<ConcentrationLevel> loadAllAsync() {
        return mRealm.where(ConcentrationLevel.class).findAllSortedAsync(ConcentrationLevel.KEY_ID);
    }

    /**
     * Get Pre-populate concentration levels
     */
    public static List<ConcentrationLevel> GetPrepopulateConcentrationLevels(){
        ArrayList<ConcentrationLevel> concentrationLevels = new ArrayList<>();
        concentrationLevels.add(new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL));
        concentrationLevels.add(new ConcentrationLevel(ConcentrationLevel.TYPE_WEAKER));
        concentrationLevels.add(new ConcentrationLevel(ConcentrationLevel.TYPE_STRONGER));
        return concentrationLevels;
    }

    @Override
    public void destroy() {
        if(mRealm != null && !mRealm.isClosed()){
            mRealm.close();
            mRealm = null;
        }
        _instance = null;
    }

    @Override
    public long getCount() {
        return mRealm.where(ConcentrationLevel.class).count();
    }
}
