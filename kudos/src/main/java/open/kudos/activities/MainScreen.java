package open.kudos.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import layout.SlidingTabLayout;
import layout.adapter.LoginViewPagerAdapter;
import layout.adapter.MainViewPagerAdapter;
import open.kudos.R;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import storage.SecurePreferences;
import web.service.Response;
import web.service.call.AuthenticateActions;
import web.service.call.KudosActions;
import web.service.call.UserActions;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainScreen extends ActionBarActivity implements View.OnClickListener {

    private SecurePreferences securePreferences;

    private static  int GET_DATE_REQUEST_CODE = 1;

    private static final CharSequence titles[] = {"Give Kudos", "Endorse", "Challenge"};
    private static final int NUMBER_OF_TABS = 3;

    private TextView receivedKudosOutput;

    private EditText giveKudosEmailInput;
    private EditText giveKudosAmountInput;
    private EditText giveKudosMessageInput;

    private FrameLayout frameLayout;

    private Spinner participantSpinner;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSlidingTabLayout(setPager());
        login();
        //addItemsToParticipantSpinner();
        handler.postDelayed(updateStatusFields(), 0);
        initializeFields();

    }
    private void addItemsToParticipantSpinner(){
        if(participantSpinner == null) {
            participantSpinner = (Spinner)findViewById(R.id.spinner_pick_participant);
        }
            List<String> suggestionsList = new LinkedList<>();
            suggestionsList.add("Or pick from list");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, suggestionsList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            participantSpinner.setAdapter(dataAdapter);

    }

    private Runnable updateStatusFields(){
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getName(),"executed");
                setStatusFields();
                handler.postDelayed(this,10000);
            }
        };
        return r;
    }

    private void initializeFields() {
        frameLayout = (FrameLayout) findViewById(R.id.kudos_frame);
        frameLayout.setOnClickListener(this);
        receivedKudosOutput = (TextView) frameLayout.findViewById(R.id.kudos_amount);
        receivedKudosOutput.setOnClickListener(this);
    }

    private void setStatusFields() {

        int receivedKudosAmount = -1;
        try {
            receivedKudosAmount = KudosActions.getReceivedKudos();
        } catch (ExecutionException e) {
            Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (receivedKudosAmount != -1) {
            receivedKudosOutput.setText(receivedKudosAmount + "");
        } else {
            Toast.makeText(MainScreen.this, receivedKudosAmount + " NOT OK", Toast.LENGTH_LONG).show();
        }
    }



    private void login() {
        try {
            if (authenticate().getCode() == 200) {
                Toast.makeText(this, "successfully logged", Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Response authenticate() throws InterruptedException, ExecutionException, IOException {
        securePreferences = new SecurePreferences(this, "xyz", "xyz", true);
        return AuthenticateActions.login(securePreferences.getString("email"),
                securePreferences.getString("password"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO make logout work?
    public void onButtonClick(View v) throws ExecutionException, InterruptedException {
        Response response = AuthenticateActions.logout();
        if (response.getCode() == 200) {
            Toast.makeText(this, "logout successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, AuthenticationScreen.class));
            finish();
        } else {
            Toast.makeText(this, response.getCode() + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private ViewPager setPager() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_screen_tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), titles, NUMBER_OF_TABS);
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

    public void giveKudos(View v) {
        initializeGiveKudosFieldsIfNeeded();

        String email = giveKudosEmailInput.getText().toString();
        String amountStr = giveKudosAmountInput.getText().toString();
        String message = giveKudosMessageInput.getText().toString();

        int amount = -1;

        boolean isEmailFieldEmpty = StringUtils.isEmpty(email);
        boolean isAmountStrEmpty = StringUtils.isEmpty(amountStr);
        boolean isMessageEmpty = StringUtils.isEmpty(message);
        boolean isAmountWrong = true;

        if (isEmailFieldEmpty) {
            Toast.makeText(this, "please enter buddy email", Toast.LENGTH_SHORT).show();
        }
        if (isAmountStrEmpty) {
            Toast.makeText(this, "please enter kudos amount", Toast.LENGTH_SHORT).show();
        }
        if (isMessageEmpty) {
            Toast.makeText(this, "please enter giving explanation", Toast.LENGTH_SHORT).show();
        }
        if (!isAmountStrEmpty && !isMessageEmpty && !isEmailFieldEmpty) {
            try {
                amount = Integer.parseInt(amountStr);
                isAmountWrong = false;
            } catch (NumberFormatException e) {
                Toast.makeText(this, "the number that you entered is incorrect", Toast.LENGTH_SHORT).show();
            }
        }
        if (!isAmountStrEmpty && !isMessageEmpty && !isEmailFieldEmpty && !isMessageEmpty && !isAmountWrong) {
            try {
                Response response = KudosActions.giveKudos(email, amount, message);
                if (response.getCode() == 200) {
                    Toast.makeText(this, "Kudos successfully sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Transaction unsucessful. The code is" + response.getCode(), Toast.LENGTH_LONG).show();
                }
            } catch (ExecutionException e) {
                Toast.makeText(this, "execution exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Toast.makeText(this, "interrupted exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initializeGiveKudosFieldsIfNeeded() {
        if (giveKudosAmountInput == null) {
            giveKudosAmountInput = (EditText) findViewById(R.id.give_kudos_amount_input);
        }
        if (giveKudosEmailInput == null) {
            giveKudosEmailInput = (EditText) findViewById(R.id.give_kudos_email_input);
        }
        if (giveKudosMessageInput == null) {
            giveKudosMessageInput = (EditText) findViewById(R.id.give_kudos_message_input);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GET_DATE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Completed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kudos_frame : Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        }
    }
}
