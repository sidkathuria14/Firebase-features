package com.example.sidkathuria14.firebase.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sidkathuria14.firebase.R;
import com.example.sidkathuria14.firebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    EditText etName, etAge;
    String name;
    int age;
    public static final String TAG = "MainActivity";


    private Button buttonChoose;
    private Button buttonUpload;
    private static final int PICK_IMAGE_REQUEST = 234;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signin);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            Toast.makeText(this, "uploading", Toast.LENGTH_SHORT).show();
uploadFile();
        }
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference riversRef = storageReference.child(
                    "images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                       //     double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " +
                                  //  ((int) progress) +
                                    "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
}





        // database
        //
        //
        //
        //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//

//        etAge = (EditText)findViewById(R.id.etAge);
//        etName = (EditText)findViewById(R.id.etName);
//        ((Button)findViewById(R.id.btnEnter)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = etName.getText().toString();
//                age = Integer.parseInt(etAge.getText().toString());
//
//
//                //  mDatabase = FirebaseDatabase.getInstance().getReference();
//                mDatabase = FirebaseDatabase.getInstance().getReference("users");
//                String userId = mDatabase.push().getKey();
//                User user = new User(name, age);
//                mDatabase.child(userId).setValue(user);
//
//                mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        User user = dataSnapshot.getValue(User.class);
//
//                        Log.d(TAG, "User name: " + user.getName() + ", email " + user.getAge());
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w(TAG, "Failed to read value.", error.toException());
//                    }
//                });
//            }
//        });



