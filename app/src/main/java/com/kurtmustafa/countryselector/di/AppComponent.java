package com.kurtmustafa.countryselector.di;

import android.app.Application;

import com.kurtmustafa.countryselector.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                AppModule.class,
                ActivityBuildersModule.class,
                FragmentBuildersModule.class,
                ViewModelFactoryModule.class,
                CountryJSONRepositoryModule.class,
                RestCountriesServiceGeneratorModule.class,
                CountryDetailsRepositoryModule.class,
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication>
    {


        @Component.Builder
        interface Builder
            {

                @BindsInstance
                Builder application(Application application);

                AppComponent build();
            }
    }
