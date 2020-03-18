package net.growdev.userojekonline.network;

import net.growdev.userojekonline.model.modelauth.ResponseAuth;
import net.growdev.userojekonline.model.modelmap.ResponseMap;
import net.growdev.userojekonline.model.modelreqorder.ResponseBooking;
import net.growdev.userojekonline.model.modeltracking.ResponseTracking;
import net.growdev.userojekonline.model.modelwaiting.ResponseWaiting;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("daftar")
    Call<ResponseAuth> register(@Field("nama" )String nama,
                                @Field("email" )String email,
                                @Field("password") String password,
                                @Field("phone") String phone);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseAuth> login(@Field("device") String device,
                             @Field("f_email") String email,
                             @Field("f_password") String password);

    @GET("json")
    Call<ResponseMap> getDataMap(@Query("origin") String origin,
                                 @Query("destination") String destination,
                                 @Query("key") String key);

    @FormUrlEncoded
    @POST("insert_booking")
    Call<ResponseBooking> insertBooking(@Field("f_idUser") String idUser,
                                        @Field("f_latAwal") String latAwal,
                                        @Field("f_lngAwal") String lonAwal,
                                        @Field("f_awal") String lokasiAwal,
                                        @Field("f_latAkhir") String latTujuan,
                                        @Field("f_lngAkhir") String lonTujuan,
                                        @Field("f_akhir") String lokasiTujuan,
                                        @Field("f_catatan") String catatan,
                                        @Field("f_jarak") String jarak,
                                        @Field("f_token") String token,
                                        @Field("f_device") String device);

    @FormUrlEncoded
    @POST("checkBooking")
    Call<ResponseWaiting> cekStatusBooking(@Field("idbooking") String idBooking);

    @FormUrlEncoded
    @POST("cancel_booking")
    Call<ResponseWaiting> cancelBooking(@Field("idBooking") String idBooking,
                                        @Field("f_token") String token,
                                        @Field("f_device") String device);

    @FormUrlEncoded
    @POST("get_driver")
    Call<ResponseTracking> getTracking(@Field("f_iddriver") String idDriver);

}
