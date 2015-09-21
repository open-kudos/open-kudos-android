package open.kudos.activities;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import layout.adapter.LoginViewPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import layout.SlidingTabLayout;
import open.kudos.R;
import storage.SecurePreferences;
import web.service.Response;
import web.service.call.AuthenticateActions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AuthenticationScreen extends ActionBarActivity {

    private static final CharSequence titles[] = {"Login", "Register"};
    private static final int NUMBER_OF_TABS = 2;

    private EditText loginEmailInput;
    private EditText loginPasswordInput;
    private CheckBox rememberMeCheck;

    private EditText registerEmailInput;
    private EditText registerPasswordInput;
    private EditText registerConfirmPasswordInput;

    private SecurePreferences securePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_screen);
        checkIfUserIsRemembered();
        setSlidingTabLayout(setPager());

    }

    private void checkIfUserIsRemembered() {
        securePreferences = new SecurePreferences(this, "xyz", "xyz", true);
        String isSaved = securePreferences.getString("saved");
        Toast.makeText(this, isSaved, Toast.LENGTH_LONG).show();
        if (isSaved != null && isSaved.equals("true")) {
            startActivity(new Intent(this, MainScreen.class));
            finish();
        }
    }

    public void logIn(View v) {
        if (loginEmailInput == null) {
            loginEmailInput = (EditText) findViewById(R.id.login_email_input);
        }
        if (loginPasswordInput == null) {
            loginPasswordInput = (EditText) findViewById(R.id.login_password_input);
        }
        if (rememberMeCheck == null) {
            rememberMeCheck = (CheckBox) findViewById(R.id.remember_me_flag);
        }
        final String email = loginEmailInput.getText().toString();
        final String password = loginPasswordInput.getText().toString();

        if (email.length() == 0) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
        }
        if (password.length() == 0) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show();
        }

        String outputString = "";
        try {
            Response response = AuthenticateActions.login(email, password);
            //TODO fix that shit with 404
            if (response.getCode() == 200 || response.getCode() == 404) {
                Toast.makeText(AuthenticationScreen.this, "success " + outputString, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainScreen.class);
                startActivity(intent);
            }
            Toast.makeText(this, response.getCode() + "", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(AuthenticationScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(AuthenticationScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(AuthenticationScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (rememberMeCheck.isChecked()) {
            securePreferences.put("email", email);
            securePreferences.put("password", password);
            securePreferences.put("saved", "true");
        }

    }

    public void register(View v) throws ExecutionException, InterruptedException {
        if(registerEmailInput == null){
            registerEmailInput = (EditText)findViewById(R.id.register_email_input);
        }
        if(registerPasswordInput == null){
            registerPasswordInput = (EditText)findViewById(R.id.register_password_input);
        }
        if(registerConfirmPasswordInput == null){
            registerConfirmPasswordInput = (EditText)findViewById(R.id.register_confirm_password_input);
        }
        String email = registerEmailInput.getText().toString();
        String password = registerPasswordInput.getText().toString();
        String confirmPassword = registerConfirmPasswordInput.getText().toString();

        validateRegistration(email, password, confirmPassword);

        Response response = AuthenticateActions.register(email,password,confirmPassword);
        Toast.makeText(this,response.getCode()+" "+response.getMessage(),Toast.LENGTH_LONG).show();
        if(response.getCode() == 200){
            Toast.makeText(this,"Registration completed. Now you can log in",Toast.LENGTH_LONG).show();
        }
    }

    private void validateRegistration(String email, String password, String confirmPassword){
        if(email.length() == 0){
            Toast.makeText(this,"please enter your email",Toast.LENGTH_LONG).show();
        }
        if(password.length() == 0){
            Toast.makeText(this,"please enter your password",Toast.LENGTH_LONG).show();
        }
        if(confirmPassword.length() == 0){
            Toast.makeText(this,"please enter confirmPassword",Toast.LENGTH_LONG).show();
        }
        if(confirmPassword.length() > 0 && password.length() > 0 && !confirmPassword.equals(password)){
            Toast.makeText(this,"passwords do not match",Toast.LENGTH_LONG).show();
        }
    }

    private ViewPager setPager() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.authentication_screen_tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final LoginViewPagerAdapter adapter = new LoginViewPagerAdapter(getSupportFragmentManager(), titles, NUMBER_OF_TABS);
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(adapter);

        return pager;
    }

    private SlidingTabLayout setSlidingTabLayout(ViewPager pager) {
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        return tabs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
