package com.kurtmustafa.countryselector.ui.fragmentcountrydetails;


import android.content.Context;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.di.qualifiers.ApplicationContext;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.models.Currency;
import com.kurtmustafa.countryselector.models.ErrorCodes;
import com.kurtmustafa.countryselector.models.Language;
import com.kurtmustafa.countryselector.repositories.CountryDetailsRepository;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.OnCountryClickListener;
import com.kurtmustafa.countryselector.utils.LiveDataEvent;

import java.text.DecimalFormat;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class CountryDetailsViewModel extends ViewModel implements OnCountryClickListener
    {
        private CountryDetailsRepository countryDetailsRepository;
        private Context context;
        private MutableLiveData<Country> mutableCountryLiveData = new MutableLiveData<>();
        private boolean displayedMultipleCountries = false;
        private MutableLiveData<LiveDataEvent<ErrorCodes>> errorCodesMutableLiveData = new MutableLiveData<>();


        @Inject
        public CountryDetailsViewModel(@ApplicationContext Context context, CountryDetailsRepository countryDetailsRepository)
            {
                super();
                this.countryDetailsRepository = countryDetailsRepository;
                this.context = context;

            }

        /**
         * Gets updated when the requested countryDetails is returned from the repository and returns a {@link String} which is ready to be displayed
         *
         * @return Formatted country details in HTML {@link String} form
         */
        public LiveData<String> getCountryDetails()
            {
                LiveData<CountryDetails> countryDetailsLiveData = countryDetailsRepository.getCountryDetails();
                return Transformations.map(countryDetailsLiveData, this::formatDetailsAsString);
            }


        /**
         * Returns a {@link LiveDataEvent} that contains {@link ErrorCodes} when an error occurred either in ViewModel or in the {@link CountryDetailsRepository}
         *
         * @return {@link LiveDataEvent} that contains a {@link ErrorCodes} code
         */
        public LiveData<LiveDataEvent<ErrorCodes>> observeForErrors()
            {
                MediatorLiveData<LiveDataEvent<ErrorCodes>> errorCodesMediatorLiveData = new MediatorLiveData<>();
                errorCodesMediatorLiveData.addSource(countryDetailsRepository.getErrors(), errorCodesMediatorLiveData::setValue);
                errorCodesMediatorLiveData.addSource(errorCodesMutableLiveData, errorCodesMediatorLiveData::setValue);

                return errorCodesMediatorLiveData;
            }

        /**
         * Can be used to keep track of the Fragment's state when it gets recreated
         *
         * @return The most recently displayed {@link Country}
         */
        public LiveData<Country> getCountry()
            {
                return mutableCountryLiveData;
            }


        /**
         * Called when the Fragment is getting initialized and getting its arguments. <br>
         * The first {@link #mutableCountryLiveData} {@link Country} is provided by this way. <br>
         * It ignores it if any other {@link Country} has gotten displayed afterwards
         */
        public void onGetCountryFromArguments(Country country)
            {
                if (!displayedMultipleCountries)
                    {
                        mutableCountryLiveData.setValue(country);
                        countryDetailsRepository.loadNewCountryDetails(country.getCode());
                    }
            }

        /**
         * Called when a {@link Country} is clicked on Fragment to load the {@link CountryDetails} of that {@link Country}
         */
        @Override
        public void onCountryClick(Country country)
            {
                if (country != null)
                    {
                        displayedMultipleCountries = true;
                        mutableCountryLiveData.setValue(country);
                        if (country.getCode() != null)
                            countryDetailsRepository.loadNewCountryDetails(country.getCode());
                        else
                            {
                                errorCodesMutableLiveData.setValue(new LiveDataEvent<>(ErrorCodes.OTHER_REQUEST_RELATED));
                                Timber.e("Could not load new country details, country code is null!");
                            }
                    } else
                    {
                        errorCodesMutableLiveData.setValue(new LiveDataEvent<>(ErrorCodes.OTHER));
                        Timber.e("Could not update country, country is null!");
                    }

            }

        /**
         * Formats the {@link CountryDetails} in a "TextView display ready" way
         *
         * @return Ready to be displayed {@link String}
         */
        private String formatDetailsAsString(CountryDetails countryDetails)
            {
                final String htmlNewline = "<br/>";

                if (countryDetails != null)
                    {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(context.getString(R.string.region)).append(countryDetails.getRegion()).append(htmlNewline);
                        stringBuilder.append(context.getString(R.string.subregion)).append(countryDetails.getSubregion()).append(htmlNewline);
                        stringBuilder.append(context.getString(R.string.capital)).append(countryDetails.getCapital()).append(htmlNewline);

                        //Add comma separators to population
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        stringBuilder.append(context.getString(R.string.population)).append(decimalFormat.format(countryDetails.getPopulation())).append(htmlNewline);

                        stringBuilder.append(context.getString(R.string.currencies));

                        for (Currency currency : countryDetails.getCurrencies())
                            {
                                stringBuilder.append(currency.getName()).append(", ");
                            }
                        stringBuilder.setLength(stringBuilder.length() - 2); //delete the last comma since there are no more elements to this list
                        stringBuilder.append(htmlNewline);

                        stringBuilder.append(context.getString(R.string.languages));

                        for (Language language : countryDetails.getLanguages())
                            {
                                stringBuilder.append(language.getName()).append(", ");
                            }
                        stringBuilder.setLength(stringBuilder.length() - 2); //delete the last comma since there are no more elements to this list
                        stringBuilder.append(htmlNewline);

                        stringBuilder.append(context.getString(R.string.timezones));

                        for (String timezone : countryDetails.getTimezones())
                            {
                                stringBuilder.append(timezone).append(", ");
                            }
                        stringBuilder.setLength(stringBuilder.length() - 2); //delete the last comma since there are no more elements to this list
                        stringBuilder.append(htmlNewline);


                        return stringBuilder.toString();
                    } else
                    {
                        Timber.e("countryDetails is NULL, cannot format&set the details!");
                        return "";
                    }

            }

    }
