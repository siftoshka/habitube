package az.siftoshka.habitube.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import az.siftoshka.habitube.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;

public class ImageLoader {

    public static void load(View view, String url, ImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadOffline(View view, String url, ImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadPersons(View view, String url, CircleImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void load(Context context, String url, ImageView into) {
        if (url != null) {
            Glide.with(context)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(into);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadBackground(Context context, String url, ImageView into) {
        if (url != null) {
            Glide.with(context)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadBackground(View view, String url, ImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void saveToInternalStorage(String imageDir, Context context, Drawable resource) {
        FileOutputStream fos = null;
        try {
            Bitmap bitmapImage = ((BitmapDrawable) resource).getBitmap();
            File mypath = new File(context.getFilesDir().getPath() + File.separator + imageDir);
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveToInternalStorage(String imageDir, Context context, ImageView imageView) {
        FileOutputStream fos = null;
        try {
            Bitmap bitmapImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            File mypath = new File(context.getFilesDir().getPath() + File.separator + imageDir);

            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] imageView2ByteArray(ImageView view) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static void loadYoutube(View view, String key, ImageView image) {
        Glide.with(view)
                .load("https://img.youtube.com/vi/" + key + "/hqdefault.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_missing)
                .into(image);
    }
}
