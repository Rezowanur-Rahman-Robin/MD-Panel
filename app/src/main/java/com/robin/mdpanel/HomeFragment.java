package com.robin.mdpanel;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View homeView;
    private TextView totalCustomers,totalMerchants,totalOm,totalDm,unchecked_merchant_reqst,unchecked_dm_reqst,unchecked_transactions;
    private long tc,tm,tOm,tDm,tUm,tUdm,tT,tT1,tT2;
    private DatabaseReference rootRef;

    private ImageView requestGo,dmReqGo,transactionGo;

    private ProgressDialog loadingBar;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);


        rootRef= FirebaseDatabase.getInstance().getReference();


        totalCustomers=homeView.findViewById(R.id.total_customers_md_dashboard);
        totalMerchants=homeView.findViewById(R.id.total_merchants_md_dashboard);
        totalOm=homeView.findViewById(R.id.total_om_md_dashboard);
        totalDm=homeView.findViewById(R.id.total_dm_md_dashboard);
        unchecked_merchant_reqst=homeView.findViewById(R.id.unchecked_merchant_requests_MD_dashBoard);
        unchecked_transactions=homeView.findViewById(R.id.unchecked_transactions_MD_dashBoard);
        unchecked_dm_reqst=homeView.findViewById(R.id.unchecked_dm_requests_MD_dashBoard);
        requestGo=homeView.findViewById(R.id.unchecked_m_req_go_button);
        dmReqGo=homeView.findViewById(R.id.unchecked_dm_req_go_button);
        transactionGo=homeView.findViewById(R.id.unchecked_transactions_go_button);

        loadingBar=new ProgressDialog(getContext());


        requestGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new MerchantsFragment()).commit();
            }
        });

        dmReqGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeliveryManFragment()).commit();
            }
        });


        transactionGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,new TransactionFragment()).commit();
            }
        });



        return homeView;
    }

    @Override
    public void onStart() {
        super.onStart();


        loadingBar.setTitle("Home");
        loadingBar.setMessage("Please Wait,until the loading is completed.....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tc=dataSnapshot.getChildrenCount();
                    totalCustomers.setText(String.valueOf(tc));
                    loadingBar.dismiss();
                }
                else {
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        rootRef.child("Merchants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tm=dataSnapshot.getChildrenCount();
                    totalMerchants.setText(String.valueOf(tm));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        rootRef.child("Office Managers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tOm=dataSnapshot.getChildrenCount();

                    totalOm.setText(String.valueOf(tOm));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rootRef.child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tDm=dataSnapshot.getChildrenCount();

                    totalDm.setText(String.valueOf(tDm));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





          tUm=0;
        rootRef.child("Merchant Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        if(model.child("status").getValue().equals("pending")){

                            tUm++;

                        }
                    }

                    unchecked_merchant_reqst.setText("+"+String.valueOf(tUm));



                }
                else{
                    tUm=0;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tUdm=0;
        rootRef.child("DM Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        if(model.child("status").getValue().equals("Pending")){

                            tUdm++;

                        }
                    }

                    unchecked_dm_reqst.setText("+"+String.valueOf(tUdm));



                }
                else{
                    tUdm=0;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        tT=0;
        tT1=0;
        tT2=0;
        rootRef.child("Parcel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        if(model.child("status").getValue().equals("Pending") && model.child("paymentMethod").getValue().equals("Mobile Banking")){

                            tT1++;

                        }
                    }



                    rootRef.child("Purchase-Order").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                for(DataSnapshot model:dataSnapshot.getChildren()){

                                    if(model.child("status").getValue().equals("Pending")  && model.child("paymentType").getValue().equals("Mobile Banking")){

                                        tT2++;

                                    }
                                }




                                tT=tT1+tT2;

                                unchecked_transactions.setText("+"+String.valueOf(tT));



                            }
                            else{
                                tT=0;
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }
                else{

                    tT=0;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
