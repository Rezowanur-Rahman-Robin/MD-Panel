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

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.ParcelHolder>{

    private ArrayList<Parcel> parcels;
    private Context myContext;

    public ParcelAdapter(ArrayList<Parcel> parcels, Context myContext) {
        this.parcels = parcels;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ParcelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.transaction_show_layout,parent,false);

        return new ParcelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelHolder holder, int position) {

        final Parcel parcel=parcels.get(position);

        holder.oId.setText("#"+String.valueOf(parcel.getParcelId()));
        holder.name.setText(parcel.getSenderName());
        holder.amount.setText(parcel.getTotalAmount()+" ৳");


        if(parcel.getStatus().equals("Pending")){
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

                        TextView oId,txnID,amount,time,sName,saddress,sPhone,rName,rPhone,rAddress,cross;
                        Button correct,wrong;

                        oId=bottomsheetView.findViewById(R.id.order_no_parcel_md);
                        txnID=bottomsheetView.findViewById(R.id.txn_id_parcel_md_details_layout);
                        amount=bottomsheetView.findViewById(R.id.amount_parcel_md_details_layout);
                        time=bottomsheetView.findViewById(R.id.order_time_md_details_layout);
                        sName=bottomsheetView.findViewById(R.id.sender_name_parcel_details_layout);
                        saddress=bottomsheetView.findViewById(R.id.sender_address_parcel_details_layout);
                        sPhone=bottomsheetView.findViewById(R.id.sender_Phone_parcel_details_layout);
                        rName=bottomsheetView.findViewById(R.id.receiver_name_parcel_details_layout);
                        rPhone=bottomsheetView.findViewById(R.id.receiver_Phone_parcel_details_layout);
                        rAddress=bottomsheetView.findViewById(R.id.receiver_address_parcel_details_layout);
                        cross=bottomsheetView.findViewById(R.id.cross_transaction_md);


                        correct=bottomsheetView.findViewById(R.id.correct_parcel_md_button);
                        wrong=bottomsheetView.findViewById(R.id.reject_parcel_md_button);



                        bottomSheetDialog.setContentView(bottomsheetView);
                        bottomSheetDialog.show();

                oId.setText("#"+String.valueOf(parcel.getParcelId()));
                txnID.setText(parcel.getTxdId());
                amount.setText(parcel.getTotalAmount()+" ৳");
                time.setText(parcel.getDate());
                sName.setText(parcel.getSenderName());
                saddress.setText(parcel.getPickupThana()+","+parcel.getPickupCity());
                sPhone.setText(parcel.getSenderPhone());
                rName.setText(parcel.getRecipientName());
                rPhone.setText(parcel.getRecipientPhone());
                rAddress.setText(parcel.getRecipientThana()+","+parcel.getPickupCity());




                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                correct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference parcelRef= FirebaseDatabase.getInstance().getReference().child("Parcel").child(String.valueOf(parcel.getParcelId()));



                        parcelRef.child("status").setValue("Verified").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(myContext, "Parcel Status Paid updated.", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();

                                }
                                else {
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        });


                    }
                });

                wrong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference parcelRef= FirebaseDatabase.getInstance().getReference().child("Parcel").child(String.valueOf(parcel.getParcelId()));



                        parcelRef.child("status").setValue("Incorrect TxnID").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(myContext, "Parcel Status Incorrect TxnID updated.", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();

                                }
                                else {
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
        return parcels==null? 0:parcels.size();
    }


    public class ParcelHolder extends RecyclerView.ViewHolder{

        TextView name,oId,amount,checked;
        LinearLayout layout;

        public ParcelHolder(@NonNull View itemView) {
            super(itemView);

            oId=itemView.findViewById(R.id.order_id_md);
            name=itemView.findViewById(R.id.trans_name__md);
            amount=itemView.findViewById(R.id.trans_amount_md);
            checked=itemView.findViewById(R.id.checked_status);
            layout=(LinearLayout) itemView.findViewById(R.id.trans_show_item_layout);
        }
    }

}
