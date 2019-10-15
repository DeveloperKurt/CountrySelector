package com.kurtmustafa.countryselector.ui.fragmentpickcountry;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.ui.MainActivity;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogFragmentCountryList;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.OnCountryClickListener;

import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerFragment;
import timber.log.Timber;


public class FragmentPickCountry extends DaggerFragment implements View.OnClickListener, OnCountryClickListener
    {
        private FloatingActionButton fabPickCountry;
        private FragmentManager fragmentManager;
        private DialogFragmentCountryList dialogFragmentCountryList;
        private TextView tvHeader, tvExplanation;
        public static final String TAG = "FragmentPickCountry";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
            {
                View view = inflater.inflate(R.layout.fragment_pick_country, container, false);
                initViews(view);
                initCountryListDialog();
                startLaunchAnimation();

                return view;
            }

        private void initViews(View view)
            {
                fabPickCountry = view.findViewById(R.id.pick_country_display_countries_fab);
                fabPickCountry.setOnClickListener(this);

                tvHeader = view.findViewById(R.id.pick_country_tv_button_action);
                tvExplanation = view.findViewById(R.id.pick_country_tv_explanation);
            }


        @Override
        public void onClick(View v)
            {

                switch (v.getId())
                    {
                        case R.id.pick_country_display_countries_fab:
                        {
                            displayCountryListDialog();
                        }
                    }
            }

        private void displayCountryListDialog()
            {
                dialogFragmentCountryList.show(fragmentManager,  DialogFragmentCountryList.TAG);
            }

        /**
         * Creates and dismisses the dialog immediately in order to initialize the loading of the countries which is a time consuming operation.<br>
         * By this way, when user clicks to display countries button, the pre loaded countries can be shown without any significant loading time.
         */
        private void initCountryListDialog()
            {
                if (getActivity() != null)
                    {
                        fragmentManager = getActivity().getSupportFragmentManager();

                        dialogFragmentCountryList = new DialogFragmentCountryList();

                        dialogFragmentCountryList.show(fragmentManager, DialogFragmentCountryList.TAG);
                        dialogFragmentCountryList.observeOnCountryClick(this);
                        dialogFragmentCountryList.dismiss();


                    } else
                    Timber.w("getActivity is NULL, cannot show the DialogFragmentCountryList");

            }


        private void startLaunchAnimation()
            {
                Animation firstItem = AnimationUtils.loadAnimation(getContext(), R.anim.launch_anim_1st_item);
                Animation secondItemGroup1 = AnimationUtils.loadAnimation(getContext(), R.anim.launch_anim_2nd_item);
                Animation secondItemGroup2 = AnimationUtils.loadAnimation(getContext(), R.anim.launch_anim_2nd_item);

                fabPickCountry.startAnimation(firstItem);
                tvHeader.startAnimation(secondItemGroup1);
                tvExplanation.startAnimation(secondItemGroup2);
            }

        private void callSetToCountryDetails(Country country)
            {
                Activity activity = getActivity();
                if (activity instanceof MainActivity)
                    ((MainActivity) activity).setToCountryDetailsFragment(country);
                else
                    Timber.w("Activity is not BaseActivity, cannot call setToCountryDetailsFragment()");
            }

        @Override
        public void onCountryClick(Country country)
            {
                dialogFragmentCountryList.stopObservingOnCountryClick(this);
                callSetToCountryDetails(country);
            }
    }
