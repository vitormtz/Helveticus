package com.example.helveticus;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Date;

public class PhotoItem {
    private long id;
    private String imageBase64;

    public PhotoItem(long id, String imageBase64) {
        this.id = id;
        this.imageBase64 = imageBase64;
    }

    public long getId() {
        return id;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void showDetail(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_photo_detail);
        dialog.setCancelable(true);

        ImageView imgDetail = dialog.findViewById(R.id.imgDetail);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        try {
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgDetail.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}