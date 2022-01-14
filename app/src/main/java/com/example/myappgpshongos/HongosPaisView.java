package com.example.myappgpshongos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.Encoder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

public class HongosPaisView extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ImageView imageView;
    boolean aceptado  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_hongos_pais);
        imageView = (ImageView) findViewById(R.id.iconImageView2);
        TextView textxView = (TextView) findViewById(R.id.titulo);
        textxView.setText("Hongos Nacionales");
        init();
    }

    public void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Hongo> elements = new ArrayList<>();
                Iterable<DataSnapshot> iterable = snapshot.getChildren();

                for(DataSnapshot i : iterable){
                        String nombrecientifico = i.child("nombrecientifico").getValue().toString();
                        String nombrecomun = i.child("nombrecomun").getValue().toString();
                        String comestible =i.child("comestible").getValue().toString();
                        String img = i.child("imagen").getValue().toString();

                        //Añado los paises a un arrayList y compruebo si existe el pais del usuario con el pais del hongo
                        int j = 0;

                        boolean paisExiste = false;

                        while (i.child("paises").child(String.valueOf(j)).exists()) {
                            if (i.child("paises").child(String.valueOf(j)).getValue().equals(getIntent().getStringExtra("pais"))) {
                                paisExiste = true;
                            }
                            j++;
                        }

                        if (paisExiste) {
                            elements.add(new Hongo(nombrecientifico, nombrecomun, "", "", comestible, null, img, i.getKey()));
                        }
                }

                HongosAdapter listAdapter = new HongosAdapter(elements, HongosPaisView.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                listAdapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String id = elements.get
                                (recyclerView.getChildAdapterPosition(view)).getId();

                      Intent i = new Intent(HongosPaisView.this, DescripcionHongo.class);
                        i.putExtra("id", id);
                        startActivity(i);

                    }

                });
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HongosPaisView.this));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
