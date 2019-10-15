package com.kurtmustafa.countryselector.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class CountryDetails implements Parcelable
    {


        @Expose
        @SerializedName("region")
        String region;
        @Expose
        @SerializedName("subregion")
        String subregion;
        @Expose
        @SerializedName("capital")
        String capital;
        @Expose
        @SerializedName("population")
        Integer population;

        @SerializedName("timezones")
        String[] timezones;

        @SerializedName("currencies")
        Currency[] currencies;

        @SerializedName("languages")
        Language[] languages;

        public CountryDetails(String region, String subregion, String capital, Integer population, String[] timezones, Currency[] currencies, Language[] languages)
            {

                this.region = region;
                this.subregion = subregion;
                this.capital = capital;
                this.population = population;
                this.timezones = timezones;
                this.currencies = currencies;
                this.languages = languages;
            }

        @Override
        public String toString()
            {
                return "CountryDetails{" +
                        ", region='" + region + '\'' +
                        ", subregion='" + subregion + '\'' +
                        ", capital='" + capital + '\'' +
                        ", population=" + population +
                        ", timezones=" + Arrays.toString(timezones) +
                        ", currencies=" + Arrays.toString(currencies) +
                        ", languages=" + Arrays.toString(languages) +
                        '}';
            }



        public String getRegion()
            {
                return region;
            }

        public void setRegion(String region)
            {
                this.region = region;
            }

        public String getSubregion()
            {
                return subregion;
            }

        public void setSubregion(String subregion)
            {
                this.subregion = subregion;
            }

        public String getCapital()
            {
                return capital;
            }

        public void setCapital(String capital)
            {
                this.capital = capital;
            }

        public Integer getPopulation()
            {
                return population;
            }

        public void setPopulation(Integer population)
            {
                this.population = population;
            }

        public String[] getTimezones()
            {
                return timezones;
            }

        public void setTimezones(String[] timezones)
            {
                this.timezones = timezones;
            }

        public Currency[] getCurrencies()
            {
                return currencies;
            }

        public void setCurrencies(Currency[] currencies)
            {
                this.currencies = currencies;
            }

        public Language[] getLanguages()
            {
                return languages;
            }

        public void setLanguages(Language[] languages)
            {
                this.languages = languages;
            }


        @Override
        public int describeContents()
            {
                return 0;
            }

        @Override
        public void writeToParcel(Parcel dest, int flags)
            {

                dest.writeString(this.region);
                dest.writeString(this.subregion);
                dest.writeString(this.capital);
                dest.writeValue(this.population);
                dest.writeStringArray(this.timezones);
                dest.writeTypedArray(this.currencies, flags);
                dest.writeTypedArray(this.languages, flags);
            }

        protected CountryDetails(Parcel in)
            {

                this.region = in.readString();
                this.subregion = in.readString();
                this.capital = in.readString();
                this.population = (Integer) in.readValue(Integer.class.getClassLoader());
                this.timezones = in.createStringArray();
                this.currencies = in.createTypedArray(Currency.CREATOR);
                this.languages = in.createTypedArray(Language.CREATOR);
            }

        public static final Creator<CountryDetails> CREATOR = new Creator<CountryDetails>()
            {
                @Override
                public CountryDetails createFromParcel(Parcel source)
                    {
                        return new CountryDetails(source);
                    }

                @Override
                public CountryDetails[] newArray(int size)
                    {
                        return new CountryDetails[size];
                    }
            };
    }
