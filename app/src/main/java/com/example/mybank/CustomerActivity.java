package com.example.mybank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class CustomerActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private ListView listView;
    private TestAdapter testAdapter;
    private int lastPos = -1;

    ArrayList<CustomerallModel> tests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        getSupportActionBar().setTitle("All Customer");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        listView = findViewById(R.id.customer_recyclview);
        testAdapter = new TestAdapter(CustomerActivity.this, tests);
        listView.setAdapter(testAdapter);

        getQues();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getQues() {
        //addListenerForSingleValueEvent


        myRef.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                tests.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerallModel t = new CustomerallModel();
                    t.setName(snapshot.child("name").getValue().toString());/////

                    t.setAccount(snapshot.getKey());////

                    t.setBalance(Long.parseLong(snapshot.child("balance").getValue().toString()));

                    tests.add(t);

                }
                testAdapter.dataList = tests;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
               // Log.e("The read success: ", "su" + tests.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
              //  Log.e("The read failed: ", databaseError.getMessage());


            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////// Adapter for this activity
    class TestAdapter extends ArrayAdapter<CustomerallModel> implements Filterable {
        private Context mContext;
        ArrayList<CustomerallModel> dataList;

        public TestAdapter(Context context, ArrayList<CustomerallModel> list) {
            super(context, 0, list);
            mContext = context;
            dataList = list;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if (listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.customer_item, parent, false);

           // Log.e("The read success: ", "supp" + tests.size());


            ((TextView) listItem.findViewById(R.id.item_name)).setText(dataList.get(position).getName());
            ((TextView) listItem.findViewById(R.id.accountno)).setText("A/c no : " + String.valueOf(dataList.get(position).getAccount()));
            ((TextView) listItem.findViewById(R.id.item_balance)).setText("Balance : " + String.valueOf(dataList.get(position).getBalance()));


            (listItem.findViewById(R.id.item_button_pay)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TransferActivity.class);
                    intent.putExtra("CUTOMERACCOUNT", dataList.get(position).getAccount());
                    intent.putExtra("CUSTOMERNAME", dataList.get(position).getName());
                    startActivity(intent);


                }


            });



            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);


            lastPos = position;


            return listItem;
        }
    }


}
