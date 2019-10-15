package com.kurtmustafa.countryselector.di;

import android.content.Context;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.di.qualifiers.ApplicationContext;
import com.kurtmustafa.countryselector.repositories.CountryJSONRepository;
import com.kurtmustafa.countryselector.repositories.CountryRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CountryJSONRepositoryModule
    {

        @Provides
        @Singleton
        public CountryRepository provideCountryRepository(@ApplicationContext Context context, @Named("CountriesJSONResource") Integer jsonResourceID)
            {
                return new CountryJSONRepository(context, jsonResourceID);
            }

        @Provides
        @Named("CountriesJSONResource")
        Integer provideJsonResourceID()
            {
                return R.raw.countries;
            }



    }
