package com.example.helveticus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView imagePreview;
    private GridView gridGallery;
    private TextView txtEmptyGallery;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private PhotoAdapter adapter;
    private Bitmap currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imagePreview = findViewById(R.id.imagePreview);
        gridGallery = findViewById(R.id.gridGallery);
        txtEmptyGallery = findViewById(R.id.txtEmptyGallery);
        Button btnCapture = findViewById(R.id.btnCapture);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImage != null) {
                    String base64Image = convertBitmapToBase64(currentImage);
                    long id = dbHelper.saveImage(base64Image);
                    if (id != -1) {
                        Toast.makeText(CameraActivity.this,
                                "Foto salva na galeria", Toast.LENGTH_SHORT).show();
                        imagePreview.setImageBitmap(null);
                        currentImage = null;
                        loadPhotos();
                    }
                } else {
                    Toast.makeText(CameraActivity.this,
                            "Impossivel salvar a foto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setEnabled(false);

        loadPhotos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhotos();
    }

    private void loadPhotos() {
        List<PhotoItem> photos = dbHelper.getAllPhotoItems();

        if (photos.isEmpty()) {
            gridGallery.setVisibility(View.GONE);
            txtEmptyGallery.setVisibility(View.VISIBLE);
        } else {
            gridGallery.setVisibility(View.VISIBLE);
            txtEmptyGallery.setVisibility(View.GONE);

            adapter = new PhotoAdapter(this, photos);
            gridGallery.setAdapter(adapter);
        }

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoItem item = (PhotoItem) adapter.getItem(position);
                item.showDetail(CameraActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                currentImage = (Bitmap) extras.get("data");
                imagePreview.setImageBitmap(currentImage);
                btnSave.setEnabled(true);
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}