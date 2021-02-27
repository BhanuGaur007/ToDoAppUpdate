package com.example.to_do_app_final.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_do_app_final.Activity.MainActivity;
import com.example.to_do_app_final.Activity.MapActivity;
import com.example.to_do_app_final.R;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;


public class FavFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final String ALLOW_KEY = "ALLOWED";
    private static final String CAMERA_PREF = "camera_pref";
    ImageView ivCap;
    TextView frCamera,frShareData,frNotification,frLocation,frEmail,frSms,frCall,frTextSpeach;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        ivCap = view.findViewById(R.id.capImage);
        frCamera = view.findViewById(R.id.cameraOpen);
        frShareData = view.findViewById(R.id.shareData);
        frNotification = view.findViewById(R.id.notificationOpen);
        frLocation = view.findViewById(R.id.locationOpen);
        frEmail = view.findViewById(R.id.emailOpen);
        frSms = view.findViewById(R.id.smsOpen);
        frCall = view.findViewById(R.id.callOpen);
        frTextSpeach =view.findViewById(R.id.textSpeach);

        phoneCall(view);
        shareData(view);
        setNotification(view);
        setLocation(view);
        setEmail(view);
        setSms(view);
        setTextToSpeach(view);
        setCameraCap(view);

        return view;
    }
    private void phoneCall(View view){
        frCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+919716008083";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(callIntent);

            }
        });

    }
    private void shareData(View view){

    }
    private void setNotification(View view){
        frNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification(view);
            }
        });

    }
    private void addNotification(View view){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getContext()).setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Notification by Bhanu")
                .setContentText("This is Notification from your App .. ");
        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(),0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
    private void setLocation(View view){
        frLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mapIntent = new Intent(getContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });

    }
    private void setEmail(View view){
        frEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                String subject ="";
//                String message = "";
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto","bhanugaur007@gmail.com",null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
//                emailIntent.putExtra(Intent.EXTRA_TEXT,message);
//                startActivity(Intent.createChooser(emailIntent,"Choose an email client :"));


                String mailto = "mailto:bhanugaur007@gmail.com" +
                        "?cc=" +
                        "&subject=" + Uri.encode("your subject") + // for mail sunject
                        "&body=" + Uri.encode("your mail body");//for mail body
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Error to open email app", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void setSms(View view){
        frSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String phonrNum ="9716008083";
//                requestPermitionSet(view);
//                sendSMS("9716008083","hii Bhanu");
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto: +919716008083" ));
                intent.putExtra("sms_body", "Thank You For Contacting us ");
                startActivity(intent);



            }
        });

    }

    private void requestPermitionSet(View view){
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},1);
        }
        else {
            Toast.makeText(getContext(), "Permission has already granted", Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_SMS},1);
        }

    }


    private void sendSMS(String phoneNo, String msg) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getContext(), "Message Sent successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            Toast.makeText(getContext(),ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }


    private void setTextToSpeach(View view){

    }
    private void setCameraCap(View view){
        frCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent imageCapIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(imageCapIntent,MY_PERMISSIONS_REQUEST_CAMERA);
            }

        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivCap.setImageBitmap(bitmap);

        }
    }

}