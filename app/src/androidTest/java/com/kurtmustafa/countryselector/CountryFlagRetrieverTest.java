package com.kurtmustafa.countryselector;

import android.content.Context;

import com.kurtmustafa.countryselector.utils.CountryFlagRetriever;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

@MediumTest //Uses resources
public class CountryFlagRetrieverTest
    {

        /**
         * Some keywords are reserved by Java, therefore cannot be used as resource name. "_reserved" added at the and of the name of these resources
         */
        private static final String[] RESERVED_RESOURCE_CODES = {"do"};



        private CountryFlagRetriever countryFlagRetriever;
        private Context context;


        @Before
        public void setUp()
            {
               context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            }

        @Test
        public void getsReservedReformattedFlags()
            {
                //Given
                for(String code: RESERVED_RESOURCE_CODES)
                    {
                        countryFlagRetriever = new CountryFlagRetriever(context, code);

                        //When - Then
                        Assert.assertNotEquals(null, countryFlagRetriever.getCountryFlag());
                    }
            }



        @Test
        public void getsAFlag()
            {
                //Given
                countryFlagRetriever = new CountryFlagRetriever(context,"SE");

                //When - Then
                Assert.assertNotEquals(null,countryFlagRetriever.getCountryFlag());
            }
    }
