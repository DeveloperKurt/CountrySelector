package com.kurtmustafa.countryselector.ui.fragmentcountrydetails;


import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.di.ViewModelProviderFactory;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.models.ErrorCodes;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogFragmentCountryList;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.OnCountryClickListener;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;

public class FragmentCountryDetails extends DaggerFragment implements View.OnClickListener, OnCountryClickListener
    {
        public static final String TAG = "FragmentCountryDetails";
        private static final String BUNDLE_KEY_COUNTRY = "BUNDLE_KEY_COUNTRY";
        private static final String BUNDLE_KEY_IS_VIEWING_AN_ERROR = "BUNDLE_KEY_IS_VIEWING_AN_ERROR";

        /**
         * If was viewing an error and the fragment got recreated (might be due to the rotation), this variable gets assigned true. <br>
         * In this case, we want to handle the error event even if it was handled before.
         */
        private boolean isViewingAnError = false;


        private FragmentManager fragmentManager;
        private DialogFragmentCountryList dialogFragmentCountryList;
        private CountryDetailsViewModel countryDetailsViewModel;
        private TextView tvCountryName, tvCountryDetails;
        private ImageView ivCountryFlag;
        private FloatingActionButton fabViewCountryListDialog;
        private ProgressBar progressBar;
        private View inflatedView;
        private ViewStub viewStub;
        private boolean isViewStubInflated = false;

        @Inject
        ViewModelProviderFactory providerFactory;


        public static FragmentCountryDetails newInstance(Country country)
            {
                FragmentCountryDetails fragmentCountryDetails = new FragmentCountryDetails();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_KEY_COUNTRY, country);
                fragmentCountryDetails.setArguments(bundle);
                return fragmentCountryDetails;

            }


        /**
         * Gets if a {@link Country} got passed to the bundle
         *
         * @return {@link Country}
         */
        private Country getCountryFromArguments()
            {
                Bundle bundle = getArguments();
                if (bundle != null && bundle.getParcelable(BUNDLE_KEY_COUNTRY) != null)
                    return bundle.getParcelable(BUNDLE_KEY_COUNTRY);
                else
                    {
                        Timber.e("OnCreate requiered bundle is null, cannot get the country");
                        return null;
                    }
            }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                View view = inflater.inflate(R.layout.fragment_country_details, container, false);
                initViews(view);
                return view;
            }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
            {
                super.onActivityCreated(savedInstanceState);

                if (savedInstanceState != null)
                    isViewingAnError = savedInstanceState.getBoolean(BUNDLE_KEY_IS_VIEWING_AN_ERROR, false);

                countryDetailsViewModel = ViewModelProviders.of(this, providerFactory).get(CountryDetailsViewModel.class);
                countryDetailsViewModel.onGetCountryFromArguments(getCountryFromArguments());

                progressBar.setVisibility(View.VISIBLE);
            }

        @Override
        public void onResume()
            {

                super.onResume();

                initCountryListDialog();
                observeCountry();
                observeCountryDetails();
                observeForErrors();
            }


        /**
         * Does the UI updates when a new CountryDetails retrieved
         *
         * @param countryDetails A {@link String} that contains Country's details in a "ready to be displayed" format
         */
        private void updateDetails(String countryDetails)
            {
                progressBar.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(countryDetails))
                    {
                        hideErrorCard();
                        tvCountryDetails.setText(Html.fromHtml(countryDetails));
                    } else
                    {
                        Timber.w("Retrieved countryDetails is NULL or empty");
                    }

            }


        /**
         * Sets the information from the {@link Country} field to the country card
         */
        private void updateCountry(Country country)
            {

                if (country.getName() != null)
                    {
                        tvCountryName.setText(country.getName());
                    } else
                    {
                        Timber.e("Could not set the country name, country name is null!");
                    }


                if (country.getFlag() != null)
                    {

                        ivCountryFlag.setImageBitmap(country.getFlag());
                    } else
                    {
                        Timber.e("Could not set the country flag, country flag is null!");
                    }


            }


        private void observeCountryDetails()
            {
                countryDetailsViewModel.getCountryDetails().observe(this, this::updateDetails);
            }

        private void observeCountry()
            {
                countryDetailsViewModel.getCountry().observe(this, this::updateCountry);
            }

        /**
         * Displays errors for only one time unless that Fragment got recreated. In that case, restores the previous state by displaying it for a n'th time.
         */
        private void observeForErrors()
            {
                countryDetailsViewModel.observeForErrors().observe(this, errorCodeEvent ->
                {

                    if (errorCodeEvent != null)
                        {
                            ErrorCodes errorCode;

                            if (isViewingAnError) //If was viewing an error and fragment recreated (rotation), this variable is true. In this case, we want to handle the event even if it was handled before.
                                errorCode = errorCodeEvent.peekContent();
                            else
                                errorCode = errorCodeEvent.getContentIfNotHandled();

                            if (errorCode != null)
                                {

                                    isViewingAnError = true;
                                    String message = "";
                                    switch (errorCode)
                                        {
                                            case NO_INTERNET:
                                            {
                                                message = getString(R.string.error_no_internet);
                                                break;
                                            }
                                            case BAD_REQUEST:
                                            {
                                                message = getString(R.string.error_not_found);
                                                break;
                                            }
                                            case NOT_FOUND:
                                            {
                                                message = getString(R.string.error_not_found);
                                                break;
                                            }
                                            case OTHER_REQUEST_RELATED:
                                            {
                                                message = getString(R.string.error_default_failed_details);
                                                break;
                                            }
                                            default:
                                            {
                                                break;
                                            }
                                        }
                                    displayErrorCard(message);
                                } else
                                {
                                    Timber.w("ErrorLiveDataEvent is NULL, cannot display the error card");
                                }
                        } else
                        {
                            Timber.e("Retrieved errorCodes from ErrorLiveDataEvent is NULL, cannot display the error card");
                        }

                });
            }


        /**
         * If not already inflated, inflates the view stub
         */
        private void inflateViewStub()
            {
                if (!isViewStubInflated)
                    {
                        inflatedView = viewStub.inflate();
                        isViewStubInflated = true;
                    }

            }

        /**
         * Inflates the view stub and makes the error card view visible.
         *
         * @param additionalMessage nullable additional explanation message that will be displayed on the card. If it is null, default message is displayed.
         */
        private void displayErrorCard(@Nullable String additionalMessage)
            {
                clearDetails();
                inflateViewStub();
                progressBar.setVisibility(View.GONE);
                inflatedView.setVisibility(View.VISIBLE);

                TextView tvDetails = inflatedView.findViewById(R.id.error_card_tv_details);
                if (TextUtils.isEmpty(additionalMessage))
                    {
                        Timber.w("Passed error details/explanation is null or empty. Displaying the card with the default error message");

                    } else
                    {
                        tvDetails.setText(additionalMessage);
                    }
            }

        private void hideErrorCard()
            {
                if (isViewStubInflated)
                    {
                        isViewingAnError = false;
                        inflatedView.setVisibility(View.GONE);
                    }
            }


        /**
         * Initializes the CountryList dialog by creating, registering the listeners and dismissing it.
         */
        private void initCountryListDialog()
            {
                if (dialogFragmentCountryList == null)
                    {
                        if (getActivity() != null)
                            {
                                fragmentManager = getActivity().getSupportFragmentManager();
                                dialogFragmentCountryList = new DialogFragmentCountryList();
                                dialogFragmentCountryList.show(fragmentManager, DialogFragmentCountryList.TAG);
                                dialogFragmentCountryList.observeOnCountryClick(countryDetailsViewModel);
                                dialogFragmentCountryList.observeOnCountryClick(this);
                                dialogFragmentCountryList.dismiss();

                            } else
                            Timber.w("getActivity is NULL, cannot show the DialogFragmentCountryList");
                    } else
                    Timber.i("dialogFragmentCountryList is initialized before, passing initialization");
            }

        private void displayCountryListDialog()
            {
                dialogFragmentCountryList.show(fragmentManager, "DialogFragmentCountryList");
            }


        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.country_details_display_countries_fab:
                        {
                            displayCountryListDialog();
                        }
                    }
            }


        @Override
        public void onSaveInstanceState(@NonNull Bundle outState)
            {
                super.onSaveInstanceState(outState);
                outState.putBoolean(BUNDLE_KEY_IS_VIEWING_AN_ERROR, isViewingAnError);
            }

        /**
         * Clears the current country details in case if the users faces a problem, and another view upon the details TextView (error card) gets displayed
         */
        private void clearDetails()
            {
                tvCountryDetails.setText("");
            }


        private void initViews(View view)
            {
                tvCountryName = view.findViewById(R.id.country_details_card_tv_country);
                ivCountryFlag = view.findViewById(R.id.country_details_card_iv_flag);
                tvCountryDetails = view.findViewById(R.id.country_details_tv_details);
                fabViewCountryListDialog = view.findViewById(R.id.country_details_display_countries_fab);
                fabViewCountryListDialog.setOnClickListener(this);
                progressBar = view.findViewById(R.id.country_details_progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                viewStub = view.findViewById(R.id.country_details_view_stub);

            }

        @Override
        public void onCountryClick(Country country)
            {
                clearDetails();
                hideErrorCard();
                progressBar.setVisibility(View.VISIBLE);
            }
    }


