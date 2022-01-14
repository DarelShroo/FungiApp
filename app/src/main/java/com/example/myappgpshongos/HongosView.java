package com.example.myappgpshongos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HongosView extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_hongos_pais);
        imageView = (ImageView) findViewById(R.id.iconImageView2);
        init();
        TextView textView = (TextView) findViewById(R.id.titulo);
        textView.setText("HONGOS");
    }

    public void init() {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Hongo> elements = new ArrayList<>();
                Iterable<DataSnapshot> iterable = snapshot.getChildren();

                for(DataSnapshot j : iterable){
                    String nombrecientifico = j.child("nombrecientifico").getValue().toString();
                    String nombrecomun = j.child("nombrecomun").getValue().toString();
                    String comestible = j.child("comestible").getValue().toString();
                    String img = j.child("imagen").getValue().toString();

                    elements.add(new Hongo(nombrecientifico, nombrecomun, "", "", comestible, null,img,j.getKey()));

                }

                HongosAdapter listAdapter = new HongosAdapter(elements, HongosView.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                listAdapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String id = elements.get
                                (recyclerView.getChildAdapterPosition(view)).getId();


                        System.out.println(id);

                        Intent i = new Intent(HongosView.this, DescripcionHongo.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }

                });

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HongosView.this));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
