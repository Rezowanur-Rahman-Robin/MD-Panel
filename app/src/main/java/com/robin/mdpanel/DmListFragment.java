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
public class DmListFragment extends Fragment {

    private View dmListView;
    private RecyclerView recyclerView;
    private DmListAdapter dmListAdapter;

    private ArrayList<DM> dmArrayList=new ArrayList<>();
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;



    public DmListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dmListView = inflater.inflate(R.layout.fragment_dm_list, container, false);



        rootRef= FirebaseDatabase.getInstance().getReference();

        recyclerView=(RecyclerView) dmListView.findViewById(R.id.dm_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dmListAdapter=new DmListAdapter(dmArrayList,getContext());
        recyclerView.setAdapter(dmListAdapter);

        loadingBar=new ProgressDialog(getContext());




        return dmListView;
    }

    @Override
    public void onStart() {
        super.onStart();

        rootRef.child("Drivers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    dmArrayList.clear();

                    for(DataSnapshot data:dataSnapshot.getChildren()){

                        DM dm=data.getValue(DM.class);

                        if(dm!=null){
                            dmArrayList.add(dm);
                            dmListAdapter .notifyDataSetChanged();
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
