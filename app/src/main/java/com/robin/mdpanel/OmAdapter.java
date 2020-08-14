package com.robin.mdpanel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OmAdapter extends RecyclerView.Adapter<OmAdapter.OmHolder>{

    private ArrayList<OM> oms;
    private Context myContext;

    public OmAdapter(ArrayList<OM> oms, Context myContext) {
        this.oms = oms;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public OmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.merchant_show_layout,parent,false);
        return new OmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OmHolder holder, int position) {

        final OM om=oms.get(position);

        holder.omName.setText(om.getName());
        holder.omLocation.setText(om.getDistrict());

        holder.profileImage.setImageResource(R.drawable.boss);

        holder.viewProfile.setText("Details");

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

                TextView text=confirmDeleteView.findViewById(R.id.confirm_Text);
                text.setText("Are You Sure to delete this Manager?");

                dialogBuilder.setView(confirmDeleteView);
                dialogue=dialogBuilder.create();
                dialogue.show();


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference omRef= FirebaseDatabase.getInstance().getReference().child("Office Managers");
                        omRef.child(om.getOm_id()).removeValue();

                        Toast.makeText(myContext, "Manager Successfully Deleted", Toast.LENGTH_SHORT).show();
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
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(myContext,R.style.BottomSheetDialogueTheme);
                View bottomsheetView = LayoutInflater.from(myContext).inflate(R.layout.om_show_bottomsheet_layout,null);

                TextView name,location,email,phone,password,nid,district,thana;
                ImageButton callBtn;

                name=bottomsheetView.findViewById(R.id.om_name_md_bottomsheet);
                location=bottomsheetView.findViewById(R.id.om_location_md_bottomsheet);
                email=bottomsheetView.findViewById(R.id.om_email_md_bottomsheet);
                phone=bottomsheetView.findViewById(R.id.om_phone_md_bottomsheet);
                password=bottomsheetView.findViewById(R.id.om_pass_md_bottomsheet);
                nid=bottomsheetView.findViewById(R.id.om_nid_md_bottomsheet);
                district=bottomsheetView.findViewById(R.id.om_district_md_bottomsheet);
                callBtn=(ImageButton) bottomsheetView.findViewById(R.id.om_call_button);

                bottomSheetDialog.setContentView(bottomsheetView);
                bottomSheetDialog.show();

                name.setText(om.getName());
                location.setText(om.getDistrict());
                email.setText(om.getEmail());
                phone.setText(om.getPhone());
                password.setText(om.getPassword());
                nid.setText(om.getN_id());
                district.setText(om.getDistrict());

                callBtn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.dismiss();

                        String s="tel:+88"+om.getPhone();

                        Intent intent=new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        myContext.startActivity(intent);

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return oms==null? 0:oms.size();
    }

    public class OmHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        TextView omName,omLocation;
        Button viewProfile,deleteBtn;

        public OmHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.merchant_image_md);
            omName = itemView.findViewById(R.id.merchant_name_md);
            omLocation = itemView.findViewById(R.id.merchant_company_md);
            viewProfile = itemView.findViewById(R.id.details_merchant_md);
            deleteBtn = itemView.findViewById(R.id.delete_merchant_md);
        }
    }

}
