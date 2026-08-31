package com.example.helveticus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {

    private List<PhotoItem> photosList;
    private LayoutInflater inflater;

    public PhotoAdapter(Context context, List<PhotoItem> photosList) {
        this.photosList = photosList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photosList.size();
    }

    @Override
    public Object getItem(int position) {
        return photosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            int layoutId = R.layout.item_photo_small;
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageItem);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoItem item = photosList.get(position);

        try {
            byte[] decodedString = Base64.decode(item.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView dateText;
    }
}