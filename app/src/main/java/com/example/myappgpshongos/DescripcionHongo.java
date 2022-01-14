package com.example.myappgpshongos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DescripcionHongo extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView textNombrecientifico, textNombrecomun, textDescripcion, textComestible, textConfusiones, textImg, textPaises;
    ImageView imageHongo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_hongo);
        init();
    }

    public void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String x = String.valueOf(getIntent().getStringExtra("id"));
                //System.out.println(snapshot.child(x).child("id").getValue().toString());

                String nombrecientifico = snapshot.child(x).child("nombrecientifico").getValue().toString();
                String nombrecomun = snapshot.child(x).child("nombrecomun").getValue().toString();
                String descripcion = "Descripcion: " +snapshot.child(x).child("descripcion").getValue().toString();
                String comestible = snapshot.child(x).child("comestible").getValue().toString();
                if(comestible.equals(true)){
                    comestible = "Comestible: Si es comestible";
                }else {
                    comestible = "Comestible: No es comestible";
                }
                String confusiones = "Confusiones: "+snapshot.child(x).child("confusiones").getValue().toString();
                String img = snapshot.child(x).child("imagen").getValue().toString();

                //Añado los paises a un arrayList y compruebo si existe el pais del usuario con el pais del hongo
                int j = 0;
                String paises = "Paises: ";
                while (snapshot.child(x).child("paises").child(String.valueOf(j)).exists()) {
                    paises += snapshot.child(x).child("paises").child(String.valueOf(j)).getValue().toString() + ", ";
                    j++;
                }

                textNombrecientifico = findViewById(R.id.textNombreCientifico);
                textNombrecomun = findViewById(R.id.textNombreCoumun);
                textDescripcion = findViewById(R.id.textDescripcion);
                textComestible = findViewById(R.id.textComestible);
                textConfusiones = findViewById(R.id.textConfusiones);
                textPaises = findViewById(R.id.textPaises);

                textNombrecientifico.setText(nombrecientifico);
                textNombrecomun.setText(nombrecomun);
                textDescripcion.setText(descripcion);
                textComestible.setText(comestible);
                textConfusiones.setText(confusiones);
                textPaises.setText(paises);
                imageHongo = (ImageView) findViewById(R.id.imageHongoDesc);


                Bitmap bitmap = StringToBitMap(img);

                imageHongo.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
