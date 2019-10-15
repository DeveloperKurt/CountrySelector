package com.kurtmustafa.countryselector.di;

import com.kurtmustafa.countryselector.di.qualifiers.ViewModelKey;
import com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist.DialogCountryListViewModel;
import com.kurtmustafa.countryselector.ui.fragmentcountrydetails.CountryDetailsViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelFactoryModule
    {

        @Binds
        public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

        @Binds
        @IntoMap
        @ViewModelKey(DialogCountryListViewModel.class)
        public abstract ViewModel bindDialogCountryListViewModel(DialogCountryListViewModel countryListViewModel);


        @Binds
        @IntoMap
        @ViewModelKey(CountryDetailsViewModel.class)
        public abstract ViewModel bindCountryDetailsViewModel(CountryDetailsViewModel countryDetailsViewModel);

    }
