package az.siftoshka.habitube.utils;

import android.content.Context;
import android.content.res.Configuration;
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
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;

import az.siftoshka.habitube.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;

public class ImageLoader {

    public static void load(View view, String url, ImageView into) {
        switch (view.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (url != null) {
                    Glide.with(view)
                            .load(IMAGE_URL + url)
                            .apply(new RequestOptions().override(200, 300))
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.color.just_grey)
                            .error(R.drawable.ic_missing)
                            .transform(new CenterCrop(), new RoundedCorners(16))
                            .into(into);
                } else {
                    Glide.with(view)
                            .load(R.drawable.ic_missing)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.color.just_grey)
                            .into(into);
                }
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                if (url != null) {
                    Glide.with(view)
                            .load(IMAGE_URL + url)
                            .apply(new RequestOptions().override(200, 300))
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.color.dark_300)
                            .error(R.drawable.ic_missing)
                            .transform(new CenterCrop(), new RoundedCorners(16))
                            .into(into);
                } else {
                    Glide.with(view)
                            .load(R.drawable.ic_missing)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.color.dark_300)
                            .into(into);
                }
                break;
        }
    }

    public static void loadOffline(View view, String url, ImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadPersons(View view, String url, CircleImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void load(Context context, String url, ImageView into) {
        if (url != null) {
            Glide.with(context)
                    .load(IMAGE_URL + url)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .error(R.drawable.ic_missing)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(into);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_missing)
                    .apply(new RequestOptions().override(200, 300))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(new ColorDrawable(Color.LTGRAY))
                    .into(into);
        }
    }

    public static void loadBackground(Context context, String url, ImageView into) {
        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                if (url != null) {
                    Glide.with(context)
                            .load(IMAGE_URL + url)
                            .apply(new RequestOptions().override(720, 405))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.color.dark_grey)
                            .error(R.drawable.ic_missing)
                            .into(into);
                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_missing)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.color.dark_grey)
                            .into(into);
                }
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                if (url != null) {
                    Glide.with(context)
                            .load(IMAGE_URL + url)
                            .apply(new RequestOptions().override(720, 405))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.color.white_is_white)
                            .error(R.drawable.ic_missing)
                            .into(into);
                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_missing)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.color.white_is_white)
                            .into(into);
                }
                break;
        }
    }

    public static void loadDiscover(View view, String url, ImageView into) {
        if (url != null) {
            Glide.with(view)
                    .load(IMAGE_URL + url)
                    .apply(new RequestOptions().override(720, 260))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .placeholder(R.color.mainBackground)
                    .error(R.drawable.ic_missing)
                    .into(into);
        } else {
            Glide.with(view)
                    .load(R.drawable.ic_missing)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.color.mainBackground)
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

    public static void loadVimeo(View view, String key, ImageView image) {
        Glide.with(view)
                .load("https://vumbnail.com/" + key + ".jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_missing)
                .into(image);
    }
}
