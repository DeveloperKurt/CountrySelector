package com.kurtmustafa.countryselector.repositories;


import android.content.Context;
import android.graphics.Bitmap;

import com.kurtmustafa.countryselector.di.qualifiers.ApplicationContext;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.utils.CountryFlagRetriever;
import com.kurtmustafa.countryselector.utils.CountrySorter;
import com.kurtmustafa.countryselector.utils.JSONResourceReader;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import timber.log.Timber;

/**
 * Can provide a list of {@link Country} from the given json resource with {@link #getCountryList()}
 */

public class CountryJSONRepository implements CountryRepository
    {


        private MutableLiveData<List<Country>> countryLiveData = new MutableLiveData<>();
        private Context context;
        private Integer jsonResourceID;
        private volatile List<Country> countryList = new ArrayList<>();
        private volatile boolean isLoadingCountries = false;


        @Inject
        public CountryJSONRepository(@ApplicationContext final Context context, @Named("CountriesJSONResource") final Integer jsonResourceID)
            {
                this.context = context;
                this.jsonResourceID = jsonResourceID;

            }

        /**
         * Returns a {@link LiveData} that contains the list of {@link Country} object
         */
        public LiveData<List<Country>> getCountryList()
            {

                loadCountriesIfNotLoaded(context);
                return countryLiveData;

            }


        private void loadCountriesIfNotLoaded(@NonNull Context context)
            {
                if (countryList.isEmpty() && !isLoadingCountries)
                    {
                        LoadCountriesRunnable loadCountriesRunnable = new LoadCountriesRunnable();
                        Thread thread = new Thread(loadCountriesRunnable);
                        thread.start();
                    }
            }

        private class LoadCountriesRunnable implements Runnable
            {

                private JSONResourceReader jsonResourceReader;

                LoadCountriesRunnable()
                    {
                        jsonResourceReader = new JSONResourceReader(context.getResources(), jsonResourceID);
                    }

                @Override
                public void run()
                    {
                        try
                            {
                                isLoadingCountries = true;

                                JSONObject countriesJSON = jsonResourceReader.getAsJsonObject();
                                countryList = getCountryListFromJSON(context, countriesJSON);

                                if (countryList.isEmpty())
                                    {
                                        Timber.w("loadCountryData failed, list is empty");
                                    } else
                                    {
                                        CountrySorter.sortAlphabetically(countryList);
                                        countryLiveData.postValue(countryList);
                                        Timber.i("loadCountryData succeeded, countries are ready to be retrieved");
                                    }
                            } finally
                            {
                                isLoadingCountries = false;
                            }

                    }


                private List<Country> getCountryListFromJSON(@NonNull Context context, JSONObject jsonObject)
                    {
                        List<Country> countryList = new ArrayList<>();
                        if (jsonObject != null)
                            {
                                try
                                    {
                                        for (Object key : jsonObject.keySet())
                                            {

                                                if (jsonObject.get(key) != null)
                                                    {

                                                        Bitmap bitmapFlag = new CountryFlagRetriever(context, key.toString()).getCountryFlag();
                                                        if (bitmapFlag != null)
                                                            {
                                                                Country country = new Country(jsonObject.get(key).toString(), bitmapFlag, key.toString());
                                                                countryList.add(country);
                                                            } else
                                                            {
                                                                Timber.w("Bitmap flag is null for the following country: %s", jsonObject.get(key).toString());
                                                            }
                                                    } else
                                                    {
                                                        Timber.w("Json country is null: %s", key);

                                                    }
                                            }
                                        return countryList;

                                    } catch (Exception e)
                                    {
                                        Timber.e(e, "getCountryListFromJSON failed");
                                        countryList.clear();
                                        return countryList;
                                    }
                            } else
                            {
                                Timber.e("JSONObject is null");
                                return countryList;
                            }
                    }


            }

    }
