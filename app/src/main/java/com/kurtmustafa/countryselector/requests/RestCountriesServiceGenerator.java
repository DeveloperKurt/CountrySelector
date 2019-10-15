package com.kurtmustafa.countryselector.requests;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Controller for retrofit country related api clients
 */


public class RestCountriesServiceGenerator
    {
        private Retrofit retrofit;


        /**
         * baseURL is injected in order to be able to easily mock the server
         */
        @Inject
        public RestCountriesServiceGenerator(@Named("RestCountriesBaseURL") String baseURL)
            {
                Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                        .baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create());

                retrofit = retrofitBuilder.build();
            }


        public CountryDetailsByCodeApi getCountryDetailsApi()
            {
                return retrofit.create(CountryDetailsByCodeApi.class);
            }


    }
