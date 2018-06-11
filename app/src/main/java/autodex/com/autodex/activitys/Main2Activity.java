package autodex.com.autodex.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import autodex.com.autodex.R;

public class Main2Activity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = (EditText) findViewById(R.id.editText);


    }

    public void onC(View view) {

        editText.setError("Hello");
    }
}
