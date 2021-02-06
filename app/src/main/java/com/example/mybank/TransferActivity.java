package com.example.mybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import static com.example.mybank.MainActivity.u;


public class TransferActivity extends AppCompatActivity {

    private String Cname, Caaount;

    private TextView name, account;
    private EditText amount;
    private Button send;
    private ConstraintLayout successlayout;
    private LinearLayout Detailslayout;
    private ProgressBar progressBarTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        getSupportActionBar().setTitle("Transfer");

        setSupportActionBar(toolbar);
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        Cname = getIntent().getStringExtra("CUSTOMERNAME");
        Caaount = getIntent().getStringExtra("CUTOMERACCOUNT");
        /// final String valueCar=getIntent().getStringExtra("Value");
        name = findViewById(R.id.transfername);
        account = findViewById(R.id.transferaccount);
        amount = findViewById(R.id.transferamount);
        send = findViewById(R.id.buttonsend);
        successlayout = findViewById(R.id.layoutTranferSuccess);
        Detailslayout = findViewById(R.id.linearLayoutTransDetail);
        progressBarTrans = findViewById(R.id.progressBarTrans);

        name.setText("Name : " + Cname);
        account.setText("Account no : " + Caaount);


        final FirebaseDatabase[] firebaseDatabase = new FirebaseDatabase[1];


        final DatabaseReference Myreference, Crefrence, TransRef;

        firebaseDatabase[0] = FirebaseDatabase.getInstance();


        Myreference = firebaseDatabase[0].getReference("Balance");
        Crefrence = firebaseDatabase[0].getReference("Customer");
        TransRef = firebaseDatabase[0].getReference("Transction");


        //////////////////////////////////////////////////////////////////////////////fetch my old balance

        final FirebaseDatabase[] database = new FirebaseDatabase[1];
        DatabaseReference myRef;
        final String[] bal = new String[1];


        database[0] = FirebaseDatabase.getInstance();
        myRef = database[0].getReference("Balance");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bal[0] = snapshot.getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(TransferActivity.this, "Try Again" + error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });


        //////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////fetch old balance of customer to transfer

        final FirebaseDatabase[] databasee = new FirebaseDatabase[1];
        DatabaseReference myRefff;
        final String[] b = new String[1];
        final String[] z = new String[1];


        databasee[0] = FirebaseDatabase.getInstance();


        myRefff = database[0].getReference("Customer").child(Caaount).child("balance");

        myRefff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                z[0] = snapshot.getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ///////////////////////////////////////////////////////////////////////////transfer function button


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setEnabled(false);

                progressBarTrans.setVisibility(View.VISIBLE);


                final String a = amount.getText().toString();

                if (!TextUtils.isEmpty(a) && Integer.parseInt(a) > 0) {


                    ///////////////////////////////////////////////////////


                    //////////////////////////////////////////// update database as per transction
                    try {
                        if (Integer.parseInt(bal[0]) >= Integer.parseInt(a)) {
                            Myreference.setValue(String.valueOf(Integer.parseInt(bal[0]) - Integer.parseInt(a))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                    CustomerallModel tranctiondata = new CustomerallModel(Cname, Caaount, a, currentDateTimeString);

                                    TransRef.push().setValue(tranctiondata).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            ////////////////////////////////////////////////////////////////////////////////////////


                                            try {


                                                Crefrence.child(Caaount).child("balance").setValue(String.valueOf(Integer.parseInt(z[0]) + Integer.parseInt(a))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        progressBarTrans.setVisibility(View.INVISIBLE);

                                                        Detailslayout.setVisibility(View.INVISIBLE);

                                                        successlayout.setVisibility(View.VISIBLE);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                            } catch (Exception e) {
                                                Toast.makeText(TransferActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }


                                            ////////////////////////////////////////////////////////////////////////////

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(TransferActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        } else {
                            Toast.makeText(TransferActivity.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                            progressBarTrans.setVisibility(View.GONE);
                            send.setEnabled(true);
                        }
                    } catch (NumberFormatException o) {

                        Toast.makeText(TransferActivity.this, "Try Again" + o.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(TransferActivity.this, "Amount field is either empty or zero", Toast.LENGTH_SHORT).show();
                    progressBarTrans.setVisibility(View.GONE);
                    send.setEnabled(true);
                }

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {


           showWarning();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {



        showWarning();


    }

    private void showWarning() {

        AlertDialog.Builder builder=new AlertDialog.Builder(TransferActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}