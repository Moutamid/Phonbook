package dev.moutamid.sampoorankranti;

import com.google.firebase.database.FirebaseDatabase;

public class ContextSampooran extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //--ADMIN IS FALSE OTHERWISE TRUE

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
