package dev.moutamid.sampoorankranti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity {

    // SIGN UP VIEWS
    private EditText nameEtSignUp, emailEtSignUp, passwordEtSignUp, confirmPasswordEtSignUp;
    private Button signUpBtn;
    private TextView alreadyAccountBtnSignUp;
    private Utils utils = new Utils();

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private void startSignUp() {

        //Getting the Text from the email and password EditTexts
        final String email = emailEtSignUp.getText().toString();
        String password = passwordEtSignUp.getText().toString();
        String confirmPassword = confirmPasswordEtSignUp.getText().toString();
        final String nickname = nameEtSignUp.getText().toString();

        final EditText emailFieldSignUp = emailEtSignUp;
        final EditText passwordFieldSignUp = passwordEtSignUp;

        //Setting Progress Dialog Message
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // TextFields are not empty then this if statement occur

        if (TextUtils.isEmpty(email)) {
            //if text fields are emtpy..

            progressDialog.dismiss();

            emailFieldSignUp.setError("Email is required!");
            emailFieldSignUp.requestFocus();
            return;

            //Toast.makeText(this, "Fields are Empty!", Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(password)) {
            //if Password field are emtpy..

            progressDialog.dismiss();

            passwordFieldSignUp.setError("Password is required!");
            passwordFieldSignUp.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //if Email Address is Invalid..

            progressDialog.dismiss();

            emailFieldSignUp.setError("Please enter a valid email!");
            emailFieldSignUp.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            //if Nickname field are emtpy..

            progressDialog.dismiss();

            nameEtSignUp.setError("Name is required!");
            nameEtSignUp.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            //if Nickname field are emtpy..

            progressDialog.dismiss();

            confirmPasswordEtSignUp.setError("Password doesn't match!");
            confirmPasswordEtSignUp.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(nickname)) {
            //if text fields are not empty


            // If the email is not valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progressDialog.dismiss();

                emailFieldSignUp.setError("Please enter a valid email!");
                emailFieldSignUp.requestFocus();
                return;
            } else {

                if (password.length() >= 6) {

                    //Starting to Sign Up the user...
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //When the Information is passed to the server and completes processing
                                    progressDialog.dismiss();


                                    if (!task.isSuccessful()) {

                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                            // Checking if the email is already registered
                                            // Setting error of low password
                                            emailFieldSignUp.setError("User is already registered!");
                                            emailFieldSignUp.requestFocus();
                                            return;

                                            //Toast.makeText(SignUpActivity.this, "User is already registered!", Toast.LENGTH_SHORT).show();

                                        } else {

                                            //Creating a Toast to show the error of not logging in the user
                                            // ---- SHOULD ALSO CHECK IF USER IS ALREADY REGISTERED ----
                                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        //Task is successful..

                                        utils.storeString(RegistrationActivity.this, "userNickname", nickname);
                                        utils.storeString(RegistrationActivity.this, "userEmail", email);

                                        mAuth.getCurrentUser().sendEmailVerification();

                                        progressDialog.dismiss();

                                        finish();
                                        Intent intent = new Intent(RegistrationActivity.this, SecondRegistrationActivity.class);
                                        intent.putExtra("username", nickname);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                        // Finishing Sign Up Activity

                                    }

                                    // ...
                                }
                            });

                } else {
                    progressDialog.dismiss();

                    // Setting error of low password
                    passwordFieldSignUp.setError("Minimum length of password must be 6");
                    passwordFieldSignUp.requestFocus();
                    return;

                    //Toast.makeText(LoginActivity.this, "Minimum Length of password must be 6", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    private void SignUpBtnClickListener() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignUp();

//                Toast.makeText(RegistrationActivity.this, nameEtSignUp.getText().toString() + emailEtSignUp.getText().toString() + passwordEtSignUp.getText().toString() + confirmPasswordEtSignUp.getText().toString(), Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(RegistrationActivity.this, SecondRegistrationActivity.class));
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

    private void startSignIn() {
        //Getting the Text from the email and password EditTexts
        final String email = emailEtLogin.getText().toString();
        String password = passwordEtLogin.getText().toString();

        //Setting Progress Dialog Message
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        // TextFields are not empty then this if statement occur

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //if text fields are not empty

            // If the email is not valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                progressDialog.dismiss();

                emailEtLogin.setError("Please enter a valid email!");
                emailEtLogin.requestFocus();

            } else {

                if (password.length() >= 6) {

                    //Starting to signing the user In
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //When the Information is passed to the server and completes processing


                            if (task.isSuccessful()) {
                                //Task is successful..

                                if (email.equals("admin@gmail.com")) {

                                    finish();
                                    Intent intent = new Intent(RegistrationActivity.this, AdminDashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    return;

                                }

                                utils.storeString(RegistrationActivity.this, "userNickname", mAuth.getCurrentUser().getDisplayName());
                                utils.storeString(RegistrationActivity.this, "userEmail", email);

                                if (!mAuth.getCurrentUser().isEmailVerified()) {
                                    mAuth.getCurrentUser().sendEmailVerification();
                                }

                                progressDialog.dismiss();

                                finish();
                                Intent intent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } else {
                                // Task is not successful
                                progressDialog.dismiss();

                                //Creating a Toast to show the error of not logging in the user
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();


                            }

                        }
                    });

                } else {
                    progressDialog.dismiss();

                    // Setting error of low password
                    passwordEtLogin.setError("Minimum length of password must be 6");
                    passwordEtLogin.requestFocus();

                    //Toast.makeText(LoginActivity.this, "Minimum Length of password must be 6", Toast.LENGTH_LONG).show();

                }
            }

        } else if (TextUtils.isEmpty(email)) {
            //if text fields are emtpy..

            progressDialog.dismiss();

            emailEtLogin.setError("Email is required!");
            emailEtLogin.requestFocus();

            //Toast.makeText(this, "Fields are Empty!", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(password)) {
            //if text fields are emtpy..

            progressDialog.dismiss();

            passwordEtLogin.setError("Password is required!");
            passwordEtLogin.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressDialog.dismiss();

            emailEtLogin.setError("Please enter a valid email!");
            emailEtLogin.requestFocus();
        }
    }

    private void LoginBtnClickListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

//                startActivity(new Intent(RegistrationActivity.this, SecondRegistrationActivity.class));
//                Toast.makeText(RegistrationActivity.this, emailEtLogin.getText().toString() + passwordEtLogin.getText().toString(), Toast.LENGTH_SHORT).show();
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

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
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