package com.loftschool.fomin.moneyloft;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET
    Call<AuthResponse> auth(@Query("social_id") String userID);

//    @GET
//    Call<> getItems(@Query("type") String type, @Query("auth-token") String token);


}
