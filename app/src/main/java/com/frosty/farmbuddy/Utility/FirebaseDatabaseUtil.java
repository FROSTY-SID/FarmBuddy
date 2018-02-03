package com.frosty.farmbuddy.Utility;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by siddh on 24-01-2018.
 */

public class FirebaseDatabaseUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getFirebaseDatabaseInstance(){
        if(mDatabase==null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
