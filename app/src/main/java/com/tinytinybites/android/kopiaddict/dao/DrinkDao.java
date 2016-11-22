package com.tinytinybites.android.kopiaddict.dao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.android.kopiaddict.model.ConcentrationLevel;
import com.tinytinybites.android.kopiaddict.model.Drink;
import com.tinytinybites.android.kopiaddict.model.Flavor;
import com.tinytinybites.android.kopiaddict.model.Sweetener;
import com.tinytinybites.android.kopiaddict.model.SweetenerLevel;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by bundee on 11/2/16.
 */
public class DrinkDao implements Dao{
    //Tag
    protected final static String TAG = DrinkDao.class.getSimpleName();

    //Variables
    private Realm mRealm;
    private static DrinkDao _instance;

    /**
     * Protected Constructor
     */
    protected DrinkDao(){
        this.mRealm = Realm.getDefaultInstance();
    }

    /**
     * Get Instance
     * @return
     */
    public static DrinkDao getInstance(){
        if(_instance == null){
            _instance = new DrinkDao();
        }
        return _instance;
    }

    /**
     * Save single drink object in async mode
     * @param drink
     */
    public void save(final Drink drink){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(drink);
            }
        });
    }

    /**
     * Save a list of drink objects in async mode
     * @param drinks
     * @param asyncMode
     */
    public void save(final List<Drink> drinks, boolean asyncMode){
        if(asyncMode) {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(drinks);
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(drinks);
                }
            });
        }
    }

    /**
     * Load all drinks in sync mode
     * @return
     */
    public RealmResults<Drink> loadAll() {
        return mRealm.where(Drink.class).findAllSorted(Drink.KEY_ID);
    }

    /**
     * Load all drinks in async mode
     * @return
     */
    public RealmResults<Drink> loadAllAsync() {
        return mRealm.where(Drink.class).findAllAsync();
    }

    /**
     * Load a specific drink given an id
     * @param id
     * @return
     */
    public Drink loadDrink(String id){
        Log.e(TAG, "loadDrink >> " + id);
        Drink result =  mRealm.where(Drink.class).equalTo(Drink.KEY_ID, id).findFirst();
        Log.e(TAG, "loadDrink result: " + result);
        return result;
    }

    public Drink loadDrink(Realm realm, String id){
        Log.e(TAG, "loadDrink >> " + id);
        Drink result =  realm.where(Drink.class).equalTo(Drink.KEY_ID, id).findFirst();
        Log.e(TAG, "loadDrink result: " + result);
        return result;
    }

    /**
     * Pre-populate Drinks in async mode
     */
    public void prepopulateDrinks(){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Populate and enter core data
                List<SweetenerLevel> sweetenerLevels = SweetenerLevelDao.GetPrepopulateSweetenerLevels();
                realm.copyToRealmOrUpdate(sweetenerLevels);

                List<Sweetener> sweeteners = SweetenerDao.GetPrepopulateSweeteners();
                realm.copyToRealmOrUpdate(sweeteners);

                List<Flavor> flavors = FlavorDao.GetPrepopulateFlavors();
                realm.copyToRealmOrUpdate(flavors);

                List<ConcentrationLevel> concentrationLevels = ConcentrationLevelDao.GetPrepopulateConcentrationLevels();
                realm.copyToRealmOrUpdate(concentrationLevels);

                //Generate 5 drinks that are more popular and common
                ArrayList<Drink> drinks = new ArrayList<>();
                drinks.add(MakeKopi());
                drinks.add(MakeKopiC());
                drinks.add(MakeYuanYang());
                drinks.add(MakeKopiPlusCSiewDai());
                drinks.add(MakeTehCPeng());
                realm.copyToRealmOrUpdate(drinks);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //TODO:
                Log.e(TAG, "Wohoooooo");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //TODO:
                Log.e(TAG, "Boooooooooo");
            }
        });
    }

    /**
     * Make a Kopi: coffee + condensed milk + water
     * @return
     */
    public static Drink MakeKopi(){
        Flavor flavor = new Flavor(Flavor.TYPE_KOPI);
        RealmList<Sweetener> sweeteners = new RealmList<>(new Sweetener(Sweetener.TYPE_CONDENSED_MILK));
        SweetenerLevel sweetenerLevel = new SweetenerLevel(SweetenerLevel.TYPE_100);
        ConcentrationLevel concentrationLevel = new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL);

        return new Drink(flavor, sweeteners, sweetenerLevel, concentrationLevel, false, true);
    }

    /**
     * Make a KopiC: coffee + evaporated milk + water (has sugar but no record of it, yet)
     * @return
     */
    public static Drink MakeKopiC(){
        Flavor flavor = new Flavor(Flavor.TYPE_KOPI);
        RealmList<Sweetener> sweeteners = new RealmList<>(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK));
        SweetenerLevel sweetenerLevel = new SweetenerLevel(SweetenerLevel.TYPE_100);
        ConcentrationLevel concentrationLevel = new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL);

        return new Drink(flavor, sweeteners, sweetenerLevel, concentrationLevel, false, true);
    }

    /**
     * Make a Yuan Yang: coffee + tea + evaporated milk + condensed milk
     * @return
     */
    public static Drink MakeYuanYang(){
        Flavor flavor = new Flavor(Flavor.TYPE_YUAN_YANG);
        RealmList<Sweetener> sweeteners = new RealmList<>(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK), new Sweetener(Sweetener.TYPE_CONDENSED_MILK));
        SweetenerLevel sweetenerLevel = new SweetenerLevel(SweetenerLevel.TYPE_100);
        ConcentrationLevel concentrationLevel = new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL);

        return new Drink(flavor, sweeteners, sweetenerLevel, concentrationLevel, false, true);
    }

    /**
     * Make a Yuan Yang: coffee + evaporated milk + condensed milk, less sweet (50%)
     * @return
     */
    public static Drink MakeKopiPlusCSiewDai(){
        Flavor flavor = new Flavor(Flavor.TYPE_KOPI);
        RealmList<Sweetener> sweeteners = new RealmList<>(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK), new Sweetener(Sweetener.TYPE_CONDENSED_MILK));
        SweetenerLevel sweetenerLevel = new SweetenerLevel(SweetenerLevel.TYPE_50);
        ConcentrationLevel concentrationLevel = new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL);

        return new Drink(flavor, sweeteners, sweetenerLevel, concentrationLevel, false, true);
    }

    /**
     * Make a Yuan Yang: tea + evaporated milk, iced
     * @return
     */
    public static Drink MakeTehCPeng(){
        Flavor flavor = new Flavor(Flavor.TYPE_TEH);
        RealmList<Sweetener> sweeteners = new RealmList<>(new Sweetener(Sweetener.TYPE_EVAPORATED_MILK));
        SweetenerLevel sweetenerLevel = new SweetenerLevel(SweetenerLevel.TYPE_100);
        ConcentrationLevel concentrationLevel = new ConcentrationLevel(ConcentrationLevel.TYPE_NORMAL);

        return new Drink(flavor, sweeteners, sweetenerLevel, concentrationLevel, true, true);
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
        return mRealm.where(Drink.class).count();
    }
}
