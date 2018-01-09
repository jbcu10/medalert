package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.config.PermissionUtils;
import jbcu10.dev.medalert.db.PatientRepository;
import jbcu10.dev.medalert.fragments.RelativeFragments;
import jbcu10.dev.medalert.model.Patient;

public class PatientActivity extends BaseActivity {
    private static final String TAG = PatientActivity.class.getSimpleName();
    public PatientRepository patientRepository;
    TextView txt_name, txt_relation, txt_contact_number, txt_email;
    @BindView(R.id.image_relation)
    ImageView image_gender;
    Patient patient;
    Fragment fragment;

    private static final int CAMERA_RQ = 6969;
    File saveFolder = new File(Environment.getExternalStorageDirectory(), "medalert");
    public static int destination = 0;
    public static String patientUuid = "";
    public static final String FILE_NAME = "temp.jpg";

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);
        patientRepository = new PatientRepository(PatientActivity.this);
        AppController appController = AppController.getInstance();
        patient = patientRepository.getById(appController.getPatientId());
        HomeActivity.selectedItem =2;

        patientUuid = patient.getUuid();
        initializeView();
        fragment = new RelativeFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_patient, fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(destination==0) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    private void initializeView() {
        txt_name = findViewById(R.id.txt_name);
        txt_relation = findViewById(R.id.txt_relation);
        txt_contact_number = findViewById(R.id.txt_contact_number);
        txt_email = findViewById(R.id.txt_email);
        image_gender = findViewById(R.id.image_relation);
        txt_name.setText(patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName());
        txt_contact_number.setText(patient.getContactNumber());
        txt_email.setText(patient.getEmail());
        txt_relation.setText(patient.getGender());


        setImage();

    }

    private void setImage(){
        try {

            Log.d("set",patient.getImageUri());
            if(!patient.getImageUri().equals("")){
               // Uri uri = Uri.parse(patient.getImageUri());
                Picasso.with(this).invalidate(patient.getImageUri());
                Picasso.with(this).load(patient.getImageUri()).into(image_gender);

            }
            image_gender.setImageDrawable(this.getImageGender(patient) != null ? this.getImageGender(patient) : getResources().getDrawable(R.drawable.male));

        }
        catch (Exception e){
            image_gender.setImageDrawable(this.getImageGender(patient) != null ? this.getImageGender(patient) : getResources().getDrawable(R.drawable.male));

        }

    }

    private Drawable getImageGender(Patient patient) {
        Log.d("Gender", patient.getGender());
        if (patient.getGender() != null && patient.getGender().toLowerCase().equals("female")) {
            return this.getResources().getDrawable(R.drawable.female);
        }
        return this.getResources().getDrawable(R.drawable.male);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.edit_medicine:
                this.editPatient();
                return true;
            case R.id.delete_medicine:
                this.deletePatient();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.image_relation)
    public void onClickImageRelation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
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
            uploadImage(data.getDataString());
        } else if (requestCode == CAMERA_RQ && resultCode == RESULT_OK) {
            //Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
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
            Picasso.with(this).invalidate(file);
            Picasso.with(this).load(file).into(image_gender);

            patient.setImageUri(file);
            patientRepository.update(patient);

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
    private void deletePatient() {
        new MaterialDialog.Builder(PatientActivity.this)
                .title("Delete Patient?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = patientRepository.deleteById(patient.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(PatientActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }
                            if (!isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Failed to Delete Medicine!", Snackbar.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }

                    }
                }).show();


    }

    private void editPatient() {
        AppController appController = AppController.getInstance();
        appController.setPatient(patient);
        Intent intent = new Intent(PatientActivity.this, EditPatientActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


}
