package autodex.com.autodex.activitys;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import autodex.com.autodex.R;

public class UserProfileActivityBackup extends AppCompatActivity implements View.OnClickListener {


    private EditText profileNmae, email;
    private RadioGroup radioGroup;
    private RadioButton male, female;
    private EditText dob;
    private PopupWindow pw;

    private FrameLayout profileimgupdate;

    private boolean is_editable = true;
    private ImageView genderprivacyiconchange, dobprivacyiconchange, emailprivacyiconchange, phonenumberprivacyiconchange, usernameprivacyiconchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        init();
    }


    private void init() {
        profileNmae = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        dob = (EditText) findViewById(R.id.dob);
        profileimgupdate = (FrameLayout) findViewById(R.id.profileimgupdate);
        profileimgupdate.setOnClickListener(this);

        usernameprivacyiconchange = (ImageView) findViewById(R.id.usernameprivacyiconchange);
        phonenumberprivacyiconchange = (ImageView) findViewById(R.id.phonenumberprivacyiconchange);
        emailprivacyiconchange = (ImageView) findViewById(R.id.emailprivacyiconchange);
        dobprivacyiconchange = (ImageView) findViewById(R.id.dobprivacyiconchange);
        genderprivacyiconchange = (ImageView) findViewById(R.id.genderprivacyiconchange);


        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if (checkedId == R.id.male) {
//
//                } else if (checkedId == R.id.female) {
//
//                }
//            }
//
//        });

        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        originalDrawable = profileNmae.getBackground();

        disableEditText(profileNmae, email, dob);
        disableRadioButton(male, female);
        disableFrameLayout(profileimgupdate);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.profileimgupdate:

                Toast.makeText(this, "Profile image update  ", Toast.LENGTH_SHORT).show();

                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userprofilemenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.edit) {

            if (is_editable) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                is_editable = false;

                enableEditText(profileNmae, email, dob);
                enableRadioButton(male, female);
                enableFrameLayout(profileimgupdate);

            } else {
                is_editable = true;
                disableEditText(profileNmae, email, dob);
                disableRadioButton(male, female);
                disableFrameLayout(profileimgupdate);
                item.setIcon(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Drawable originalDrawable;

    private void enableEditText(EditText editText, EditText editText1, EditText textView) {

//        editText.setFocusable(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
        editText.setBackgroundDrawable(originalDrawable);

//        editText1.setFocusable(true);
        editText1.setEnabled(true);
        editText1.setCursorVisible(true);
        editText1.setBackgroundDrawable(originalDrawable);

        textView.setEnabled(true);
        textView.setEnabled(true);
        textView.setCursorVisible(true);
        textView.setBackgroundDrawable(originalDrawable);
//        textView.setFocusable(true);
//        editText.setBackgroundColor(getResources().getColor(R.color.blackalpha1));
//        editText.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }

    private void disableEditText(EditText editText, EditText editText1, EditText textView) {
//        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
//        editText.getBackground().clearColorFilter();
//        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);

//        editText1.setFocusable(false);
        editText1.setEnabled(false);
        editText1.setCursorVisible(false);
        editText1.setBackgroundColor(Color.TRANSPARENT);

        textView.setEnabled(false);
        textView.setEnabled(false);
        textView.setCursorVisible(false);
        textView.setBackgroundColor(Color.TRANSPARENT);
//        textView.setFocusable(false);
    }

    private void enableRadioButton(RadioButton radioButton, RadioButton radioButton1) {
        radioButton.setEnabled(true);
        radioButton1.setEnabled(true);
    }

    private void disableRadioButton(RadioButton radioButton, RadioButton radioButton1) {
        radioButton.setEnabled(false);
        radioButton1.setEnabled(false);
    }

    private void enableFrameLayout(FrameLayout frameLayout) {
        frameLayout.setEnabled(true);
    }

    private void disableFrameLayout(FrameLayout frameLayout) {
        frameLayout.setEnabled(false);
    }

    public void showPopup(View view) {

        initiatePopupWindow(view);
    }


    private void initiatePopupWindow(View v) {


        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            pw.showAsDropDown(v, -175, -150);

            publiclayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(UserProfileActivityBackup.this, "publiclayout", Toast.LENGTH_SHORT).show();
//                    usericonchange.setImageResource(R.drawable.ic_message);
                }
            });

            privatelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    usericonchange.setImageResource(R.drawable.ic_logout);
                    Toast.makeText(UserProfileActivityBackup.this, "privatelayout", Toast.LENGTH_SHORT).show();
                }
            });

            personallayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    usericonchange.setImageResource(R.drawable.ic_calendar);
                    Toast.makeText(UserProfileActivityBackup.this, "personallayout", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
