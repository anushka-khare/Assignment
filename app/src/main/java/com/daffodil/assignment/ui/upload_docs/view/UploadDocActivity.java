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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.daffodil.assignment.common.AppConstants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.daffodil.assignment.common.AppConstants.READ_EXTERNAL_STORAGE_REQUEST_CODE;

public class UploadDocActivity extends AppCompatActivity {

    private ImageView choose_front_img, choose_back_img;
    private ImageView preview_front_img, remove_front_preview;
    private ImageView preview_back_img, remove_back_preview;
    private Button upload_btn;
    private RelativeLayout front_preview_layout, back_preview_layout;
    private RadioGroup doc_type_rg;
    //    private TextView choose_an_id_lbl;
    private UploadDocViewModel uploadDocViewModel;
    private String mCurrentPhotoPath;
    private ActivityResultLauncher<Intent> mGetGalleryImage;
    private ActivityResultLauncher<Intent> mGetCameraImage;
    private ActivityResultLauncher<Intent> mGetBackImage;
    private ActivityResultLauncher<Intent> mGetBackCameraImage;
    private String chosenFileUrl = null;
    private String chosenBackFileUrl = null;
    private String userId;
    private static int FRONT = 1;
    private static int BACK = 2;
    private int savedCount = 0;
    private List<String> uploadedUrls = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc);
        userId = getIntent().getStringExtra(AppConstants.USER_ID);
        initializeView();
        uploadDocViewModel = ViewModelProviders.of(this).get(UploadDocViewModel.class);
        subscribeToLiveData();

        mGetGalleryImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        Uri uri = null;
                        if (intent != null) {
                            uri = intent.getData();
                        }
                        // Handle the returned Uri
                        if (uri != null && uri.getPath() != null) {
                            front_preview_layout.setVisibility(View.VISIBLE);
                            remove_front_preview.setEnabled(true);
                            chosenFileUrl = Utility.getImagePath(UploadDocActivity.this, uri);
                            if (chosenFileUrl != null) {
                                File file = new File(chosenFileUrl);
                                Picasso.get().load(file).into(preview_front_img);
                                choose_front_img.setVisibility(View.INVISIBLE);
                                choose_front_img.setEnabled(false);
                            }
                        }


                    }
                });

        mGetCameraImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Handle the Intent
                            chosenFileUrl = mCurrentPhotoPath;
                            File file = new File(mCurrentPhotoPath);
                            Picasso.get().load(file).into(preview_front_img);
                            front_preview_layout.setVisibility(View.VISIBLE);
                            remove_front_preview.setEnabled(true);
//                            choose_an_id_lbl.setVisibility(View.INVISIBLE);
                            choose_front_img.setVisibility(View.INVISIBLE);
                            choose_front_img.setEnabled(false);
                        }
                    }
                });

        mGetBackImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        Uri uri = null;
                        if (intent != null) {
                            uri = intent.getData();
                        }
                        // Handle the returned Uri
                        if (uri != null && uri.getPath() != null) {
                            back_preview_layout.setVisibility(View.VISIBLE);
                            remove_back_preview.setEnabled(true);
                            chosenBackFileUrl = Utility.getImagePath(UploadDocActivity.this, uri);
                            if (chosenBackFileUrl != null) {
                                File file = new File(chosenBackFileUrl);
                                Picasso.get().load(file).into(preview_back_img);
                                choose_back_img.setVisibility(View.INVISIBLE);
                                choose_back_img.setEnabled(false);
                            }
                        }


                    }
                });

        mGetBackCameraImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            // Handle the Intent
                            chosenBackFileUrl = mCurrentPhotoPath;
                            File file = new File(mCurrentPhotoPath);
                            Picasso.get().load(file).into(preview_back_img);
                            back_preview_layout.setVisibility(View.VISIBLE);
                            remove_back_preview.setEnabled(true);
                            choose_back_img.setVisibility(View.INVISIBLE);
                            choose_back_img.setEnabled(false);
                        }
                    }
                });

    }

    private void subscribeToLiveData() {

        uploadDocViewModel.uploadedUrlLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s != null) {
                    savedCount++;
                    Toast.makeText(UploadDocActivity.this, s + getString(R.string.uploaded_successfully), Toast.LENGTH_SHORT).show();
                    uploadedUrls.add(s);
                    if (uploadedUrls.size() == 2) {
                        int selectedId = doc_type_rg.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        uploadDocViewModel.saveImagePathInDB(uploadedUrls, userId, radioButton.getText().toString());

                    }
                }
            }
        });

        uploadDocViewModel.imagesDBdata.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {

                if (aLong != null) {
                    Utility.dismissLoader();
                    Intent intent = new Intent(UploadDocActivity.this, ShowInfoActivity.class);
                    intent.putExtra(AppConstants.USER_ID, userId);
                    startActivity(intent);

                }

            }
        });
    }

    private void initializeView() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.id_docs));
        }

        choose_front_img = findViewById(R.id.choose_front_img);
        choose_back_img = findViewById(R.id.choose_back_img);
        preview_front_img = findViewById(R.id.preview_front_img);
        preview_back_img = findViewById(R.id.preview_back_img);
        upload_btn = findViewById(R.id.upload_btn);
        remove_front_preview = findViewById(R.id.remove_front_preview);
        remove_back_preview = findViewById(R.id.remove_back_preview);
        front_preview_layout = findViewById(R.id.front_preview_layout);
        back_preview_layout = findViewById(R.id.back_preview_layout);
        doc_type_rg = findViewById(R.id.doc_type_rg);
        choose_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogToSelectFile(BACK);
            }
        });

        choose_front_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogToSelectFile(FRONT);
            }
        });

        remove_front_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_front_img.setVisibility(View.VISIBLE);
                choose_front_img.setEnabled(true);
                front_preview_layout.setVisibility(View.INVISIBLE);
                remove_front_preview.setEnabled(false);
                chosenFileUrl = null;
            }
        });

        remove_back_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_back_img.setVisibility(View.VISIBLE);
                choose_back_img.setEnabled(true);
                back_preview_layout.setVisibility(View.INVISIBLE);
                remove_back_preview.setEnabled(false);
                chosenBackFileUrl = null;
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFileUrl != null && chosenBackFileUrl != null) {
                    Utility.showLoader(UploadDocActivity.this);
                    uploadDocViewModel.uploadImageForChat(chosenFileUrl);
                    uploadDocViewModel.uploadImageForChat(chosenBackFileUrl);
                } else {
                    Toast.makeText(UploadDocActivity.this, getString(R.string.choose_a_file), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDialogToSelectFile(int side) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.open_file_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        Button selectFile = dialogView.findViewById(R.id.select_file);
        selectFile.setOnClickListener(view -> {
            if (Utility.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGalleyIntent(side);
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
                openCameraIntent(side);
                alertDialog.cancel();
            } else {

                Utility.requestPermission(UploadDocActivity.this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void openGalleyIntent(int side) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent = Intent.createChooser(intent, getString(R.string.choose_a_file));

        if (side == FRONT)
            mGetGalleryImage.launch(intent);
        else
            mGetBackImage.launch(intent);
    }

    private void openCameraIntent(int side) {
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
                if (side == FRONT)
                    mGetCameraImage.launch(intent);
                else
                    mGetBackCameraImage.launch(intent);

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

    @Override
    protected void onStop() {
        super.onStop();
        Utility.dismissLoader();
    }
}
