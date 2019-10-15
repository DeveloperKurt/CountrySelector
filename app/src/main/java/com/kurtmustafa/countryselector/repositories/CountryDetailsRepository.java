package com.kurtmustafa.countryselector.repositories;

import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.models.ErrorCodes;
import com.kurtmustafa.countryselector.requests.AppExecutors;
import com.kurtmustafa.countryselector.requests.RestCountriesServiceGenerator;
import com.kurtmustafa.countryselector.utils.LiveDataEvent;

import java.net.UnknownHostException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Response;
import timber.log.Timber;


/**
 * Provides country details if {@link #loadNewCountryDetails(String)} is called by exposing {@link #getCountryDetails()}.
 * <p/>
 * Also provides {@link #getErrors()} to notify if any error occurs during the operation.
 */
public class CountryDetailsRepository
    {
        public final static int REQUEST_TIMEOUT_TIME = 5000;

        public static final int WAIT_TIME_BEFORE_RETRY = 250;
        public static final int RETRY_COUNT = 2;
        private MutableLiveData<LiveDataEvent<ErrorCodes>> errorCodesMutableLiveData = new MutableLiveData<>();
        private MutableLiveData<CountryDetails> countryDetailsMutableLiveData = new MutableLiveData<>();
        private RetrieveCountryDetailsRunnable retrieveCountryDetailsRunnable;
        private RestCountriesServiceGenerator restCountriesServiceGenerator;

        /**
         * @param restCountriesServiceGenerator A {@link RestCountriesServiceGenerator} class instance that has a retrofit instance which handles API's.
         */
        @Inject
        public CountryDetailsRepository(RestCountriesServiceGenerator restCountriesServiceGenerator)
            {
                this.restCountriesServiceGenerator = restCountriesServiceGenerator;
            }

        /**
         * Can be called to get a {@link LiveData} that contains {@link CountryDetails} if loaded.
         * <p/>
         * It will be updated if {@link #loadNewCountryDetails(String)} gets called.
         *
         * @return {@link LiveData} that contains the most recently loaded {@link CountryDetails}
         */
        public LiveData<CountryDetails> getCountryDetails()
            {
                return countryDetailsMutableLiveData;
            }

        /**
         * Can be called to get a {@link LiveData} that contains {@link ErrorCodes} if any error has occurred. <br>
         *
         * @return {@link LiveData} that contains the most recent {@link ErrorCodes}
         */
        public LiveData<LiveDataEvent<ErrorCodes>> getErrors()
            {
                return errorCodesMutableLiveData;
            }

        public void loadNewCountryDetails(@NonNull String countryCode)
            {

                if (retrieveCountryDetailsRunnable != null)
                    {
                        retrieveCountryDetailsRunnable = null;
                    }

                retrieveCountryDetailsRunnable = new RetrieveCountryDetailsRunnable(countryCode, restCountriesServiceGenerator);
                final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveCountryDetailsRunnable);

                AppExecutors.getInstance().networkIO().schedule(() -> {

                    // let the user know it timed out
                    if (handler.cancel(true))
                        {
                            postError(ErrorCodes.OTHER_REQUEST_RELATED);
                        }
                }, REQUEST_TIMEOUT_TIME, TimeUnit.MILLISECONDS);
            }

        /**
         * Removes the previous country's {@link CountryDetails} in the {@link #countryDetailsMutableLiveData},
         * and posts the error to the {@link #errorCodesMutableLiveData} in the right invocation order
         * (if {@link #countryDetailsMutableLiveData} gets updated first, even it is null, it might cause confusion for the observers,
         * thus they might hide the error).
         *
         * @param errorCode Appropriate {@link ErrorCodes} code of the error
         */
        private void postError(ErrorCodes errorCode)
            {
                countryDetailsMutableLiveData.postValue(null);
                errorCodesMutableLiveData.postValue(new LiveDataEvent<>(errorCode));

            }

        /**
         * Clears the data of the {@link #countryDetailsMutableLiveData} by reinitializing it (setting null might cause unintended invocations)
         * <p/>
         * Can be used to prevent preloaded old LiveData to be retrieved before other countries' details retrieved
         * when trying to get new {@link CountryDetails} rapidly in a loop.
         * <p/>
         * Since {@link #loadNewCountryDetails(String)} doesn't notify any listeners when the data is ready.
         */
        @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
        public void reinitCountryDetailsLiveData()
            {
                countryDetailsMutableLiveData = null;
                countryDetailsMutableLiveData = new MutableLiveData<>();
            }

        private class RetrieveCountryDetailsRunnable implements Runnable
            {

                private String queryCountryCode;

                /**
                 * Wait time before any retrying attempt in milliseconds
                 */

                private int retryIterator = 0;
                private RestCountriesServiceGenerator restCountriesServiceGenerator;

                private RetrieveCountryDetailsRunnable(String queryCountryCode, RestCountriesServiceGenerator restCountriesServiceGenerator)
                    {
                        this.queryCountryCode = queryCountryCode.toLowerCase();
                        this.restCountriesServiceGenerator = restCountriesServiceGenerator;
                    }


                @Override
                public void run()
                    {
                        sendRequest();
                    }


                void sendRequest()
                    {
                        try
                            {

                                Response response = restCountriesServiceGenerator.getCountryDetailsApi().getCountryDetails(queryCountryCode).execute();


                                switch (response.code())
                                    {

                                        case 200:
                                        {
                                            Timber.i("RAW: %s", response.raw().request().url());

                                            CountryDetails countryDetails = (CountryDetails) response.body();

                                            if (countryDetails != null)
                                                {
                                                    countryDetailsMutableLiveData.postValue(countryDetails);
                                                    Timber.i("CLASS: %s", countryDetails.toString());
                                                } else
                                                {
                                                    Timber.e("countryDetails is NULL: ");
                                                    postError(ErrorCodes.OTHER_REQUEST_RELATED);
                                                }

                                            break;
                                        }

                                        case 400:
                                        {
                                            Timber.e("Response was not successful, BAD REQUEST. %s", response.raw());
                                            postError(ErrorCodes.BAD_REQUEST);
                                            break;
                                        }

                                        case 404:
                                        {
                                            Timber.e("Response was not successful, NOT FOUND.  %s", response.raw());
                                            postError(ErrorCodes.NOT_FOUND);
                                            break;
                                        }

                                        default:
                                        {

                                            Timber.e("Response was not successful.\n Response code: [%d] \n Message: [%s] \n Raw: [%s] ", response.code(), response.message(), response.raw());
                                            postError(ErrorCodes.OTHER_REQUEST_RELATED);
                                            break;
                                        }
                                    }


                            } catch (UnknownHostException e)
                            {
                                if (retryIterator < RETRY_COUNT)
                                    {

                                        retryIterator++;
                                        Timber.w("No host or internet, cannot get a response. Retry attempt: %d", retryIterator);
                                        sleepThread();
                                        sendRequest();

                                    } else
                                    {
                                        Timber.e(e, "No host or internet, cannot get a response. Posting error");
                                        postError(ErrorCodes.NO_INTERNET);
                                        retryIterator = 0;

                                    }

                            } catch (Exception e)
                            {
                                Timber.e(e, "Exception thrown while getting the response");
                                postError(ErrorCodes.OTHER_REQUEST_RELATED);
                            }
                    }

                private void sleepThread()
                    {
                        try
                            {
                                Thread.sleep(WAIT_TIME_BEFORE_RETRY);
                            } catch (InterruptedException e)
                            {
                                Timber.e(e, "Sleeping attempt interrupted.");
                            }
                    }


            }


    }
