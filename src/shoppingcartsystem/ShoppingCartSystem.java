/*

Project/Assignment Title: Shopping Cart System
File Name: ShoppingCartSystem.java
Date Complied Last: May 9, 2021
Author:David Truong
Complied in: NetBeans 8.2
Version Control Number: 10.21

Integrity Statement: By submitting this project,
    I am taking the integrity oath that I have
    not copied any line(s) of code –partially
    or completely -from any other individual,
    former studentwork, textbook, online
    resources, website, and any reference
    available anywhere.

*/
package shoppingcartsystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ShoppingCartSystem extends Application {
    private DoubleProperty subtotalProperty = new SimpleDoubleProperty(0);
    private DoubleProperty taxProperty = new SimpleDoubleProperty(0);
    private DoubleProperty totalProperty = new SimpleDoubleProperty(0);
    private Label subtotalAmountLabel = new Label();
    private Label taxAmountLabel = new Label();
    private Label totalAmountLabel = new Label();
    private ObservableList<String> shoppingCartObservableList  = FXCollections.observableArrayList();
    private ObservableList<String> storefrontObservableList  = FXCollections.observableArrayList();
    private Scene storefrontScene, shoppingCartScene, checkoutScene, thankYouScene;
    private Stage window;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;        
        
        // Bind cost properties to their appropriate buttons.        
        subtotalAmountLabel.textProperty().bind(Bindings.concat("$", subtotalProperty.asString()));
        taxAmountLabel.textProperty().bind(Bindings.concat("$", taxProperty.asString()));
        totalAmountLabel.textProperty().bind(Bindings.concat("$", totalProperty.asString()));
        
        // Delcare borderpanes for each scene.
        BorderPane checkoutBorderPane, shoppingCartBorderPane, storefrontBorderPane, thankYouBorderPane;

        // Code for the storefront scene begins here.
        storefrontBorderPane = new BorderPane();
        storefrontScene = new Scene(storefrontBorderPane, 980, 1026);
        
        storefrontBorderPane.setTop(addStorefrontTopLayout());
        storefrontBorderPane.setCenter(addStorefrontCenterLayout());
        // Code for the storefront scene ends here.
        
        // Code for the shopping cart scene begins here.
        shoppingCartBorderPane = new BorderPane();
        shoppingCartScene = new Scene(shoppingCartBorderPane, 980, 1026);
        
        shoppingCartBorderPane.setTop(addShoppingCartTopLayout());
        shoppingCartBorderPane.setCenter(addShoppingCartCenterLayout());
        // Code for the shopping cart scene ends here.
        
        // Code for checkout scene begins here.
        checkoutBorderPane = new BorderPane();
        checkoutScene = new Scene(checkoutBorderPane, 980, 1026);
        
        checkoutBorderPane.setTop(addcheckoutTopLayout());
        checkoutBorderPane.setCenter(addCheckoutCenterLayout());
        // Code for checkout scene ends here.
        
        // Code for thank you scene begins here.
        thankYouBorderPane = new BorderPane();
        thankYouScene = new Scene(thankYouBorderPane, 980, 1026);
        
        thankYouBorderPane.setTop(addThankYouTopLayout());
        thankYouBorderPane.setCenter(addThankYouCenterLayout());
        // Code for thank you scene ends here.
        
        // Code for clicking the window close button on the top-right.
        primaryStage.setOnCloseRequest(closeRequest -> {
            Alert exitProgram = new Alert(AlertType.CONFIRMATION);
            exitProgram.setTitle("Exit Program?");
            exitProgram.setHeaderText("Exit Program?");
            exitProgram.setContentText("Are you sure you want to exit?\n"
                    + "You will lose all unsubmitted changes.");
            
            exitProgram.showAndWait();
            
            if (exitProgram.getResult() == ButtonType.CANCEL) {
                closeRequest.consume();
            }
        });
        
        primaryStage.setScene(storefrontScene);
        primaryStage.setTitle("Impact009.com Online Book Store");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    // Top layout of the storefront.
    public HBox addStorefrontTopLayout() {
        HBox storefrontTopLayoutHBox = new HBox();
        storefrontTopLayoutHBox.setPadding(new Insets(10, 10, 10, 10));
        storefrontTopLayoutHBox.setSpacing(10);
        storefrontTopLayoutHBox.setStyle("-fx-background-color: #444444;");

        // Add a logo to the top layout.
        Image logo = new Image("File:Logo.png");
        ImageView imageView = new ImageView(logo);
        
        StackPane storefrontStack = new StackPane();        
        
        // Add a View Cart button.
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setStyle("-fx-font-size:23");
        viewCartButton.setOnAction (clickViewCartButton -> window.setScene(shoppingCartScene));
        
        storefrontStack.getChildren().add(viewCartButton);
        storefrontStack.setAlignment(Pos.CENTER_RIGHT);
        storefrontTopLayoutHBox.getChildren().addAll(imageView, storefrontStack);

        HBox.setHgrow(storefrontStack, Priority.ALWAYS);
        
        return storefrontTopLayoutHBox;
    }
    
    // Center layout of the storefront.
    public GridPane addStorefrontCenterLayout() throws IOException {
        GridPane storefrontGridPane = new GridPane();
        storefrontGridPane.setHgap(50);
        storefrontGridPane.setPadding(new Insets(10, 10, 10, 10));
        
        ListView<String> storefrontListView = new ListView<>();
        
        // Create the storefront's ListView.
        storefrontListView.setItems(storefrontObservableList);
        FileInputStream fileByteStream = null;
        
        try {
            // Read items and prices from file.
            fileByteStream = new FileInputStream("BookPriceList.txt");
            Scanner inFS = new Scanner(fileByteStream);
            
            while(inFS.hasNext()) {
                String fileLine = inFS.nextLine();
                
                // Add items to a ListView.
                storefrontObservableList.add(fileLine);
            }
        } catch (IOException excpt) {
            System.out.println(excpt.getMessage());
        } finally {
            fileByteStream.close();
        }
        
        // Give each item an image.
        storefrontListView.setCellFactory(param -> new ListCell<String>() {
            Image image;
            ImageView imageView = new ImageView();
            String formatteditemLine;
            
            @Override
            public void updateItem(String itemLine, boolean empty) {
                super.updateItem(itemLine, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    switch (itemLine) {
                        case "I Did It Your Way, 11.95":
                            image = new Image("File:I_Did_It_Your_Way.png");
                            
                            formatteditemLine = itemLine.format("%-41s", itemLine.substring(0, itemLine.indexOf(","))) + "$" +
                                    itemLine.substring(itemLine.indexOf(",") + 2, itemLine.length());
                            break;
                        case "The History of Scotland, 14.50":
                            image = new Image("File:The_History_of_Scotland.png");
                            
                            formatteditemLine = itemLine.format("%-37s", itemLine.substring(0, itemLine.indexOf(","))) + "$" +
                                    itemLine.substring(itemLine.indexOf(",") + 2, itemLine.length());
                            break;
                        case "Learn Calculus in One Day, 29.95":
                            image = new Image("File:Learn_Calculus_in_One_Day.png");
                            
                            formatteditemLine = itemLine.format("%-35s", itemLine.substring(0, itemLine.indexOf(","))) + "$" +
                                    itemLine.substring(itemLine.indexOf(",") + 2, itemLine.length());
                            break;
                        case "Feel the Stress, 18.50":
                            image = new Image("File:Feel_the_Stress.png");
                            
                            formatteditemLine = itemLine.format("%-44s", itemLine.substring(0, itemLine.indexOf(","))) + "$" +
                                    itemLine.substring(itemLine.indexOf(",") + 2, itemLine.length());
                            break;
                        case "Starting out with Java, 49.99":
                            image = new Image("File:Starting_out_with_Java.png");
                            
                            formatteditemLine = itemLine.format("%-39s", itemLine.substring(0, itemLine.indexOf(","))) + "$" +
                                    itemLine.substring(itemLine.indexOf(",") + 2, itemLine.length());
                            break;
                        default:
                            break;
                    }
                    
                    imageView.setImage(image);
                    setGraphic(imageView);
                    setText(formatteditemLine);
                    setFont(Font.font(18));
                }
            }
        });
        
        // Allow selection of multiple books.
        storefrontListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        storefrontListView.setPrefWidth(501);
        storefrontListView.setPrefHeight(862);
        GridPane.setConstraints(storefrontListView, 0, 0);
        
        // Create an Add to Cart button.
        Button addToCart = new Button("Add to Cart");
        addToCart.setStyle("-fx-font-size:24");
        
        addToCart.setOnAction(clickAddToCart -> {
            try {                
                // Store selected items.
                ObservableList<String> addItems;
                addItems = storefrontListView.getSelectionModel().getSelectedItems();
                
                for (int i = 0; i < addItems.size(); i++ ) {
                    String selectedItemName = addItems.get(i).substring(0, addItems.get(i).indexOf(","));

                    // Add the selected item to the shopping cart if the latter is empty.
                    if (shoppingCartObservableList.isEmpty()) {
                        shoppingCartObservableList.add(selectedItemName + Integer.toString(1));
                    } else {

                        // Iterate through the shopping cart and get the current item from it.
                        for (String shoppingCartItem: shoppingCartObservableList) {

                            // This is the string index belonging to quantity of the item.
                            int digitIndex = 0;

                            // Get the string index of the item's quantity.
                            for (int j = 0; j < shoppingCartItem.length(); j++) {

                                if (Character.isDigit(shoppingCartItem.charAt(j))) {
                                    digitIndex = j;

                                    break;
                                }
                            }

                            // If the titles match, then increment the item's quantity.
                            if (selectedItemName.equals(shoppingCartItem.substring(0, digitIndex))) {

                                int itemQuantity = Integer.parseInt(shoppingCartItem.substring(digitIndex, shoppingCartItem.length()));
                                itemQuantity++;

                                shoppingCartObservableList.set(i, selectedItemName + Integer.toString(itemQuantity));

                                // The item was found, so break out of the loop.
                                break;
                            } else {

                                // The item wasn't found, so add it to the list.
                                shoppingCartObservableList.add(selectedItemName + Integer.toString(1));

                                break;
                            }   
                        }
                    }      
                }
                
                // Calculate the shopping cart's costs.
                calculateShoppingCart();
                
                // Change the scene to the shopping cart.
                window.setScene(shoppingCartScene);
            } catch (Exception nullSelectionExceptionStorefront) {
                Alert nullSelectionAlert = new Alert(AlertType.ERROR, "Please select an item.");
                nullSelectionAlert.showAndWait();
            }
        });
        
        GridPane.setValignment(addToCart, VPos.CENTER);        
        GridPane.setConstraints(addToCart, 1, 0);
        
        // Create a note for the user on how to use the GUI.
        Label noteLabel = new Label("Note: Ctrl + Left-Click or Shift + Left-Click to select multiple books.");
        noteLabel.setFont(Font.font(18));
        noteLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(noteLabel, 0, 1);
        
        storefrontGridPane.getChildren().addAll(storefrontListView, addToCart, noteLabel);
        
        return storefrontGridPane;
    }

    // Top layout of the shopping cart.
    public HBox addShoppingCartTopLayout() {
        HBox shoppingCartTopLayout = new HBox();
        shoppingCartTopLayout.setPadding(new Insets(10, 10, 10, 10));
        shoppingCartTopLayout.setSpacing(10);
        shoppingCartTopLayout.setStyle("-fx-background-color: #444444;");

        // Add a logo to the top layout.
        Image logo = new Image("File:Logo.png");
        ImageView imageView = new ImageView(logo);
        
        Button backToStoreButton = new Button("Back to Store");
        backToStoreButton.setStyle("-fx-font-size:23");
        backToStoreButton.setOnAction (clickBackToStoreButton -> window.setScene(storefrontScene));
        
        // Create a Clear Cart button.
        Button clearCart = new Button("Clear Cart");
        clearCart.setStyle("-fx-font-size:23");
        clearCart.setOnAction(clickClearCart -> {
            shoppingCartObservableList.clear();
            
            subtotalProperty.setValue(0);
            taxProperty.setValue(0);
            totalProperty.setValue(0);
        });
        
        StackPane shoppingCartStack = new StackPane();        
        
        // Create a checkout button.
        Button checkout = new Button("Checkout");
        checkout.setStyle("-fx-font-size:23");
        checkout.setOnAction(clickCheckoutButton -> {
            try {
                if (subtotalProperty.getValue() > 0 && taxProperty.getValue() > 0 && totalProperty.getValue() > 0) {
                    window.setScene(checkoutScene);
                } else {
                    throw new NullPointerException();
                }
            } catch (Exception emptyShoppingCartException) {
                Alert emptyShoppingCartAlert = new Alert(AlertType.ERROR, "The shopping cart is empty.");
                emptyShoppingCartAlert.showAndWait();
            }
        });
        shoppingCartStack.getChildren().add(checkout);
        shoppingCartStack.setAlignment(Pos.CENTER_RIGHT);
        shoppingCartTopLayout.getChildren().addAll(imageView, backToStoreButton, clearCart, shoppingCartStack);

        HBox.setHgrow(shoppingCartStack, Priority.ALWAYS);
        
        return shoppingCartTopLayout;
    }
    
    // Center layout of the shopping cart.
    public GridPane addShoppingCartCenterLayout() {
        GridPane shoppingCartGridPane = new GridPane();
        shoppingCartGridPane.setHgap(10);
        shoppingCartGridPane.setVgap(10);
        shoppingCartGridPane.setPadding(new Insets(10, 10, 10, 10));
        
        // Create the header for the shopping cart's ListView.
        Label itemLabel = new Label("Item                              " +
                "                                             Quantity");
        itemLabel.setFont(Font.font(18));
        GridPane.setConstraints(itemLabel, 0, 0);
        
        // Create the shopping cart's ListView.
        ListView<String> shoppingCartListView = new ListView<>();
        shoppingCartListView.setItems(shoppingCartObservableList);
        
        // Give each item an image.
        shoppingCartListView.setCellFactory(param -> new ListCell<String>() {
            Image image;
            ImageView imageView = new ImageView();
            String formatteditemLine;
            
            @Override
            public void updateItem(String itemLine, boolean empty) {
                super.updateItem(itemLine, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    
                    int digitIndex = 0;
                    
                    for (int i = 0; i < itemLine.length(); i++) {
                        if (Character.isDigit(itemLine.charAt(i))) {
                            digitIndex = i;
                            
                            break;
                        }
                    }
                    
                    switch (itemLine.substring(0, digitIndex)) {
                        case "I Did It Your Way":
                            image = new Image("File:I_Did_It_Your_Way.png");
                            
                            formatteditemLine = itemLine.format("%-41s", itemLine.substring(0, digitIndex)) +
                                    itemLine.substring(digitIndex, itemLine.length());
                            break;
                        case "The History of Scotland":
                            image = new Image("File:The_History_of_Scotland.png");
                            
                            formatteditemLine = itemLine.format("%-37s", itemLine.substring(0, digitIndex)) +
                                    itemLine.substring(digitIndex, itemLine.length());
                            break;
                        case "Learn Calculus in One Day":
                            image = new Image("File:Learn_Calculus_in_One_Day.png");
                            
                            formatteditemLine = itemLine.format("%-35s", itemLine.substring(0, digitIndex)) +
                                    itemLine.substring(digitIndex, itemLine.length());
                            break;
                        case "Feel the Stress":
                            image = new Image("File:Feel_the_Stress.png");
                            
                            formatteditemLine = itemLine.format("%-44s", itemLine.substring(0, digitIndex)) +
                                    itemLine.substring(digitIndex, itemLine.length());
                            break;
                        case "Starting out with Java":
                            image = new Image("File:Starting_out_with_Java.png");
                            
                            formatteditemLine = itemLine.format("%-39s", itemLine.substring(0, digitIndex)) +
                                    itemLine.substring(digitIndex, itemLine.length());
                            break;
                        default:
                            break;
                    }
                    
                    imageView.setImage(image);
                    setGraphic(imageView);
                    setText(formatteditemLine);
                    setFont(Font.font(18));
                }
            }
        });
        
        // Allow multiple selections.
        shoppingCartListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        shoppingCartListView.setPrefWidth(501);
        shoppingCartListView.setPrefHeight(862);
        GridPane.setConstraints(shoppingCartListView, 0, 1);
        
        // Create a Remove Item(s) button.
        Button removeItemsButton = new Button("Remove Item(s)");
        removeItemsButton.setFont(Font.font(24));
        removeItemsButton.setOnAction (clickRemoveItemsButton -> {
            try {
                ObservableList<String> removeItemsObservableList;
                removeItemsObservableList = shoppingCartListView.getSelectionModel().getSelectedItems();
                
                for (int i = 0; i < shoppingCartObservableList.size(); i++) {
                    for (String selecteditemLine: removeItemsObservableList) {
                        int digitIndex = 0;
                        
                        for (int j = 0; j < selecteditemLine.length(); j++) {
                            if (Character.isDigit(selecteditemLine.charAt(j))) {
                                digitIndex = j;
                                break;
                            }
                        }
                        
                        String selectedItemName = selecteditemLine.substring(0, digitIndex);
                        
                        if (shoppingCartObservableList.get(i).contains(selectedItemName)) {
                            shoppingCartObservableList.remove(i);
                            break;
                        }
                    }
                }
                
                // Calculate the shopping cart's costs.
                calculateShoppingCart();
                
            } catch (Exception nullSelectionExceptionShoppingCart) {
                Alert nullSelectionAlert = new Alert(AlertType.ERROR, "Please select at least one item.");
                nullSelectionAlert.showAndWait();
            }
        });
        GridPane.setConstraints(removeItemsButton, 1, 1);
        
        // Beginning of the code for shopping cart cost panes.
        GridPane anchorPaneGridPane = new GridPane();
        AnchorPane shoppingCartAnchorPane = new AnchorPane(anchorPaneGridPane);
        GridPane.setConstraints(shoppingCartAnchorPane, 2, 1);
        
        Label subtotalLabel = new Label("Subtotal:   ");
        subtotalLabel.setFont(Font.font(18));
        GridPane.setConstraints(subtotalLabel, 0, 0);
        
        subtotalAmountLabel.setFont(Font.font(18));
        GridPane.setConstraints(subtotalAmountLabel, 1, 0);
        
        Label taxLabel = new Label("Sales Tax:   ");
        taxLabel.setFont(Font.font(18));
        GridPane.setConstraints(taxLabel, 0, 1);
        
        taxAmountLabel.setFont(Font.font(18));
        GridPane.setConstraints(taxAmountLabel, 1, 1);
        
        Label totalLabel = new Label("Total:   ");
        totalLabel.setFont(Font.font(18));
        GridPane.setConstraints(totalLabel, 0, 2);
        
        totalAmountLabel.setFont(Font.font(18));
        GridPane.setConstraints(totalAmountLabel, 1, 2);
        
        anchorPaneGridPane.getChildren().addAll(subtotalLabel, subtotalAmountLabel, taxLabel, taxAmountLabel, totalLabel, totalAmountLabel);
        
        AnchorPane.setTopAnchor(anchorPaneGridPane, 10.0);
        AnchorPane.setRightAnchor(anchorPaneGridPane, 10.0);
        AnchorPane.setBottomAnchor(anchorPaneGridPane, 10.0);
        AnchorPane.setLeftAnchor(anchorPaneGridPane, 10.0);
        // End of the code for shopping cart cost panes.
        
        Label noteLabel = new Label("Note: Ctrl + Left-Click or Shift + Left-Click to select multiple books.");
        noteLabel.setFont(Font.font(18));
        noteLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(noteLabel, 0, 2);
        
        shoppingCartGridPane.getChildren().addAll(itemLabel, shoppingCartListView, removeItemsButton, shoppingCartAnchorPane, noteLabel);
        
        return shoppingCartGridPane;
    }
    
    // Top layout of the checkout scene.
    public HBox addcheckoutTopLayout() {
        HBox checkoutTopLayoutHBox = new HBox();
        checkoutTopLayoutHBox.setPadding(new Insets(10, 10, 10, 10));
        checkoutTopLayoutHBox.setSpacing(10);
        checkoutTopLayoutHBox.setStyle("-fx-background-color: #444444;");

        // Add a logo to the top layout.
        Image logo = new Image("File:Logo.png");
        ImageView imageView = new ImageView(logo);
        
        Button backToStoreButton = new Button("Back to Store");
        backToStoreButton.setStyle("-fx-font-size:23");
        backToStoreButton.setOnAction (clickBackToStoreButton -> window.setScene(storefrontScene));
        
        StackPane checkoutStackPane = new StackPane();        
        
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setStyle("-fx-font-size:23");
        viewCartButton.setOnAction (clickViewCartButton -> window.setScene(shoppingCartScene));
        
        checkoutStackPane.getChildren().add(viewCartButton);
        checkoutStackPane.setAlignment(Pos.CENTER_RIGHT);
        checkoutTopLayoutHBox.getChildren().addAll(imageView, backToStoreButton, checkoutStackPane);

        HBox.setHgrow(checkoutStackPane, Priority.ALWAYS);
        
        return checkoutTopLayoutHBox;
    }
    
    // Center layout of the checkout scene.
    public GridPane addCheckoutCenterLayout() {
        GridPane checkoutGridPane = new GridPane();
        checkoutGridPane.setHgap(10);
        checkoutGridPane.setVgap(10);
        checkoutGridPane.setPadding(new Insets(10, 10, 10, 10));
        checkoutGridPane.setPrefSize(1100, 862);
        
        Label shippingAddressLabel = new Label("Shipping Address");
        GridPane.setConstraints(shippingAddressLabel, 0, 0);
        
        Label nameLabel = new Label("Full Name: ");
        GridPane.setConstraints(nameLabel, 1, 0);
        
        TextField nameTextField = new TextField();
        nameTextField.setEditable(true);
        GridPane.setConstraints(nameTextField, 2, 0);

        Label phoneNumberLabel = new Label("Phone Number: ");
        GridPane.setConstraints(phoneNumberLabel, 1, 1);
        
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.setEditable(true);
        GridPane.setConstraints(phoneNumberTextField, 2, 1);        
        
        Label streetLabel = new Label("Street: ");
        GridPane.setConstraints(streetLabel, 1, 2);
        
        TextField streetTextField1 = new TextField();
        streetTextField1.setEditable(true);
        streetTextField1.setText("Street Address or P.O. Box");
        GridPane.setConstraints(streetTextField1, 2, 2);
        
        TextField streetTextField2 = new TextField();
        streetTextField2.setEditable(true);
        streetTextField2.setText("Apt., suite, unit, building, floor, etc.");
        GridPane.setConstraints(streetTextField2, 2, 3);

        Label cityLabel = new Label("City: ");
        GridPane.setConstraints(cityLabel, 1, 4);
        
        TextField cityTextField = new TextField();
        cityTextField.setEditable(true);
        GridPane.setConstraints(cityTextField, 2, 4);
        
        Label stateLabel = new Label("State: ");
        GridPane.setConstraints(stateLabel, 3, 4);
        
        ChoiceBox<String> stateChoiceBox = new ChoiceBox<>();
        stateChoiceBox.getItems().addAll("Select", "Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas", "California", "Colorado", "Conneticut",
                "Delaware", "District of Columbia", "Federated States of Micronesia", "Florida", "Georgia", "Guam", "Hawaii", "Idaho", "Illinois",
                "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Marshall Islands", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina",
                "North Dakota", "Northern Mariana Islands", "Ohio", "Oklahoma", "Oregon", "Palau", "Pennsylvania", "Puerto Rico", "Rhode Island",
                "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virgin Islands", "Virgina", "Washingtno", "West Virginia",
                "Wisconsin", "Wyoming", "Armed Forces AA", "Armed Forces AE", "Armed Forces AP");
        stateChoiceBox.setValue("Select");
        GridPane.setConstraints(stateChoiceBox, 4, 4);
        
        Label postalCodeLabel = new Label("Postal Code: ");
        GridPane.setConstraints(postalCodeLabel, 1, 5);
        
        TextField postalCodeTextField = new TextField();
        postalCodeTextField.setEditable(true);
        GridPane.setConstraints(postalCodeTextField, 2, 5);
        
        Label paymentMethodLabel = new Label("Payment Method");
        GridPane.setConstraints(paymentMethodLabel, 0, 6);
        
        Label cardNumberLabel = new Label("Card Number: ");
        GridPane.setConstraints(cardNumberLabel, 1, 6);
        
        TextField cardNumberTextField = new TextField();
        cardNumberTextField.setEditable(true);
        GridPane.setConstraints(cardNumberTextField, 2, 6);
        
        Label nameOnCardLabel = new Label ("Name on Card:");
        GridPane.setConstraints(nameOnCardLabel, 1, 7);
        
        TextField nameOnCardTextField = new TextField();
        nameOnCardTextField.setEditable(true);
        GridPane.setConstraints(nameOnCardTextField, 2, 7);
        
        Label expirationDateLabel = new Label("Expiration Date: ");
        GridPane.setConstraints(expirationDateLabel, 1, 8);
        
        ChoiceBox<Integer> monthChoiceBox = new ChoiceBox<>();
        monthChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        GridPane.setHalignment(monthChoiceBox, HPos.RIGHT);
        GridPane.setConstraints(monthChoiceBox, 2, 8);
        
        ChoiceBox<Integer> yearChoiceBox = new ChoiceBox<>();
        yearChoiceBox.getItems().addAll(2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029,
                2030, 2031, 2032, 2033, 2034, 2035, 2036, 2037, 2038, 2039, 2040, 2041);
        GridPane.setConstraints(yearChoiceBox, 3, 8);
        
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(clickSubmitButton -> {
            
            // Validate inputs in a reasonable manner. International postal codes can include letters.
            try {
                if (nameTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty() || streetTextField1.getText().isEmpty() ||
                        cityTextField.getText().isEmpty() || postalCodeTextField.getText().isEmpty() || cardNumberTextField.getText().isEmpty() ||
                        nameOnCardTextField.getText().isEmpty() || monthChoiceBox.getValue()== null || yearChoiceBox.getValue() == null) {
                    throw new Exception();
                }
                window.setScene(thankYouScene);
            } catch (Exception nullException) {
                Alert nullTextFieldAlert = new Alert(AlertType.ERROR, "Some text fields can't be empty.");
                nullTextFieldAlert.showAndWait();
            }
            
            
            
        });
        GridPane.setConstraints(submitButton, 3, 9);
        
        checkoutGridPane.getChildren().addAll(shippingAddressLabel, nameLabel, nameTextField, phoneNumberLabel, phoneNumberTextField, streetLabel,
                streetTextField1, streetTextField2, cityLabel, cityTextField, stateLabel, stateChoiceBox, postalCodeLabel, postalCodeTextField,
                paymentMethodLabel, cardNumberLabel, cardNumberTextField, nameOnCardLabel, nameOnCardTextField, expirationDateLabel, monthChoiceBox,
                yearChoiceBox, submitButton);
        
        return checkoutGridPane;
    }
    
    // Top layout of the thank you scene.
    public HBox addThankYouTopLayout() {
        HBox thankYouTopLayoutHBox = new HBox();
        thankYouTopLayoutHBox.setPadding(new Insets(10, 10, 10, 10));
        thankYouTopLayoutHBox.setSpacing(10);
        thankYouTopLayoutHBox.setStyle("-fx-background-color: #444444;");

        // Add a logo to the top layout.
        Image logo = new Image("File:Logo.png");
        ImageView imageView = new ImageView(logo);
        
        thankYouTopLayoutHBox.getChildren().add(imageView);
        
        return thankYouTopLayoutHBox;
    }
    
    // Center layout of the thank you screen.
    public VBox addThankYouCenterLayout() {
        VBox thankYouCenterLayoutVBox = new VBox();
        thankYouCenterLayoutVBox.setAlignment(Pos.CENTER);
        thankYouCenterLayoutVBox.setPadding(new Insets(10, 10, 10, 10));
        thankYouCenterLayoutVBox.setPrefSize(1100, 862);
        thankYouCenterLayoutVBox.setSpacing(10);
        
        Label thankYouLabel = new Label("Your order has been submitted.\n                Thank you!");
        thankYouLabel.setFont(Font.font(18));
        
        Button exitButton = new Button("Exit");
        exitButton.setFont(Font.font(18));
        exitButton.setOnAction(clickExitButton -> {
            System.out.println("Your order has been submitted.");
            System.out.println("Thank you!");
            
            window.close();
        });
        
        thankYouCenterLayoutVBox.getChildren().addAll(thankYouLabel, exitButton);
        
        return thankYouCenterLayoutVBox;
    }
    
    // Calculate the cost of items in the shopping cart.
    public void calculateShoppingCart() {
        double subtotal = 0;
        
        for (String shoppingCartItemLine: shoppingCartObservableList) {
            double itemTotal = 0;
            double shoppingCartItemPrice = 0;
            int shoppingCartItemQuantity = 0;
            int shoppingCartItemLineDigitIndex = 0;

            for (int i = 0; i < shoppingCartItemLine.length(); i++) {
                if (Character.isDigit(shoppingCartItemLine.charAt(i))) {
                    shoppingCartItemLineDigitIndex = i;
                    break;
                }
            }            

            String shoppingCartItemName = shoppingCartItemLine.substring(0, shoppingCartItemLineDigitIndex);
            shoppingCartItemQuantity = Integer.parseInt(shoppingCartItemLine.substring(shoppingCartItemLineDigitIndex, shoppingCartItemLine.length()));

            for (String storefrontItemLine: storefrontObservableList) {
                String storefrontItemName = storefrontItemLine.substring(0, storefrontItemLine.indexOf(","));
                double storefrontItemPrice = Double.parseDouble(storefrontItemLine.substring(storefrontItemLine.indexOf(",") + 2,
                        storefrontItemLine.length()));

                if (shoppingCartItemName.equals(storefrontItemName)) {
                    shoppingCartItemPrice = storefrontItemPrice;
                    itemTotal += shoppingCartItemQuantity * shoppingCartItemPrice;
                    break;
                }
            }
            subtotal += itemTotal;
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        
        subtotalProperty.setValue(subtotal);
        taxProperty.setValue(Math.round(subtotalProperty.getValue() * 0.0825 * 100.0) / 100.0);
        totalProperty.setValue(Math.round((subtotalProperty.getValue() + taxProperty.getValue()) * 100.0) / 100.0);
    }
}