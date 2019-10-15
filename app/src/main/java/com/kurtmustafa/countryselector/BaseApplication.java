package com.kurtmustafa.countryselector;

import com.kurtmustafa.countryselector.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class BaseApplication extends DaggerApplication
    {

        @Override
        public void onCreate()
            {
                super.onCreate();

                //Show logs only in debug mode.
                if (BuildConfig.DEBUG)
                    {
                        Timber.plant(new Timber.DebugTree());
                    }
            }

        /**
         * Inject application component
         */
        @Override
        protected AndroidInjector<? extends DaggerApplication> applicationInjector()
            {
                return DaggerAppComponent.builder().application(this).build();
                //return null;
            }



    }