package layout.tab.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import open.kudos.R;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;
import web.service.Response;
import web.service.call.ChallengesActions;
import web.service.call.RelationActions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.21.
 */
public class ChallengeTab extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner participantSpinner;
    private Spinner refereeSpinner;
    private Spinner kudosAmountSpinner;
    private Spinner challengeNameSpinner;

    private EditText participantEmailInput;
    private EditText refereeEmailInput;
    private EditText kudosAmountInput;
    private EditText challengeNameInput;

    private Button challengeButton;
    private Button setDateButton;
    private Button setTimeButton;

    private View v;
    private List<String> userList = new LinkedList<>();
    private final Handler handler = new Handler();

    private static final LocalDateTime localDateTime = new LocalDateTime();

    private String date;
    private String time;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.main_challenge,container,false);
        initializeFields();
        handler.postDelayed(updateSpinners(),0);
        return v;
    }

    private void initializeFields(){
        participantSpinner = (Spinner) v.findViewById(R.id.spinner_pick_participant);
        participantSpinner.setOnItemSelectedListener(this);

        refereeSpinner = (Spinner) v.findViewById(R.id.spinner_pick_referee);
        refereeSpinner.setOnItemSelectedListener(this);

        kudosAmountSpinner = (Spinner) v.findViewById(R.id.spinner_pick_kudos_amount);
        kudosAmountSpinner.setOnItemSelectedListener(this);

        challengeNameSpinner = (Spinner) v.findViewById(R.id.spinner_pick_challenge_name);
        challengeNameSpinner.setOnItemSelectedListener(this);

        participantEmailInput = (EditText)v.findViewById(R.id.participant_email_input);
        refereeEmailInput = (EditText)v.findViewById(R.id.referee_email_input);
        kudosAmountInput = (EditText)v.findViewById(R.id.kudos_amount_input);
        challengeNameInput = (EditText)v.findViewById(R.id.challenge_name_input);

        challengeButton = (Button)v.findViewById(R.id.challenge_button);
        challengeButton.setOnClickListener(this);

        setDateButton = (Button)v.findViewById(R.id.button_set_date);
        setDateButton.setOnClickListener(this);

        setTimeButton = (Button)v.findViewById(R.id.button_set_time);
        setTimeButton.setOnClickListener(this);

    }

    private ArrayAdapter<String> getArrayAdapter(List suggestions){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, suggestions);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    private void updateParticipantSpinner() throws ExecutionException, InterruptedException {
        participantSpinner.setAdapter(getArrayAdapter(getNewestUserList()));
    }

    private void updateRefereeSpinner(){
        refereeSpinner.setAdapter(getArrayAdapter(userList));
    }

    private List<String> getNewestUserList() throws ExecutionException, InterruptedException {
        final List<String> newParticipantList = new LinkedList<>();
        newParticipantList.add("Or pick from list");
        newParticipantList.addAll(RelationActions.getFollowedPeople());
        if(!newParticipantList.equals(userList)) {
            userList = newParticipantList;
        }
        return userList;
    }

    private Runnable updateSpinners(){
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getName(), "executed");
                try {
                    if(refereeSpinner.getSelectedItemPosition() < 0 && participantSpinner.getSelectedItemPosition() < 0) {
                        updateParticipantSpinner();
                        updateRefereeSpinner();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this,10000);
            }
        };
        return r;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.spinner_pick_participant :
                changeInputFieldAccordingToPosition(
                        participantEmailInput,
                        participantSpinner.getSelectedItemPosition(),
                        participantSpinner.getSelectedItem().toString());
            break;

            case R.id.spinner_pick_referee :
                changeInputFieldAccordingToPosition(
                        refereeEmailInput,
                        refereeSpinner.getSelectedItemPosition(),
                        refereeSpinner.getSelectedItem().toString());
            break;

            case R.id.spinner_pick_challenge_name :
                changeInputFieldAccordingToPosition(
                        challengeNameInput,
                        challengeNameSpinner.getSelectedItemPosition(),
                        challengeNameSpinner.getSelectedItem().toString());
            break;

            case R.id.spinner_pick_kudos_amount :
                changeInputFieldAccordingToPosition(
                        kudosAmountInput,
                        kudosAmountSpinner.getSelectedItemPosition(),
                        kudosAmountSpinner.getSelectedItem().toString());
            break;

        }
    }

    private void changeInputFieldAccordingToPosition(EditText inputField, int position, String text){
        if(position != 0){
            inputField.setText(text);
        } else {
            inputField.setText("");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.challenge_button :

                String challengeName = challengeNameInput.getText().toString();
                String kudosAmount = kudosAmountInput.getText().toString();
                String participantEmail = participantEmailInput.getText().toString();
                String refereeEmail = participantEmailInput.getText().toString();

                if(areInputFieldsValid(challengeName,kudosAmount,participantEmail,refereeEmail,date,time)){
                    String dateTime = date + " " + time;
                    try {
                        Response response = ChallengesActions.createChallenge(participantEmail, refereeEmail, kudosAmount, dateTime, challengeName);
                        if(response.getCode() == 200){
                            showToast("Success "+ response.getMessage());
                        } else {
                            showToast("fail: " + response.getCode() );
                        }
                    } catch (ExecutionException e) {
                        showToast(e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        showToast(e.getMessage());
                        e.printStackTrace();
                    }
                }

            break;

            case R.id.button_set_time :

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                        time = correctDateTime(selectedHour)
                                + ":" +
                                correctDateTime(selectedMinute)
                                + ":" +
                                correctDateTime(localDateTime.getSecondOfMinute()) +
                                "," +
                                correctMilliseconds(localDateTime.getMillisOfSecond());
                    }
                },localDateTime.getHourOfDay(),localDateTime.getMinuteOfHour(),true);
                timePickerDialog.show();
            break;

            case R.id.button_set_date :
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth){
                        date = year + "-" + correctDateTime(monthOfYear)+ "-" + correctDateTime(dayOfMonth);

                    }
                },localDateTime.getYear(),localDateTime.getMonthOfYear(),localDateTime.getDayOfMonth());
                datePickerDialog.show();
            break;
        }
    }

    private String correctDateTime(int input){
        if(input < 10){
            return "0"+input;
        }
        if(input == 0){
            return "00";
        }
        return input+"";
    }

    private String correctMilliseconds(int input){
        if(input < 10){
            return "00" + input;
        }
        if(input < 100){
            return "0" + input;
        }
        if(input == 0){
            return "000";
        }
        return input+"";
    }

    private void showToast(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

    private boolean areInputFieldsValid(String challengeName, String kudosAmount, String participantEmail, String refereeEmail, String date, String time){
        boolean areValid = true;

        if(StringUtils.isEmpty(challengeName)){
            showToast("Please enter challenge name");
            areValid = false;
        }
        if(StringUtils.isEmpty(kudosAmount)){
            showToast("Please enter kudos amount");
            areValid = false;
        }
        if(StringUtils.isEmpty(participantEmail)){
            showToast("Please enter participant email");
            areValid = false;
        }
        if(StringUtils.isEmpty(refereeEmail)){
            showToast("Please enter referee email");
            areValid = false;
        }
        if(StringUtils.isEmpty(date)){
            showToast("Please select date");
            areValid = false;
        }
        if(StringUtils.isEmpty(time)){
            showToast("Please select time");
            areValid = false;
        }

        return areValid;

    }

}
