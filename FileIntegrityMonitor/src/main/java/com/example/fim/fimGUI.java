package com.example.fim;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.util.List;



class fimGUI implements EventHandler<ActionEvent> {
	
	/*****************************************
	 * 
	 * Attributes
	 * 
	 *****************************************/
	
	// Fields used throughout GUI controller
	private StackPane root;
	private Image bgImage;
	private BackgroundImage background;
	private Font segoeBold;
	private Button backButton;

	
	// For initialize
	private static Stage mainStage;
	private FIMApp fim;
	private Image icon;
	
	
	// For main menu
	private Scene mainMenu;
	private Text menuText;
	private Button viewAndCompBaseline;
	private Button createNewBaseline;
	private Font segoeFont;
	private VBox vbox;
	private VBox menuOptions;
	
	
	// For View and Compare Baseline
	private Button compareButton;
	
	
	// For Create New Baseline
	private Scene createNBL;
	private Button chooseFile;
	private Button chooseDir;
	private VBox createOptions;
	private VBox backBox;
	private Font segoeLabel;
	private VBox mainLayout;
	private Text output;
	
	
	/*
	 * 	Method is used to initialize GUI. Sets up GUI
	 * 	display size, title, icons, and makes it so it is
	 * 	not resizable. Method calls switchScene to display main menu.
	 */
	public void initialize(Stage mainStage, FIMApp fim) throws IOException {
		
		fimGUI.mainStage = mainStage;
		this.fim = fim;
		
		icon = new Image(getClass().getResource("Resources/icon_4.png").toExternalForm());
		mainStage.getIcons().add(icon);
		mainStage.setTitle("File Integrity Monitor");
		mainStage.setResizable(false);
		switchScene(mainMenu());
	
	}
	
	
	/*
	 * 	Method is used to switch between scenes.
	 */
	public static void switchScene(Scene newScene) {
		mainStage.setScene(newScene);
		mainStage.show();
	}
	
	
	/*
	 * 	Method is used to set scene of Main Menu. Displays text prompting user
	 * 	to pick one of two options, "View and Compare Baseline" or "Create New Baseline."
	 * 	Returns scene.
	 */
	public Scene mainMenu() {
		
		bgImage = new Image(getClass().getResource("Resources/background.jpeg").toExternalForm());
		
		
		viewAndCompBaseline = new Button();
		createButton(viewAndCompBaseline, "View and Compare Baseline", "vista-button", 300);
		
		
		createNewBaseline = new Button();
		createButton(createNewBaseline, "Create New Baseline", "vista-button", 300);
		
		// Fonts used for buttons, labels, texts
		segoeFont = Font.loadFont(getClass().getResourceAsStream("Resources/Segoe UI/SEGOEUIL.TTF"), 47);
		segoeBold = Font.loadFont(getClass().getResourceAsStream("Resources/Segoe UI/SEGOEUI.TTF"), 20);
		segoeLabel = Font.loadFont(getClass().getResourceAsStream("Resources/Segoe UI/SEGOEUIL.TTF"), 25);
		
		
		background = new BackgroundImage(
				bgImage,
				BackgroundRepeat.NO_REPEAT, 		// Repeat X
	            BackgroundRepeat.NO_REPEAT, 		// Repeat Y
	            BackgroundPosition.CENTER,  		// Position
	            new BackgroundSize(
	                BackgroundSize.AUTO, 			// Width
	                BackgroundSize.AUTO, 			// Height
	                false,               			// Cover width
	                false,               			// Cover height
	                true,                			// Contain width proportionally
	                true                 			// Contain height proportionally
	            )
	        );
		
		root = new StackPane();
		root.setBackground(new Background (background));
		
		
		menuText = new Text("What would you like to do?");
		menuText.setFont(segoeFont);
		menuText.setFill(Color.WHITE);
		
		
		vbox = new VBox(90);
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPadding(new Insets(140, 0, 0, 0));
		vbox.getChildren().add(menuText);
		
		
		menuOptions = new VBox(40);
		menuOptions.setAlignment(Pos.CENTER);
		menuOptions.getChildren().addAll(viewAndCompBaseline, createNewBaseline);
		
		vbox.getChildren().add(menuOptions);
		
		root.getChildren().add(vbox);
		
		mainMenu = new Scene(root, 1600, 700);
		// fetching CSS resource
		mainMenu.getStylesheets().add(getClass().getResource("Resources/vista-button.css").toExternalForm());
		
		
		viewAndCompBaseline.setStyle("-fx-border-color: #1ab3ff;");
		viewAndCompBaseline.setFont(segoeBold);
		createNewBaseline.setFont(segoeBold);
		
		return mainMenu;
		
	}
	
	
	/*
	 * 	Method is used to set scene for the "View and Compare Baseline"
	 * 	page. Displays table of hashed files with file path, hash value, 
	 * 	last modified date, and last monitored date. Displays "Compare" 
	 * 	and "Back" buttons. Returns scene.	
	 */
	public Scene viewAndCompareBaseline() {
		
		root = new StackPane();
		root.setBackground(new Background(background));
		
		
		backButton = new Button();
		createButton(backButton, "Back", "vista-button", 200);
		
		compareButton = new Button();
		createButton(compareButton, "Compare", "vista-button", 200);
		
		Label compLabel = new Label();
		compLabel.setText("To test the integrity of a file, highlight (left-click) a file and then hit the \"Compare\" button");
		compLabel.setFont(segoeLabel);
		compLabel.setTextFill(Color.WHITE);
		
		VBox compBox = new VBox(20);
		compBox.setAlignment(Pos.CENTER);
		compBox.getChildren().addAll(compareButton, compLabel);
		
		// List created holding hashedFiles to display hashed files list in table
		List<FIMApp.hashedFile> hashedFilesList = FIMApp.db.getHashedFiles();
		
		
		TableView<FIMApp.hashedFile> hashedFilesTable = new TableView<>();
		
		// Columns for table
		TableColumn<FIMApp.hashedFile, String> columnFilePath = new TableColumn<>("File Path");
		columnFilePath.setCellValueFactory(new PropertyValueFactory<>("filePath"));
		
		TableColumn<FIMApp.hashedFile, String> columnHashValue = new TableColumn<>("Hash Value");
		columnHashValue.setCellValueFactory(new PropertyValueFactory<>("hashValue"));
		
		TableColumn<FIMApp.hashedFile, String> columnLastMod = new TableColumn<>("Last Modified");
		columnLastMod.setCellValueFactory(new PropertyValueFactory<>("lastMod"));
		
		TableColumn<FIMApp.hashedFile, String> columnMonitorTimeStamp = new TableColumn<>("Last Monitored");
		columnMonitorTimeStamp.setCellValueFactory(new PropertyValueFactory<>("monitorTimeStamp"));
		
		
		List<TableColumn<FIMApp.hashedFile, ?>> columns = List.of(
				columnFilePath, columnHashValue, columnLastMod, columnMonitorTimeStamp);
				
		hashedFilesTable.getColumns().addAll(columns);
		
		// Making list observable
		ObservableList<FIMApp.hashedFile> fileList = FXCollections.observableArrayList(hashedFilesList);
		
		hashedFilesTable.setItems(fileList);
		
		VBox compAndTable = new VBox(10);
		compAndTable.setAlignment(Pos.CENTER);
		compAndTable.getChildren().addAll(compBox, hashedFilesTable);
		
		
		VBox viewAndCompLayout = new VBox(40);
		viewAndCompLayout.setAlignment(Pos.CENTER);
		viewAndCompLayout.getChildren().addAll(compAndTable, backButton);
		
		
		root.getChildren().add(viewAndCompLayout);
		
		Scene viewScene = new Scene(root, 1600, 700);
		// fetching CSS resource
		viewScene.getStylesheets().add(getClass().getResource("Resources/vista-button.css").toExternalForm());
		
		backButton.setFont(segoeBold);
		compareButton.setFont(segoeBold);
		compareButton.setStyle("-fx-border-color: #1ab3ff;");
		
		// comparing hashed files
		compareButton.setOnAction( e -> {
			
			FIMApp.hashedFile selectedFile = hashedFilesTable.getSelectionModel().getSelectedItem();
			
			if (selectedFile != null) {
				String filePath = selectedFile.getFilePath();
				String storedHash = selectedFile.getHashValue();
				
				// pop-ups for comparison results
				try {
					String currentHash = selectedFile.getComparisonHash(new File(filePath));
					
					if (storedHash.equals(currentHash)) {
						showPopup("Integrity Check",
								  "Success: The file's integrity is intact.\n\nFile: " + filePath,
								  Alert.AlertType.INFORMATION);
					} else {
						showPopup("Integrity Check",
								  "Warning: The file has been modified!\n\nFile: " + filePath,
								  Alert.AlertType.WARNING);
					}
				} catch (Exception ee) {
					showPopup("Error",
							  "Error: Could not compute hash for the file.\n\nDetails: " + ee.getMessage(),
							  Alert.AlertType.ERROR);
				}
				
			} else {
				showPopup("No selection",
						  "Please select a file entry to compare.",
						   Alert.AlertType.WARNING);
			}
		});
		
		return viewScene;
		
	}
	
	
	/*
	 * 	Method shows a pop-up when a baseline is compared.
	 * 	Pop-up will show that integrity of a file has been maintained,
	 * 	has been corrupted, or no comparison can be made.
	 */
	private void showPopup(String title, String message, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	
	/*
	 * 	Method is used to create a scene that displays the 
	 * 	"Create New Baseline" page. Displays buttons for the user
	 * 	so they can create a hash for whichever directory or file they choose.
	 * 	Returns the scene.
	 */
	public Scene createNewBaseline() {
		
		root = new StackPane();
		root.setBackground(new Background(background));
		
		
		chooseFile = new Button();
		createButton(chooseFile, "Choose a File", "vista-button", 300);
		
		chooseDir = new Button();
		createButton(chooseDir, "Choose a Directory", "vista-button", 300);
		
		output = new Text();
	    output.setText("No directory or file selected.");
	    
	    
		backButton = new Button();
		createButton(backButton, "Back", "vista-button", 150);
		
		
		backBox = new VBox();
		backBox.setAlignment(Pos.BOTTOM_LEFT);
		backBox.setPadding(new Insets(0, 0, 50, 50));
		backBox.getChildren().add(backButton);
		
		
		createOptions = new VBox(40);
		createOptions.setAlignment(Pos.CENTER);
		createOptions.setPadding(new Insets(250, 0, 175, 0));
		createOptions.getChildren().addAll(chooseFile, chooseDir, output);
		
		mainLayout = new VBox();
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.getChildren().addAll(createOptions, backBox);
		
		
		root.getChildren().add(mainLayout);
		
		
		createNBL =  new Scene(root, 1600, 700);
		// fetching CSS resource
		createNBL.getStylesheets().add(getClass().getResource("Resources/vista-button.css").toExternalForm());
		
		chooseFile.setFont(segoeBold);
		chooseFile.setStyle("-fx-border-color: #1ab3ff;");
		
		chooseDir.setFont(segoeBold);
		chooseDir.setStyle("-fx-border-color: #1ab3ff;");
		
		output.setFont(segoeLabel);
	    output.setFill(Color.WHITE);
		
		chooseFile.setOnAction(e -> {
			chooseFile();
		});
		
		chooseDir.setOnAction( e -> {
			chooseDirectory();
		});
		
		backButton.setFont(segoeBold);
		
		
		return createNBL;
		
	}
	
	
	/*
	 * 	Method is used to give user the ability to select a file to hash.
	 */
	private void chooseFile() {
		
		FileChooser fileChooser = new FileChooser();
		// select a file (plan to make selection of multiple files available in later rendition of program, hence the list)
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
		
		FIMApp.hashedFile fileHashed = new FIMApp.hashedFile();
		
		
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			
			for (File file : selectedFiles) {
				fileHashed.accessHashFile(file);						// hash file method called with file as argument
			}
			output.setText("Hash created, new baseline for file");
		}
		
		else {
			output.setText("No file selected.");
		}	
	}
	
	
	/*
	 * 	Method is used to give user the ability to select a directory whose
	 * 	files it will hash and create a baseline for.
	 */
	private void chooseDirectory() {

	    	DirectoryChooser dirChooser = new DirectoryChooser();
	    	// select a directory
	    	File selectedDir = dirChooser.showDialog(null);
	    	
	    	FIMApp.hashedFile fileHashed = new FIMApp.hashedFile();
	    	
	    	if (selectedDir != null) {
	    		File[] filesInDir = selectedDir.listFiles();
	    		if (filesInDir != null) {
	    			for (File file : filesInDir) {
	    				if (file.isFile()) {							// make sure file is a file and not another directory
	    					fileHashed.accessHashFile(file);			// hash file method called with file as argument
	    				}
	    			}
	    		}
	    		output.setText("Hash created, new baseline for: " + selectedDir.getAbsolutePath());
	    	}
	    	
	    	else {
	    		output.setText("No directory selected.");
	    	}
	}
	
	
	/*
	 * 	Method is used to create a button.
	 * 	Sets buttons title, style, width, and action.
	 * 	Returns button.
	 */
	public Button createButton(Button newButton, String title, String styleClass, int minWidth) {
		
		newButton.setText(title);
		newButton.getStyleClass().add(styleClass);
		newButton.setMinWidth(minWidth);
		newButton.setOnAction(this);
		
		return newButton;
		
	}
	
	/*
	 * 	Method handles action events (button clicks),
	 * 	switches scenes based on what button is clicked.
	 */
	@Override
	public void handle(ActionEvent event) {
		
		
		if (event.getSource() == viewAndCompBaseline) {
			switchScene(viewAndCompareBaseline());
		}
		
		if (event.getSource() == createNewBaseline) {
			switchScene(createNewBaseline());
		}
		
		if (event.getSource() == backButton) {
			switchScene(mainMenu);
		}
		
	}
}