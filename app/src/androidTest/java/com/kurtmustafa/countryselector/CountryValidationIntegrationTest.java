package com.kurtmustafa.countryselector;

import android.content.Context;

import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.repositories.CountryJSONRepository;
import com.kurtmustafa.countryselector.requests.RestCountriesServiceGenerator;
import com.kurtmustafa.countryselector.utils.testutils.LiveDataTestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

/**
 * This class includes tests that validates the following modules and resources can work together and they deliver the desired results:<br><br>
 * countries.json, {@link CountryJSONRepository}, {@link CountryDetailsRepository},
 * {@link RestCountriesServiceGenerator} and country rest server.
 *
 * <br><br>
 * This test exposed that following countries were actually could not be found on the server:<br><br>
 * <p>
 * England - GB-ENG <br>
 * Europe - EU <br>
 * Netherlands Antilles - AN <br>
 * Northern Ireland - GB-NIR <br>
 * Scotland - GB-SCT <br>
 * Wales - GB-WLS <br>
 */

@LargeTest
public class CountryValidationIntegrationTest
    {

        @Rule //This rule is needed to be able to use the LiveData
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


        private CountryDetailsRepository countryDetailsRepository;
        private CountryJSONRepository countryJSONRepository;


        @Before
        public void setUp()
            {

                Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

                countryJSONRepository = new CountryJSONRepository(context, R.raw.countries);

                RestCountriesServiceGenerator restCountriesServiceGenerator = new RestCountriesServiceGenerator(context.getString(R.string.base_url_restcountries));

                countryDetailsRepository = new CountryDetailsRepository(restCountriesServiceGenerator);
            }

        /**
         * Gets the details of every country in countries.json and validates that they can be found on the server.
         */
        @Test
        public void countriesCanBeFoundOnServer() throws InterruptedException
            {
                final int COUNTRY_JSON_REPO_TIMEOUT_SECONDS = 3;
                final int COUNTRY_DETAIL_TIMEOUT_SECONDS_FOR_EACH = 4;
                List<String> countriesThatCannotBeFound = new ArrayList<>();

                //Given
                List<Country> localCountryList = LiveDataTestUtil.getOrAwaitValue(countryJSONRepository.getCountryList(), COUNTRY_JSON_REPO_TIMEOUT_SECONDS);



                for (Country country : localCountryList)
                    {

                        countryDetailsRepository.reinitCountryDetailsLiveData();//Clear the old data to prevent retrieval of false data. (Read the method documentation)
                        countryDetailsRepository.loadNewCountryDetails(country.getCode());

                        CountryDetails countryDetails = LiveDataTestUtil.getOrAwaitValue(countryDetailsRepository.getCountryDetails(), COUNTRY_DETAIL_TIMEOUT_SECONDS_FOR_EACH);

                 //When

                        if (countryDetails == null)
                            {
                                System.out.println("NULL COUNTRY DETECTED. CODE : NAME:  " + country.getName() + " : " + country.getCode());
                                countriesThatCannotBeFound.add("\n" + country.toString());
                            }
                    }

                //Then

                Assert.assertFalse(localCountryList.isEmpty());
                Assert.assertTrue("Following countries cannot be found on the rest countries server: " + Arrays.toString(countriesThatCannotBeFound.toArray()),countriesThatCannotBeFound.isEmpty());

            }


    }


