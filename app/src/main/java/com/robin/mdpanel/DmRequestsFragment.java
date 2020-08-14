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
public class DmRequestsFragment extends Fragment {

    private View dmRequest;
    private RecyclerView recyclerView;
    private DmRequestAdapter dmRequestAdapter;

    private ArrayList<DmRequest> dmRequestArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;



    public DmRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dmRequest = inflater.inflate(R.layout.fragment_dm_requests, container, false);

        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView)dmRequest.findViewById(R.id.dm_request_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dmRequestAdapter=new DmRequestAdapter(dmRequestArrayList,getContext());
        recyclerView.setAdapter(dmRequestAdapter);

        loadingBar=new ProgressDialog(getContext());


        return dmRequest;
    }

    @Override
    public void onStart() {
        super.onStart();


        loadingBar.setTitle("Delivery Man Requests");
        loadingBar.setMessage("Please Wait,until the loading is completed.....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("DM Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dmRequestArrayList.clear();


                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        DmRequest dmRequest=data.getValue(DmRequest.class);

                        if(dmRequest!=null){

                            if(dmRequest.getStatus().equals("Pending")){
                                dmRequestArrayList.add(dmRequest);
                                dmRequestAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                    loadingBar.dismiss();
                }
                {
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
