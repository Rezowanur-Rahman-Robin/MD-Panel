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
public class MerchantListFragment extends Fragment {

    private View merchantListView;
    private RecyclerView recyclerView;
    private MerchantAdapter merchantAdapter;

    private ArrayList<Merchant> merchantArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;

    public MerchantListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        merchantListView = inflater.inflate(R.layout.fragment_merchant_list, container, false);


        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) merchantListView.findViewById(R.id.merchant_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        merchantAdapter= new MerchantAdapter(merchantArrayList,getContext());
        recyclerView.setAdapter(merchantAdapter);

        loadingBar=new ProgressDialog(getContext());


        return merchantListView;
    }

    @Override
    public void onStart() {
        super.onStart();



        rootRef.child("Merchants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    merchantArrayList.clear();

                    for(DataSnapshot data:dataSnapshot.getChildren()){

                        Merchant merchant=data.getValue(Merchant.class);

                        if(merchant!=null){
                            merchantArrayList.add(merchant);
                            merchantAdapter.notifyDataSetChanged();
                        }
                    }


                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });



    }
}
