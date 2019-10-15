package com.kurtmustafa.countryselector.di;

import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.requests.RestCountriesServiceGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CountryDetailsRepositoryModule
    {
        @Singleton
        @Provides
        CountryDetailsRepository provideCountryDetailsRepository(RestCountriesServiceGenerator restCountriesServiceGenerator)
            {
                return new CountryDetailsRepository(restCountriesServiceGenerator);
            }
    }
