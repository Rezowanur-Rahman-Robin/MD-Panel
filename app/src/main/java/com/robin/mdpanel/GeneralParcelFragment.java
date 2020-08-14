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
public class GeneralParcelFragment extends Fragment {

    private View generalParcel;
    private RecyclerView recyclerView;
    private ParcelAdapter parcelAdapter;

    private ArrayList<Parcel> parcelArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;


    public GeneralParcelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        generalParcel = inflater.inflate(R.layout.fragment_general_parcel, container, false);


        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) generalParcel.findViewById(R.id.general_parcel_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);


        parcelAdapter=new ParcelAdapter(parcelArrayList,getContext());
        recyclerView.setAdapter(parcelAdapter);




        loadingBar= new ProgressDialog(getActivity());




        return generalParcel;
    }

    @Override
    public void onStart() {
        super.onStart();


        loadingBar.setTitle("General Parcel");
        loadingBar.setMessage("Please Wait,until the loading is completed.....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        rootRef.child("Parcel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    parcelArrayList.clear();

                    for(DataSnapshot model:dataSnapshot.getChildren()){

                        Parcel parcel=model.getValue(Parcel.class);

                        if(parcel!=null){

                            if(parcel.getPaymentMethod().equals("Mobile Banking")){

                                parcelArrayList.add(parcel);
                                parcelAdapter.notifyDataSetChanged();
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
