package com.example.myappgpshongos;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AgregarHongo extends AppCompatActivity {
    Button btnExaminarImagen, btnAgregarHongo, btnAgregarPais;
    EditText editTextNombreCientifico, editTextNombreComun, editTextPais, editTextDescripcion, editTextConfusiones;
    ImageView imgHongo;
    ListView listaPaises;
    RadioGroup comestible;
    RadioButton comestibleSelection;
    String sImage, nombrecientifico, nombrecomun, descripcion, confusiones, sComestible;
    ArrayList paises;
    ArrayAdapter adaptador1;
    Bitmap bmp;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_hongos);

        btnExaminarImagen = findViewById(R.id.btnExaminarImg);
        btnAgregarHongo = findViewById(R.id.btnAgregarHongo);
        btnAgregarPais = findViewById(R.id.btnPais);
        editTextConfusiones = findViewById(R.id.textEditConfusiones);
        editTextNombreCientifico = findViewById(R.id.textEditNombreCientifico);
        editTextNombreComun = findViewById(R.id.textEditNombreComun);
        editTextPais = findViewById(R.id.textEditPais);
        editTextDescripcion = findViewById(R.id.textEditDescripcion);
        comestible = findViewById(R.id.radioGroup);
        imgHongo = findViewById(R.id.uploadImg);
        listaPaises = findViewById(R.id.listViewPaises);
        imgHongo = (ImageView) findViewById(R.id.uploadImg);


        paises = new ArrayList();

        adaptador1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, paises);

        listaPaises.setAdapter(adaptador1);

        listaPaises.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView adapterView, View view, int i, long l) {
                final int posicion = i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(AgregarHongo.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Eliminar este Pais ?");
                dialogo1.setCancelable(false);

                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        paises.remove(posicion);
                        adaptador1.notifyDataSetChanged();
                    }
                });

                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });

                dialogo1.show();

                return false;
            }
        });


        btnAgregarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar(v);
            }
        });

        btnExaminarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AgregarHongo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AgregarHongo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                } else {
                    abrirGaleria(v);
                }
            }
        });
        btnAgregarHongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombrecientifico = editTextNombreCientifico.getText().toString();
                nombrecomun = editTextNombreComun.getText().toString();
                descripcion = editTextDescripcion.getText().toString();
                confusiones = editTextConfusiones.getText().toString();
                checkButton(v);
                if (sImage == null) {
                    sImage = "";
                }

                init();

                editTextNombreCientifico.clearAnimation();
                editTextNombreComun.setText("");
                editTextPais.setText("");
                editTextDescripcion.setText("");
                editTextConfusiones.setText("");
                paises.clear();
                imgHongo.setImageResource(R.drawable.ic_imagen_no_disponible_svg);

                Toast.makeText(getApplicationContext(), "Se a añadido un nuevo hongo", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void init() {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Hongo h = new Hongo(nombrecientifico, nombrecomun, descripcion, confusiones, confusiones, paises, sImage, String.valueOf(snapshot.getChildrenCount()+1));
                mDatabase.push().setValue(h);
                System.out.println(nombrecientifico);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void agregar(View v) {
        paises.add(editTextPais.getText().toString());
        adaptador1.notifyDataSetChanged();
        scrollMyListViewToBottom();
        editTextPais.setText("");
    }

    public void abrirGaleria(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                100);

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    String selectedPath = selectedImage.getPath();
                    if (requestCode == 100) {

                        if (selectedPath != null) {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                            bmp = BitmapFactory.decodeStream(imageStream);

                            sImage = BitMapToString(bmp);

                            // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                            imgHongo.setImageBitmap(StringToBitMap(sImage));
                        }
                    }
                }
                break;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

    private void scrollMyListViewToBottom() {
        listaPaises.setSelection(paises.size() - 1);
    }

    public void checkButton(View v){
        comestibleSelection = findViewById(comestible.getCheckedRadioButtonId());
        sComestible = comestibleSelection.getText().toString();
        if(sComestible.equals("si")){
            sComestible = "true";
        }else {
            sComestible = "false";
        }
    }
}