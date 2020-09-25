package com.daffodil.assignment.ui.upload_docs.repo;

import androidx.annotation.NonNull;

import com.daffodil.assignment.base.BaseRepoImpl;
import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.network.ApiService;
import com.daffodil.assignment.network.ResponseWrapper;
import com.daffodil.assignment.network.RetrofitCallback;
import com.daffodil.assignment.ui.upload_docs.model.UploadImageResponse;
import com.daffodil.assignment.utilities.Utility;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UploadRepo extends BaseRepoImpl {

    private BaseViewModelImp.ViewModelCallback<UploadImageResponse> mUploadImageViewModelCallback;

    public UploadRepo(ApiService apiService) {
        super(apiService);
    }


    public void uploadImage(BaseViewModelImp.ViewModelCallback<UploadImageResponse> callback, File file) {

        MultipartBody.Part multiPartFile = null;
        mUploadImageViewModelCallback = callback;
        if (file != null) {
            String mimeType = Utility.getMimeTypeByExtension(file.getName().substring(file.getName().lastIndexOf('.') + 1));
            RequestBody reqFile = RequestBody.create(MediaType.parse(mimeType), file);
            multiPartFile = MultipartBody.Part.createFormData("documents", file.getName(), reqFile);
        }

        Call<UploadImageResponse> call= mApiService.uploadImage(multiPartFile);
        call.enqueue(new ResponseWrapper<>(uploadImageRetrofitCallback));
    }

    private RetrofitCallback<UploadImageResponse> uploadImageRetrofitCallback = new RetrofitCallback<UploadImageResponse>() {
        @Override
        public void onSuccess(@NonNull UploadImageResponse data) {
            mUploadImageViewModelCallback.onSuccess(data);
        }

        @Override
        public void onFailure(@NonNull ErrorResponse errorResponse) {
            mUploadImageViewModelCallback.onError(errorResponse);
        }
    };
}
