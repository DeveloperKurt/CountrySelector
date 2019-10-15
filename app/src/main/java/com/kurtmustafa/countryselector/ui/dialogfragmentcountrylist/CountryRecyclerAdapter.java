package com.kurtmustafa.countryselector.ui.dialogfragmentcountrylist;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kurtmustafa.countryselector.R;
import com.kurtmustafa.countryselector.models.Country;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;


public class CountryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter
    {


        private List<Country> countryList;
        private OnCountryClickListener onCountryClickListener;

         CountryRecyclerAdapter(OnCountryClickListener onCountryClickListener)
            {

                this.onCountryClickListener = onCountryClickListener;

            }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_country, viewGroup, false);
                return new CountryViewHolder(view, onCountryClickListener);


            }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {

                ((CountryViewHolder) viewHolder).tvName.setText(countryList.get(i).getName());
                ((CountryViewHolder) viewHolder).ivFlag.setImageBitmap(countryList.get(i).getFlag());
            }


        @Override
        public int getItemCount()
            {
                if (countryList != null)
                    {
                        return countryList.size();
                    }
                return 0;
            }


         void setCountryList(List<Country> countryList)
            {
                this.countryList = countryList;
                notifyDataSetChanged();
            }

        @NonNull
        @Override
        public String getSectionName(int position)
            {
                return (countryList.get(position).getName().substring(0, 1)); //Get first letter of the country name
            }

        class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            {
                ImageView ivFlag;
                TextView tvName;
                OnCountryClickListener onCountryClickListener;

                 CountryViewHolder(@NonNull View itemView, OnCountryClickListener onCountryClickListener)
                    {
                        super(itemView);
                        this.onCountryClickListener = onCountryClickListener;

                        ivFlag = itemView.findViewById(R.id.country_iv_flag);
                        tvName = itemView.findViewById(R.id.country_tv_name);

                        itemView.setOnClickListener(this);
                    }

                @Override
                public void onClick(View v)
                    {
                        if( CountryRecyclerAdapter.this.onCountryClickListener!= null)
                            {
                                Country country = countryList.get(this.getAdapterPosition());
                                CountryRecyclerAdapter.this.onCountryClickListener.onCountryClick(country);
                                Timber.i("Clicked on: %s", country.toString());
                            }
                    }
            }


    }
