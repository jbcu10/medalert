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
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.MedicineHelperActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.config.ImageFilePath;
import jbcu10.dev.medalert.config.PermissionUtils;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class EditMedicineActivity extends MedicineHelperActivity implements DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        ButterKnife.bind(this);
        initializedViews();
        AppController appController = AppController.getInstance();
        medicine = appController.getMedicine();
        setMedicineValue(medicine);
    }

    private void setMedicineValue(Medicine medicine) {
        edit_name.setText(medicine.getName());
        edit_generic_name.setText(medicine.getGenericName());
        edit_description.setText(medicine.getDescription());
        edit_diagnosis.setText(medicine.getDiagnosis());
        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        edit_expiration.setText(df.format(date));
        edit_type.setText(medicine.getType());
        edit_total.setText(String.valueOf(medicine.getTotal()));
        edit_dosage.setText(medicine.getDosage());
        edit_stock.setText(String.valueOf(medicine.getStock()));
        setImage();
    }

    @OnClick(R.id.button_submit)
    public void onClickButtonSubmit(View view) {
        if (isMedicineValid()) {
            new MaterialDialog.Builder(EditMedicineActivity.this)
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
                                boolean isCreated = medicineRepository.update(new Medicine(medicine.getId(), medicine.getUuid(), edit_name.getText().toString(), edit_generic_name.getText().toString(), edit_diagnosis.getText().toString(), edit_description.getText().toString(), milliseconds, Integer.parseInt(edit_total.getText().toString()), null, edit_type.getText().toString(), medicine.isEnabled(),edit_dosage.getText().toString(),Integer.parseInt(edit_stock.getText().toString()),medicine.getImageUri()));
                                if (isCreated) {
                                    Intent intent = new Intent(EditMedicineActivity.this, HomeActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditMedicineActivity.this);
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


            //imageUri = file;
            medicine.setImageUri(file);
            medicineRepository.update(medicine);

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

    private void setImage(){
        try {

            Log.d("set",medicine.getImageUri());
            if(!medicine.getImageUri().equals("")){
                Glide.with(this).load(medicine.getImageUri()).into(image_type);

            }
            if(medicine.getImageUri().equals("")) {
                if (medicine.getType() != null && medicine.getType().equals("Tablet")) {
                    image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_lozenge));
                }
                if (medicine.getType() != null && medicine.getType().equals("Injectible")) {
                    image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_syringe));
                }
                if (medicine.getType() != null && medicine.getType().equals("Capsule")) {
                    image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill));
                }
                if (medicine.getType() != null && medicine.getType().equals("Ointment")) {
                    image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_ointment));
                }            }
        }
        catch (Exception e){
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_pill));

        }

    }

}
