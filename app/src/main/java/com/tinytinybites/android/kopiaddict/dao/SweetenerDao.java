package com.tinytinybites.android.kopiaddict.dao;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.model.Sweetener;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bundee on 11/2/16.
 */
public class SweetenerDao implements Dao{
    //Tag
    protected final static String TAG = SweetenerDao.class.getSimpleName();

    //Variables
    private Realm mRealm;
    private static SweetenerDao _instance;

    /**
     * Constructor
     */
    protected SweetenerDao(){
        this.mRealm = Realm.getDefaultInstance();
    }

    /**
     * Get instance
     * @return
     */
    public static SweetenerDao getInstance(){
        if(_instance == null){
            _instance = new SweetenerDao();
        }
        return _instance;
    }

    /**
     * Save single sweetener object in async mode
     * @param sweetener
     */
    public void save(final Sweetener sweetener){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(sweetener);
            }
        });
    }

    /**
     * Save a list of sweetener objects in async mode
     * @param sweeteners
     * @param asyncMode
     */
    public void save(final List<Sweetener> sweeteners, boolean asyncMode){
        if(asyncMode) {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(sweeteners);
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(sweeteners);
                }
            });
        }
    }

    /**
     * Load all Sweetener in sync mode
     * @return
     */
    public RealmResults<Sweetener> loadAll() {
        return mRealm.where(Sweetener.class).findAllSorted(Sweetener.KEY_ID);
    }

    /**
     * Load all Sweetener in async mode
     * @return
     */
    public RealmResults<Sweetener> loadAllAsync() {
        return mRealm.where(Sweetener.class).findAllSortedAsync(Sweetener.KEY_ID);
    }

    /**
     * Get Pre-populate Sweeteners
     */
    public static List<Sweetener> GetPrepopulateSweeteners(){
        ArrayList<Sweetener> sweeteners = new ArrayList<>();
        sweeteners.add(new Sweetener(Sweetener.TYPE_CONDENSED_MILK));
        sweeteners.add(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK));
        sweeteners.add(new Sweetener(Sweetener.TYPE_PALM_SUGAR));
        return sweeteners;
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
        return mRealm.where(Sweetener.class).count();
    }
}
