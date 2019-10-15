package com.kurtmustafa.countryselector.ui_tests.idlingResources;


import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogFragmentCountryList;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;
import timber.log.Timber;

/**
 * Idles resource when the countries list is is filled.
 * <br>
 * Can be used to efficiently wait for a process to finish before checking any conditions or performing any actions with espresso.
 */
public class DialogCountryListLoadingIdlingResource implements IdlingResource
    {
        private ResourceCallback resourceCallback;
        private boolean isIdle;
        private FragmentActivity activity;

        public DialogCountryListLoadingIdlingResource(FragmentActivity activity)
            {
                this.activity = activity;
            }

        @Override
        public String getName()
            {
                return "DialogCountryListLoadingIdlingResource";
            }

        @Override
        public boolean isIdleNow()
            {
                Timber.d("isIdle: %b", isIdle);
                if (isIdle) return true;


                if (activity == null)
                    {
                        Timber.d("activity is null, returning (isIdle) FALSE");
                        return false;
                    }

                //  FragmentPickCountry fragmentPickCountry = (FragmentPickCountry) activity.getSupportFragmentManager().findFragmentByTag(FragmentPickCountry.TAG);


                DialogFragmentCountryList dialogFragmentCountryList = (DialogFragmentCountryList) activity.getSupportFragmentManager()
                        .findFragmentByTag(DialogFragmentCountryList.TAG);


                if (dialogFragmentCountryList != null)
                    {
                        RecyclerView recyclerView = dialogFragmentCountryList.getDialog().findViewById(R.id.country_list_recycler_view);
                        if (recyclerView.getAdapter() != null)
                            {
                                isIdle = recyclerView.getAdapter().getItemCount() > 0;

                                if (isIdle)
                                    {
                                        Timber.d("recyclerView is filled, returning (isIdle) TRUE");
                                        isIdle = true;
                                        resourceCallback.onTransitionToIdle();
                                    }
                            } else
                            {
                                Timber.d("recyclerView is null, returning (isIdle) FALSE");
                                isIdle = false;
                            }

                    } else
                    {
                        Timber.d("dialogFragmentCountryList is null, returning (isIdle) FALSE");
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
