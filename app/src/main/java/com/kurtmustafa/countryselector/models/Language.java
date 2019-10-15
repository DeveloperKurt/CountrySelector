package com.kurtmustafa.countryselector.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language implements Parcelable
    {
        @Expose
        @SerializedName("iso639_1")
        String abbreviation1;
        @Expose
        @SerializedName("iso639_2")
        String abbreviation2;
        @Expose
        @SerializedName("name")
        String name;
        @Expose
        @SerializedName("nativeName")
        String nativeName;

        public Language(String abbreviation1, String abbreviation2, String name, String nativeName)
            {
                this.abbreviation1 = abbreviation1;
                this.abbreviation2 = abbreviation2;
                this.name = name;
                this.nativeName = nativeName;
            }

        @Override
        public String toString()
            {
                return "Language{" +
                        "abbreviation1='" + abbreviation1 + '\'' +
                        ", abbreviation2='" + abbreviation2 + '\'' +
                        ", name='" + name + '\'' +
                        ", nativeName='" + nativeName + '\'' +
                        '}';
            }

        public String getAbbreviation1()
            {
                return abbreviation1;
            }

        public void setAbbreviation1(String abbreviation1)
            {
                this.abbreviation1 = abbreviation1;
            }

        public String getAbbreviation2()
            {
                return abbreviation2;
            }

        public void setAbbreviation2(String abbreviation2)
            {
                this.abbreviation2 = abbreviation2;
            }

        public String getName()
            {
                return name;
            }

        public void setName(String name)
            {
                this.name = name;
            }

        public String getNativeName()
            {
                return nativeName;
            }

        public void setNativeName(String nativeName)
            {
                this.nativeName = nativeName;
            }


        @Override
        public int describeContents()
            {
                return 0;
            }

        @Override
        public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeString(this.abbreviation1);
                dest.writeString(this.abbreviation2);
                dest.writeString(this.name);
                dest.writeString(this.nativeName);
            }

        protected Language(Parcel in)
            {
                this.abbreviation1 = in.readString();
                this.abbreviation2 = in.readString();
                this.name = in.readString();
                this.nativeName = in.readString();
            }

        public static final Parcelable.Creator<Language> CREATOR = new Parcelable.Creator<Language>()
            {
                @Override
                public Language createFromParcel(Parcel source)
                    {
                        return new Language(source);
                    }

                @Override
                public Language[] newArray(int size)
                    {
                        return new Language[size];
                    }
            };
    }
