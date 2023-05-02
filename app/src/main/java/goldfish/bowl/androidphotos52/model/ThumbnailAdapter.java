package goldfish.bowl.androidphotos52.model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class ThumbnailAdapter extends BaseAdapter {
    private Context context;
    private List<Photo> photos;

    public ThumbnailAdapter(Context context, List<Photo> imageUris) {
        this.context = context;
        this.photos = imageUris;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri imageUri = Uri.parse(photos.get(position).getPath());
        ContentResolver contentResolver = context.getContentResolver();
        Bitmap thumbnail = null;
        try {
            thumbnail = contentResolver.loadThumbnail(imageUri, new Size(200, 200), null);
        } catch (IOException e) {
            AndroidUtils.showAlert(context, "Error", "Error loading thumbnail for " + imageUri.toString());
            return imageView;
        }
        imageView.setImageBitmap(thumbnail);

        return imageView;
    }
}
