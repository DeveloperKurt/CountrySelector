package com.kurtmustafa.countryselector.di;

import android.content.Context;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.di.qualifiers.ApplicationContext;
import com.kurtmustafa.countryselector.requests.RestCountriesServiceGenerator;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RestCountriesServiceGeneratorModule
    {

        @Singleton
        @Provides
        public RestCountriesServiceGenerator restCountriesServiceGeneratorModule(@Named("RestCountriesBaseURL") String baseUrl)
            {
                return new RestCountriesServiceGenerator(baseUrl);
            }

        @Provides
        @Named("RestCountriesBaseURL")
        String provideRestCountriesBaseURL(@ApplicationContext Context context)
            {
                return context.getString(R.string.base_url_restcountries);
            }
    }
