package com.example.snapmind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesActivity extends AppCompatActivity {

    //declaring the variables for floating action button
    FloatingActionButton mcreatenotesfab;

    private FirebaseAuth firebaseAuth;

    //declare the variable for recycler view
    RecyclerView mrecylerview;

    //we are using stagged layout inside the recycler view
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    //declare the firebase uses
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    //declare a variable of firestore recycler adapter type (Main Variable)
    FirestoreRecyclerAdapter<firebasemodel, NotViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //assign the id
        mcreatenotesfab = findViewById(R.id.createnotefab);

        firebaseAuth = FirebaseAuth.getInstance(); //fetching the notes for the particular user
        firebaseUser = firebaseAuth.getCurrentUser(); // Initialize firebaseUser
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSupportActionBar().setTitle("Snap Mind");

        //to open new activity when click on the floating fab button
        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notesActivity.this, createnote.class));
            }
        });

        Query query = firebaseFirestore.collection("notes")
                .document(firebaseUser.getUid()) // Use firebaseUser.getUid() here
                .collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING);

        //to assign the get data form the recycler view
        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NotViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NotViewHolder holder, int position, @NonNull firebasemodel model) {
                //to get random color
                int colourcode = getRandomColor();
                holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor((colourcode),null));

                holder.notetitle.setText(model.getTitle()); // Use holder instead of noteViewHolder
                holder.notecontent.setText(model.getContent()); // Use holder instead of noteViewHolder

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"This is clicked",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public NotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NotViewHolder(view);
            }
        };

        mrecylerview = findViewById(R.id.recyclerview);
        mrecylerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrecylerview.setLayoutManager(staggeredGridLayoutManager);
        mrecylerview.setAdapter(noteAdapter);
    }



    public class NotViewHolder extends RecyclerView.ViewHolder {

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public NotViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
}

    private int getRandomColor()
    {
        List<Integer> colourcode = new ArrayList<>();
        colourcode.add(R.color.blue);
        colourcode.add(R.color.pink);
        colourcode.add(R.color.gray);
        colourcode.add(R.color.green);
        colourcode.add(R.color.yellow);
        colourcode.add(R.color.purple);
        colourcode.add(R.color.red);

        Random random = new Random();
        int number = random.nextInt(colourcode.size());
        return colourcode.get(number);
    }
}

