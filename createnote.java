package com.example.snapmind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createnote extends AppCompatActivity {

    EditText mcreatetitleofnote , mcreatecontentofnote ;

    FloatingActionButton msavenotes;

    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        msavenotes = findViewById(R.id.savenote);
        mcreatecontentofnote =findViewById(R.id.createcontentofnote);
        mcreatetitleofnote = findViewById(R.id.createtitleofnote);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //to storing data into the firebasefirestore
        msavenotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to storing the title, check whether user input something or not
                String title = mcreatetitleofnote.getText().toString();

                //to storing the content, check whether user input something or not
                String content = mcreatecontentofnote.getText().toString();

                //now checking the user enter something or not
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Both Fields are require to save the file", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //to storing data in cloud firestore, take the object of the optimal reference ,create document for notes and the user
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();

                    //create a map
                    Map<String , Object > note = new HashMap<>();

                    //to see the title and the content if the note
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //to display the toast
                            Toast.makeText(getApplicationContext(),"Note created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createnote.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to create note", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}