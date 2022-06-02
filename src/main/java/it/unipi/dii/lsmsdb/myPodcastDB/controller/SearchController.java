package it.unipi.dii.lsmsdb.myPodcastDB.controller;

import it.unipi.dii.lsmsdb.myPodcastDB.MyPodcastDB;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Admin;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Author;
import it.unipi.dii.lsmsdb.myPodcastDB.model.Podcast;
import it.unipi.dii.lsmsdb.myPodcastDB.model.User;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.ImageCache;
import it.unipi.dii.lsmsdb.myPodcastDB.utility.Logger;
import it.unipi.dii.lsmsdb.myPodcastDB.view.StageManager;
import it.unipi.dii.lsmsdb.myPodcastDB.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchController {
    @FXML
    private ImageView home;

    @FXML
    private ImageView actorPicture;

    @FXML
    private ImageView logout;

    @FXML
    private VBox boxActorProfile;

    @FXML
    private VBox boxLogout;

    @FXML
    private ImageView searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private Label searchingForText;

    @FXML
    private GridPane gridFoundAuthors;

    @FXML
    private GridPane gridFoundUsers;

    @FXML
    private GridPane gridFoundPodcasts;

    @FXML
    private ScrollPane scrollFoundPodcasts;

    @FXML
    private Label podcastsFound;

    @FXML
    private Label authorsFound;

    @FXML
    private Label usersFound;

    @FXML
    private VBox boxUsersFound;

    @FXML
    private Label noPodcastsText;

    @FXML
    private Label noAuthorsText;

    @FXML
    private Label noUsersFound;

    /*********** Navigator Events (Profile, Home, Search) *************/
    @FXML
    void onClickActorProfile(MouseEvent event) throws IOException {
        Logger.info("Actor profile clicked");
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author"))
            StageManager.showPage(ViewNavigator.AUTHORPROFILE.getPage(), ((Author)MyPodcastDB.getInstance().getSessionActor()).getName());
        else if (actorType.equals("User"))
            StageManager.showPage(ViewNavigator.USERPAGE.getPage());
        else if (actorType.equals("Admin"))
            StageManager.showPage(ViewNavigator.ADMINDASHBOARD.getPage());
        else
            Logger.error("Unidentified Actor Type!");
    }
    @FXML
    void onClickSearch(MouseEvent event) throws IOException {
        if (!searchText.getText().isEmpty()) {
            String text = searchText.getText();
            Logger.info("Searching for " + text);
            StageManager.showPage(ViewNavigator.SEARCH.getPage(), text);
        } else {
            Logger.error("Field cannot be empty!");
        }
    }

    @FXML
    void onEnterPressedSearch(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (!searchText.getText().isEmpty()) {
                String text = searchText.getText();
                Logger.info("Searching for " + text);
                StageManager.showPage(ViewNavigator.SEARCH.getPage(), text);
            } else {
                Logger.error("Field cannot be empty!");
            }
        }
    }

    @FXML
    void onClickHome(MouseEvent event) throws IOException {
        StageManager.showPage(ViewNavigator.HOMEPAGE.getPage());
        Logger.info(MyPodcastDB.getInstance().getSessionType() +  " Home Clicked");
    }

    @FXML
    void onClickLogout(MouseEvent event) throws IOException {
        Logger.info("Logout button clicked");
        // TODO: clear the session
        MyPodcastDB.getInstance().setSession(null, null);
        StageManager.showPage(ViewNavigator.LOGIN.getPage());
    }

    /**********************************************************/

    public void initialize() throws IOException {
        // Load information about the actor of the session
        String actorType = MyPodcastDB.getInstance().getSessionType();

        if (actorType.equals("Author")) {
            Author sessionActor = (Author)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an actor: " + sessionActor.getName());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

        } else if (actorType.equals("User")) {
            User sessionActor = (User)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an user: " + sessionActor.getUsername());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath(sessionActor.getPicturePath());
            actorPicture.setImage(image);

        } else if (actorType.equals("Admin")) {
            Admin sessionActor = (Admin)MyPodcastDB.getInstance().getSessionActor();
            Logger.info("I'm an administrator: " + sessionActor.getName());

            // Setting GUI params
            Image image = ImageCache.getImageFromLocalPath("/img/userPicture.png");
            actorPicture.setImage(image);

        } else if (actorType.equals("Unregistered")) {
            Logger.info("I'm an unregistered user");

            // Disabling User Profile Page and Logout Button
            boxActorProfile.setVisible(false);
            boxActorProfile.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

            boxLogout.setVisible(false);
            boxLogout.setStyle("-fx-min-height: 0; -fx-pref-height: 0; -fx-min-width: 0; -fx-pref-width: 0;");

        } else {
            Logger.error("Unidentified Actor Type");
        }


        // Load word searched
        this.searchingForText.setText("Searching for \"" + StageManager.getObjectIdentifier() + "\"");

        // Author Podcasts
        Author author = new Author();
        author.setName("Robespierre Janjaq");

        List<Podcast> previewList = new ArrayList<>();
        Podcast p1 = new Podcast("54eb342567c94dacfb2a3e50", "Scaling Global", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts126/v4/ab/41/b7/ab41b798-1a5c-39b6-b1b9-c7b6d29f2075/mza_4840098199360295509.jpg/600x600bb.jpg", "Business");
        Podcast p2 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg", "Roman");
        Podcast p3 = new Podcast("061a68eb754c400eae8027d7", "Average O Podcast", new Date(), "https://is2-ssl.mzstatic.com/image/thumb/Podcasts125/v4/54/e4/84/54e48471-6971-03c8-83f4-4f973dc2a8cb/mza_8686729233410161200.jpg/600x600bb.jpg", "Chill");
        Podcast p4 = new Podcast("34e734b09246d17dc5d56f63", "Getting Smart Podcast", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts115/v4/52/e3/25/52e325bd-e6ba-3899-b7b4-71e512a48472/mza_18046006527881111713.png/600x600bb.jpg", "");
        Podcast p5 = new Podcast("84baff1495bff70bb81bd016", "Sofra Sredom", new Date(), "https://is4-ssl.mzstatic.com/image/thumb/Podcasts115/v4/98/ca/c7/98cac700-4398-7489-100a-416ec28d6662/mza_15500803433364327137.jpg/600x600bb.jpg", "Science");
        Podcast p6 = new Podcast("34e734b09246d17dc5d56f63", "Cornerstone Baptist Church of Orlando", new Date(), "https://is5-ssl.mzstatic.com/image/thumb/Podcasts125/v4/d3/06/0f/d3060ffe-613b-74d6-9594-cca7a874cd6c/mza_12661332092752927859.jpg/600x600bb.jpg", "Moon");
        previewList.add(p1);
        previewList.add(p2);
        previewList.add(p3);
        previewList.add(p4);
        previewList.add(p5);
        previewList.add(p6);

        author.setOwnPodcasts(previewList);

        // author.getOwnPodcasts().clear(); (No, is not public member)
        if (author.getOwnPodcasts().isEmpty()) {
            this.gridFoundPodcasts.setVisible(false);
            this.noPodcastsText.setVisible(true);
        } else {
            this.noPodcastsText.setVisible(false);
            this.noPodcastsText.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");
        }

        int row = 0;
        int column = 0;
        for (Podcast entry : author.getOwnPodcasts()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorReducedPodcast.fxml"));

            AnchorPane newPodcast = fxmlLoader.load();
            AuthorReducedPodcastController controller = fxmlLoader.getController();
            controller.setData(entry.getId(), entry.getName(), entry.getReleaseDate(), entry.getPrimaryCategory(), entry.getArtworkUrl600());

            gridFoundPodcasts.add(newPodcast, column, row++);
        }

        this.podcastsFound.setText("Podcasts (" + author.getOwnPodcasts().size() + ")");

        /********************************************************************************/

        // Authors Followed
        List<Author> authorsFound = new ArrayList<>();
        for (int j = 0; j < 2; j++){
            Author a = new Author();
            a.setName("Apple Inc. " + j);
            a.setPicturePath("/img/authorAnonymousPicture.png");
            authorsFound.add(a);
        }

        //authorsFound.clear();
        if (authorsFound.isEmpty()) {
            this.gridFoundAuthors.setVisible(false);
            this.noAuthorsText.setVisible(true);
        } else {
            this.noAuthorsText.setVisible(false);
            this.noAuthorsText.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");
        }

        row = 0;
        column = 0;
        for (Author a : authorsFound){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("AuthorSearchPreview.fxml"));

            AnchorPane newAuthor = fxmlLoader.load();
            AuthorSearchPreviewController controller = fxmlLoader.getController();
            controller.setData(a);

            gridFoundAuthors.add(newAuthor, column, row++);
        }

        this.authorsFound.setText("Authors (" + authorsFound.size() + ")");

        /********************************************************************************/

        // User found
        if (!actorType.equals("Author") && !actorType.equals("Unregistered")) {
            List<User> usersFound = new ArrayList<>();

            for (int j = 0; j < 20; j++){
                User u = new User();
                u.setUsername("Jhonny " + j);
                u.setPicturePath("/img/test.jpg");
                usersFound.add(u);
            }

            //usersFound.clear();
            if (usersFound.isEmpty()) {
                this.gridFoundUsers.setVisible(false);
                this.noUsersFound.setVisible(true);
            } else {
                this.noUsersFound.setVisible(false);
                this.noUsersFound.setStyle("-fx-min-height: 0; -fx-pref-height: 0px");
            }

            row = 0;
            column = 0;
            for (User u : usersFound) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("UserSearchPreview.fxml"));

                AnchorPane newUser = fxmlLoader.load();
                UserSearchPreviewController controller = fxmlLoader.getController();
                controller.setData(u);

                gridFoundUsers.add(newUser, column, row++);
            }

            this.usersFound.setText("Users (" + usersFound.size() + ")");

        } else {
            this.boxUsersFound.setVisible(false);
        }

        /********************************************************************************/
    }
}
