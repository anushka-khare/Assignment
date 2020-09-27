package com.daffodil.assignment.ui.upload_docs.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.daffodil.assignment.R;
import com.daffodil.assignment.common.AppConstants;
import com.daffodil.assignment.ui.upload_docs.view_model.UploadDocViewModel;
import com.daffodil.assignment.ui.view_saved_info.view.ShowInfoActivity;
import com.daffodil.assignment.utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.daffodil.assignment.common.AppConstants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.daffodil.assignment.common.AppConstants.READ_EXTERNAL_STORAGE_REQUEST_CODE;

public class UploadDocActivity extends AppCompatActivity {

    ImageView choose_file_iv;
    ImageView preview_iv;
    Button upload_btn, cancel_button;
    private UploadDocViewModel uploadDocViewModel;
    private String mCurrentPhotoPath;
    private ActivityResultLauncher<String> mGetGalleryImage;
    private ActivityResultLauncher<Intent> mGetCameraImage;
    private String chosenFileUrl = null;
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc);
        userId = getIntent().getStringExtra(AppConstants.USER_ID);
        initializeView();
        uploadDocViewModel = ViewModelProviders.of(this).get(UploadDocViewModel.class);
        subscribeToLiveData();

        mGetGalleryImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null && uri.getPath() != null) {
                            preview_iv.setVisibility(View.VISIBLE);
                            chosenFileUrl = Utility.getImagePath(UploadDocActivity.this, uri);
                            File file = new File(chosenFileUrl);
                            Picasso.get().load(file).into(preview_iv);
                            choose_file_iv.setVisibility(View.GONE);
                        }


                    }
                });

        mGetCameraImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            // Handle the Intent
                            chosenFileUrl = mCurrentPhotoPath;
                            File file = new File(mCurrentPhotoPath);
                            Picasso.get().load(file).into(preview_iv);
                            preview_iv.setVisibility(View.VISIBLE);
                            choose_file_iv.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void subscribeToLiveData() {

        uploadDocViewModel.uploadedUrlLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s != null) {
                    choose_file_iv.setVisibility(View.VISIBLE);
                    preview_iv.setVisibility(View.GONE);
                    uploadDocViewModel.saveImagePathInDB(s, userId);
                    Toast.makeText(UploadDocActivity.this, s + getString(R.string.uploaded_successfully), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeView() {
        choose_file_iv = findViewById(R.id.choose_file_iv);
        preview_iv = findViewById(R.id.preview_iv);
        upload_btn = findViewById(R.id.upload_btn);
        cancel_button = findViewById(R.id.cancel_button);

        choose_file_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogToSelectFile();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_file_iv.setVisibility(View.VISIBLE);
                preview_iv.setVisibility(View.GONE);
                chosenFileUrl = null;
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFileUrl != null) {
                    Utility.showLoader(UploadDocActivity.this);
                    uploadDocViewModel.uploadImageForChat(chosenFileUrl);
                } else {
                    Toast.makeText(UploadDocActivity.this, getString(R.string.choose_a_file), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadDocActivity.this, ShowInfoActivity.class);
                intent.putExtra(AppConstants.USER_ID, userId);
                startActivity(intent);
            }
        });
    }

    private void openDialogToSelectFile() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.open_file_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        Button selectFile = dialogView.findViewById(R.id.select_file);
        selectFile.setOnClickListener(view -> {
            if (Utility.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGalleyIntent();
                alertDialog.cancel();
            } else {
                Utility.requestPermission(UploadDocActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                alertDialog.cancel();

            }
        });

        Button takePhoto = dialogView.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(view -> {
            if (Utility.checkPermission(this, Manifest.permission.CAMERA)) {
                openCameraIntent();
                alertDialog.cancel();
            } else {

                Utility.requestPermission(UploadDocActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void openGalleyIntent() {
       /* Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
        *//*String[] mimeTypes = {"image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);*//*
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent = Intent.createChooser(intent, getString(R.string.choose_a_file));
        startActivityForResult(intent, READ_REQUEST_CODE);*/

        mGetGalleryImage.launch("image/*");
    }

    private void openCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the FileRequest where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the FileRequest was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
                mGetCameraImage.launch(intent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
