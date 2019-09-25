
package blogutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *Author : Josh Lucas (991372911), Yatharth Soni (991503772)
 *Created : 2018/08/06 
 *Instructor : Haya Ghalayini
 *Project : Final Assignment
 * Blog Util
 * File Name : BlogUtil.java
 */

public class BlogUtil extends Application {
    
    //menu bar items declared 
    MenuItem itemLoad, itemSave, itemExit, itemClear;
    
    //Text fields for author, title of the blog, search box text and the records
    TextField txtAuthor, txtTitle, txtSearch, txtRecord;
    
    //Combo box for selection of province as well as subject of the blog
    ComboBox cbProvince, cbSubject;
    
    //TextArea instances fro content and list 
    TextArea txaPostContent, txaPostList;
    
    //Instances for submit, edit and delete under Button type
    Button btnSubmit, btnEdit, btnDelete;
    Date date;
    Alert alert, alert2;
    
    //ArrayList holding all the posts
    ArrayList<Post> posts = new ArrayList<>();
    
    @Override
    public void start(Stage primaryStage) {
        
        BorderPane root = new BorderPane();
        
        root.setTop(initMenu());
        root.setLeft(initMain());
        root.setRight(initSide());
        
        //lambda expression called after the user releases the key
        txtAuthor.setOnKeyReleased(e->handleKey(e));
        txtTitle.setOnKeyReleased(e->handleKey(e));
        txaPostContent.setOnKeyReleased(e->handleKey(e));
        Scene scene = new Scene(root, 600, 420);
        
        
        //Setting the Stage title
        primaryStage.setTitle("BlogUtil - A Notekeeping Utility");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
//    handleKey method called after the user releases the keys in the textBox
    public void handleKey(KeyEvent e) {
        //if the lambda expression is passed through the Search box
        if (e.getSource() == txtSearch) {
            
            //Calls the method named showFormatPost() if the expression is passed through the Search Box
            showFormatPost();
        } else {
            if (txtAuthor.getText().equals("") || txtTitle.getText().equals("")
                    || txaPostContent.getText().equals("")) {
                
                //Submit and edit buttons disabled if any of the text fields remains empty
                btnSubmit.setDisable(true);
                btnEdit.setDisable(true);
            } else {
                
                //Submitable as well as editable all of the fields are non-empty
                btnSubmit.setDisable(false);
                btnEdit.setDisable(false);
            }
        }
    }
    
    public void handle(ActionEvent e) { 
        
        //If the lambda expression is passed after the Submit button is pressed
        if (e.getSource() == btnSubmit) {
            
            //Calls the method addPost()
            addPost();
            
            //Calls the method ClearAll
            clearAll();
            
            //Everything set to default after the Submission is done
            btnSubmit.setDisable(true);
            showFormatPost();
            
        } else if (e.getSource() == itemClear) {
            //Calls method ClearAll if the Clear Button is pressed
            clearAll();
        } else if (e.getSource() == itemSave) {
            //Calls method named writeToFile() if Save button is clicked
            writeToFile();
        } else if (e.getSource() == itemLoad) {
            
            //Calls the method openFile() if the Load button is pressed
            openFile();
        } else if (e.getSource() == itemExit) {
            
            //Stops the Execution of program once the Exit button is pressed
            System.exit(0);
        } else if (e.getSource() == btnEdit) {
            
            //Allows user to edit the post after which everything gets cleared 
            editPost();
            clearAll();
            
            //Edit button gets disabled once editting starts and outputting the Format of the File after editting
            btnEdit.setDisable(true);
            showFormatPost();
        } else if (e.getSource() == btnDelete) {
           
            //Calls method deletePost() to delete the post after pressing the delete button
            deletePost();
            showFormatPost(); 
       }
    }
    
    public void editPost() {
        //updating the date
        date = new Date();
        
        //updating all the textfield entries
        Post newPost = new Post(
                txtAuthor.getText(),
                txtTitle.getText(),
                cbSubject.getValue().toString(),
                cbProvince.getValue().toString(),
                date.toString(),
                txaPostContent.getText() 
        );
        
        try { 
            
            //updating the list elements based on index
            posts.set(Integer.parseInt(txtRecord.getText()) - 1, newPost);
        } catch (IndexOutOfBoundsException|NumberFormatException e) {
            //System.out.println("That record does not exist.");
            
            //Windows dialouge Box showing errors to the user for teh index out of bounds exception
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("That record does not exist!");
            alert.showAndWait();
            
        }
    }
    
    public void addPost() {
        //updating the date
        date = new Date();
        
        //Filling up the text fields and saving it to the instance post of addPost()        
                Post post = new Post(
                txtAuthor.getText(),
                txtTitle.getText(),
                cbSubject.getValue().toString(),
                cbProvince.getValue().toString(),
                date.toString(),
                txaPostContent.getText());
        posts.add(post);
    }
    
    public void openFile() {
        
        //opening the chosen file 
        FileChooser openChooser = new FileChooser();
        openChooser.setInitialDirectory(new File("data/"));
        openChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter filterTxt = 
                new FileChooser.ExtensionFilter("Text Files", "*.txt");
        
        openChooser.getExtensionFilters().add(filterTxt);
        openChooser.setSelectedExtensionFilter(filterTxt);
        
        //Creating an instance named selectedFile of type File
        File selectedFile = openChooser.showOpenDialog(null);
        
            if (selectedFile != null) {
                //sending the path of the file of the selectedFile to the instance file of type File()
                File file = new File(selectedFile.getPath());
                ////// What to do with the file, e.g. display it in a TextArea
            }
        try (Scanner fileReader = new Scanner(selectedFile)) {
            
            //Clearing the ArrayList Post
            posts.clear();
            
            //declaring the String variables 
            String author, title, subject, province, date, post;
            
            //using the delimeter to separate the content of the files
            fileReader.useDelimiter(";|\\n");
             
            //looping until the last token available from fileReader instance
            while (fileReader.hasNext()) {
                try {
                    
                    //Returning the next complete tokens for the respective variables
                    author = fileReader.next();
                    title = fileReader.next();
                    subject = fileReader.next();
                    province = fileReader.next();
                    date = fileReader.next();
                    post = fileReader.next();
                    
                    //Adding everything to the ArrayList
                    posts.add(new Post(author, title, subject,
                        province, date, post));
                    
                    //Catching the File corruption exception and displaying a windows dialouge box
                } catch (Exception ioe) {
                    //System.out.println("Error! File is corrupt.");
                    
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("File is corrupt!");
                alert.showAndWait();
                    
                //clears the inputs added in to the arrayList
                posts.clear();
                    return;
                }
            }
            //Closing FileReader
            fileReader.close();
            showFormatPost();
            
            //Catching the file not found exception and displaying a windows dialouge box
        } catch (FileNotFoundException e) {
            //System.out.println("File not found.");
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("File not found!");
            alert.showAndWait();
        }
    }
    
    public void writeToFile() {
        
        //Dialouge box pops up if no content in any post
        if (posts.isEmpty()) {
            //System.out.println("Cannot save: no posts in this file.");
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Cannot Save : no posts in this field!");
            alert.showAndWait();
            return;
        }
        
        String title1 = JOptionPane.showInputDialog(null,"Enter the File Name:");
        //Creating a new instance of type File and sending myPost.txt as a parameter
        File f = new File("data/" + title1 + ".txt");
        
        //Declaring all the variables of type String
        String author, title, subject, province, date, post;
        try (PrintWriter pw = new PrintWriter(f)) {
            
            //Loop prints all the content to the file using printWriter
            for ( Post x : posts ) {
                author = x.getAuthor();
                title = x.getTitle();
                subject = x.getSubject();
                province = x.getProvince();
                date = x.getDate();
                post = x.getPost();
                
                pw.print(author + ";");
                pw.print(title + ";");
                pw.print(subject + ";");
                pw.print(province + ";");
                pw.print(date + ";"); 
                
                pw.print(post);   
                pw.println();
            }
            //Dialouge box pop up if printWriter fails to write the file
        } catch (IOException e) {
            //System.out.println("Failed to write.");
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Failed To Write!");
            alert.showAndWait();
        }
    }
    
    //Clear all methods clears all the entries generated into the text area as well as user inserted content from the text fields 
    public void clearAll() {
        txtAuthor.clear();
        txtTitle.clear();
        txaPostContent.clear();
        cbSubject.setValue(cbSubject.getItems().get(0));
        cbProvince.setValue(cbProvince.getItems().get(0));
        
        btnSubmit.setDisable(true);
        btnEdit.setDisable(true);
    }
    
    public void showFormatPost() {
        
        //Clears the content from the text Area
        txaPostList.clear();
        int recordNum = 0;
        String author, title, subject, province, date, post;
        
        /////Loop generates the content to be displayed to the textArea
        for ( Post p : posts ) {
            if (p.getRawStrings().contains(txtSearch.getText().toLowerCase())
                    || txtSearch.getText().equals("")) {
                recordNum++;
                author = p.getAuthor();
                title = p.getTitle();
                subject = p.getSubject();
                province = p.getProvince();
                date = p.getDate();
                post = p.getPost();

                txaPostList.appendText("[" + recordNum + "]" + date
                        + ":\n-------\n");

                txaPostList.appendText(String.format(
                "%s from %s, wrote about %s: \n\t%s\n\"%s\"\n\n",
                        author,
                        province,
                        subject,
                        title,
                        post
                ));
            }
        } 
    }
    
    //Deleting the post!
    public void deletePost() {
        try { 
           //get the number of record user wants to be deleted from the textField and delete that from the ArrayList 
            posts.get(Integer.parseInt(txtRecord.getText()) - 1);
            
                        
            alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("WHEE");
            alert2.setHeaderText(null);
            alert2.setContentText("Are you sure?");
            
            Optional<ButtonType> result = alert2.showAndWait();
            if (result.get() == ButtonType.OK) {
                posts.remove(Integer.parseInt(txtRecord.getText()) - 1);
            }
            
        } catch (IndexOutOfBoundsException|NumberFormatException f) {
            //System.out.println("That record does not exist.");
            //Dialogue box pops up if there is no existence of the record that the user wanted to be deleted
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("That record does not exist!");
            alert.showAndWait();
        } catch (NullPointerException g) {
            return;
        }
    }
    
    @FXML
    public GridPane initMain() {       
        //Creating all the labels allwoing the user to know the text field type
        Label lblWelcome = new Label("Create a new post");
        
        lblWelcome.setStyle("-fx-font-size:20;");
        lblWelcome.setAlignment(Pos.CENTER);
        
        
        
        Label lblName = new Label("Author name: ");
        Label lblTitle = new Label("The title of your post: ");
        Label lblSubject = new Label("Pick a subject: ");
        Label lblProvince = new Label("Your province: ");
        Label lblPostContent = new Label("Enter your post: ");
        
        txtAuthor = new TextField();
        txtTitle = new TextField();
        
        //Combo box to allow the user to enter the information regarding his province
        cbProvince = new ComboBox();
        cbProvince.setPadding(new Insets(5, 5, 5, 5));
        //ArrayList providing the options for the selection of user's province
        ObservableList<String> provinces = 
                FXCollections.observableArrayList(
                "Ontario",
                "British Columbia",
                "Quebec",
                "Alberta",
                "Nova Scotia",
                "Saskatchewan",
                "Manitoba",
                "New Brunswick",
                "Newfoundland",
                "PEI",
                "Nunavut",
                "Yukon",
                "NW Territories"
        );
        cbProvince = new ComboBox(provinces);
        cbProvince.setValue(provinces.get(0));
       
        //Combo box to allow the user to enter the information regarding their subject
        cbSubject = new ComboBox();
        cbSubject.setPadding(new Insets(5, 5, 5, 5));
        //List of the subject stored in subjects variable of type ArrayList
        ObservableList<String> subjects = 
                FXCollections.observableArrayList(
                "Technology",
                "Philosophy",
                "Literature",
                "Other"
        );
        cbSubject = new ComboBox(subjects);
        cbSubject.setValue(subjects.get(0));
        
        txaPostContent = new TextArea();
        txaPostContent.setWrapText(true);
        
        
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        
        btnSubmit = new Button("Submit"); 
        btnSubmit.setPadding(new Insets(5, 5, 5, 5));
        btnSubmit.setDisable(true);
        
        btnSubmit.setOnAction(e->handle(e));
        
     //Positioning all the instances used in the program according the Grid Pane 
        hb.getChildren().addAll(btnSubmit);
     
        GridPane main = new GridPane();
        main.add(lblWelcome, 0, 0, 2, 1);
        
        main.add(lblName, 0, 1);
        main.add(txtAuthor, 1, 1);
        
        main.add(lblTitle, 0, 2);
        main.add(txtTitle, 1, 2);
        
        main.add(lblSubject, 0, 3);
        main.add(cbSubject, 1, 3);
        
        main.add(lblProvince, 0, 4);
        main.add(cbProvince, 1, 4);
        
        main.add(lblPostContent, 0, 5);
        main.add(txaPostContent, 0, 6, 2, 2);
        
        main.add(hb, 0, 12, 2, 1);
        
        main.setMinHeight(400);
        main.setMaxWidth(300);
        main.setPadding(new Insets(5, 5, 5, 5));
        
        return main;
    }
    
    //Setting a MenuBar and Menus init
    public MenuBar initMenu() {
        
        //Setting a menu bar
        MenuBar bar = new MenuBar();
        
        //Menu - file added in the Menu Bar
        Menu menuFile = new Menu("_File");
    
        //Menu Edit added into the Menu Bar
        Menu menuEdit = new Menu("_Edit");
        
        itemLoad = new MenuItem("_Load");
        
        itemSave = new MenuItem("_Save");
       
        itemExit = new MenuItem("E_xit");
        
        itemClear  = new MenuItem("Clear");
        
        //Lambda expression for all the menu items
        itemSave.setOnAction(e->handle(e));
        itemLoad.setOnAction(e->handle(e));
        itemExit.setOnAction(e->handle(e));
        itemClear.setOnAction(e->handle(e));
        
        //adding all the menu items into the menus and all the menus into the menu bar
        menuFile.getItems().addAll(itemLoad, itemSave, itemExit);
        menuEdit.getItems().add(itemClear);
        bar.getMenus().addAll(menuFile, menuEdit);
        return bar;
    }
    
    public VBox initSide() {
        Label lblPosts = new Label("View posts");
        lblPosts.setStyle("-fx-font-size:20");
        Label postsInFile = new Label("Filter posts in this file: ");
        
        
        
        txaPostList = new TextArea();
        txaPostList.setEditable(false);
        txaPostList.setWrapText(true);
        
        txaPostList.setMinHeight(290);
        txaPostList.setMaxHeight(290);
        txtSearch = new TextField();
        
        //Lamda expression generated on releasing keys
        txtSearch.setOnKeyReleased(e->handleKey(e));
        
        txtRecord = new TextField();
        btnEdit = new Button("Edit");
        btnEdit.setPadding(new Insets(5, 5, 5, 5));
        btnEdit.setDisable(true);
        btnDelete = new Button("Delete");
        btnDelete.setPadding(new Insets(5, 5, 5, 5));
        
        //Lambda expressions generated after clicking the edit and delete buttons
        btnEdit.setOnAction(e->handle(e));
        btnDelete.setOnAction(e->handle(e));
        
        Label lblRecord = new Label("Record#: ");
        HBox hb = new HBox();
        //Adding all the instances to the Hbox hb
        hb.getChildren().addAll(lblRecord, txtRecord, btnEdit, btnDelete);
        
        VBox vb = new VBox();
        vb.setPadding(new Insets (5, 5, 5, 5));
        
        
        
       
        //Adding all the instances to the Vbox vb
        vb.getChildren().addAll(lblPosts, postsInFile, txtSearch, txaPostList);
        vb.setMaxWidth(300);
        vb.getChildren().add(hb);
        
        return vb;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}  