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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DmListAdapter extends RecyclerView.Adapter<DmListAdapter.DmHolder> {

    private ArrayList<DM> dms;
    private Context myContext;



    public DmListAdapter(ArrayList<DM> dms, Context myContext) {
        this.dms = dms;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.merchant_show_layout,parent,false);

        return new DmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DmHolder holder, int position) {

        final DM dm =dms.get(position);

        holder.dmName.setText(dm.getName());
        holder.businessName.setText(dm.getJobLocationArea()+","+dm.getJobLocationCity());
        holder.profileImage.setImageResource(R.drawable.delivery_man2);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogueBuilder;
                final AlertDialog dialog;
                Button delete,cancel;

                dialogueBuilder = new AlertDialog.Builder(myContext);
                LayoutInflater li =(LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View confirmDeleteView = li.inflate(R.layout.delete_merchant_confirm_layout,null);

                delete = (Button) confirmDeleteView.findViewById(R.id.delete_popup_confirm_md);
                cancel = (Button) confirmDeleteView.findViewById(R.id.cancel_popup_confirm_md);

                dialogueBuilder.setView(confirmDeleteView);
                dialog=dialogueBuilder.create();
                dialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference dmRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(dm.getDriverId());

                        dmRef.removeValue();

                        Toast.makeText(myContext, "Successfully deleted the Delivery Man", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent proIntent=new Intent(myContext,DmProfileActivity.class);
                proIntent.putExtra("dm_id",dm.getDriverId());
                myContext.startActivity(proIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dms==null? 0:dms.size();
    }


    public class DmHolder extends RecyclerView.ViewHolder{


        ImageView profileImage;
        TextView dmName,businessName;
        Button viewProfile,deleteBtn;

        public DmHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.merchant_image_md);
            dmName = itemView.findViewById(R.id.merchant_name_md);
            businessName = itemView.findViewById(R.id.merchant_company_md);
            viewProfile = itemView.findViewById(R.id.details_merchant_md);
            deleteBtn = itemView.findViewById(R.id.delete_merchant_md);
        }
    }



}
