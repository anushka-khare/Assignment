package com.daffodil.assignment.ui.input_user_details.view_model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.MutableLiveData;

import com.daffodil.assignment.R;
import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.base.ErrorResponse;
import com.daffodil.assignment.ui.input_user_details.model.UserDetail;
import com.daffodil.assignment.ui.input_user_details.repo.SaveDataRepo;
import com.daffodil.assignment.ui.input_user_details.repo.UserDataHelper;
import com.daffodil.assignment.utilities.Utility;
import com.daffodil.assignment.utilities.ValidationUtil;

public class UserDetailViewModel extends BaseViewModelImp {

    private SaveDataRepo saveDataRepo;
    public MutableLiveData<Long> dataSaved;
    public MutableLiveData<UserDetail> userDetailLiveData;

    public UserDetailViewModel(@NonNull Application application) {
        super(application);
        saveDataRepo = new SaveDataRepo(UserDataHelper.getUserDataHelper(application.getApplicationContext()));
        dataSaved = new MutableLiveData<>();
        userDetailLiveData = new MutableLiveData<>();
    }

    public void saveDataInDB(AppCompatEditText nameEdt, AppCompatEditText mobileEdt, AppCompatEditText emailEdt, String latLng, String place) {

        if (isAllFieldsValid(nameEdt, mobileEdt, emailEdt)) {
            long row = saveDataRepo.insertUserData(nameEdt.getText().toString(), mobileEdt.getText().toString(), emailEdt.getText().toString(), latLng, place);

            if (row > 0) {

                dataSaved.setValue(row);
            }
        }

    }

    private boolean isAllFieldsValid(AppCompatEditText nameEdt, AppCompatEditText mobileEdt, AppCompatEditText emailEdt) {

        if (!ValidationUtil.hasText(getApplication(), nameEdt, R.string.is_empty))
            return false;
        if (!ValidationUtil.isValidMobile(getApplication(), mobileEdt, true))
            return false;
        return ValidationUtil.isEmailAddress(getApplication(), emailEdt, true);
    }


    public void fetchUserDetail(long row) {

        saveDataRepo.fetchUser(row, new ViewModelCallback<UserDetail>() {
            @Override
            public void onSuccess(UserDetail data) {
                userDetailLiveData.setValue(data);
                Utility.dismissLoader();
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                Utility.dismissLoader();
                Toast.makeText(getApplication().getApplicationContext(), errorResponse.getDetail(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserDetailWithImg(String userId) {

        saveDataRepo.getUserDetailWithImg(userId, new ViewModelCallback<UserDetail>() {
            @Override
            public void onSuccess(UserDetail data) {
                userDetailLiveData.setValue(data);
                Utility.dismissLoader();
            }

            @Override
            public void onError(ErrorResponse errorResponse) {
                Toast.makeText(getApplication().getApplicationContext(), errorResponse.getDetail(), Toast.LENGTH_SHORT).show();
                Utility.dismissLoader();
            }
        });

    }
}
