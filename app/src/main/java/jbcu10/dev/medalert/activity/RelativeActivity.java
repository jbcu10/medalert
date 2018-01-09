package jbcu10.dev.medalert.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.config.PermissionUtils;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Relative;

public class RelativeActivity extends BaseActivity {
    private static final String TAG = RelativeActivity.class.getSimpleName();
    public RelativeRepository relativeRepository;
    TextView txt_name, txt_relation, txt_contact_number, txt_email;
    ImageView image_relation;
    Relative relative;
    CheckBox ch_enabled;
    private static final int CAMERA_RQ = 6969;
    File saveFolder = new File(Environment.getExternalStorageDirectory(), "medalert");

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative);
        ButterKnife.bind(this);
        relativeRepository = new RelativeRepository(RelativeActivity.this);
        AppController appController = AppController.getInstance();
        relative = relativeRepository.getById(appController.getRelativeId());
        initializeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        PatientActivity.destination=0;
    }

    private void initializeView() {
        txt_name = findViewById(R.id.txt_name);
        txt_relation = findViewById(R.id.txt_relation);
        txt_contact_number = findViewById(R.id.txt_contact_number);
        txt_email = findViewById(R.id.txt_email);
        image_relation = findViewById(R.id.image_relation);
        ch_enabled = findViewById(R.id.ch_enabled);
        txt_name.setText(relative.getFirstName() + " " + relative.getMiddleName() + " " + relative.getLastName());
        txt_contact_number.setText(relative.getContactNumber());
        txt_email.setText(relative.getEmail());
        txt_relation.setText(relative.getRelationship());
        setImage();

    }

    private void setImage(){
        try {

            Log.d("set",relative.getImageUri());
            if(!relative.getImageUri().equals("")){
                // Uri uri = Uri.parse(patient.getImageUri());
                Picasso.with(this).invalidate(relative.getImageUri());
                Picasso.with(this).load(relative.getImageUri()).into(image_relation);

            }
            image_relation.setImageDrawable(this.getImageRelation(relative) != null ? this.getImageRelation(relative) : getResources().getDrawable(R.drawable.relative));

        }
        catch (Exception e){
            image_relation.setImageDrawable(this.getImageRelation(relative) != null ? this.getImageRelation(relative) : getResources().getDrawable(R.drawable.relative));

        }

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
                this.editRelative();
                return true;
            case R.id.delete_medicine:
                this.deleteRelative();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Drawable getImageRelation(Relative relative) {
        try {
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("mother")) {
                return getResources().getDrawable(R.drawable.mother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("father")) {
                return getResources().getDrawable(R.drawable.father);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand father")) {
                return getResources().getDrawable(R.drawable.grandfather);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("grand mother")) {
                return getResources().getDrawable(R.drawable.grandmother);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("sister")) {
                return getResources().getDrawable(R.drawable.daughter);
            }
            if (relative.getRelationship() != null && relative.getRelationship().toLowerCase().equals("brother")) {
                return getResources().getDrawable(R.drawable.boy);
            }
            return getResources().getDrawable(R.drawable.relative);

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            return null;
        }
    }

    private void deleteRelative() {
        new MaterialDialog.Builder(RelativeActivity.this)
                .title("Delete Relative?")
                .content("Are you sure you want delete this items?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        try {
                            boolean isDeleted = relativeRepository.deleteById(relative.getId());
                            if (isDeleted) {
                                Snackbar.make(findViewById(android.R.id.content), "Successfully Deleted Medicine!", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(RelativeActivity.this, HomeActivity.class);
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

    private void editRelative() {
        AppController appController = AppController.getInstance();
        appController.setRelative(relative);
        Intent intent = new Intent(RelativeActivity.this, EditRelativeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    @OnClick(R.id.image_relation)
    public void onClickImageRelation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RelativeActivity.this);
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
            Picasso.with(this).load(file).into(image_relation);

            relative.setImageUri(file);
            relativeRepository.update(relative);

        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this,"Image picker gave us a null image.", Toast.LENGTH_LONG).show();
        }
    }
}
