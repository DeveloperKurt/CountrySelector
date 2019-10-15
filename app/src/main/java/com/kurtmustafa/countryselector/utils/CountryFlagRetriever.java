package com.kurtmustafa.countryselector.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;


/**
 * This class can be used to retrieve countries' flag from local resources by a country's 2 letter code by calling {@link #getCountryFlag()}.
 */
public class CountryFlagRetriever
    {
        /**
         * Some keywords are reserved by Java, thus, cannot be used as a resource name. "_reserved" added at the and of the name of these resources
         */
        private  final String[] RESERVED_RESOURCE_CODES = {"do"};

        private Context context;
        private String countryCode;

        public CountryFlagRetriever(@NonNull Context context, String countryCode)
            {
                this.context = context;
                this.countryCode = countryCode;
            }

        /**
         * @return Returns the country's flag in a {@link Bitmap} format. If the resource could not be found, return value will be null.
         */
        @Nullable
        public Bitmap getCountryFlag()
            {
                countryCode = formatCodeForResources();

                int flagResourceId = context.getResources().getIdentifier(countryCode, "drawable", context.getPackageName());

                if (flagResourceId == 0)
                    {
                        Timber.w("Flag could not be found in the resources");
                        return null;
                    } else
                    {
                        Timber.v("Returning flag resource file, code & id:[%s : %d]", countryCode, flagResourceId);
                        return BitmapFactory.decodeResource(context.getResources(), flagResourceId);
                    }
            }

        /**
         * Some keywords are reserved by Java, therefore they cannot be used as resource file names.
         * These files have "_reserved" at the end of their names.
         * To access them by their name, this class appends "_reserved" to the country code.
         *
         * @return usable resource country code name
         */
        private  String formatCodeForResources()
            {
                countryCode = countryCode.toLowerCase();

                for (String code : RESERVED_RESOURCE_CODES)
                    {
                        if (countryCode.equals(code))
                            {

                                Timber.i("Found exceptional country code, appending '_reserved' to : %s", countryCode);
                                return countryCode + "_reserved";
                            }
                    }

                return countryCode;
            }

    }
