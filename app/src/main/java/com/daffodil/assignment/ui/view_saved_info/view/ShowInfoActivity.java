package com.daffodil.assignment.ui.view_saved_info.view;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daffodil.assignment.R;
import com.daffodil.assignment.base.BaseViewModelImp;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.ui.input_user_details.model.UserDetail;
import com.daffodil.assignment.ui.input_user_details.view_model.UserDetailViewModel;
import com.daffodil.assignment.utilities.Utility;
import com.squareup.picasso.Picasso;

public class ShowInfoActivity extends AppCompatActivity {

    UserDetailViewModel userDetailViewModel;
    private TextView user_info_tv;
    private ImageView doc_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        String userId = getIntent().getStringExtra(AppConstants.USER_ID);
        initializeView();
        userDetailViewModel = ViewModelProviders.of(this).get(UserDetailViewModel.class);
        subscribeToLiveData();
        userDetailViewModel.getUserDetailWithImg(userId);
    }

    private void subscribeToLiveData() {
        userDetailViewModel.userDetailLiveData.observe(this, new Observer<UserDetail>() {
            @Override
            public void onChanged(UserDetail userDetail) {
                if (userDetail != null) {
                    StringBuilder userInfo = new StringBuilder("");

                    userInfo.append("<b>").append(getString(R.string.name)).append("</b> ").append(userDetail.getName())
                            .append("<br>").append("<b>").append(getString(R.string.mobile)).append("</b> ").append(userDetail.getMobile())
                            .append("<br>").append("<b>").append(getString(R.string.email)).append("</b> ").append(userDetail.getEmail())
                            .append("<br>").append("<b>").append(getString(R.string.location)).append("</b> ").append(userDetail.getLatLng())
                            .append("<br>").append("<b>").append(getString(R.string.place)).append("</b> ").append(userDetail.getPlace());

                    user_info_tv.setText(Html.fromHtml(userInfo.toString()));

                    Picasso.get().load(AppConstants.BASE_URL + AppConstants.FILE_ID
                            + userDetail.getImgPath() + "&Authorization=" + AppConstants.authToken)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(doc_img);
                }
            }
        });
    }

    private void initializeView() {
        doc_img = findViewById(R.id.doc_img);
        user_info_tv = findViewById(R.id.user_info_tv);
    }


}
