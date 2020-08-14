package com.robin.mdpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.MerchantHolder>{

    private ArrayList<Merchant> merchants;
    private Context myContext;

    public MerchantAdapter(ArrayList<Merchant> merchants, Context myContext) {
        this.merchants = merchants;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public MerchantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.merchant_show_layout,parent,false);

        return new MerchantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantHolder holder, int position) {

        final Merchant merchant=merchants.get(position);

        holder.merchantName.setText(merchant.getName());
        holder.businessName.setText(merchant.getBusinessName());


        if(merchant.getProfile_image_link()!=""){

            Picasso.with(myContext).load(merchant.getProfile_image_link()).placeholder(R.drawable.businessman).into(holder.profileImage);

        }


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder;
                final AlertDialog dialogue;
                Button delete,cancel;

                dialogBuilder =new AlertDialog.Builder(myContext);
                LayoutInflater li = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View confirmDeleteView= li.inflate(R.layout.delete_merchant_confirm_layout,null);

                delete =(Button) confirmDeleteView.findViewById(R.id.delete_popup_confirm_md);
                cancel =(Button) confirmDeleteView.findViewById(R.id.cancel_popup_confirm_md);

                dialogBuilder.setView(confirmDeleteView);
                dialogue=dialogBuilder.create();
                dialogue.show();


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference merchantRef = FirebaseDatabase.getInstance().getReference().child("Merchants").child(merchant.getId());

                        merchantRef.removeValue();




                        final DatabaseReference productRef=FirebaseDatabase.getInstance().getReference().child("Merchant-Products");

                        productRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){
                                    String MID,PID;

                                    for(DataSnapshot data:dataSnapshot.getChildren()){

                                        MID=data.child("id").getValue().toString();
                                        PID=data.child("p_id").getValue().toString();


                                        if(MID.equals(merchant.getId())){

                                            productRef.child(PID).removeValue();
                                        }


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        final DatabaseReference merchantParcelRef=FirebaseDatabase.getInstance().getReference().child("Merchant-parcel");

                        merchantParcelRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){

                                    String Mid,parcelId;
                                    for(DataSnapshot data:dataSnapshot.getChildren()){

                                        Mid=data.child("merchant_id").getValue().toString();
                                        parcelId=data.child("percel_id").getValue().toString();


                                        if(Mid.equals(merchant.getId())){

                                            merchantParcelRef.child(parcelId).removeValue();

                                        }


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dialogue.dismiss();

                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogue.dismiss();
                    }
                });

            }
        });



        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileIntent=new Intent(myContext,MerchantProfile.class);

                profileIntent.putExtra("merchant_id",merchant.getId());
                myContext.startActivity(profileIntent);

            }
        });





    }

    @Override
    public int getItemCount() {
        return merchants==null? 0: merchants.size();
    }

    public class MerchantHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        TextView merchantName,businessName;
        Button viewProfile,deleteBtn;


        public MerchantHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.merchant_image_md);
            merchantName = itemView.findViewById(R.id.merchant_name_md);
            businessName = itemView.findViewById(R.id.merchant_company_md);
            viewProfile = itemView.findViewById(R.id.details_merchant_md);
            deleteBtn = itemView.findViewById(R.id.delete_merchant_md);
        }
    }




}
