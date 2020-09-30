package com.daffodil.assignment.ui.input_user_details.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daffodil.assignment.R;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.ui.input_user_details.view_model.UserDetailViewModel;
import com.daffodil.assignment.ui.upload_docs.view.UploadDocActivity;
import com.daffodil.assignment.utilities.Utility;

public class AddUserDetailActivity extends AppCompatActivity {

    private EditText userNameEdt;
    private EditText mobileEdt;
    private EditText emailEdt;
    private Button saveBtn;
    private UserDetailViewModel userDetailViewModel;
    private String latLng, place;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user_detail);
        userDetailViewModel = ViewModelProviders.of(this).get(UserDetailViewModel.class);
        latLng = getIntent().getStringExtra(AppConstants.LAT_LNG);
        place = getIntent().getStringExtra(AppConstants.PLACE);
        initializeView();
        subscribeToLiveData();
    }

    private void initializeView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.user_details));
        }
        userNameEdt = findViewById(R.id.user_name_edt);
        mobileEdt = findViewById(R.id.mobile_edt);
        emailEdt = findViewById(R.id.email_edt);
        saveBtn = findViewById(R.id.save_btn);

        saveBtn.setOnClickListener(v -> {
            userDetailViewModel.saveDataInDB(userNameEdt, mobileEdt, emailEdt, latLng, place);
        });

    }

    private void clearEdt() {
        userNameEdt.setText("");
        mobileEdt.setText("");
        emailEdt.setText("");
    }

    private void subscribeToLiveData() {

        userDetailViewModel.dataSaved.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long row) {
                if (row != null && row > 0) {
                    userId = String.valueOf(row);
                    Toast.makeText(AddUserDetailActivity.this, R.string.data_saved, Toast.LENGTH_SHORT).show();
                    clearEdt();
                    Utility.showLoader(AddUserDetailActivity.this);
                    userDetailViewModel.fetchUserDetail(row);
                    if (userId != null) {
                        Intent intent = new Intent(AddUserDetailActivity.this, UploadDocActivity.class);
                        intent.putExtra(AppConstants.USER_ID, userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddUserDetailActivity.this, getString(R.string.enter_user_details), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}