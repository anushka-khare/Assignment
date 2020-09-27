package com.daffodil.assignment.ui.upload_docs.view_model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.daffodil.assignment.App;
import com.daffodil.assignment.R;
import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo;
import com.daffodil.assignment.ui.input_user_details.repo.UserDataHelper;
import com.daffodil.assignment.ui.upload_docs.model.UploadImageResponse;
import com.daffodil.assignment.ui.upload_docs.repo.UploadRepo;
import com.daffodil.assignment.utilities.Utility;

import java.io.File;

public class UploadDocViewModel extends BaseViewModelImp {

    private UploadRepo uploadRepo;
    public MutableLiveData<String> uploadedUrlLiveData;
    private SaveDataRepo saveDataRepo;

    public UploadDocViewModel(@NonNull Application application) {
        super(application);
        uploadRepo = new UploadRepo(((App)application).getAppModule().provideRetrofit());
        saveDataRepo = new SaveDataRepo(UserDataHelper.getUserDataHelper(application));
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
                uploadedUrlLiveData.setValue(data.getDocuments());
                Utility.dismissLoader();
            }
        }

        @Override
        public void onError(ErrorResponse errorResponse) {

            Toast.makeText(getApplication(), errorResponse.getDetail(), Toast.LENGTH_SHORT).show();
            Utility.dismissLoader();
        }
    };

    public void saveImagePathInDB(String imgPath, String userId){
        long row = saveDataRepo.insertUserImage(userId,imgPath);
        if(row > 0){
            Toast.makeText(getApplication(),getApplication().getString(R.string.data_saved),Toast.LENGTH_SHORT).show();
        }
    }


}
