package com.daffodil.assignment;

import android.app.Application;

import com.daffodil.assignment.network.AppModule;

public class App extends Application {

    private static AppModule appModule;

    @Override
    public void onCreate() {
        super.onCreate();

        if(appModule == null){
            appModule = new AppModule();
        }

    }

    public AppModule getAppModule(){
        return appModule;
    }
}
