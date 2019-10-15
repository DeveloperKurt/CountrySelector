package com.kurtmustafa.countryselector;


import com.kurtmustafa.countryselector.consts.Constants;
import com.kurtmustafa.countryselector.utils.JSONResourceReader;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Note: This integration test is depended on a JSON file.
 *
 */
@MediumTest //Uses resources
public class CountryResourceReaderTest
    {

        private static JSONResourceReader jsonResourceReader;
        private static JSONObject jsonObject;


        @BeforeClass
        public static void setUp()
            {
                // Load the JSON file.
                jsonResourceReader = new JSONResourceReader(InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(), R.raw.countries);
                jsonObject = jsonResourceReader.getAsJsonObject();

            }

        @Test
        public void jsonObjectNotNull()
            {
                assertNotNull(jsonResourceReader.getAsJsonObject());
            }


        @Test
        public void jsonObjectHasTheRightContent()
            {
                assertEquals("Sweden", jsonObject.get("SE"));
            }


        @Test
        public void hasTheRightAmountOfCountries()
            {
                assertEquals(Constants.INCLUDED_COUNTRIES, jsonObject.size());
            }


    }
