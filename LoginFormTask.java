import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {

  private String username;
  private String password;
  private String role;

  User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }
}

class WelcomePage extends Frame {

  WelcomePage(User user) {
    super("Welcome Page");
    setLayout(new GridLayout(2, 1, 10, 10));

    Label welcomeLabel = new Label("Hello, " + user.getUsername(), Label.CENTER);
    welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

    Label roleLabel = new Label("Role: " + user.getRole(), Label.CENTER);
    roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

    add(welcomeLabel);
    add(roleLabel);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });

    setSize(350, 200);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

public class LoginFormTask extends Frame {

  private List<User> users = new ArrayList<>();
  private int failedAttempts = 0;

  public LoginFormTask() {
    super("Login");
    loadUsers();

    setLayout(new GridBagLayout());
    Panel content = new Panel(new BorderLayout(10, 15));

    Label title = new Label("Welcome back", Label.CENTER);
    title.setFont(new Font("SansSerif", Font.BOLD, 20));
    content.add(title, BorderLayout.NORTH);

    Panel form = new Panel(new GridLayout(3, 2, 8, 8));
    form.add(new Label("Username:", Label.RIGHT));
    TextField username = new TextField(18);
    form.add(username);

    form.add(new Label("Password:", Label.RIGHT));
    TextField password = new TextField(18);
    password.setEchoChar('*');
    form.add(password);

    form.add(new Label("Role:", Label.RIGHT));
    Choice role = new Choice();
    role.add("Student");
    role.add("TA");
    role.add("Professor");
    form.add(role);

    content.add(form, BorderLayout.CENTER);

    Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    Button cancel = new Button("Cancel");
    Button login = new Button("Login");
    buttons.add(cancel);
    buttons.add(login);
    content.add(buttons, BorderLayout.SOUTH);

    add(content);

    login.addActionListener(e -> {
      String enteredUser = username.getText().trim();
      String enteredPass = password.getText();
      String selectedRole = role.getSelectedItem();

      if (enteredUser.isEmpty() || enteredPass.isEmpty()) {
        showMessage("Please fill in all fields!");
        return;
      }

      User foundUser = findUser(enteredUser);
      if (foundUser == null) {
        showMessage("User does not exist!");
        return;
      }

      if (!foundUser.getPassword().equals(enteredPass)) {
        failedAttempts++;
        if (failedAttempts >= 3) {
          login.setEnabled(false);
          showMessage("Wrong password 3 times! Login button is now disabled.");
        } else {
          showMessage("Incorrect password! Attempts remaining: " + (3 - failedAttempts));
        }
        return;
      }

      if (!foundUser.getRole().equals(selectedRole)) {
        showMessage("Invalid role selected for this user!");
        return;
      }

      failedAttempts = 0;
      new WelcomePage(foundUser);
      dispose();
    });

    cancel.addActionListener(e -> {
      dispose();
    });

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });
    setSize(420, 340);
    setLocationRelativeTo(null);
  }

  private void loadUsers() {
    users.add(new User("ahmed", "1234", "Student"));
    users.add(new User("sara", "abcd", "TA"));
    users.add(new User("dr_mona", "prof99", "Professor"));
  }

  private User findUser(String name) {
    for (User u : users) {
      if (u.getUsername().equalsIgnoreCase(name)) {
        return u;
      }
    }
    return null;
  }

  private void showMessage(String message) {
    Dialog dialog = new Dialog(this, "Alert", true);
    dialog.setLayout(new BorderLayout(10, 10));

    Label msgLabel = new Label(message, Label.CENTER);
    Button okButton = new Button("OK");
    okButton.addActionListener(e -> dialog.dispose());

    Panel btnPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
    btnPanel.add(okButton);

    dialog.add(msgLabel, BorderLayout.CENTER);
    dialog.add(btnPanel, BorderLayout.SOUTH);

    dialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dialog.dispose();
      }
    });

    dialog.setSize(340, 130);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  public static void main(String[] args) {
    new LoginFormTask().setVisible(true);
  }
}