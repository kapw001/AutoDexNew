package autodex.com.autodex.customview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import autodex.com.autodex.R;

/**
 * Created by yasar on 26/9/17.
 */

public class ProfileCustomView extends FrameLayout {

    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ProfileCustomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProfileCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfileCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProfileCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        View view = inflate(context, R.layout.userprofilelayout, this);

        imageView = (ImageView) view.findViewById(R.id.privacyiconchange);

    }

    public void changeImageView(int img) {
        imageView.setImageResource(img);
    }

}
