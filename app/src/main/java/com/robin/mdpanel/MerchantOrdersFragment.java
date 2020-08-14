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
public class MerchantOrdersFragment extends Fragment {

    private View merchantOrderView;

    private Spinner country_Spinner;



    private ArrayAdapter<MerchantOrdersFragment.Country> countryArrayAdapter;



    private ArrayList<MerchantOrdersFragment.Country> countries;

    private int getSelectedDistrictcode,getSelectedThanacode;
    private String selectedDistrict;

    private ProgressDialog loadingBar;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;


    private RecyclerView recyclerView;
    private MerchantOrderAdapter merchantOrderAdapter;

    private ArrayList<MParcel> mParcelArrayList=new ArrayList<>();



    public MerchantOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        merchantOrderView = inflater.inflate(R.layout.fragment_merchant_orders, container, false);


        mAuth=FirebaseAuth.getInstance();

        rootRef= FirebaseDatabase.getInstance().getReference();

        loadingBar=new ProgressDialog(getContext());

        recyclerView=(RecyclerView) merchantOrderView.findViewById(R.id.merchant_orders_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        merchantOrderAdapter =new MerchantOrderAdapter(mParcelArrayList,getContext());
        recyclerView.setAdapter(merchantOrderAdapter);



        country_Spinner = (Spinner) merchantOrderView.findViewById(R.id.district_merchant_orders_Md);


        countries = new ArrayList<>();


        createLists();

        countryArrayAdapter = new ArrayAdapter<MerchantOrdersFragment.Country>(getContext(), R.layout.simple_spinner_dropdown_item, countries);
        countryArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        country_Spinner.setAdapter(countryArrayAdapter);





        country_Spinner.setOnItemSelectedListener(country_listener);



        return merchantOrderView;
    }



    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final MerchantOrdersFragment.Country country = (MerchantOrdersFragment.Country) country_Spinner.getItemAtPosition(position);
                selectedDistrict=country.getCountryName();
                getSelectedDistrictcode=country.getCountryID();

                loadingBar.setTitle("Getting sll orders of "+selectedDistrict);
                loadingBar.setMessage("Please Wait,until the loading is completed.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                rootRef.child("Merchant-parcel").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            mParcelArrayList.clear();
                            merchantOrderAdapter.notifyDataSetChanged();

                            for(DataSnapshot model:dataSnapshot.getChildren()){
                                MParcel mParcel=model.getValue(MParcel.class);

                                if(mParcel!=null){

                                    if(mParcel.getReceiver_district().equals(selectedDistrict)){

                                        mParcelArrayList.add(mParcel);
                                        merchantOrderAdapter.notifyDataSetChanged();
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
        MerchantOrdersFragment.Country country0 = new MerchantOrdersFragment.Country(0, "Choose District");
        MerchantOrdersFragment.Country country1 = new MerchantOrdersFragment.Country(1, "Dhaka");
        MerchantOrdersFragment.Country country2 = new MerchantOrdersFragment.Country(2, "Chittagong");
        MerchantOrdersFragment.Country country3 = new MerchantOrdersFragment.Country(3, "Faridpur");
        MerchantOrdersFragment.Country country4 = new MerchantOrdersFragment.Country(4, "Jamalpur");

        countries.add(new MerchantOrdersFragment.Country(0, "Choose District"));
        countries.add(new MerchantOrdersFragment.Country(1, "Dhaka"));
        countries.add(new MerchantOrdersFragment.Country(2, "Chittagong"));
        countries.add(new MerchantOrdersFragment.Country(2, "Faridpur"));
        countries.add(new MerchantOrdersFragment.Country(2, "Jamalpur"));





    }


    private class Country implements Comparable<MerchantOrdersFragment.Country> {

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
        public int compareTo(MerchantOrdersFragment.Country another) {
            return this.getCountryID() - another.getCountryID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
        }
    }


}
