package com.app.buy.Helper;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v3.1/all")  // Fetch all countries
    Call<List<Country>> getCountries();
}
