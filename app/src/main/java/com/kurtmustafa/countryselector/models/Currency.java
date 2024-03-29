package com.kurtmustafa.countryselector.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency implements Parcelable
    {
        @Expose
        @SerializedName("code")
        private String code;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("symbol")
        private String symbol;

        public Currency(String code, String name, String symbol)
            {
                this.code = code;
                this.name = name;
                this.symbol = symbol;
            }

        @Override
        public String toString()
            {
                return "Currency{" +
                        "code='" + code + '\'' +
                        ", name='" + name + '\'' +
                        ", symbol='" + symbol + '\'' +
                        '}';
            }

        public String getCode()
            {
                return code;
            }

        public void setCode(String code)
            {
                this.code = code;
            }

        public String getName()
            {
                return name;
            }

        public void setName(String name)
            {
                this.name = name;
            }

        public String getSymbol()
            {
                return symbol;
            }

        public void setSymbol(String symbol)
            {
                this.symbol = symbol;
            }


        @Override
        public int describeContents()
            {
                return 0;
            }

        @Override
        public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeString(this.code);
                dest.writeString(this.name);
                dest.writeString(this.symbol);
            }

        protected Currency(Parcel in)
            {
                this.code = in.readString();
                this.name = in.readString();
                this.symbol = in.readString();
            }

        public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>()
            {
                @Override
                public Currency createFromParcel(Parcel source)
                    {
                        return new Currency(source);
                    }

                @Override
                public Currency[] newArray(int size)
                    {
                        return new Currency[size];
                    }
            };
    }
