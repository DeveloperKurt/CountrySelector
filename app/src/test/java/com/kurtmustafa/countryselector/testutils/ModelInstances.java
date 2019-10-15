package com.kurtmustafa.countryselector.testutils;


import com.kurtmustafa.countryselector.models.CountryDetails;
import com.kurtmustafa.countryselector.models.Currency;
import com.kurtmustafa.countryselector.models.Language;



public class ModelInstances
    {
        private static final String[] timezones_SE = {"UTC+01:00"};
        private static final Currency[] currencies_SE = {new Currency("SEK", "Swedish krona", "kr")};
        private static final Language[] languages_SE = {new Language("sv", "swe", "Swedish", "svenska")};

        public static final CountryDetails SwedenCountryDetails = new CountryDetails("Europe", "Northern Europe", "Stockholm", 9894888,
                timezones_SE, currencies_SE, languages_SE);





    }
