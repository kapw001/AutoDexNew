package autodex.com.autodex.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import autodex.com.autodex.R;
import autodex.com.autodex.activitys.LoginActivity;
import autodex.com.autodex.activitys.SignUpActivity;

/**
 * Created by yasar on 7/9/17.
 */

public class SignUpFragment extends Fragment {


    private EditText name, phonenumber;
    private TextView signin;
    private Button signup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_signup, container, false);

        phonenumber = (EditText) view.findViewById(R.id.phonenumber);
        name = (EditText) view.findViewById(R.id.name);
        signup = (Button) view.findViewById(R.id.signup);
        signin = (TextView) view.findViewById(R.id.signin);

        listener();
        return view;

    }


    private void listener() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sign In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sign Up", Toast.LENGTH_SHORT).show();


            }
        });


    }
}
