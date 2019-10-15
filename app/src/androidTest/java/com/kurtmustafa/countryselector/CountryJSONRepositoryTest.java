package com.kurtmustafa.countryselector;


import android.content.Context;

import com.kurtmustafa.countryselector.consts.Constants;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.repositories.CountryJSONRepository;
import com.kurtmustafa.countryselector.utils.CountrySorter;
import com.kurtmustafa.countryselector.utils.JSONResourceReader;
import com.kurtmustafa.countryselector.utils.testutils.LiveDataTestUtil;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Note: This integration test is depended on {@link JSONResourceReader} and a JSON file that contains list of countries in the following format:
 *
 * {"country code": "country name","country code2": "country name2"  }
 */
@MediumTest //Uses local resources and other modules (without mocking them)
public class CountryJSONRepositoryTest
    {

        @Rule //This rule is needed to be able to use the LiveData
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

        private static CountryJSONRepository countryJSONRepository;



        @BeforeClass
        public static void setUp()
            {
                //Given
                Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
                countryJSONRepository = new CountryJSONRepository(context,  R.raw.countries);
            }


        @Test
        public void CountriesDoesntHaveEmptyFields() throws InterruptedException
            {
                //When
                List<Country> countryList = LiveDataTestUtil.getOrAwaitValue(countryJSONRepository.getCountryList(),5);

                for (Country country: countryList)
                    {
                        //Then
                        assertNotEquals("Country name cannot be null! Null in: " + country,null,country.getName());
                        assertNotEquals("Country flag cannot be null! Null in: " + country,null,country.getFlag());
                        assertNotEquals("Country code cannot be null! Null in: " + country,null,country.getCode());
                    }
            }


        /**
         * Verifies the amount of retrieved countries by comparing to INCLUDED_COUNTRIES.
         */
        @Test
        public void allCountriesRetrieved() throws InterruptedException
            {

                //When
                List<Country> countryList = LiveDataTestUtil.getOrAwaitValue(countryJSONRepository.getCountryList(),5);


                //Then
                assertEquals("Missing countries!",Constants.INCLUDED_COUNTRIES, countryList.size());

            }

        @Test
        public void countriesAlphabeticallySorted() throws InterruptedException
            {

                //When

                List<Country> countryList = LiveDataTestUtil.getOrAwaitValue(countryJSONRepository.getCountryList(),5);

                // Copies all of the elements from to list sorted
                List<Country> sortedCountryList = new ArrayList<>(countryList);

                CountrySorter.sortAlphabetically(sortedCountryList);

                //Then
                assertEquals(sortedCountryList, countryList);

            }



    }