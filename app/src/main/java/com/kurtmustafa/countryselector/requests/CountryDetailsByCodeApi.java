package com.kurtmustafa.countryselector.requests;

import com.kurtmustafa.countryselector.models.CountryDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CountryDetailsByCodeApi
    {


        @Headers("Content-Type:application/json")
        @GET("alpha/{country_code}")
        Call<CountryDetails> getCountryDetails(@Path("country_code") String country_code);
    }
