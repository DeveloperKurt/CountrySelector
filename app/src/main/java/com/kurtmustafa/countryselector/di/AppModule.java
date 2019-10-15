package com.kurtmustafa.countryselector.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.kurtmustafa.countryselector.di.qualifiers.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule
    {

        @Provides
        @ApplicationContext
        @Singleton
        Context provideApplicationContext(Application application)
            {
                return application;
            }


        @Provides
        @Singleton
        Resources provideApplicationResources(Application application)
            {
                return application.getResources();
            }



    }
