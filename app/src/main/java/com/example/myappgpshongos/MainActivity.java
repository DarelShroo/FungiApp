package com.example.myappgpshongos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button button_location;
    LocationManager locationManager;
    Button aspectosLegales;
    Button button_listado_hongos, btnAgregarHongoInicial;
    private DatabaseReference mDatabase;
    private String pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geoLocation();


        button_location = findViewById(R.id.listadohongospais);
        button_listado_hongos = findViewById(R.id.listadohongos);
        aspectosLegales = (Button) findViewById(R.id.aspectosLegales);
        btnAgregarHongoInicial = (Button) findViewById(R.id.btnAgregarHongoInicio);
        TextView t = findViewById(R.id.textView2);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, web.class);
                startActivity(i);
            }
        });

        btnAgregarHongoInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AgregarHongo.class);
                startActivity(i);
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        aspectosLegales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, aspectosLegales.class);
                startActivity(i);
            }
        });

        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent();
            }
        });

        button_listado_hongos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, HongosView.class);
                startActivity(i);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void geoLocation() {
        try {
            locationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5, (LocationListener) MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            pais = addresses.get(0).getCountryCode();
            String paises[][] = {{"ES", "DE", "JP", "US"},
                    {"Spain", "Germany", "Japan", "United States"}};

            boolean paisActivo = true;
            int i = 0;


            while(paisActivo){
                if(i< paises[0].length){
                    System.out.println(paises[0][i]);
                    if(paises[0][i].equals(pais)){
                        pais = paises[1][i];
                    }
                }
                i++;

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void intent() {
        Intent i = new Intent(MainActivity.this, HongosPaisView.class);
        i.putExtra("pais", pais);
        startActivity(i);
    }
}