package com.kurtmustafa.countryselector;

import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.repositories.CountryRepository;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogCountryListViewModel;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.OnCountryClickListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.test.filters.SmallTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class DialogCountryListViewModelTest
    {
        private DialogCountryListViewModel dialogCountryListViewModel;
        private  List<OnCountryClickListener> onCountryClickListeners = new ArrayList<>();

        @Mock
        private CountryRepository countryRepository;

        @Mock
        private Country country;

        @Mock
        private OnCountryClickListener o1;

        @Mock
        private OnCountryClickListener o2;

        @Before
        public void setUp()
            {
                MockitoAnnotations.initMocks(this);
                dialogCountryListViewModel = new DialogCountryListViewModel(countryRepository);

                //Given
                onCountryClickListeners.add(o1);
                onCountryClickListeners.add(o2);

                dialogCountryListViewModel.registerCountryClickObservers(onCountryClickListeners);
            }




        @Test
        public void notifiesObserversOnCountryClick()
            {
                //When clicked
                dialogCountryListViewModel.onCountryClick(country);

                //Then
                verify(o1,times(1)).onCountryClick(country);
                verify(o2,times(1)).onCountryClick(country);
            }



        @Test
        public void removesObservers()
            {
                //When unregistered
                dialogCountryListViewModel.unregisterCountryClickObservers(o1,o2);
                dialogCountryListViewModel.onCountryClick(country);

                //Then
                verify(o1,times(0)).onCountryClick(country);
                verify(o2,times(0)).onCountryClick(country);
            }

        @Test
        public void doesntAllowDuplicateObservers()
            {
                //Given duplicates
                dialogCountryListViewModel.registerCountryClickObservers(new ArrayList<>(Arrays.asList(o1,o2)));

                //When
                dialogCountryListViewModel.onCountryClick(country);

                //Then
                verify(o1,times(1)).onCountryClick(country);
                verify(o2,times(1)).onCountryClick(country);
            }




        @Test
        public void implementsOnCountryClick()
            {
                Assert.assertTrue(dialogCountryListViewModel instanceof OnCountryClickListener);
            }



       /* //This rule is needed to be be to use LiveData
        @Rule
        public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
        @Test
        public void testGetCountryList_isLiveDataEmitting() throws InterruptedException
            {
                //Given
                List<Country> countries = new ArrayList<>(Arrays.asList(mock(Country.class),mock(Country.class)));

                MutableLiveData<List<Country>> mutableLiveData = new MutableLiveData<>();
                mutableLiveData.setValue(countries);

                //When
                Mockito.when(dialogCountryListViewModel.getCountries())
                        .thenReturn(mutableLiveData);

                //Then
               Assert.assertArrayEquals(countries.toArray(),LiveDataTestUtil.getOrAwaitValue(mutableLiveData).toArray());
            }
*/



    }
