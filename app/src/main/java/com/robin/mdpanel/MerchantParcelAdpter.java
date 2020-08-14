package com.robin.mdpanel;

import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MerchantParcelAdpter extends RecyclerView.Adapter<MerchantParcelAdpter.MerchantParcelHolder> {

    private ArrayList<Purchase> purchases;
    private Context myContext;


    public MerchantParcelAdpter(ArrayList<Purchase> purchases, Context myContext) {
        this.purchases = purchases;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public MerchantParcelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.transaction_show_layout,parent,false);
        return new MerchantParcelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantParcelHolder holder, int position) {

        final Purchase purchase=purchases.get(position);

        holder.oId.setText("#"+String.valueOf(purchase.getOrderNo()));
        holder.name.setText(purchase.getBuyerName());
        holder.amount.setText(purchase.getTotal_taka()+" ৳");

        if(purchase.getStatus().equals("Pending")){
            holder.checked.setText("Unchecked");
        }
        else {
            holder.checked.setText("Checked");
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);

                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.transaction_details_bottomsheet_layout,null);

                final TextView oId,txnID,amount,time,sName,saddress,rName,rPhone,rAddress,cross,merchantInfo,buyerInfo;
                Button correct,wrong;
                LinearLayout receiverLayout;
                final TextView sPhone;

                oId=bottomsheetView.findViewById(R.id.order_no_parcel_md);
                txnID=bottomsheetView.findViewById(R.id.txn_id_parcel_md_details_layout);
                amount=bottomsheetView.findViewById(R.id.amount_parcel_md_details_layout);
                time=bottomsheetView.findViewById(R.id.order_time_md_details_layout);
                sName=bottomsheetView.findViewById(R.id.sender_name_parcel_details_layout);
                sPhone=bottomsheetView.findViewById(R.id.sender_Phone_parcel_details_layout);
                saddress=bottomsheetView.findViewById(R.id.sender_address_parcel_details_layout);
                rName=bottomsheetView.findViewById(R.id.receiver_name_parcel_details_layout);
                rPhone=bottomsheetView.findViewById(R.id.receiver_Phone_parcel_details_layout);
                rAddress=bottomsheetView.findViewById(R.id.receiver_address_parcel_details_layout);
                cross=bottomsheetView.findViewById(R.id.cross_transaction_md);
                buyerInfo=bottomsheetView.findViewById(R.id.sender_bottomsheet);
                merchantInfo=bottomsheetView.findViewById(R.id.receiver_bottomsheet);
                receiverLayout=(LinearLayout)bottomsheetView.findViewById(R.id.receiver_info_bottomSheet);

                buyerInfo.setText("Buyer Information");
                merchantInfo.setText("Merchant Information");
                receiverLayout.setVisibility(View.GONE);







                correct=bottomsheetView.findViewById(R.id.correct_parcel_md_button);
                wrong=bottomsheetView.findViewById(R.id.reject_parcel_md_button);


                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();



                oId.setText("#"+String.valueOf(purchase.getOrderNo()));
                txnID.setText(purchase.getTrxID());
                amount.setText(purchase.getTotal_taka()+" ৳");
                time.setText(purchase.getDate());
                sName.setText(purchase.getBuyerName());
                saddress.setText(purchase.getShippingThana()+","+purchase.getShippingCity());

                DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Users");

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sPhone.setText(dataSnapshot.child(purchase.getUserID()).getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                correct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference purchaseRef=FirebaseDatabase.getInstance().getReference().child("Purchase-Order").child(String.valueOf(purchase.getOrderNo()));

                        purchaseRef.child("status").setValue("Verified").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){



                                    Toast.makeText(myContext, "Parcel Status Verified Updated", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();


                                }
                                else {

                                    Toast.makeText(myContext, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        });
                    }
                });

                wrong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference purchaseRef=FirebaseDatabase.getInstance().getReference().child("Purchase-Order").child(String.valueOf(purchase.getOrderNo()));

                        purchaseRef.child("status").setValue("Incorrect TxnID").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){



                                    Toast.makeText(myContext, "Parcel Status Incorrect TxnID is Updated", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();


                                }
                                else {

                                    Toast.makeText(myContext, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        return purchases==null? 0:purchases.size();
    }


    public class MerchantParcelHolder extends RecyclerView.ViewHolder{


        TextView name,oId,amount,checked;
        LinearLayout layout;
        public MerchantParcelHolder(@NonNull View itemView) {
            super(itemView);

            oId=itemView.findViewById(R.id.order_id_md);
            name=itemView.findViewById(R.id.trans_name__md);
            amount=itemView.findViewById(R.id.trans_amount_md);
            checked=itemView.findViewById(R.id.checked_status);
            layout=(LinearLayout) itemView.findViewById(R.id.trans_show_item_layout);
        }
    }

}
