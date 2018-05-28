package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.config.ImageFilePath;
import jbcu10.dev.medalert.config.PermissionUtils;
import jbcu10.dev.medalert.db.MedicineRepository;
import jbcu10.dev.medalert.model.Medicine;

public class MedicineActivity extends BaseActivity {
    public MedicineRepository medicineRepository;
    TextView txt_name, txt_generic_name, txt_description, txt_diagnosis, txt_expiration, txt_doctor_name;
    CheckBox ch_enabled;
    Medicine medicine = null;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private static final String TAG = PatientActivity.class.getSimpleName();
    @BindView(R.id.image_type)
    ImageView image_type;

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
        setContentView(R.layout.activity_medicine);
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);

        txt_diagnosis = findViewById(R.id.txt_diagnosis);
        txt_name = findViewById(R.id.txt_name);
        txt_generic_name = findViewById(R.id.txt_generic_name);
        txt_description = findViewById(R.id.txt_description);
        txt_expiration = findViewById(R.id.txt_expiration);
        txt_doctor_name = findViewById(R.id.txt_doctor_name);
        image_type = findViewById(R.id.image_type);
        ch_enabled = findViewById(R.id.ch_enabled);
        medicineRepository = new MedicineRepository(MedicineActivity.this);
        HomeActivity.selectedItem = 1;
        AppController appController = AppController.getInstance();
        medicine = medicineRepository.getById(appController.getMedicineId());
        setMedicineValue(medicine);

    }

    private void setMedicineValue(Medicine medicine) {

        txt_name.setText(medicine.getName());
        txt_generic_name.setText(medicine.getGenericName());
        txt_description.setText(medicine.getDescription());
        txt_diagnosis.setText(medicine.getDiagnosis());

        Date date = new Date(medicine.getExpiration());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        txt_expiration.setText(df.format(date));
        txt_doctor_name.setText(medicine.getDoctor() != null ? medicine.getDoctor().getFirstName() + " " + medicine.getDoctor().getFirstName() : "Not Available..");
        ch_enabled.setChecked(medicine.isEnabled());

        ch_enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                medicine.setEnabled(true);
            } if (!isChecked){
                medicine.setEnabled(false);
            }
            medicineRepository.update(medicine);
        });

        setImage();
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
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
                this.editMedicine();
                return true;
            case R.id.delete_medicine:
                this.deleteMedicine();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @OnClick(R.id.fab)
    public void onClickFAB(View view) {
        Intent intent = new Intent(MedicineActivity.this, NewRemindersActivity.class);
        startActivity(intent);
         overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    private void editMedicine() {
        AppController appController = AppController.getInstance();
        appController.setMedicine(medicine);
        Intent intent = new Intent(MedicineActivity.this, EditMedicineActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    private void deleteMedicine() {
        new MaterialDialog.Builder(MedicineActivity.this)
                .title("Delete Medicine?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = medicineRepository.deleteById(medicine.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(MedicineActivity.this, HomeActivity.class);
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


    @OnClick(R.id.image_type)
    public void onClickImageRelation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MedicineActivity.this);
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
}
