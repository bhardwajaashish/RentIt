package com.example.rentit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InformationActivity extends AppCompatActivity {

    EditText c1,a1,p1,d1;
    Button image1,image2,upload;
    String selectedPath1,selectedPath2;
    ImageView imageView1,imageView2;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        c1=(EditText)findViewById(R.id.city);
        a1=(EditText)findViewById(R.id.address);
        p1=(EditText)findViewById(R.id.phone);
        d1=(EditText)findViewById(R.id.description);

        image1=(Button)findViewById(R.id.image1);
        image2=(Button)findViewById(R.id.image2);
        upload=(Button)findViewById(R.id.upload);

        imageView1=(ImageView)findViewById(R.id.image_upload1);
        imageView2=(ImageView)findViewById(R.id.image_upload2);

        storage = FirebaseStorage.getInstance();

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.icon).setVisibility(View.GONE);
                findViewById(R.id.form_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.main_layout).setVisibility(View.GONE);
                findViewById(R.id.images_layout).setVisibility(View.VISIBLE);
            }
        });
        //add user
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(1);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                DatabaseReference child=myRef.child("Rents");
                DatabaseReference newUserRef=child.push();

                Rent rents=new Rent(c1.getText().toString(),a1.getText().toString(),p1.getText().toString(),d1.getText().toString());
               /* Map<String, Rent> rents = new HashMap<>();
                rents.put("Rent_Item",new Rent(c1.getText().toString(),a1.getText().toString(),p1.getText().toString(),d1.getText().toString()));*/
                newUserRef.setValue(rents);

                storageReference = storage.getReference();
                StorageReference newRef=storageReference.child(newUserRef.getKey().toString());

                StorageReference image1ref=newRef.child("image1.jpg");
                StorageReference image1=newRef.child("Rentit/"+newUserRef.getKey().toString()+"/image1.jpg");

                imageView1.setDrawingCacheEnabled(true);
                imageView1.buildDrawingCache();
                Bitmap bitmap = imageView1.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = image1ref.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

                StorageReference image2ref=newRef.child("image2.jpg");
                StorageReference image2=newRef.child("Rentit/"+newUserRef.getKey().toString()+"/image2.jpg");

                imageView2.setDrawingCacheEnabled(true);
                imageView2.buildDrawingCache();
                Bitmap bitmap2 = imageView2.getDrawingCache();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                byte[] data2 = baos2.toByteArray();

                UploadTask uploadTask2 = image2ref.putBytes(data2);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
                /*if(selectedPath1 != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(InformationActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    newRef.putFile(Uri.parse(selectedPath1))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InformationActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(InformationActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }*/

            }
        });


    }
    public void openGallery(int req_code){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == 1)
            {
                selectedPath1 = getPath(selectedImageUri);
                System.out.println("selectedPath1 : " + selectedPath1);
                imageView1.setImageURI(selectedImageUri);
            }
            if (requestCode == 2)
            {
                selectedPath2 = getPath(selectedImageUri);
                System.out.println("selectedPath2 : " + selectedPath2);
                imageView2.setImageURI(selectedImageUri);
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
