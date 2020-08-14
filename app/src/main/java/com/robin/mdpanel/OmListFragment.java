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
public class OmListFragment extends Fragment {

    private View omListView;
    private RecyclerView recyclerView;
    private OmAdapter omAdapter;

    private ArrayList<OM> omArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;


    public OmListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        omListView = inflater.inflate(R.layout.fragment_om_list, container, false);


        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) omListView.findViewById(R.id.om_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        omAdapter=new OmAdapter(omArrayList,getContext());
        recyclerView.setAdapter(omAdapter);

        loadingBar=new ProgressDialog(getContext());


        return omListView;
    }

    @Override
    public void onStart() {
        super.onStart();

        rootRef.child("Office Managers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    omArrayList.clear();

                    for(DataSnapshot data:dataSnapshot.getChildren()){

                        OM om=data.getValue(OM.class);

                        if(om!=null){
                            omArrayList.add(om);
                            omAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
