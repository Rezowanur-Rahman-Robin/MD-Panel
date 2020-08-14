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
public class MerchantRequestFragment extends Fragment {

    private View requestView;
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;

    private ArrayList<MerchantRequest> merchantRequestArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;


    public MerchantRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestView = inflater.inflate(R.layout.fragment_merchant_request, container, false);

        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) requestView.findViewById(R.id.merchant_request_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestAdapter= new RequestAdapter(merchantRequestArrayList,getContext());
        recyclerView.setAdapter(requestAdapter);

        loadingBar=new ProgressDialog(getContext());


        return requestView;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadingBar.setTitle("Merchant Requests");
        loadingBar.setMessage("Please Wait,until the loading is completed.....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("Merchant Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    merchantRequestArrayList.clear();

                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        MerchantRequest merchantRequest=data.getValue(MerchantRequest.class);

                        if(merchantRequest!=null){

                            if(merchantRequest.getStatus().equals("pending")){
                                merchantRequestArrayList.add(merchantRequest);
                                requestAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                    loadingBar.dismiss();
                }
                else{
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingBar.dismiss();

            }
        });
    }
}
