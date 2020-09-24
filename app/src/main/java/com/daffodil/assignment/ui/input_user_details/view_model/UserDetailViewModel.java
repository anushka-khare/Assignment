package com.daffodil.assignment.ui.input_user_details.view_model;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.ui.input_user_details.model.UserDetail;
import com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo;
import com.daffodil.assignment.ui.input_user_details.repo.UserDataHelper;

public class UserDetailViewModel extends BaseViewModelImp {

    private SaveDataRepo saveDataRepo;
    public MutableLiveData<Long> dataSaved;
    public MutableLiveData<UserDetail> userDetailLiveData;

    public UserDetailViewModel(@NonNull Application application) {
        super(application);
        saveDataRepo = new SaveDataRepo(new UserDataHelper(application.getApplicationContext()));
        dataSaved = new MutableLiveData<>();
        userDetailLiveData = new MutableLiveData<>();
    }

    public void saveDataInDB(AppCompatEditText nameEdt, AppCompatEditText mobileEdt, AppCompatEditText emailEdt) {

        if (isAllFieldsValid(nameEdt, mobileEdt, emailEdt)) {
            long row = saveDataRepo.insertUserData(nameEdt.getText().toString(), mobileEdt.getText().toString(), emailEdt.getText().toString());

            if (row > 0) {

                dataSaved.setValue(row);
            }
        }

    }

    private boolean isAllFieldsValid(AppCompatEditText nameEdt, AppCompatEditText mobileEdt, AppCompatEditText emailEdt) {

        if (TextUtils.isEmpty(nameEdt.getText()))
            return false;
        if (TextUtils.isEmpty(mobileEdt.getText()))
            return false;
        if (TextUtils.isEmpty(emailEdt.getText()))
            return false;
        return true;
    }


    public void fetchUserDetail(long row) {

        saveDataRepo.fetchUser(row, new ViewModelCallback<UserDetail>() {
            @Override
            public void onSuccess(UserDetail data) {
                userDetailLiveData.setValue(data);
            }

            @Override
            public void onError(ErrorResponse errorResponse) {

                Toast.makeText(getApplication().getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
