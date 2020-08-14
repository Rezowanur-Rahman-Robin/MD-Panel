package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MerchantProfile extends AppCompatActivity {


    private String mId;

    private TextView topMerchantName,merchantEmail,topTotalPro;
    private TextView merchantName,merchantPhone,businessName,businessDetails,businessAdd,bkashno,rocketno;
    private ImageButton callButton;


    private View profileView;

    private CircleImageView merchantProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String currentMerchantId;

    private ProgressDialog loadingBar;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile);

        mId=getIntent().getExtras().get("merchant_id").toString();

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();






        topMerchantName=findViewById(R.id.merchant_name_profile_top_md);
        callButton=(ImageButton) findViewById(R.id.merchant_call_button);
        merchantEmail=findViewById(R.id.merchant_email_profile_account_md);
        merchantName=findViewById(R.id.merchant_name_profile_TextView_md);
        merchantPhone=findViewById(R.id.merchant_phone_profile_md);
        businessName=findViewById(R.id.business_name_profile_TextView_md);
        businessDetails=findViewById(R.id.business_details_profile_md);
        businessAdd=findViewById(R.id.business_address_profile_md);
        bkashno=findViewById(R.id.bkash_num_profile_TextView_md);
        rocketno=findViewById(R.id.merchant_rocket_num_profile_md);
        merchantProfileImage=findViewById(R.id.merchant_profile_image_profile_md);
        topTotalPro=findViewById(R.id.merchant_toal_products_md);



        loadingBar=new ProgressDialog(MerchantProfile.this);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              callMerchantAction();

            }
        });





    }



    @Override
    protected void onStart() {
        super.onStart();


        loadingBar.setTitle("Merchant Profile");
        loadingBar.setMessage("Please Wait Until Your Profile Loading Successfully Completed...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("Merchants").child(mId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) ){

                            Merchant merchant=dataSnapshot.getValue(Merchant.class);

                            if(dataSnapshot.hasChild("profile_image_link")){
                                String retriveMerchantImage=dataSnapshot.child("profile_image_link").getValue().toString();

                                if(retriveMerchantImage!=""){
                                    Picasso.with(getApplicationContext()).load(retriveMerchantImage).placeholder(R.drawable.businessman).into(merchantProfileImage);
                                }
                            }






                            topMerchantName.setText(merchant.getName());
                            merchantName.setText(merchant.getName());
                            merchantPhone.setText(merchant.getPhone());
                            merchantEmail.setText(merchant.getEmail());
                            businessName.setText(merchant.getBusinessName());
                            businessDetails.setText(merchant.getBusinessDetails());
                            businessAdd.setText(merchant.getBusinessAddress());
                            bkashno.setText(merchant.getBkash());
                            rocketno.setText(merchant.getRocket());

                        }







                        loadingBar.dismiss();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        loadingBar.dismiss();

                    }
                });




        rootRef.child("Merchant-Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    int c=0;

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        String m_id=model.child("id").getValue().toString();

                        if(m_id.equals(mId)){
                            c++;
                        }
                    }
                    topTotalPro.setText(c+" Products");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @SuppressLint("MissingPermission")
    private void callMerchantAction() {
        String s="tel:+88"+merchantPhone.getText().toString();

        Intent intent=new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(s));
        startActivity(intent);
    }




}
