package com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.di.ViewModelProviderFactory;
import com.kurtmustafa.countryselector.models.Country;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.android.support.DaggerAppCompatDialogFragment;
import timber.log.Timber;

public class DialogFragmentCountryList extends DaggerAppCompatDialogFragment implements OnCountryClickListener
    {

        public static final String TAG = "DialogFragmentCountryList";
        private CountryRecyclerAdapter countryRecyclerAdapter;
        private FastScrollRecyclerView recyclerViewCountryList;
        private List<OnCountryClickListener> countryListenerHolderList = new ArrayList<>();

        @Inject
        ViewModelProviderFactory providerFactory;
        private DialogCountryListViewModel dialogCountryListViewModel;


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
            {
                super.onActivityCreated(savedInstanceState);
                dialogCountryListViewModel = ViewModelProviders.of(this, providerFactory).get(DialogCountryListViewModel.class);
                getCountryList();
                registerCountryClickObservers();
            }


        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
            {
                if (getActivity() != null && getContext() != null)
                    {

                        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_country_list, new RelativeLayout(getActivity()), false);

                        initViews(view);
                        initRecyclerView();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.CountrySelector_DialogFragment);
                        dialog.setCancelable(true);
                        dialog.setView(view);

                        return dialog.create();

                    } else
                    {
                        Timber.e("getContext() returned NULL, cannot return the Dialog");
                        return super.onCreateDialog(savedInstanceState);
                    }
            }




        /**
         * Adds a  listener who wants to get notified when a country is clicked
         * Adding to temporary list in order to call register observers from ViewModel after ViewModel is initialized in onActivityCreate.
         * @param onCountryClickListener Listener to be notified when a country from the dialog country list is clicked
         */
        public void observeOnCountryClick(OnCountryClickListener onCountryClickListener)
            {
                if(!countryListenerHolderList.contains(onCountryClickListener))
                    countryListenerHolderList.add(onCountryClickListener);

            }


        public void stopObservingOnCountryClick(OnCountryClickListener onCountryClickListener)
            {
                if(dialogCountryListViewModel !=null)
               dialogCountryListViewModel.unregisterCountryClickObservers(onCountryClickListener);
                else
                    Timber.w("Viewmodel is null, cannot unregister observer");

            }



        private void initRecyclerView()
            {
                countryRecyclerAdapter = new CountryRecyclerAdapter(dialogCountryListViewModel);
                if (getContext() != null)
                    recyclerViewCountryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                recyclerViewCountryList.setAdapter(countryRecyclerAdapter);

                recyclerViewCountryList.setLayoutManager(new LinearLayoutManager(getContext()));

            }

        private void initViews(@NonNull View view)
            {
                recyclerViewCountryList = view.findViewById(R.id.country_list_recycler_view);

            }

        private void getCountryList()
            {

                dialogCountryListViewModel.getCountries().observe(this, countryList -> {
                    if (countryList != null)
                        {
                            countryRecyclerAdapter.setCountryList(countryList);
                        } else
                        Timber.w("Retrieved country list is NULL");

                });

            }

        /**
         * Registers the listeners who got passed by via {@link #observeOnCountryClick(OnCountryClickListener)}
         */
        private void registerCountryClickObservers()
            {
                if( dialogCountryListViewModel != null)
                    {
                        if(!countryListenerHolderList.contains(this))
                            countryListenerHolderList.add(this);
                        dialogCountryListViewModel.registerCountryClickObservers(countryListenerHolderList);
                    }
                else
                    Timber.e(" viewmodel is null, cannot register countryclick observers");
            }


        @Override
        public void onStop()
            {
                super.onStop();
                dismissAllowingStateLoss(); //Prevents the null onCountryClick listener in the adapter when fragment gets recreated
            }

        @Override
        public void onCountryClick(Country country)
            {
                dismiss();
            }
    }
