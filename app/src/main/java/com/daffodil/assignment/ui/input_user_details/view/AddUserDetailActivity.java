package com.daffodil.assignment.ui.input_user_details.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daffodil.assignment.R;
import com.daffodil.assignment.ui.input_user_details.model.UserDetail;
import com.daffodil.assignment.ui.input_user_details.view_model.UserDetailViewModel;

public class AddUserDetailActivity extends AppCompatActivity {

    private AppCompatEditText userNameEdt;
    private AppCompatEditText mobileEdt;
    private AppCompatEditText emailEdt;
    private AppCompatTextView userNameTv;
    private AppCompatTextView mobileTv;
    private AppCompatTextView emailTv;
    private AppCompatButton saveBtn, clearBtn;
    private UserDetailViewModel userDetailViewModel;
    private Long recentlySavedRow ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user_detail);
        userDetailViewModel = ViewModelProviders.of(this).get(UserDetailViewModel.class);
        initializeView();
        subscribeToLiveData();
    }

    private void initializeView() {

        userNameEdt = findViewById(R.id.user_name_edt);
        mobileEdt = findViewById(R.id.mobile_edt);
        emailEdt = findViewById(R.id.email_edt);
        saveBtn = findViewById(R.id.save_btn);
        clearBtn = findViewById(R.id.clear_btn);
        userNameTv = findViewById(R.id.user_name_tv);
        mobileTv = findViewById(R.id.mobile_tv);
        emailTv = findViewById(R.id.email_tv);

        saveBtn.setOnClickListener(v -> {
            userDetailViewModel.saveDataInDB(userNameEdt, mobileEdt, emailEdt);
        });

        clearBtn.setOnClickListener(v -> {
            clearEdt();
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
                    recentlySavedRow = row;
                    Toast.makeText(AddUserDetailActivity.this, R.string.data_saved, Toast.LENGTH_SHORT).show();
                    clearEdt();
                    userDetailViewModel.fetchUserDetail(row);
                }
            }
        });

        userDetailViewModel.userDetailLiveData.observe(this, new Observer<UserDetail>() {
            @Override
            public void onChanged(UserDetail userDetail) {
                if(userDetail != null){
                    userNameTv.setText(userDetail.getName());
                    mobileTv.setText(userDetail.getMobile());
                    emailTv.setText(userDetail.getEmail());
                }
            }
        });
    }


}
