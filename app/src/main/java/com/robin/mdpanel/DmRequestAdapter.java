package com.robin.mdpanel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DmRequestAdapter extends RecyclerView.Adapter<DmRequestAdapter.DmRequestHOlder> {

    private ArrayList<DmRequest> dmRequests;
    private Context myContext;
    private DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public DmRequestAdapter(ArrayList<DmRequest> dmRequests, Context myContext) {
        this.dmRequests = dmRequests;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DmRequestHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.merchant_request_layout,parent,false);

        return new DmRequestHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DmRequestHOlder holder, int position) {

        final DmRequest dmRequest=dmRequests.get(position);

        holder.Id.setText(dmRequest.getId());
        holder.Name.setText(dmRequest.getName());
        holder.BusinessName.setText(dmRequest.getService_thana()+","+dmRequest.getService_district());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);

                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.request_details_bottomsheet_dm,null);

                final TextView name,email,namePP,emailPP,phonePP,nidPP,districtPP,thanaPP,addressPP,slDistrict,slThana,vehType,vehLicense,cross;

                Button acceptButton,rejectButton;

                name=bottomsheetView.findViewById(R.id.dm_name_details_layout);
                email=bottomsheetView.findViewById(R.id.dm_email_details_layout);
                namePP=bottomsheetView.findViewById(R.id.dm_pp_name_details_layout);
                emailPP=bottomsheetView.findViewById(R.id.dm_pp_email_details_layout);
                phonePP=bottomsheetView.findViewById(R.id.dm_pp_phone_details_layout);
                nidPP=bottomsheetView.findViewById(R.id.dd_pp_nid_details_layout);
                districtPP=bottomsheetView.findViewById(R.id.dd_pp_district_details_layout);
                thanaPP=bottomsheetView.findViewById(R.id.dd_pp_thana_details_layout);
                addressPP=bottomsheetView.findViewById(R.id.dd_pp_address_details_layout);
                slDistrict=bottomsheetView.findViewById(R.id.sl_district_dm_details_layout);
                slThana=bottomsheetView.findViewById(R.id.sl_thana_dm_details_layout);
                vehType=bottomsheetView.findViewById(R.id.dm_vehicle_type_details_layout);
                vehLicense=bottomsheetView.findViewById(R.id.dm_vehicle_license_details_layout);

                acceptButton=(Button)bottomsheetView.findViewById(R.id.accept_dm_req_button);
                rejectButton=(Button)bottomsheetView.findViewById(R.id.reject_dm_req_button);

                cross= bottomsheetView.findViewById(R.id.cross_dm);

                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();

                name.setText(dmRequest.getName());
                email.setText(dmRequest.getEmail());
                namePP.setText(dmRequest.getName());
                emailPP.setText(dmRequest.getEmail());
                phonePP.setText(dmRequest.getPhone());
                nidPP.setText(dmRequest.getNid());
                districtPP.setText(dmRequest.getDistrict());
                thanaPP.setText(dmRequest.getThana());
                addressPP.setText(dmRequest.getAddress_details());
                slDistrict.setText(dmRequest.getService_district());
                slThana.setText(dmRequest.getService_thana());
                vehType.setText(dmRequest.getVehicle_type());
                vehLicense.setText(dmRequest.getLicenseNo());

                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                final ProgressDialog loadingBar=new ProgressDialog(myContext);

                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        rootRef.child("DM Requests").child(dmRequest.getId()).child("status").setValue("Rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(myContext, "You have Rejected the Delivery Man Request.", Toast.LENGTH_LONG).show();
                                    bottomSheetDialog.dismiss();

                                }
                                else
                                {
                                    Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });


                    }
                });


                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingBar.setTitle("Accept Delivery Man");
                        loadingBar.setMessage("Please Wait Until the Process is Successfully Completed...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        rootRef.child("DM Requests").child(dmRequest.getId()).child("status").setValue("Accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    String nm=name.getText().toString();

                                    nm.replaceAll(" ","");

                                    final String password=nm+phonePP.getText().toString();

                                    mAuth.createUserWithEmailAndPassword(dmRequest.getEmail(),password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if (task.isSuccessful()){
                                                        String currentUserId=mAuth.getCurrentUser().getUid();

                                                        DM dm=new DM(addressPP.getText().toString(),districtPP.getText().toString(),currentUserId,slThana.getText().toString(),slDistrict.getText().toString(),vehLicense.getText().toString(),nidPP.getText().toString(),name.getText().toString(),phonePP.getText().toString(),"Active",thanaPP.getText().toString(),vehType.getText().toString());

                                                        rootRef.child("Drivers").child(currentUserId).setValue(dm);

                                                        loadingBar.dismiss();
                                                        bottomSheetDialog.dismiss();

                                                        String message="Dear "+name.getText().toString()+",\n You have requested for being Delivery Man of Online Courier System.We have reviewed your informations and accepted You as a Delivery of Our Service.\nYour Password is :'"+password+"'\nYour Email is :"+email.getText().toString()+"\nPlease Update your Profile info and Change the Password from your while first signin.\nThank You so much.";



                                                        Activity activity = (Activity) myContext;

                                                        List<String> list=new ArrayList<String>();

                                                        list.add(email.getText().toString());

                                                        new SendMailTask(activity).execute("rob1510697@gmail.com",
                                                                "vozwdcfhadslrlwz", list,"Delivery Man Request Accepted" , message);


                                                    }

                                                    else {
                                                        Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        loadingBar.dismiss();
                                                        bottomSheetDialog.dismiss();
                                                    }
                                                }
                                            });



                                }
                                else {
                                    Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        });
                    }
                });




            }
        });

    }

    @Override
    public int getItemCount() {
        return dmRequests==null? 0: dmRequests.size();
    }

    public class DmRequestHOlder extends RecyclerView.ViewHolder{
        TextView Name,Id,BusinessName;
        LinearLayout layout;

        public DmRequestHOlder(@NonNull View itemView) {
            super(itemView);

            Id=itemView.findViewById(R.id.request_id_md);
            Name=itemView.findViewById(R.id.merchant_name_request_md);
            BusinessName=itemView.findViewById(R.id.merchant_business_name_request_md);
            layout=(LinearLayout) itemView.findViewById(R.id.merchant_req_item_layout);
        }
    }
}
