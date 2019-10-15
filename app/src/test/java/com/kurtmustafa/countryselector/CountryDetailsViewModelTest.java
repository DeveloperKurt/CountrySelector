package com.kurtmustafa.countryselector;

import android.content.Context;

import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.models.ErrorCodes;
import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.testutils.LiveDataTestUtil;
import com.kurtmustafa.countryselector.testutils.ModelInstances;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.OnCountryClickListener;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.CountryDetailsViewModel;
import com.kurtmustafa.countryselector.utils.LiveDataEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.test.filters.MediumTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MediumTest //Execution time is greater than 200ms (400ms)
@RunWith(MockitoJUnitRunner.class)
public class CountryDetailsViewModelTest
    {
        @Rule //This rule is needed to be able to use the LiveData
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


        private CountryDetailsViewModel countryDetailsViewModel;

        @Mock
        private CountryDetailsRepository countryDetailsRepository;

        @Before
        public void setUp()
            {
                MockitoAnnotations.initMocks(this);


                Context mockedContext = mock(Context.class);
                doReturn("Placeholder Header String").when(mockedContext).getString(any(Integer.class));

                countryDetailsViewModel = new CountryDetailsViewModel(mockedContext, countryDetailsRepository);

            }

        @Test
        public void getsCountryDetailsAsFormattedString() throws InterruptedException
            {

                //Given
                MutableLiveData<CountryDetails> countryDetailsMutableLiveData = new MutableLiveData<>();
                CountryDetails swedenCountryDetails = ModelInstances.SwedenCountryDetails;
                countryDetailsMutableLiveData.setValue(swedenCountryDetails);
                doReturn(countryDetailsMutableLiveData).when(countryDetailsRepository).getCountryDetails();

                //When
                String countryDetailsInString = LiveDataTestUtil.getOrAwaitValue(countryDetailsViewModel.getCountryDetails());

                //Then
                verify(countryDetailsRepository, times(1)).getCountryDetails();
                Assert.assertNotEquals(null, countryDetailsInString);

            }


        @Test
        public void observersForErrorsInRepo() throws InterruptedException
            {

                //Given
                MutableLiveData<LiveDataEvent<ErrorCodes>> errorCodesMutableLiveData = new MutableLiveData<>();
                errorCodesMutableLiveData.setValue(new LiveDataEvent<>(ErrorCodes.OTHER_REQUEST_RELATED));

                // When
                doReturn(errorCodesMutableLiveData).when(countryDetailsRepository).getErrors();

                //Then
                Assert.assertEquals(ErrorCodes.OTHER_REQUEST_RELATED, LiveDataTestUtil.getOrAwaitValue(countryDetailsViewModel.observeForErrors()).peekContent());

            }

        /**
         * Shouldn't load country from view arguments if multiple country is displayed, otherwise it causes inconsistencies when screen is rotated etc.
         */
        @Test
        public void handlesCountryOnViewRecreation() throws InterruptedException
            {
                //Given
                Country country1 = mock(Country.class);
                Country country2 = mock(Country.class);

                //When view gets created
                countryDetailsViewModel.onGetCountryFromArguments(country1);

                //And user displays another country
                countryDetailsViewModel.onCountryClick(country2);

                //And rotates the screen therefore view gets created again
                countryDetailsViewModel.onGetCountryFromArguments(country1);

                //Then
                Assert.assertEquals("The Country in the ViewModel's LiveData still should be the clicked country (country2) to prevent inconsistencies",
                        country2,LiveDataTestUtil.getOrAwaitValue(countryDetailsViewModel.getCountry()));

            }

        @Test
        public void handlesLocalErrors() throws InterruptedException
            {
                //Given
                MutableLiveData<LiveDataEvent<ErrorCodes>> dummyLiveData = new MutableLiveData<>(); //For mediatorlivedata to not to crash
                doReturn(dummyLiveData).when(countryDetailsRepository).getErrors();


                //When there is an error within viewmodel ( Country is null)
                countryDetailsViewModel.onCountryClick(new Country(null,null,null));

                //Then
                Assert.assertNotEquals(null, LiveDataTestUtil.getOrAwaitValue(countryDetailsViewModel.observeForErrors()).peekContent());
            }

        @Test
        public void implementsOnCountryClick()
            {
                Assert.assertTrue(countryDetailsViewModel instanceof OnCountryClickListener);
            }


    }
