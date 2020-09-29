package com.daffodil.assignment.network;


import com.daffodil.assignment.ui.upload_docs.model.UploadImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("file")
    Call<UploadImageResponse> uploadImage(@Part MultipartBody.Part documents);
}
