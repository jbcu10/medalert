package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.MedicineHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.config.ImageFilePath;
import jbcu10.dev.medalert.config.PermissionUtils;
import jbcu10.dev.medalert.model.Medicine;

public class NewMedicineActivity extends MedicineHelperActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);
        ButterKnife.bind(this);
        initializedViews();
    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        if(isMedicineValid()) {
            new MaterialDialog.Builder(NewMedicineActivity.this)
                    .title("Save Medicine?")
                    .content("Are you sure you want save this items?")
                    .positiveText("Save")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            String expirationDateString = edit_expiration.getText().toString();
                            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            Date expirationDate;
                            long milliseconds = 0;
                            try {
                                expirationDate = df.parse(expirationDateString);
                                milliseconds = expirationDate.getTime();

                            } catch (ParseException e) {
                                Log.d("Error", e.getMessage());
                            }


                            try {
                                String uuid = UUID.randomUUID().toString();
                                boolean isCreated = medicineRepository.create(new Medicine(uuid, edit_name.getText().toString(),
                                        edit_generic_name.getText().toString(), edit_diagnosis.getText().toString(),
                                        edit_description.getText().toString(), milliseconds,
                                        Integer.parseInt(edit_total.getText().toString()), null, edit_type.getText().toString(),
                                        true,edit_dosage.getText().toString(),Integer.parseInt(edit_stock.getText().toString()),imageUri));

                                if (isCreated) {
                                    Intent intent = new Intent(NewMedicineActivity.this, MedicineActivity.class);
                                    AppController appController = AppController.getInstance();
                                    appController.setMedicineId(medicineRepository.getByUuid(uuid).getId());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                                if (!isCreated) {
                                    Snackbar.make(findViewById(android.R.id.content), "Failed to Save Medicine!", Snackbar.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.d("Error", e.getMessage());
                            }

                        }
                    }).show();
        }
    }

    @OnClick(R.id.image_type)
    public void onClickImageRelation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewMedicineActivity.this);
        builder
                .setMessage("Add Picture")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();
    }
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            new MaterialCamera(this)
                    .stillShot()
                    .allowRetry(true)
                    .autoSubmit(true)
                    .saveDir(saveFolder)
                    .labelConfirm(R.string.save)
                    .labelRetry(R.string.retry)
                    .start(CAMERA_RQ);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir,  new Date().getTime()+".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            String realPath = ImageFilePath.getPath(this, data.getData());

            uploadImage(realPath);
        } else if (requestCode == CAMERA_RQ && resultCode == RESULT_OK) {




            uploadImage(data.getDataString());
        }
    }

    public void uploadImage(String file) {
        if (file != null) {
            Log.d("file",file);
            // scale the image to save on bandwidth


              /*  Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

               image_gender.setImageBitmap(bitmap);*/
            Glide.with(this).load(file).into(image_type);


            imageUri = file;
            //medicine.setImageUri(file);
            //medicineRepository.update(medicine);

        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this,"Image picker gave us a null image.", Toast.LENGTH_LONG).show();
        }
    }


    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }



}
