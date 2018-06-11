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

public class LoginFragment extends Fragment {


    private EditText phonenumber, password;
    private TextView signup, forgotPassword;
    private Button signin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_signin, container, false);

        phonenumber = (EditText) view.findViewById(R.id.phonenumber);
        password = (EditText) view.findViewById(R.id.password);

        signup = (TextView) view.findViewById(R.id.signup);
        forgotPassword = (TextView) view.findViewById(R.id.forgotpassword);

        signin = (Button) view.findViewById(R.id.signin);
        listener();

        return view;

    }


    private void listener() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sign In", Toast.LENGTH_SHORT).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Sign Up", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SignUpActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
