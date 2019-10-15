package com.kurtmustafa.countryselector.repositories;

import com.kurtmustafa.countryselector.models.Country;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Flexible repository type in case if I want to change CountryRepository to something else other than the current (JSON) one.
 */
public interface CountryRepository
    {
         LiveData<List<Country>> getCountryList();
    }
