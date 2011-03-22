package com.haikuwind.tabs.buttons;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;

public class FavoriteController implements OnClickListener {
    private static final String TAG = FavoriteController.class.getSimpleName();
    
    private Haiku haiku;
    private Context context;

    private FavoriteController(Context context, Haiku haiku) {
        this.haiku = haiku;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if (!haiku.isFavoritedByMe()) {
            try {
                HttpRequest.favorite(haiku.getId());
                haiku.setFavoritedByMe(true);

                UpdateNotifier.fireUpdate(Update.ADD_FAVORITE, haiku);
            } catch (Exception e) {
                Log.e(TAG, "error while marking favorite", e);
                Toast.makeText(context,
                        R.string.toast_error_try_again, Toast.LENGTH_SHORT);
            } finally {
                updateToggleFavorite(v, haiku);
            }
        }

    }

    private static void updateToggleFavorite(View toggle, Haiku h) {
        if(h.isFavoritedByMe()) {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_checked);
        } else {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_unchecked);
        }
    }

    public static void bind(View haikuView, Haiku haiku, Context context) {
        View favoriteToggle = haikuView.findViewById(R.id.haiku_favorite);
        favoriteToggle.setVisibility(View.VISIBLE);
        
        favoriteToggle.setOnClickListener(new FavoriteController(context, haiku));
        updateToggleFavorite(favoriteToggle, haiku);
    }
}
