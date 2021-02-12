package dev.moutamid.sampoorankranti;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    // SIGN UP VIEWS
    private EditText nameEtSignUp, emailEtSignUp, passwordEtSignUp, confirmPasswordEtSignUp;
    private Button signUpBtn;
    private TextView alreadyAccountBtnSignUp;

    private void SignUpBtnClickListener() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this, nameEtSignUp.getText().toString() + emailEtSignUp.getText().toString() + passwordEtSignUp.getText().toString() + confirmPasswordEtSignUp.getText().toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this, SecondRegistrationActivity.class));
            }
        });

        alreadyAccountBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loginlinearlayout).setVisibility(View.VISIBLE);
                findViewById(R.id.signuplinearlayout).setVisibility(View.GONE);
            }
        });

    }

    // LOGIN VIEWS
    private EditText emailEtLogin, passwordEtLogin;
    private Button loginBtn;
    private TextView donthaveanaccountBtnLogin;

    private void LoginBtnClickListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this, emailEtLogin.getText().toString() + passwordEtLogin.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        donthaveanaccountBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loginlinearlayout).setVisibility(View.GONE);
                findViewById(R.id.signuplinearlayout).setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        // SIGN UP VIEWS
        nameEtSignUp = findViewById(R.id.editTextName_signup);
        emailEtSignUp = findViewById(R.id.editTextEmail_signup);
        passwordEtSignUp = findViewById(R.id.editTextPassword_signup);
        confirmPasswordEtSignUp = findViewById(R.id.editTextConfirmPassword_signup);
        signUpBtn = findViewById(R.id.cirSignUpButton_signup);
        alreadyAccountBtnSignUp = findViewById(R.id.alreadyaccountbtn_signup);

        // LOGIN VIEWS
        emailEtLogin = findViewById(R.id.editTextEmail_login);
        passwordEtLogin = findViewById(R.id.editTextPassword_login);
        loginBtn = findViewById(R.id.cirLoginButton_login);
        donthaveanaccountBtnLogin = findViewById(R.id.donthaveanaccountbtn_login);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        checkIntentForLayout();

        initView();

        SignUpBtnClickListener();

        LoginBtnClickListener();//utils.changeStatusBarColor(ActivityMain.this, R.color.pinkish);

    }

    private void checkIntentForLayout() {
        if (getIntent().hasExtra("isLoginButtonClicked")) {

            Intent intent = getIntent();

            if (intent.getBooleanExtra("isLoginButtonClicked", false)) {
                findViewById(R.id.loginlinearlayout).setVisibility(View.VISIBLE);
                findViewById(R.id.signuplinearlayout).setVisibility(View.GONE);

            }

        }
    }
}