package blog.users.model;

public class UserModel {
    private long id;
    private String username;
    private String password;

    // Default constructor
    public UserModel() {
    }

    // Parameterized constructor
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for id
    public long getId() {
        return id;
    }

    // Setter for id
    public void setId(long id) {
        this.id = id;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}
