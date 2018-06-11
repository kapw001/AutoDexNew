package autodex.com.autodex.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.UpdateContactActivity;
import autodex.com.autodex.activitys.UserProfileActivity;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by yasar on 30/10/17.
 */

public class UserCustomView extends FrameLayout implements View.OnClickListener {

    private ImageView img, privacyiconchange;
    private EditText editText;
    private LinearLayout linearLayout;
    private Drawable originalDrawable;
    private String category = "personal";
    private boolean isShow = false;
    private String hint;

    private Drawable image;
    private String text;
    private float alpha;
    private int inputType = InputType.TYPE_CLASS_TEXT;

    private boolean isDateEditable = false;


    public UserCustomView(@NonNull Context context) {
        super(context);
        init(null);
        init(context, null);
    }

    public UserCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        init(context, attrs);

    }

    public UserCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        init(context, attrs);
    }

    public UserCustomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
        init(context, attrs);
    }

    public void setText(String text) {
        this.editText.setText(text);
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {

            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserCustomView);

            String t = a.getString(R.styleable.UserCustomView_text);
            String hinttxt = a.getString(R.styleable.UserCustomView_hint);

            if (hinttxt != null) {
                hint = hinttxt;
            }

            Drawable drawable = a.getDrawable(R.styleable.UserCustomView_image);

            float al = a.getFloat(R.styleable.UserCustomView_alpha, 1f);

            alpha = al;

            isDateEditable = a.getBoolean(R.styleable.UserCustomView_isDateEditable, false);

            isShow = a.getBoolean(R.styleable.UserCustomView_isShow, false);

            String type = a.getString(R.styleable.UserCustomView_inputType);

            if (type != null && type.contains("number")) {
                inputType = InputType.TYPE_CLASS_NUMBER;
            }


//            Log.e(TAG, "UserCustomView: " + src);

            try {

                if (drawable != null) {

                    image = drawable;
//                    img.setImageResource(Integer.parseInt(src));
                }
                if (t != null) {
                    text = t;
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

            a.recycle();

        }

    }


    private void init(Context context, AttributeSet attrs) {

        inflate(context, R.layout.usercustomview, this);

        img = (ImageView) findViewById(R.id.img);
        privacyiconchange = (ImageView) findViewById(R.id.privacyiconchange);
        editText = (EditText) findViewById(R.id.editText);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout3);

        if (isShow) {
            linearLayout.setVisibility(VISIBLE);
        } else {
            linearLayout.setVisibility(GONE);
        }

        editText.setInputType(inputType);

        linearLayout.setOnClickListener(this);
        originalDrawable = editText.getBackground();

        img.setImageDrawable(image);

        img.setAlpha(alpha);

        editText.setText(text);
        editText.setHint(hint);


        if (isDateEditable) {

            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        callDatePicker(editText);
                    }
                    return true;
                }
            });
        }


    }

    private void callDatePicker(EditText birthday) {
        Utility.callDatePicker(getContext(), birthday);
    }

    public void setImage(int id) {
        img.setImageResource(id);
    }

    public void setPrivacyiconchange(int id) {
        privacyiconchange.setImageResource(id);
    }

    public void disableEditText() {
        editText.setEnabled(false);
        editText.setCursorVisible(false);
//        editText.getBackground().clearColorFilter();
//        editText.setKeyListener(null);
//        name.setBackgroundColor(Color.TRANSPARENT);
    }

    public void enabelEditText() {
        editText.setEnabled(true);
        editText.setCursorVisible(true);
//        name.setBackgroundDrawable(originalDrawable);
    }

    public String getName() {
        return editText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout3:
                initiatePopupWindow(v);
                break;
        }
    }


    private void initiatePopupWindow(View v) {

        final PopupWindow pw;
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
//            View layout = inflater.inflate(R.layout.popuplayout,(ViewGroup) findViewById(R.id.rootview));
            View layout = inflater.inflate(R.layout.popuplayout, null);

            LinearLayout publiclayout = (LinearLayout) layout.findViewById(R.id.publiclayout);
            LinearLayout privatelayout = (LinearLayout) layout.findViewById(R.id.privatelayout);
            LinearLayout personallayout = (LinearLayout) layout.findViewById(R.id.personallayout);
            // create a 300px width and 470px height PopupWindow
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            pw = new PopupWindow(layout, width, height, true);
            pw = new PopupWindow(layout, width, height, true);
            // display the popup in the center
            pw.setAnimationStyle(android.R.style.Animation_Dialog);
            if (Build.VERSION.SDK_INT >= 21) {
                pw.setElevation(5.0f);
            }
            pw.setOutsideTouchable(true);
            pw.setFocusable(true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            pw.showAsDropDown(v, (int) (-(v.getX() + v.getWidth()) + 10), -150);
//            pw.showAsDropDown(v, -175, -150, Gravity.CENTER);


            pw.showAsDropDown(v, -v.getWidth() - pw.getWidth() - 100, -v.getHeight() - pw.getHeight() - 100);


//            pw.showAsDropDown(v);

            publiclayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "public", Toast.LENGTH_SHORT).show();
                    category = "public";
                    setPrivacyiconchange(R.drawable.ic_message);
                    pw.dismiss();
                }
            });

            privatelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPrivacyiconchange(R.drawable.ic_logout);
//                    Toast.makeText(view.getContext(), "private", Toast.LENGTH_SHORT).show();
                    category = "private";
                    pw.dismiss();
                }
            });

            personallayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPrivacyiconchange(R.drawable.ic_calendar);
//                    Toast.makeText(view.getContext(), "personal", Toast.LENGTH_SHORT).show();
                    category = "personal";
                    pw.dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return category;
    }
}
