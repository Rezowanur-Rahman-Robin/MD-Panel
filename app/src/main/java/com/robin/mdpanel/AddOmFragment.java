package com.robin.mdpanel;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOmFragment extends Fragment {

    private View addOmView;
    private EditText name,email,password,nid,Phone;
    private Button addButton;

    private Spinner country_Spinner;


    private ArrayAdapter<Country> countryArrayAdapter;


    private ArrayList<Country> countries;
    private int getSelectedDistrictcode,getSelectedThanacode;
    private String selectedDistrict;

    private ProgressDialog loadingBar;

    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;

    private String omName,omEmail,omPass,onNid,omphone,omDistrict,omThana;


    public AddOmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addOmView=  inflater.inflate(R.layout.fragment_add_om, container, false);



        name=addOmView.findViewById(R.id.m_name_MD);
        email=addOmView.findViewById(R.id.m_email_MD);
        Phone=addOmView.findViewById(R.id.m_phone_MD);
        password=addOmView.findViewById(R.id.m_pass_MD);
        nid=addOmView.findViewById(R.id.m_nid_MD);
        addButton=addOmView.findViewById(R.id.addOmButton);


        mAuth=FirebaseAuth.getInstance();

        rootRef= FirebaseDatabase.getInstance().getReference();

        loadingBar=new ProgressDialog(getContext());



        country_Spinner = (Spinner) addOmView.findViewById(R.id.district_om_add_Md);


        countries = new ArrayList<>();


        createLists();

        countryArrayAdapter = new ArrayAdapter<Country>(getContext(), R.layout.simple_spinner_dropdown_item, countries);
        countryArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        country_Spinner.setAdapter(countryArrayAdapter);





        country_Spinner.setOnItemSelectedListener(country_listener);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewManager();
            }
        });




        return addOmView;
    }




    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Country country = (Country) country_Spinner.getItemAtPosition(position);
                selectedDistrict=country.getCountryName();
                getSelectedDistrictcode=country.getCountryID();




            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };






    private void createLists() {
        Country country0 = new Country(0, "Choose District Of Manager");
        Country country1 = new Country(1, "Dhaka");
        Country country2 = new Country(2, "Chittagong");
        Country country3 = new Country(3, "Faridpur");
        Country country4 = new Country(4, "Jamalpur");

        countries.add(new Country(0, "Choose District Of Manager"));
        countries.add(new Country(1, "Dhaka"));
        countries.add(new Country(2, "Chittagong"));
        countries.add(new Country(3, "Faridpur"));
        countries.add(new Country(4, "Jamalpur"));





    }


    private class Country implements Comparable<Country> {

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
        public int compareTo(Country another) {
            return this.getCountryID() - another.getCountryID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
        }
    }



    private void addNewManager() {



        omName=name.getText().toString();
        omEmail=email.getText().toString();
        omphone=Phone.getText().toString();
        omPass=password.getText().toString();
        onNid=nid.getText().toString();


        if(omName.isEmpty()){
            name.setError("Enter Manager Name");
            name.requestFocus();
            return;
        }

        if(onNid.isEmpty()){
            nid.setError("Enter NID No");
            nid.requestFocus();
            return;
        }


        if(omEmail.isEmpty()){
            email.setError("Enter Manager Email");
            email.requestFocus();
            return;
        }
        if(omphone.isEmpty()){
            Phone.setError("Enter Manager Phone");
            Phone.requestFocus();
            return;
        }

        if(omPass.isEmpty()){
            password.setError("Enter Manager Password");
            password.requestFocus();
            return;
        }
        if (omPass.length()<6){
            Toast.makeText(getContext(), "Password should be at least 6 letters.", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if(selectedDistrict.isEmpty()){
            Toast.makeText(getContext(), "Select Manager District", Toast.LENGTH_SHORT).show();
            return;
        }


        loadingBar.setTitle("Add Manager");
        loadingBar.setMessage("Please wait,until the loading is completed...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth.createUserWithEmailAndPassword(omEmail,omPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String currentUserId=mAuth.getCurrentUser().getUid();

                           OM om=new OM(omName,omEmail,omphone,omPass,selectedDistrict,currentUserId,onNid);


                            rootRef.child("Office Managers").child(currentUserId).setValue(om).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "Successfully Added Office Manager.", Toast.LENGTH_LONG).show();

                                        loadingBar.dismiss();

                                        name.setText("");
                                        email.setText("");
                                        Phone.setText("");
                                        password.setText("");
                                        nid.setText("");
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Error:"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        name.setText("");
                                        email.setText("");
                                        Phone.setText("");
                                        password.setText("");
                                        nid.setText("");
                                    }
                                }
                            });





                        }
                        else {
                            Toast.makeText(getContext(), "Error:"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            loadingBar.dismiss();
                            name.setText("");
                            email.setText("");
                            Phone.setText("");
                            password.setText("");
                            nid.setText("");
                        }




                    }
                });



    }






}
