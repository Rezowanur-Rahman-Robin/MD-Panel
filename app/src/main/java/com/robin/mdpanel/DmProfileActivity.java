package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DmProfileActivity extends AppCompatActivity {

    private String dm_id;
    private TextView topDmName,changePassword,dmName,dmPhone,dmEmail,dmNid,vehType,vehLicenseNo,serviceDistrict,serviceThana;
    private ImageButton callButton;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentDmId;

    private ProgressDialog loadingBar;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm_profile);


        rootRef= FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();

        currentDmId= intent.getStringExtra("dm_id");


        topDmName=findViewById(R.id.dm_name_profile_top_md);
        callButton=(ImageButton)findViewById(R.id.dm_call_button);
        dmName=findViewById(R.id.dm_name_profile_TextView_md);
        dmPhone=findViewById(R.id.dm_phone_profile_md);
        dmNid=findViewById(R.id.dm_nid_profile_md);
        vehType=findViewById(R.id.vehicle_type_profile_TextView_md);
        vehLicenseNo=findViewById(R.id.license_no_profile_md);
        serviceDistrict=findViewById(R.id.district_dm_profile_TextView_md);
        serviceThana=findViewById(R.id.thana_dm_profile_md);


        changePassword=findViewById(R.id.change_password_dm_profile);

        loadingBar=new ProgressDialog(this);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DmCallAction();
            }
        });


    }




    @Override
    protected void onStart() {
        super.onStart();

        loadingBar.setTitle("Delivery Man Profile");
        loadingBar.setMessage("Please Wait Until The Profile Loading Successfully Completed...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("Drivers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            for(DataSnapshot model:dataSnapshot.getChildren()){

                                DM dm=model.getValue(DM.class);

                                if(dm!=null){

                                    if(dm.getDriverId().equals(currentDmId)){
                                        topDmName.setText(dm.getName());
                                        dmName.setText(dm.getName());
                                        dmPhone.setText(dm.getPhoneNumber());
                                        dmNid.setText(dm.getnId());
                                        serviceDistrict.setText(dm.getJobLocationCity());
                                        serviceThana.setText(dm.getJobLocationArea());
                                        vehType.setText(dm.getVehicleType());
                                        vehLicenseNo.setText(dm.getLicenceNumber());

                                        break;
                                    }



                                }
                            }
                            loadingBar.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        loadingBar.dismiss();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void DmCallAction() {

        String s="tel:+88"+dmPhone.getText().toString();

        Intent intent=new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(s));
        startActivity(intent);

    }
}
