package com.robin.mdpanel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder>{

    private ArrayList<MerchantRequest> merchantRequests;
    private Context myContext;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRequestRef= FirebaseDatabase.getInstance().getReference().child("Merchant Requests");



    public RequestAdapter(ArrayList<MerchantRequest> merchantRequests, Context myContext) {
        this.merchantRequests = merchantRequests;
        this.myContext = myContext;
    }



    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.merchant_request_layout,parent,false);

        return new RequestHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RequestHolder holder, int position) {

        final MerchantRequest merchantRequest=merchantRequests.get(position);

        holder.mId.setText(merchantRequest.getId());
        holder.mName.setText(merchantRequest.getName());
        holder.mBusinessName.setText(merchantRequest.getBusinessname());



        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);

                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.request_details_bottomsheet_layout,null);

                final TextView name,email,phn,b_name,b_details,b_address,bkash,rocket,cross;


                Button acceptButton,rejectButton;

                name=bottomsheetView.findViewById(R.id.merchant_name_details_layout);
                email=bottomsheetView.findViewById(R.id.merchant_email_details_layout);
                phn=bottomsheetView.findViewById(R.id.merchant_number_details_layout);
                b_name=bottomsheetView.findViewById(R.id.business_name_details_layout);
                b_details=bottomsheetView.findViewById(R.id.business_details_details_layout);
                b_address=bottomsheetView.findViewById(R.id.business_address_details_layout);
                bkash=bottomsheetView.findViewById(R.id.bkash_details_layout);
                rocket=bottomsheetView.findViewById(R.id.rocket_details_layout);
                acceptButton=bottomsheetView.findViewById(R.id.accept_merchant_req_button);
                rejectButton=bottomsheetView.findViewById(R.id.reject_merchant_req_button);

                cross= bottomsheetView.findViewById(R.id.cross_md);


                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();


                name.setText(merchantRequest.getName());
                email.setText(merchantRequest.getEmail());
                phn.setText(merchantRequest.getPhone());
                b_name.setText(merchantRequest.getBusinessname());
                b_details.setText(merchantRequest.getBusinessdetails());
                b_address.setText(merchantRequest.getBusinessaddress());
                bkash.setText(merchantRequest.getBkash());
                rocket.setText(merchantRequest.getRocket());


                final ProgressDialog loadingBar=new ProgressDialog(myContext);


                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });


                rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mRequestRef.child(merchantRequest.getId()).child("status").setValue("rejected").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Toast.makeText(myContext, "You have Rejected the Merchant Request.", Toast.LENGTH_LONG).show();

                                }
                                else {
                                    Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });




                    }
                });


                acceptButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        loadingBar.setTitle("Accept Merchant");
                        loadingBar.setMessage("Please Wait Until the Process is Successfully Completed...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();


                        mRequestRef.child(merchantRequest.getId()).child("status").setValue("accepted").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    final String selectedDistrict=merchantRequest.getPickUp_district();
                                    final String selectedThana=merchantRequest.getPickUp_thana();
                                    final String addressDetails=merchantRequest.getAddress_details();



                                    final String password=name.getText().toString().trim()+"123456";

                                    mAuth.createUserWithEmailAndPassword(merchantRequest.getEmail(),password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if(task.isSuccessful()){
                                                        String currentUserId=mAuth.getCurrentUser().getUid();

                                                        HashMap<String,Object> merchantRegisterMap=new HashMap<>();
                                                        merchantRegisterMap.put("id",currentUserId);
                                                        merchantRegisterMap.put("name",name.getText());
                                                        merchantRegisterMap.put("phone",phn.getText());
                                                        merchantRegisterMap.put("email",email.getText());
                                                        merchantRegisterMap.put("businessName",b_name.getText());
                                                        merchantRegisterMap.put("businessDetails",b_details.getText());
                                                        merchantRegisterMap.put("businessAddress",b_address.getText());
                                                        merchantRegisterMap.put("pickUp_district",selectedDistrict);
                                                        merchantRegisterMap.put("pickUp_thana",selectedThana);
                                                        merchantRegisterMap.put("address_details",addressDetails);
                                                        merchantRegisterMap.put("bkash",bkash.getText());
                                                        merchantRegisterMap.put("rocket",rocket.getText());
                                                        merchantRegisterMap.put("profile-image-link","");


                                                        rootRef.child("Merchants").child(currentUserId).setValue(merchantRegisterMap);

                                                        Toast.makeText(myContext, "Successfully Accept the Merchant Request.", Toast.LENGTH_LONG).show();


                                                        loadingBar.dismiss();

                                                        String message="Dear "+name.getText().toString()+",\n You have requested for being merchant of Online Courier System.We have reviewed your informations and accepted You as a Merchant of Our Service.\nYour Password is :'"+password+"'\nYour Email is :"+email.getText().toString()+"\nPlease Update your Profile info and Change the Password of Your Merchant Id while first signin.\nThank You so much.";


                                                        //JavaMailAPI javaMailAPI=new JavaMailAPI(myContext,email.getText().toString(),"Merchant Request Accepted",message);

                                                       // javaMailAPI.execute();

                                                      /*  try {
                                                            GMailSender sender = new GMailSender("courieronline2020@gmail.com", "Service_courieronline2020");
                                                            sender.sendMail("Merchant Request Accepted",
                                                                    message,
                                                                    "courieronline2020@gmail.com",
                                                                    email.getText().toString());

                                                            Toast.makeText(myContext, "Mail Sent", Toast.LENGTH_SHORT).show();
                                                        } catch (Exception e) {
                                                            Log.e("SendMail", e.getMessage(), e);
                                                        }

                                                       */

                                                        Activity activity = (Activity) myContext;

                                                        List<String> list=new ArrayList<String>();

                                                        list.add(email.getText().toString());

                                                        new SendMailTask(activity).execute("rob1510697@gmail.com",
                                                                "vozwdcfhadslrlwz", list,"Merchant Request Accepted" , message);


                                                    }
                                                    else {
                                                        Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        loadingBar.dismiss();
                                                    }




                                                }
                                            });





                                }
                                else {

                                    Toast.makeText(myContext, "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    loadingBar.dismiss();
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
        return merchantRequests==null? 0:merchantRequests.size();
    }


    public class RequestHolder extends RecyclerView.ViewHolder{

        TextView mName,mId,mBusinessName;
        LinearLayout layout;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            mId=itemView.findViewById(R.id.request_id_md);
            mName=itemView.findViewById(R.id.merchant_name_request_md);
            mBusinessName=itemView.findViewById(R.id.merchant_business_name_request_md);
            layout=(LinearLayout) itemView.findViewById(R.id.merchant_req_item_layout);

        }
    }
}
