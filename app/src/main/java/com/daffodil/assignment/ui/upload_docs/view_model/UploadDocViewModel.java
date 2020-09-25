package com.daffodil.assignment.ui.upload_docs.view_model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.daffodil.assignment.R;
import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.network.AppModule;
import com.daffodil.assignment.ui.upload_docs.model.UploadImageResponse;
import com.daffodil.assignment.ui.upload_docs.repo.UploadRepo;
import com.daffodil.assignment.utilities.Utility;

import java.io.File;

import retrofit2.Call;

public class UploadDocViewModel extends BaseViewModelImp {

    private UploadRepo uploadRepo;
    public MutableLiveData<String> uploadedUrlLiveData;

    public UploadDocViewModel(@NonNull Application application) {
        super(application);
        uploadRepo = new UploadRepo(new AppModule().provideRetrofit());
        uploadedUrlLiveData = new MutableLiveData<>();
    }


    public void uploadImageForChat(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.length() > 0) {
                if (file.length() <= getApplication().getResources().getInteger(R.integer.max_file_size)) {
                    uploadRepo.uploadImage(uploadImageViewModelCallback, file);
                } else {
                    Utility.dismissLoader();
                    Toast.makeText(getApplication(), getApplication().getString(R.string.file_size_msg), Toast.LENGTH_SHORT).show();
                }
            } else {
                Utility.dismissLoader();
                Toast.makeText(getApplication(), getApplication().getString(R.string.empty_file_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ViewModelCallback<UploadImageResponse> uploadImageViewModelCallback = new ViewModelCallback<UploadImageResponse>() {
        @Override
        public void onSuccess(UploadImageResponse data) {

            if (data != null && data.getDocuments() != null) {
                uploadedUrlLiveData.postValue(data.getDocuments());
                Utility.dismissLoader();
            }
        }

        @Override
        public void onError(ErrorResponse errorResponse) {

            Toast.makeText(getApplication(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
            Utility.dismissLoader();
        }
    };


}
