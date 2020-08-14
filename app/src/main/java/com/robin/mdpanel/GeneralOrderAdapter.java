package com.robin.mdpanel;

import android.content.Context;
import android.text.Layout;
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

import java.util.ArrayList;

public class GeneralOrderAdapter extends RecyclerView.Adapter<GeneralOrderAdapter.GeneralOrderHolder> {

    private ArrayList<Parcel> parcels;
    private Context myContext;


    public GeneralOrderAdapter(ArrayList<Parcel> parcels, Context myContext) {
        this.parcels = parcels;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public GeneralOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.order_layout,parent,false);

        return new GeneralOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralOrderHolder holder, int position) {

        final Parcel parcel=parcels.get(position);

        holder.id.setText(String.valueOf(parcel.getParcelId()));
        holder.name.setText(parcel.getSenderName());
        holder.number.setText(parcel.getTotalAmount()+" ৳");
        holder.status.setText(parcel.getStatus());

        String DateString=parcel.getDate();

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

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);

                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.transaction_details_bottomsheet_layout,null);

                TextView oId,txIDTextView,txnID,amount,time,sName,saddress,sPhone,rName,rPhone,rAddress,cross;
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


                correct=bottomsheetView.findViewById(R.id.correct_parcel_md_button);
                wrong=bottomsheetView.findViewById(R.id.reject_parcel_md_button);

                correct.setVisibility(View.GONE);
                wrong.setVisibility(View.GONE);

                txIDTextView.setText("Payment Type: ");







                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();

                oId.setText("#"+String.valueOf(parcel.getParcelId()));
                txnID.setText(parcel.getPaymentMethod());
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
            }
        });





    }

    @Override
    public int getItemCount() {
        return parcels==null?0:parcels.size();
    }

    public class GeneralOrderHolder extends RecyclerView.ViewHolder{


        TextView year,date,month,id,name,number,status;
        ImageView statusImage;
        LinearLayout layout;

        public GeneralOrderHolder(@NonNull View itemView) {
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
