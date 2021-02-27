package com.example.to_do_app_final.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_do_app_final.DataBaseFile.DbHelper;
import com.example.to_do_app_final.R;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Compressor;

public class Profile extends Fragment {
    private static final int STORAGE_PERMISSION_CODE = 111;
    public static final int SELECT_PICTURE = 100;
    public static final int SELECT_PICTURE_LOAD = 115;
    DbHelper dbHelper;
    private StorageTask uploadTask;
    private Uri imageUri ;
    private FirebaseAuth mAuth;
    private ActionBar toolbar;
    private  Bitmap bitmap;
    private ProgressDialog progressDialog;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference storageReference;

    DatabaseReference reference;
    CircleImageView profilePicChange;
    TextInputLayout editProfile;
    TextView name, email, phone, gender;

    String userName, userEmail, userPhone, userGender, userPass;

    String filePath="";
    String docId;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dbHelper = new DbHelper(getContext());

        storage = FirebaseStorage.getInstance("gs://mynotes-28f90.appspot.com");
 //       database=FirebaseDatabase.getInstance();
 //       reference=database.getReference("users");
        storageReference=storage.getReference("Uploads");
        progressDialog = new ProgressDialog(getContext());

        name = view.findViewById(R.id.namefragment);
        email = view.findViewById(R.id.emailfragment);
        phone = view.findViewById(R.id.phonefragment);
        gender = view.findViewById(R.id.genderfragment);
        editProfile = view.findViewById(R.id.editProfileImage);
        profilePicChange = view.findViewById(R.id.profileChange);

//        userName = new PrefranceManager(getContext()).getNameProfile();
//        name.setText(userName);
//        userEmail = new PrefranceManager(getContext()).getEmail();
//        email.setText(userEmail);
//        userPhone = new PrefranceManager(getContext()).getPhone();
//        phone.setText(userPhone);
//        userGender = new PrefranceManager(getContext()).getGender();
//        gender.setText(userGender);
        showUserDAtaFromFirebse();
        profilePicChanger(view);
        // showUserProfileData(view);


        return view;
    }

    //    private void showUserProfileData(View view){
//        Cursor res = dbHelper.getData();
//
//        if (res.getCount()==0){
//
//            Toast.makeText(getContext(), "Data Already exist", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
////        StringBuffer buffer = new StringBuffer();
//
//
//
//
//
//
//        while (res.moveToNext()){
//
//              userName=res.getString(1);
//              userEmail = res.getString(2);
//              userPhone = res.getString(3);
//              userGender = res.getString(4);
//
//            Log.i("name",userName);
//
//        }
//
//        name.setText(userName);
//        email.setText(userEmail);
//        phone.setText(userPhone);
//        gender.setText(userGender);
////        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////        builder.setCancelable(true);
////        builder.setTitle("User Detail");
////        builder.setMessage(buffer.toString());
////        builder.show();
//
//    }
    private void profilePicChanger(View view) {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionStorage();


//               Intent imageCapIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(imageCapIntent, MY_PERMISSIONS_REQUEST_CAMERA);

            }
        });
    }



    public void dialogShowPhoto(final int REQUEST_CAMERA, final int RESULT_LOAD_IMAGE) {
        String takephoto = "take Photo";
        String chooseFromLibrary = "gallery";
        String cancel = "cancel";
        String addPhoto = "add photo";
        final CharSequence[] items = {takephoto, chooseFromLibrary, cancel};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(addPhoto);
        final String finalTakephoto = takephoto;
        final String finalChooseFromLibrary = chooseFromLibrary;
        final String finalCancel = cancel;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                if (items[item].equals(finalTakephoto)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(finalChooseFromLibrary)) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                } else if (items[item].equals(finalCancel)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageUri=getImageUri(getContext(),bitmap);
            uploadImage();

            profilePicChange.setImageURI(imageUri);

        } else if(requestCode ==SELECT_PICTURE_LOAD) {
            imageUri=data.getData();
            profilePicChange.setImageURI(imageUri);
            uploadImage();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void requestPermissionStorage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  )
        {
            dialogShowPhoto(SELECT_PICTURE,SELECT_PICTURE_LOAD);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dialogShowPhoto(SELECT_PICTURE,SELECT_PICTURE_LOAD);
            }
        } else {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }





    private void showUserDAtaFromFirebse() {

        db.collection("users")
                .document(currentUser.getUid()).collection("details").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                               // if (currentUser.getUid().equals(document.getString("uid"))) {
                                    String nameFirebase = document.getString("name");
                                    String emailFirebase = document.getString("email");
                                    String phoneFirebase = document.getString("phone");
                                    String genderFirebase = document.getString("gender");
                                    String  imageFirebase=document.getString("imageUrl");

                                    docId=document.getId();
                                    name.setText(nameFirebase);
                                    email.setText(emailFirebase);
                                    phone.setText(phoneFirebase);
                                    gender.setText(genderFirebase);
                                if(!imageFirebase.isEmpty()){
                                    Picasso.get()
                                            .load(imageFirebase)
                                            .placeholder(R.mipmap.ic_bhanu_gaur)
                                            .error(R.drawable.ic_edit)
                                            .into(profilePicChange);
                                }


                               // }
                                Log.d("success", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("failed", "Error getting documents.", task.getException());
                        }
                    }
                });
    }



    private String getPath(Uri contentUri) {
        ContentResolver resolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(resolver.getType(contentUri));
    }

    private void uploadImage(){

        if(imageUri !=null){

            final  StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getPath(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                        reference=FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageUrl",mUri);
                        reference.updateChildren(map);
                        db.collection("users").document(currentUser.getUid()).collection("details").document(docId).update(map);
                       // cloudStoreFirebaseInsertData(mUri);

                    } else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {

            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void cloudStoreFirebaseInsertData(String image){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("image",image);

// Add a new document with a generated ID
        db.collection("users").document(currentUser.getUid()).collection("details").document()
                .update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.i("image Upodate",task.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("failedUodate",e.getMessage());
            }
        });
    }



}








