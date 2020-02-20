package az.siftoshka.habitube.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.ByteArrayOutputStream;

import az.siftoshka.habitube.R;

import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;

public class ImageLoader {

    public static void load(View view, String url, ImageView into) {
        Glide.with(view)
                .load(IMAGE_URL + url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_box)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(into);
    }

    public static void loadLocally(View view, byte[] image, ImageView into) {
        Glide.with(view)
                .load(image)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_box)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(into);
    }

    public static void load(Context context, String url, ImageView into) {
        Glide.with(context)
                .load(IMAGE_URL + url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_box)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(into);
    }

    public static void loadBackground(Context context, String url, ImageView into) {
        Glide.with(context)
                .load(IMAGE_URL + url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_box)
                .into(into);
    }

    public static byte[] imageView2Bitmap(ImageView view) {
        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
