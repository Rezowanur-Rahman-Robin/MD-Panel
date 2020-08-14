package com.robin.mdpanel;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MerchantParcelFragment extends Fragment {

    private View merchantParcel;
    private RecyclerView recyclerView;
    private MerchantParcelAdpter merchantParcelAdpter;

    private ArrayList<Purchase> purchaseArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;



    public MerchantParcelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        merchantParcel = inflater.inflate(R.layout.fragment_merchant_parcel, container, false);


        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) merchantParcel.findViewById(R.id.merchant_parcel_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);

        merchantParcelAdpter=new MerchantParcelAdpter(purchaseArrayList,getContext());
        recyclerView.setAdapter(merchantParcelAdpter);


        loadingBar= new ProgressDialog(getActivity());


        return merchantParcel;
    }

    @Override
    public void onStart() {
        super.onStart();

        rootRef.child("Purchase-Order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    purchaseArrayList.clear();

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        Purchase purchase=model.getValue(Purchase.class);

                        if(purchase!=null){

                            if(purchase.getPaymentType().equals("Mobile Banking")){

                                purchaseArrayList.add(purchase);
                                merchantParcelAdpter.notifyDataSetChanged();

                            }
                        }
                    }


                }

                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });
    }
}
