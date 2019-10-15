package com.kurtmustafa.countryselector.utils;

import com.kurtmustafa.countryselector.models.Country;

import java.util.Collections;
import java.util.List;

public class CountrySorter
    {
        /**
         * Since we are getting country list from a JSON file and the JSON objects are defined as: <br>
         * "An object is an unordered set of name/value pairs" in <a href="http://www.json.org/"> json.org </a>
         * <p/>
         * The object orders might get mixed up during the parsing, therefor this method must be called before displaying the countries.
         *
         * @param countryList {@link Country}List to sort
         */
        public static void sortAlphabetically(List<Country> countryList)
            {
                Collections.sort(countryList, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
            }
    }
