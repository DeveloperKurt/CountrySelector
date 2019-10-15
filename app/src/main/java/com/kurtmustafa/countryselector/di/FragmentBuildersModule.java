package com.kurtmustafa.countryselector.di;

import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogFragmentCountryList;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.FragmentCountryDetails;
import com.kurtmustafa.countryselector.ui.fragmentpickcountry.FragmentPickCountry;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule
    {
        @ContributesAndroidInjector
        abstract DialogFragmentCountryList contributeDialogFragmentCountryList();

        @ContributesAndroidInjector
        abstract FragmentPickCountry  contributeFragmentPickCountry();

        @ContributesAndroidInjector
        abstract FragmentCountryDetails contributeFragmentCountryDetails();
    }
