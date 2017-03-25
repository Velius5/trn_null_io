package com.nullio.opinieallegro;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nullio.opinieallegro.model.Review;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddReviewActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String OFFER_ID = "offerId";
    private static final int MY_PERMISSIONS_REQUEST = 77;
    public static final String OFFER_NAME = "offerName";
    private int selectedId;
    private LinearLayout photosContainer;
    private Button addPhotoButton;
    private Button sendReview;
    private EditText reviewDescription;
    private RelativeLayout progressLayout;
    private List<String> filePaths;
    private StorageReference storageReference;
    private String userId;
    private String offerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        photosContainer = (LinearLayout) findViewById(R.id.photosContainer);
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        sendReview = (Button) findViewById(R.id.sendReview);
        reviewDescription = (EditText) findViewById(R.id.reviewDescription);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        offerId = getIntent().getStringExtra(OFFER_ID);
        filePaths = new ArrayList<>();
        String activityTitle = getIntent().getStringExtra(OFFER_NAME);
        setTitle(activityTitle);
        getUserId();
        addListeners();
    }

    private void getUserId() {
        SharedPreferences prefs = getSharedPreferences("settings", 0);
        userId = prefs.getString("userId", "0");
    }

    private void addListeners() {
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermissions();
            }
        });
        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinner();
                uploadPhotos();
            }
        });
    }

    private void showSpinner() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void uploadPhotos() {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (String s : filePaths) {
            final UUID uuid = UUID.randomUUID();
            Uri file = Uri.fromFile(new File(s));
            StorageReference imageReference = storageReference.child("images/" + uuid + ".jpg");
            imageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saveReviewToDatabase(taskSnapshot.getDownloadUrl().toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }

    private void saveReviewToDatabase(String photoId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String newKey = database.getReference().child("reviews").push().getKey();
        DatabaseReference ref = database.getReference().child("reviews").child(newKey);
        List<String> tmpList = new ArrayList<>();
        tmpList.add(photoId);
        String content = reviewDescription.getText().toString();
        Review rev = new Review(tmpList, content);
        ref.setValue(rev);
        Map<String, Object> reviewAddedMap = new HashMap<>();
        reviewAddedMap.put(offerId, newKey);
        database.getReference().child("users").child(userId).child("reviews").updateChildren(reviewAddedMap);
        reviewAddedMap = new HashMap<>();
        reviewAddedMap.put(newKey, true);
        database.getReference().child("offers").child(offerId).child("reviews").updateChildren(reviewAddedMap);
        goToComplete();
    }

    private void goToComplete() {
        Intent intent = new Intent(this, ReviewAddedComplete.class);
        startActivity(intent);
        finish();
    }

    private void makePhoto() {
        if (filePaths.size() > 1) {
            Toast.makeText(this, "Można umieścić maksymalnie 2 zdjęcia", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        filePaths.add(image.getAbsolutePath());
        return image;
    }

    private void handlePermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        } else {
            makePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        makePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photosContainer.removeAllViews();
        for (String s : filePaths) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
            imageView.setLayoutParams(params);
            Picasso.with(this).load("file:///" + s).into(imageView);
            photosContainer.addView(imageView);
        }
    }
}
