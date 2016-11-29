package com.tinytinybites.android.kopiaddict.application;

import android.app.Application;
import android.util.Log;

import com.tinytinybites.android.kopiaddict.dao.DrinkDao;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bundee on 11/1/16.
 */
public class EApplication extends Application{
    //Tag
    protected static final String TAG = EApplication.class.getSimpleName();

    //Variables
    protected static EApplication _instance;
    private boolean mDatabaseInitialized;
    private Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        initDatabaseEngine();

        //Check whether database is initialized. If not, pre-populate required objects
        //TODO: Proper RealmMigration structure has changed
        //TODO: Have db initialization in separate thread
        checkDatabase();
    }

    /**
     * Get application instance
     */
    public static EApplication getInstance(){
        //Note: Application class started when application starts, _instance should never be null.
        return _instance;
    }

    /**
     * Init database engine
     */
    private void initDatabaseEngine(){
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("kopiaddict.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        //Realm.deleteRealm(realmConfig);
        mRealm = Realm.getDefaultInstance();


        //Default flag to indicate check needed
        mDatabaseInitialized = false;
    }

    /**
     * Check to see if database has been populated with data
     */
    private void checkDatabase(){
        Log.e(TAG, "checkDatabase 1 :" + DrinkDao.getInstance().getCount());
        if(DrinkDao.getInstance().getCount() == 0){

            DrinkDao.getInstance().prepopulateDrinks();
        }

        /*Log.e(TAG, "checkDatabase 3 : " + DrinkDao.getInstance().getCount());
        RealmResults<Drink> prepopulatedDrinks = mRealm.where(Drink.class).findAllAsync();
        Log.e(TAG, ">>>> mmmm " + prepopulatedDrinks.size());

        Log.e(TAG, "checkDatabase 4");
        prepopulatedDrinks.addChangeListener(new RealmChangeListener<RealmResults<Drink>>() {
            @Override
            public void onChange(RealmResults<Drink> element) {
                if(element.size() == 0){
                    Log.e(TAG, ">>>> We got nothing :( ");

                }else{
                    Log.e(TAG, ">>>> We prepopulated stuff: " + element.size());

                }
            }
        });*/
    }


}
