package com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist;

import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.repositories.CountryRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class DialogCountryListViewModel extends ViewModel implements OnCountryClickListener
    {
        private CountryRepository countryRepository;
        private List<OnCountryClickListener> observerList = new ArrayList<>();


        @Inject
        public DialogCountryListViewModel(CountryRepository countryRepository)
            {
                super();
                this.countryRepository = countryRepository;
            }

        public LiveData<List<Country>> getCountries()
            {
                return countryRepository.getCountryList();
            }

        /**
         * Registers all of the listeners that wants to be notified from a country click event
         *
         * @param observerList List of {@link OnCountryClickListener} to be registered
         */
        public void registerCountryClickObservers(@NonNull List<OnCountryClickListener> observerList)
            {

                for (OnCountryClickListener listener : observerList)
                    {

                        if (!this.observerList.contains(listener))
                            {
                                Timber.i("registering country click listener: %s", listener);
                                this.observerList.add(listener);
                            }

                    }

            }

        /**
         * Unregisters the listeners that are passed to the arguments
         *
         * @param observers Any number of {@link OnCountryClickListener} to be unregistered
         */
        public void unregisterCountryClickObservers(OnCountryClickListener... observers)
            {
                if (observers != null)
                    {
                        for (OnCountryClickListener listener : observers)
                            {
                                Timber.i("Unregistering country click listener: %s", listener);
                                observerList.remove(listener);
                            }
                    }
            }

        /**
         * Notifies the listeners that a country is clicked, also the clicked {@link Country} can be found on the callback
         *
         * @param country The clicked {@link OnCountryClickListener}
         */
        private void notifyCountryClickObservers(Country country)
            {
                for (OnCountryClickListener listener : observerList)
                    {
                        if (listener != this)
                            listener.onCountryClick(country);
                    }
            }


        @Override
        public void onCountryClick(Country country)
            {
                notifyCountryClickObservers(country);
            }
    }
