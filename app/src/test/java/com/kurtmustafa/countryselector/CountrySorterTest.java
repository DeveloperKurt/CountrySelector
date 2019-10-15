package com.kurtmustafa.countryselector;

import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.utils.CountrySorter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.test.filters.SmallTest;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class CountrySorterTest
    {

        private Country country1,country2;


        @Before
        public void setUp()
            {
                //Given
                country1 = new Country("abc",null,"ab");
                country2 = new Country("cba",null,"cb");

            }
        @Test
        public void correctlyAlphabeticallySorts()
            {

               //Given
                List<Country> countryList = new ArrayList<>(Arrays.asList(country2,country1));

                //When
                CountrySorter.sortAlphabetically(countryList);

                //Then
                Assert.assertSame(countryList.get(0), country1);
            }
    }
