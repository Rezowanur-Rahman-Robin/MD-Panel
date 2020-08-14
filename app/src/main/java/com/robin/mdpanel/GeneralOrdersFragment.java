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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralOrdersFragment extends Fragment {

    private View generalOrdersView;

    private Spinner country_Spinner;



    private ArrayAdapter<GeneralOrdersFragment.Country> countryArrayAdapter;



    private ArrayList<GeneralOrdersFragment.Country> countries;

    private int getSelectedDistrictcode,getSelectedThanacode;
    private String selectedDistrict;

    private ProgressDialog loadingBar;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;


    private RecyclerView recyclerView;
    private GeneralOrderAdapter generalOrderAdapter;

    private ArrayList<Parcel> parcelArrayList=new ArrayList<>();



    public GeneralOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        generalOrdersView = inflater.inflate(R.layout.fragment_general_orders, container, false);

        mAuth=FirebaseAuth.getInstance();

        rootRef= FirebaseDatabase.getInstance().getReference();

        loadingBar=new ProgressDialog(getContext());

        recyclerView=(RecyclerView) generalOrdersView.findViewById(R.id.general_orders_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        generalOrderAdapter=new GeneralOrderAdapter(parcelArrayList,getContext());
        recyclerView.setAdapter(generalOrderAdapter);




        country_Spinner = (Spinner) generalOrdersView.findViewById(R.id.district_general_orders_Md);


        countries = new ArrayList<>();


        createLists();

        countryArrayAdapter = new ArrayAdapter<GeneralOrdersFragment.Country>(getContext(), R.layout.simple_spinner_dropdown_item, countries);
        countryArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        country_Spinner.setAdapter(countryArrayAdapter);





        country_Spinner.setOnItemSelectedListener(country_listener);








        return generalOrdersView;
    }


    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final GeneralOrdersFragment.Country country = (GeneralOrdersFragment.Country) country_Spinner.getItemAtPosition(position);
                selectedDistrict=country.getCountryName();
                getSelectedDistrictcode=country.getCountryID();

                loadingBar.setTitle("Getting sll orders of "+selectedDistrict);
                loadingBar.setMessage("Please Wait,until the loading is completed.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                rootRef.child("Parcel").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            parcelArrayList.clear();
                            generalOrderAdapter.notifyDataSetChanged();

                            for(DataSnapshot model:dataSnapshot.getChildren()){
                                Parcel parcel=model.getValue(Parcel.class);

                                if(parcel!=null){

                                    if(parcel.getPickupCity().equals(selectedDistrict)){

                                        parcelArrayList.add(parcel);
                                        generalOrderAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            loadingBar.dismiss();
                        }
                        else {
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

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void createLists() {
        GeneralOrdersFragment.Country country0 = new Country(0, "Choose District");
        GeneralOrdersFragment.Country country1 = new Country(1, "Dhaka");
        GeneralOrdersFragment.Country country2 = new Country(2, "Chittagong");
        GeneralOrdersFragment.Country country3 = new Country(2, "Faridpur");
        GeneralOrdersFragment.Country country4 = new Country(2, "Jamalpur");

        countries.add(new Country(0, "Choose District"));
        countries.add(new Country(1, "Dhaka"));
        countries.add(new Country(2, "Chittagong"));
        countries.add(new Country(3, "Faridpur"));
        countries.add(new Country(4, "Jamalpur"));





    }


    private class Country implements Comparable<GeneralOrdersFragment.Country> {

        private int countryID;
        private String countryName;


        public Country(int countryID, String countryName) {
            this.countryID = countryID;
            this.countryName = countryName;
        }

        public int getCountryID() {
            return countryID;
        }

        public String getCountryName() {
            return countryName;
        }

        @Override
        public String toString() {
            return countryName;
        }


        @Override
        public int compareTo(GeneralOrdersFragment.Country another) {
            return this.getCountryID() - another.getCountryID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
        }
    }





}
