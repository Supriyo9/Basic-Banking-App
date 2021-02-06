package com.example.mybank;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TranctionAdapter extends RecyclerView.Adapter<TranctionAdapter.ViewHolder> {

    Context context;
    static ArrayList<CustomerallModel> balanceHelpers;

    public TranctionAdapter(Context context, ArrayList<CustomerallModel> balanceHelpers) {
        this.context = context;
        this.balanceHelpers = balanceHelpers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Customername.setText(balanceHelpers.get(position).getName());

        if(balanceHelpers.get(position).getName().equals("Self"))

        {
            holder.CustomerTransc.setTextColor(Color.parseColor("#FF51DC07"));
            holder.CustomerTransc.setText("Amount : + " + balanceHelpers.get(position).getTranctionmoney());
        }
        else
        {
            holder.CustomerTransc.setTextColor(Color.parseColor("#FFF40000"));
            holder.CustomerTransc.setText("Amount : - " + balanceHelpers.get(position).getTranctionmoney());
        }

        holder.accountno.setText("A/c no: "+balanceHelpers.get(position).getAccount());
        holder.transId.setText("Tranction Id: "+balanceHelpers.get(position).getTransctionId());
        holder.transTime.setText(balanceHelpers.get(position).getComment_time());


    }

    @Override
    public int getItemCount() {
        return balanceHelpers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Customername, CustomerTransc, accountno, transId,transTime;
        private Button pay;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Customername = (TextView) itemView.findViewById(R.id.item_name);
            CustomerTransc = (TextView) itemView.findViewById(R.id.item_balance);
            accountno = (TextView) itemView.findViewById(R.id.accountno);
            transId = (TextView) itemView.findViewById(R.id.transctionid);
            transTime = (TextView) itemView.findViewById(R.id.transctiontime);
            pay=(Button)itemView.findViewById(R.id.item_button_pay);

            pay.setVisibility(View.INVISIBLE);

            transId.setVisibility(View.VISIBLE);
            transTime.setVisibility(View.VISIBLE);



        }
    }
}


