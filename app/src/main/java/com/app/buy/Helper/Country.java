package com.app.buy.Helper;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("name")
    private Name name;

    public String getCountryName() {
        return name.common;
    }

    public static class Name {
        @SerializedName("common")
        public String common;
    }
}
