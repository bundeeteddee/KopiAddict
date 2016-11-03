package com.tinytinybites.android.kopiaddict.dao;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.model.SweetenerLevel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bundee on 11/2/16.
 */
public class SweetenerLevelDao implements Dao{
    //Tag
    protected final static String TAG = SweetenerLevelDao.class.getSimpleName();

    //Variables
    private Realm mRealm;
    private static SweetenerLevelDao _instance;

    /**
     * Constructor
     */
    protected SweetenerLevelDao(){
        this.mRealm = Realm.getDefaultInstance();
    }

    /**
     * Get instance
     * @return
     */
    public static SweetenerLevelDao getInstance(){
        if(_instance == null){
            _instance = new SweetenerLevelDao();
        }
        return _instance;
    }

    /**
     * Save single sweetenerLevel object in async mode
     * @param sweetenerLevel
     */
    public void save(final SweetenerLevel sweetenerLevel){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(sweetenerLevel);
            }
        });
    }

    /**
     * Save a list of sweetenerLevel objects in async mode
     * @param sweetenerLevels
     * @param asyncMode
     */
    public void save(final List<SweetenerLevel> sweetenerLevels, boolean asyncMode){
        if(asyncMode) {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(sweetenerLevels);
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(sweetenerLevels);
                }
            });
        }
    }

    /**
     * Load all sweetenerLevel in sync mode
     * @return
     */
    public RealmResults<SweetenerLevel> loadAll() {
        return mRealm.where(SweetenerLevel.class).findAllSorted(SweetenerLevel.KEY_ID);
    }

    /**
     * Load all sweetenerLevel in async mode
     * @return
     */
    public RealmResults<SweetenerLevel> loadAllAsync() {
        return mRealm.where(SweetenerLevel.class).findAllSortedAsync(SweetenerLevel.KEY_ID);
    }

    /**
     * Get Pre-populate sweetener levels
     */
    public static List<SweetenerLevel> GetPrepopulateSweetenerLevels(){
        ArrayList<SweetenerLevel> sweetenerLevels = new ArrayList<>();
        sweetenerLevels.add(new SweetenerLevel(SweetenerLevel.TYPE_0));
        sweetenerLevels.add(new SweetenerLevel(SweetenerLevel.TYPE_25));
        sweetenerLevels.add(new SweetenerLevel(SweetenerLevel.TYPE_50));
        sweetenerLevels.add(new SweetenerLevel(SweetenerLevel.TYPE_100));
        sweetenerLevels.add(new SweetenerLevel(SweetenerLevel.TYPE_150));

        return sweetenerLevels;
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
        return mRealm.where(SweetenerLevel.class).count();
    }
}
