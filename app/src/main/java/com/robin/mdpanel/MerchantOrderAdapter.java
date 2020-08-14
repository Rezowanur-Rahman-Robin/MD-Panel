package com.robin.mdpanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MerchantOrderAdapter extends RecyclerView.Adapter<MerchantOrderAdapter.MerchantOrderHolder> {

    private ArrayList<MParcel> mParcels;
    private Context myContext;
    DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();

    public MerchantOrderAdapter(ArrayList<MParcel> mParcels, Context myContext) {
        this.mParcels = mParcels;
        this.myContext = myContext;

    }

    @NonNull
    @Override
    public MerchantOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.order_layout,parent,false);

        return new MerchantOrderHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MerchantOrderHolder holder, int position) {

        final MParcel mParcel=mParcels.get(position);

        holder.id.setText(String.valueOf(mParcel.getOrder_no()));
        holder.status.setText(mParcel.getStatus());

        String DateString=mParcel.getOrder_time();

        int length=DateString.length();
        if(length==22){

            String datee=DateString.substring(4,6);
            String month=DateString.substring(7,10);
            String yearr=DateString.substring(11,15);

            holder.year.setText(yearr);
            holder.month.setText(month);
            holder.date.setText(datee);
        }
        else if(length==23){


            String datee=DateString.substring(5,7);
            String month=DateString.substring(8,11);
            String yearr=DateString.substring(12,16);

            holder.year.setText(yearr);
            holder.month.setText(month);
            holder.date.setText(datee);

        }

        rootRef.child("Merchants").child(mParcel.getMerchant_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Merchant merchant=dataSnapshot.getValue(Merchant.class);

                    holder.name.setText(merchant.getName()+"   "+"("+merchant.getBusinessName()+")");
                    holder.number.setText(mParcel.getTotal_amount_of_order_for_merchant()+" ৳");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);

                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.transaction_details_bottomsheet_layout,null);

                final TextView oId,txIDTextView,txnID,amount,time,sName,saddress,sPhone,rName,rPhone,rAddress,cross,senderBtmSHeet,ReceiverBottomsheet;
                Button correct,wrong;

                oId=bottomsheetView.findViewById(R.id.order_no_parcel_md);
                txnID=bottomsheetView.findViewById(R.id.txn_id_parcel_md_details_layout);
                txIDTextView=bottomsheetView.findViewById(R.id.txnid_details_layout);
                amount=bottomsheetView.findViewById(R.id.amount_parcel_md_details_layout);
                time=bottomsheetView.findViewById(R.id.order_time_md_details_layout);
                sName=bottomsheetView.findViewById(R.id.sender_name_parcel_details_layout);
                saddress=bottomsheetView.findViewById(R.id.sender_address_parcel_details_layout);
                sPhone=bottomsheetView.findViewById(R.id.sender_Phone_parcel_details_layout);
                rName=bottomsheetView.findViewById(R.id.receiver_name_parcel_details_layout);
                rPhone=bottomsheetView.findViewById(R.id.receiver_Phone_parcel_details_layout);
                rAddress=bottomsheetView.findViewById(R.id.receiver_address_parcel_details_layout);
                cross=bottomsheetView.findViewById(R.id.cross_transaction_md);
                senderBtmSHeet=bottomsheetView.findViewById(R.id.sender_bottomsheet);
                ReceiverBottomsheet=bottomsheetView.findViewById(R.id.receiver_bottomsheet);


                correct=bottomsheetView.findViewById(R.id.correct_parcel_md_button);
                wrong=bottomsheetView.findViewById(R.id.reject_parcel_md_button);

                correct.setVisibility(View.GONE);
                wrong.setVisibility(View.GONE);
                senderBtmSHeet.setText("Merchant Information");
                ReceiverBottomsheet.setText("Buyer Information");

                txIDTextView.setText("Payment Type: ");







                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();

                oId.setText("#"+String.valueOf(mParcel.getOrder_no()));
                txnID.setText(mParcel.getPayment_type());
                amount.setText(mParcel.getTotal_amount_of_order_for_merchant()+" ৳");
                time.setText(mParcel.getOrder_time());
                rName.setText(mParcel.getReceiver_name());
                rPhone.setText(mParcel.getReceiver_phone());
                rAddress.setText(mParcel.getReceiver_district()+","+mParcel.getReceiver_thana());


                rootRef.child("Merchants").child(mParcel.getMerchant_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Merchant merchant=dataSnapshot.getValue(Merchant.class);
                            sName.setText(merchant.getName()+" ("+merchant.getBusinessName()+")");
                            saddress.setText(merchant.getPickUp_thana()+","+merchant.getPickUp_district());
                            sPhone.setText(merchant.getPhone());

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
            }
        });




    }

    @Override
    public int getItemCount() {
        return mParcels==null? 0: mParcels.size();
    }


    public class MerchantOrderHolder extends RecyclerView.ViewHolder{

        TextView year,date,month,id,name,number,status;
        ImageView statusImage;
        LinearLayout layout;

        public MerchantOrderHolder(@NonNull View itemView) {
            super(itemView);

            year=itemView.findViewById(R.id.order_date_year_merchant);
            date=itemView.findViewById(R.id.order_date_date_merchant);
            month=itemView.findViewById(R.id.order_date_month_merchant);
            id=itemView.findViewById(R.id.order_id_orders);
            name=itemView.findViewById(R.id.customer_name_orders);
            number=itemView.findViewById(R.id.orders_customer_number);
            status=itemView.findViewById(R.id.delivery_status_order);
            statusImage=itemView.findViewById(R.id.delivery_status_pic_order);
            layout=(LinearLayout) itemView.findViewById(R.id.order_lay_out);
        }
    }

}
