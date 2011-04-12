package com.haikuwind.tabs.buttons;

import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.haikuwind.R;
import com.haikuwind.feed.Haiku;
import com.haikuwind.feed.HttpRequest;
import com.haikuwind.notification.Update;
import com.haikuwind.notification.UpdateNotifier;

public class FavoriteController extends HaikuController {
    private static final String TAG = FavoriteController.class.getSimpleName();
    
    private Context context;

    public FavoriteController(Map<String, Haiku> haikuMap, Context context) {
        super(haikuMap);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        View haikuView = (View) v.getParent();
        Haiku haiku = getHaiku(haikuView);
        
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
                updateToggleFavorite(haikuView);
            }
        }

    }

    private void updateToggleFavorite(View haikuView) {
        View toggle = haikuView.findViewById(R.id.haiku_favorite);
        if(getHaiku(haikuView).isFavoritedByMe()) {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_checked);
        } else {
            toggle.setBackgroundResource(R.drawable.toggle_favorite_unchecked);
        }
    }

    public void bind(View haikuView) {
        View favoriteToggle = haikuView.findViewById(R.id.haiku_favorite);
        favoriteToggle.setVisibility(View.VISIBLE);
        
        favoriteToggle.setOnClickListener(this);
        updateToggleFavorite(haikuView);
    }
}