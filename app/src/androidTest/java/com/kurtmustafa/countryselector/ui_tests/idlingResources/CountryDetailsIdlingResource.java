package com.kurtmustafa.countryselector.ui_tests.idlingResources;

import android.widget.TextView;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.FragmentCountryDetails;

import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.IdlingResource;
import timber.log.Timber;


/**
 * Idles the resources when the R.id.country_details_tv_details TextView's text changes ({@link FragmentCountryDetails}).
 * <br>
 * Can be used to efficiently wait for a process to finish before checking any conditions or performing any actions with espresso.
 */
public class CountryDetailsIdlingResource implements IdlingResource
    {
        private ResourceCallback resourceCallback;
        private boolean isIdle;
        private FragmentActivity activity;
        private String lastText = "";

        public CountryDetailsIdlingResource(FragmentActivity activity)
            {
                this.activity = activity;
            }

        @Override
        public String getName()
            {
                return "CountryDetailsIdlingResource";
            }

        @Override
        public boolean isIdleNow()
            {
                Timber.d("lastCountry: %s \n isIdle: %b \n", lastText,isIdle);
                if (isIdle) return true;


                if (activity == null)
                    {
                        Timber.d("activity is null, returning, isIdle FALSE");
                        return false;
                    }

                FragmentCountryDetails fragmentCountryDetails = (FragmentCountryDetails) activity.getSupportFragmentManager().findFragmentByTag(FragmentCountryDetails.TAG);


                if (fragmentCountryDetails != null)
                    {
                        if(fragmentCountryDetails.getView()!= null)
                            {
                                TextView textView = fragmentCountryDetails.getView().findViewById(R.id.country_details_tv_details);
                                if (textView != null)
                                    {
                                        if(!textView.getText().toString().equals(lastText))
                                            {
                                                Timber.d("new text retrieved, setting isIdle to TRUE and calling onTransitionToIdle()");
                                                isIdle = true;
                                                resourceCallback.onTransitionToIdle();
                                            }

                                        lastText = textView.getText().toString();

                                    } else
                                    {
                                        Timber.d("textView  country_details_tv_details is null, setting isIdle to FALSE");
                                        isIdle = false;
                                    }
                            }
                        else
                            {
                                Timber.d("fragmentCountryDetails.getView() is null,  setting isIdle to FALSE");
                                isIdle = false;
                            }

                    } else
                    {
                        Timber.d("fragmentCountryDetails is null, setting isIdle to FALSE");
                        isIdle = false;
                    }

                return isIdle;
            }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback)
            {
                this.resourceCallback = resourceCallback;
            }


    }
