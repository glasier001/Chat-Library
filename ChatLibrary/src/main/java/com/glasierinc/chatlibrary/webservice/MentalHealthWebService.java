package com.glasierinc.chatlibrary.webservice;



import com.glasierinc.chatlibrary.chat.model.GetFileUploadLinkResp;
import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomGetLoginTypeResp;
import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomGetUserList;
import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomListResp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface MentalHealthWebService {



    @FormUrlEncoded
    @POST("get_login_type")
    Call<ClassroomGetLoginTypeResp> classroomGetLoginType(@Field("email") String email, @Field("fcm_token") String fcm_token, @Field("app_name") String app_name, @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("class_room_list")
    Call<ClassroomListResp> getClassroomList(@Field("user_id") String user_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("get_classroom_user_list")
    Call<ClassroomGetUserList> classroomGetUserLIst(@Field("room_id") String room_id);

    @Multipart
    @POST("upload_attach_file")
    Call<GetFileUploadLinkResp> getImageUrl(
            @Part("room_id") RequestBody room_id,
            @Part("user_id") RequestBody user_id,
            @Part("type") RequestBody type,
            @Part("file_type") RequestBody file_type,
            @Part MultipartBody.Part attach_file);

}