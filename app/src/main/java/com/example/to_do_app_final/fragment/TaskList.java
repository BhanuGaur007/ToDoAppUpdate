package com.example.to_do_app_final.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.to_do_app_final.DataBaseFile.DbHelper;
import com.example.to_do_app_final.R;
import com.example.to_do_app_final.adapter.TaskAddRecyclerAdapter;
import com.example.to_do_app_final.adapter.TaskHolderNameRecyAdapter;
import com.example.to_do_app_final.model.TaskAddRecyclerModel;
import com.example.to_do_app_final.model.TaskHolderNameRecy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class TaskList extends Fragment {
    DbHelper dbHelper;
    private StorageTask uploadTask;
    private Uri imageUri ;
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference storageReference;
    DatabaseReference reference;
    GifImageView progressBar;
    LinearLayout progressBarLayout;
    ImageView iv_add;
    AlertDialog.Builder builder;
    String docId;
    String documentId;


    RecyclerView recyclerView;
    ArrayList<TaskAddRecyclerModel> arrayList = new ArrayList<>();
    TaskAddRecyclerAdapter adapter;
    String fragDocId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dbHelper = new DbHelper(getContext());

        fragDocId=getArguments().getString("docId");

        iv_add = view.findViewById(R.id.iv_add);
        builder = new AlertDialog.Builder(getContext());
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv_add,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );
        objectAnimator.setDuration(400);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        addButtonPreform(view);
        showUserNameData();
        inisilization(view);
        return view;
    }



    private void addButtonPreform(View view){
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
                final EditText input = new EditText(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);
//               builder.setMessage("Do you want to close this application ?");
                builder.setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
//                               getActivity().finish();
//                               arrayList.add(new TaskHolderNameRecy(input.getText().toString()));
//                               adapter.notifyDataSetChanged();
                                //   Collections.reverse(arrayList);
                                insertData(input.getText().toString(),currentUser.getUid());

                                Toast.makeText(getContext(), "you  save your Task", Toast.LENGTH_LONG).show();
                                dialog.dismiss();


//                               arrayList.add(new TaskHolderNameRecy(input.getText().toString()));
//                               adapter.notifyDataSetChanged();
//                               Collections.reverse(arrayList); // for add new data on top
//
                            }


                        });

                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(getActivity(),"you Didn't Save Your Task", Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alert = builder.create();
                alert.setTitle("Add New Task ");
                alert.show();

            }
        });
    }
    private  void inisilization(View view){

        recyclerView = view.findViewById(R.id.taskName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TaskAddRecyclerAdapter(getContext(),arrayList);

//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name One"));
//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name Two"));
//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name Three"));
//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name Four"));
//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name Five"));
//        arrayList.add(new TaskHolderNameRecy("TaskHolder Name Six"));
        recyclerView.setAdapter(adapter);



    }
    private  void insertData(String taskname, String id){
        Map<String, Object> user = new HashMap<>();
        user.put("uid",id);
        user.put("taskName",taskname);

        db.collection("users").document(id).collection("userData").document().collection("userNotes")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentId = documentReference.getId();
                        Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failed", "Error adding document", e);
                    }
                });
    }
    private void showUserNameData(){
        db.collection("users").document(currentUser.getUid()).collection("userData")
                .document(fragDocId).collection("userNotes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String defineTaskHolder = document.getString("taskName");
                                docId=document.getId();
                                arrayList.add(new TaskAddRecyclerModel(defineTaskHolder) );
                                adapter.notifyDataSetChanged();
                                Collections.reverse(arrayList);

                            }
                        }
                    }
                });
    }

}