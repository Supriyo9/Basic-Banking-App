package com.example.mybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class TransctionActivity extends AppCompatActivity {

    private AVLoadingIndicatorView avLoadingIndicatorView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TranctionAdapter tranctionAdapter;
    private RecyclerView recyclerView;
    ArrayList<CustomerallModel> tests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transction);



        Toolbar toolbar = findViewById(R.id.toolTrans);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        getSupportActionBar().setTitle("Tranctions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        recyclerView= findViewById(R.id.recycleTranction);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransctionActivity.this));
        tranctionAdapter = new TranctionAdapter(TransctionActivity.this, tests);
        recyclerView.setAdapter(tranctionAdapter);

        getQues();




    }



    public void getQues() {
        //addListenerForSingleValueEvent


        myRef.child("Transction").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                tests.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerallModel t = new CustomerallModel();
                    t.setTransctionId(snapshot.getKey());



                    t.setAccount(snapshot.child("account").getValue().toString());
                    t.setComment_time((snapshot.child("comment_time").getValue().toString()));
                    t.setName((snapshot.child("name").getValue().toString()));

                    Log.e("The read success: ", "suooo" + tests.size());
                    t.setTranctionmoney(snapshot.child("tranctionmoney").getValue().toString());

                    tests.add(t);

                }
                tranctionAdapter.balanceHelpers = tests;
                tranctionAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read success: ", "su" + tests.size());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: ", databaseError.getMessage());


            }
        });
    }















    ////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}