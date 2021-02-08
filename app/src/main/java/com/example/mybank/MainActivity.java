package com.example.mybank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button Customerbtn,balancebtn,tranctionbntn,tranferbtn;
    private Dialog balanceDialog,adddialog;

    static final String[] u = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Customerbtn=findViewById(R.id.button_customer);
        tranctionbntn=findViewById(R.id.button_tranction);
        balancebtn=findViewById(R.id.button_checkbalance);
        tranferbtn=findViewById(R.id.button_transfer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        getSupportActionBar().setTitle(" ");



        Customerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,CustomerActivity.class);
               // intent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(intent);

            }
        });


        balancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                balanceDialog=new Dialog(MainActivity.this);
                balanceDialog.setContentView(R.layout.balance_dialog);
                balanceDialog.setCancelable(false);
                balanceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                Button dialogaddbutton=balanceDialog.findViewById(R.id.cancelbutton);
                Button dialogokbutton2=balanceDialog.findViewById(R.id.okbutton2);
                final TextView CustomerBalance=balanceDialog.findViewById(R.id.quantity_no);
                final ProgressBar progressBar=balanceDialog.findViewById(R.id.BalnceprogressBar);
                progressBar.setVisibility(View.VISIBLE);






                ///////////////////////////////////////////////////////////fectch balance

                final FirebaseDatabase[] database = new FirebaseDatabase[1];
                 DatabaseReference myRef;
               /////// final String[] u = new String[1];




                database[0] = FirebaseDatabase.getInstance();
                myRef = database[0].getReference("Balance");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                         u[0] =snapshot.getValue().toString();
                         //String n=u;
                        // Log.e("aisa kyu","sahi"+Integer.parseInt(n));
                        CustomerBalance.setText(u[0] +".00");
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this,"Try Again"+error.getMessage(),Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });




                ///////////////////////////////////////////////////////////////////////////////

                        dialogaddbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {






                        adddialog=new Dialog(MainActivity.this);
                        adddialog.setContentView(R.layout.balance_dialog);
                        adddialog.setCancelable(false);
                        adddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        final Button addDone=adddialog.findViewById(R.id.cancelbutton);
                        Button dialogcancel=adddialog.findViewById(R.id.okbutton2);
                        final EditText Customeraddbalance=adddialog.findViewById(R.id.enterbalance);

                        balanceDialog.dismiss();
                        adddialog.show();



                        dialogcancel.setText("Cancel");
                        addDone.setText("Add");

                        CustomerBalance.setVisibility(View.INVISIBLE);
                        Customeraddbalance.setVisibility(View.VISIBLE);

                        dialogcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                adddialog.dismiss();
                            }
                        });

////////////////////////////////////////////////////////////////////////////////////////add done button
                        addDone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                addDone.setEnabled(false);

                                balanceDialog.dismiss();

                                final DatabaseReference myReftranc,myRefupdt;

                                myReftranc= database[0].getReference("Transction");
                                myRefupdt= database[0].getReference("Balance");

                               final String money= Customeraddbalance.getText().toString();




                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                CustomerallModel tranctiondata = new CustomerallModel("Self","123456789",money,currentDateTimeString);

                                myReftranc.push().setValue(tranctiondata).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        myRefupdt.setValue(String.valueOf(Integer.parseInt(money)+ Integer.parseInt(u[0])));

                                        Toast.makeText(MainActivity.this,"Added succesfully",Toast.LENGTH_SHORT).show();

                                        adddialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,"Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });




                            }
                        });




                    }
                });


                dialogokbutton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                       // productQuantity.setText("Qty: "+productQuantityno.getText());
                        balanceDialog.dismiss();
                    }
                });

                balanceDialog.show();



            }
        });


        tranctionbntn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,TransctionActivity.class);
               // intent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(intent);
            }
        });


        tranferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ChooseActivity.class);
                //intent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(intent);

            }
        });
    }


}
