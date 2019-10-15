package com.kurtmustafa.countryselector.ui;


import android.os.Bundle;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.models.Country;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.FragmentCountryDetails;
import com.kurtmustafa.countryselector.ui.fragmentpickcountry.FragmentPickCountry;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends FragmentActivity //ViewModelProviders requires to use FragmentActivity
    {
        public enum ActiveFragment
            {
                PICK_COUNTRY_FRAGMENT,
                COUNTRY_DETAILS_FRAGMENT
            }

        private static final String BUNDLE_KEY_ACTIVE_FRAGMENT = "BUNDLE_KEY_ACTIVE_FRAGMENT";



        private ActiveFragment activeFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                if (savedInstanceState == null)
                    setToPickCountryFragment();

                setTheme(R.style.CountrySelector_DARK);
            }

        /**
         * Prevents MainActivity layout from getting displayed when back button is pressed within a fragment.<br>
         * Which is merely an empty view, a placeholder for fragments.
         */
        @Override
        public void onBackPressed()
            {

                switch (activeFragment)
                    {
                        case PICK_COUNTRY_FRAGMENT:
                            this.finish();
                            break;
                        case COUNTRY_DETAILS_FRAGMENT:
                            activeFragment = ActiveFragment.PICK_COUNTRY_FRAGMENT;
                            super.onBackPressed();
                            break;
                        default:
                            super.onBackPressed();

                    }
            }


        public void setToPickCountryFragment()
            {
                FragmentPickCountry fragmentPickCountry = new FragmentPickCountry();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_base_frame_layout_container, fragmentPickCountry,FragmentPickCountry.TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                activeFragment = ActiveFragment.PICK_COUNTRY_FRAGMENT;

            }

        public void setToCountryDetailsFragment(Country country)
            {
                FragmentCountryDetails fragmentCountryDetails = FragmentCountryDetails.newInstance(country);


                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_down,R.anim.slide_out_up);
                fragmentTransaction.replace(R.id.activity_base_frame_layout_container, fragmentCountryDetails,FragmentCountryDetails.TAG);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                activeFragment = ActiveFragment.COUNTRY_DETAILS_FRAGMENT;
            }

        @Override
        protected void onSaveInstanceState(@NonNull Bundle outState)
            {
                super.onSaveInstanceState(outState);
                outState.putSerializable(BUNDLE_KEY_ACTIVE_FRAGMENT, activeFragment);

            }

        @Override
        protected void onRestoreInstanceState(Bundle savedInstanceState)
            {
                super.onRestoreInstanceState(savedInstanceState);
                activeFragment = (ActiveFragment) savedInstanceState.get(BUNDLE_KEY_ACTIVE_FRAGMENT);
            }

        public ActiveFragment getActiveFragment()
            {
                return activeFragment;
            }
    }
