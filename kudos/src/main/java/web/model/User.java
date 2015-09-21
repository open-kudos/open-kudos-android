package web.model;

/**
 * Created by chc on 15.8.19.
 */
public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private String birthday;
    private String phone;

    private String startedToWorkDate;
    private String position;

    private String emailHash;

    private boolean isCompleted = false;
    private boolean showBirthday = false;
    private boolean isConfirmed = false;

    private boolean isRegistered = true;

    private String department;
    private String location;
    private String team;

    public User(String email, String password, String firstName, String lastName, String birthday, String phone, String startedToWorkDate, String position,
                String emailHash, boolean isCompleted, boolean showBirthday, boolean isConfirmed, boolean isRegistered, String department, String location, String team) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.startedToWorkDate = startedToWorkDate;
        this.position = position;
        this.emailHash = emailHash;
        this.isCompleted = isCompleted;
        this.showBirthday = showBirthday;
        this.isConfirmed = isConfirmed;
        this.isRegistered = isRegistered;
        this.department = department;
        this.location = location;
        this.team = team;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getStartedToWorkDate() {
        return startedToWorkDate;
    }

    public String getPosition() {
        return position;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isShowBirthday() {
        return showBirthday;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getDepartment() {
        return department;
    }

    public String getLocation() {
        return location;
    }

    public String getTeam() {
        return team;
    }
}
