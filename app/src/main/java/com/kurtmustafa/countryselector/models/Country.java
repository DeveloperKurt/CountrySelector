package com.kurtmustafa.countryselector.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Purpose of this class is to be displayed in the countries list.
 */
public class Country implements Parcelable
    {
        private String name;
        private Bitmap flag;
        private String code;

        public Country(String name, Bitmap flag, String code)
            {
                this.name = name;
                this.flag = flag;
                this.code = code;
            }

        @Override
        public String toString()
            {
                return "Country{" +
                        "name='" + name + '\'' +
                        ", flag=" + flag +
                        ", code='" + code + '\'' +
                        '}';
            }

        public String getName()
            {
                return name;
            }

        public void setName(String name)
            {
                this.name = name;
            }

        public Bitmap getFlag()
            {
                return flag;
            }

        public void setFlag(Bitmap flag)
            {
                this.flag = flag;
            }

        public String getCode()
            {
                return code;
            }

        public void setCode(String code)
            {
                this.code = code;
            }


        @Override
        public int describeContents()
            {
                return 0;
            }

        @Override
        public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeString(this.name);
                dest.writeParcelable(this.flag, flags);
                dest.writeString(this.code);
            }

        protected Country(Parcel in)
            {
                this.name = in.readString();
                this.flag = in.readParcelable(Bitmap.class.getClassLoader());
                this.code = in.readString();
            }

        public static final Creator<Country> CREATOR = new Creator<Country>()
            {
                @Override
                public Country createFromParcel(Parcel source)
                    {
                        return new Country(source);
                    }

                @Override
                public Country[] newArray(int size)
                    {
                        return new Country[size];
                    }
            };
    }
