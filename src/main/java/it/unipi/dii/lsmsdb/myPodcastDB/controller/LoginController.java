package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label loginUsernameLabel;
    @FXML
    private RadioButton loginUserRadioButton;

    @FXML
    private RadioButton loginAdminRadioButton;

    @FXML
    private RadioButton loginAuthorRadioButton;

    @FXML
    private RadioButton loginUnUserRadioButton;

    @FXML
    private Button loginLoginButton;

    @FXML
    private TextField loginPasswordTextField;


    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    public void initialize(){

        ToggleGroup tg = new ToggleGroup();
        loginUserRadioButton.setToggleGroup(tg);
        loginAuthorRadioButton.setToggleGroup(tg);
        loginAdminRadioButton.setToggleGroup(tg);
        loginUnUserRadioButton.setToggleGroup(tg);
        loginUserRadioButton.setSelected(true);
    }

    @FXML
    void loginLoginButtonClick(MouseEvent event) throws IOException {

        String actorname = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();

        if(actorname.isEmpty() || password.isEmpty()) {
            Logger.error("Login clicked: invalid values");
            return;
        }

        if(loginUserRadioButton.isSelected()) {
            Logger.info("User actor selected");
            String actorType = "User";
            User user = (User)simActorService(actorname, password, actorType);
            MyPodcastDB.getInstance().setSession(user, actorType);
        }
        else if(loginAuthorRadioButton.isSelected()) {
            Logger.info("Author actor selected");
            String actorType = "Author";
            Author author = (Author)simActorService(actorname, password, actorType);
            MyPodcastDB.getInstance().setSession(author, actorType);
        }
        else if(loginAdminRadioButton.isSelected()){
            Logger.info("Admin actor selected");
            String actorType = "Admin";
            Admin admin = (Admin)simActorService(actorname, password, actorType);
            MyPodcastDB.getInstance().setSession(admin, actorType);
        }
        else
            Logger.error("No actor selected");

        String log = "Login clicked: (" + actorname + ", " + password +")";
        Logger.info(log);

        if(loginAdminRadioButton.isSelected())
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }

    @FXML
    void loginLoginButtonPressed(MouseEvent event){
        Logger.info("login button pressed");
        loginLoginButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginLoginButtonReleased(MouseEvent event){
        Logger.info("login buttod released");
        loginLoginButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginSignUpButtonPressed(MouseEvent event){
        Logger.info("sugnup butto pressed");
        loginSignUpButton.setStyle(
                "-fx-background-color: #bcbcbc;" +
                        "-fx-border-color:  #e0e0e0;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }

    @FXML
    void loginSgnUpButtonReleased(MouseEvent event){
        Logger.info("login butto released");
        loginSignUpButton.setStyle(
                "-fx-background-color: #e0e0e0;" +
                        "-fx-border-color:  #bcbcbc;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
    }


    @FXML
    void loginSignUpButtonClick(MouseEvent event) throws IOException {
        Logger.info("SignUp clicked");
        StageManager.showPage(ViewNavigator.SIGNUP.getPage());

    }

    @FXML
    void userRadioButtonClick(){

        loginUsernameLabel.setText("Username");
    }

    @FXML
    void authorRadioButtonClick(){
        loginUsernameLabel.setText("Author Name");
    }

    @FXML
    void adminRadioButtonClick(){
        loginUsernameLabel.setText("Admin Name");
    }

    @FXML
    void unUserRadioButtonClick() throws IOException {
        Logger.info("Sign in as unregistered user");
        MyPodcastDB.getInstance().setSession("","Unregistered");
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
    }


    Object simActorService(String actorname, String password, String actorType){

        if(actorType.equals("User")){
            User user = new User("af679", actorname, password, "userTestName", "userTestSurname", "userTest@example.com", "Italy", "/img/account_50px.png", "Horror", 34, "Male");
            return user;
        }
        else if(actorType.equals("Author")){
            Author author = new Author("1234",actorname, password, "authorTest@example.com","/img/authorAnonymousePicture.png");
            return author;
        }
        else if(actorType.equals("Admin")){
            Admin admin = new Admin("sacr23", actorname, password, "adminTest@example.com");
            return admin;
        }
        else{
            Logger.error("Uncorrected actorType");
            return null;
        }

    }
}