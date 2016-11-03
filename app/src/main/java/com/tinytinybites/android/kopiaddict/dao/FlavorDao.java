package com.tinytinybites.android.kopiaddict.dao;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.model.Flavor;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bundee on 11/2/16.
 */
public class FlavorDao implements Dao{
    //Tag
    protected final static String TAG = FlavorDao.class.getSimpleName();

    //Variables
    private Realm mRealm;
    private static FlavorDao _instance;

    /**
     * Constructor
     */
    protected FlavorDao(){
        this.mRealm = Realm.getDefaultInstance();
    }

    /**
     * Get Instance
     * @return
     */
    public static FlavorDao getInstance(){
        if(_instance == null){
            _instance = new FlavorDao();
        }
        return _instance;
    }

    /**
     * Save single flavor object in async mode
     * @param flavor
     */
    public void save(final Flavor flavor){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(flavor);
            }
        });
    }

    /**
     * Save a list of flavor objects in async mode
     * @param flavors
     * @param asyncMode
     */
    public void save(final List<Flavor> flavors, boolean asyncMode){
        if(asyncMode) {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(flavors);
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(flavors);
                }
            });
        }
    }

    /**
     * Load all flavor in sync mode
     * @return
     */
    public RealmResults<Flavor> loadAll() {
        return mRealm.where(Flavor.class).findAllSorted(Flavor.KEY_ID);
    }

    /**
     * Load all flavors in async mode
     * @return
     */
    public RealmResults<Flavor> loadAllAsync() {
        return mRealm.where(Flavor.class).findAllSortedAsync(Flavor.KEY_ID);
    }

    /**
     * Get Pre-populate flavors
     */
    public static List<Flavor> GetPrepopulateFlavors(){
        ArrayList<Flavor> flavors = new ArrayList<>();
        flavors.add(new Flavor(Flavor.TYPE_KOPI));
        flavors.add(new Flavor(Flavor.TYPE_TEH));
        flavors.add(new Flavor(Flavor.TYPE_YUAN_YANG));
        return flavors;
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
        return mRealm.where(Flavor.class).count();
    }
}
