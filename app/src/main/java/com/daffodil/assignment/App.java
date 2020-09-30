package com.daffodil.assignment;

import android.app.Application;

import com.daffodil.assignment.network.AppModule;
import com.google.android.gms.maps.MapsInitializer;

public class App extends Application {

    private static AppModule appModule;

    @Override
    public void onCreate() {
        super.onCreate();

        MapsInitializer.initialize(this);

        if(appModule == null){
            appModule = new AppModule();
        }

    }

    public AppModule getAppModule(){
        return appModule;
    }
}
