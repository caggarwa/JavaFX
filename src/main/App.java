package main;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utility.DateUtil;


public class App extends Application {  

	private static App mInstance;
	
	DataAnalyzer SummaryMem_dataAnalyzer ;
	DataAnalyzer DetailMem_dataAnalyzer ;
	DataAnalyzer DetailCust_dataAnalyzer ;
	

	public static App getInstance() {
		return mInstance;
	}


	static SummaryTask summaryTask;
	static DetailTask detailTask;
	static CustDetailTask cusDetailTask;

	@Override 
	public void start(Stage stage)  {    

		TabPane tabs = new TabPane();
		Tab tabSummary = new Tab();
		tabSummary.setText("Summary Membership Report");
		tabSummary.setContent(getSummaryGridPane(stage));

		Tab tabDetail = new Tab();
		tabDetail.setText("Detail Membership Report");
		tabDetail.setContent(getDetailGridPane(stage));
		
		Tab tabCustomerDetail = new Tab();
		tabCustomerDetail.setText("Detail Customer Report");
		tabCustomerDetail.setContent(getCustomerDetailGridPane(stage));

		tabs.getTabs().addAll(tabCustomerDetail,tabSummary, tabDetail);

		//Creating a scene object 
		Scene scene = new Scene(tabs); 
		InputStream IO=null;
		try {
			IO= new FileInputStream(new File("OptumLogo.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Setting title to the Stage 
		stage.setTitle("Customer-Membership Analyser"); 
		stage.getIcons().add(new Image(IO)); 
		//Adding scene to the stage 
		stage.setScene(scene);  
		
		//Displaying the contents of the stage 
		stage.show(); 
	}  

	private static void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
		directoryChooser.setTitle("Select any Directory");
		// Set Initial Directory
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	public static void main(String args[]){           
		launch(args);      
	} 

	
public GridPane getCustomerDetailGridPane(Stage stage) {
		
		//Create an object for variables used for Summary detail
		Variables var= new Variables();
		
		//Label for Env name 
		Text EnvLabel = new Text("Environment"); 

		//Toggle group of radio buttons       
		ToggleGroup groupEnv = new ToggleGroup(); 
		
		RadioButton SIT1Radio = new RadioButton("SIT1"); 
		SIT1Radio.setToggleGroup(groupEnv); 
		SIT1Radio.setUserData("SIT1");

		RadioButton SIT2Radio = new RadioButton("SIT2"); 
		SIT2Radio.setUserData("SIT2");
		SIT2Radio.setToggleGroup(groupEnv); 

		RadioButton UATRadio = new RadioButton("UAT"); 
		UATRadio.setUserData("UAT");
		UATRadio.setToggleGroup(groupEnv); 

		RadioButton UT1Radio = new RadioButton("UT1"); 
		UT1Radio.setUserData("UT1");
		UT1Radio.setToggleGroup(groupEnv); 

		RadioButton UT2Radio = new RadioButton("UT2"); 
		UT2Radio.setUserData("UT2");
		UT2Radio.setToggleGroup(groupEnv); 

		RadioButton UT3Radio = new RadioButton("UT3");
		UT3Radio.setUserData("UT3");
		UT3Radio.setToggleGroup(groupEnv); 

		groupEnv.selectToggle(SIT1Radio);
		var.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());

		HBox hbRadio = new HBox(15);
		hbRadio.setAlignment(Pos.CENTER_LEFT);
		hbRadio.getChildren().add(SIT1Radio);
		hbRadio.getChildren().add(SIT2Radio);
		hbRadio.getChildren().add(UATRadio);
		hbRadio.getChildren().add(UT1Radio);
		hbRadio.getChildren().add(UT2Radio);
		hbRadio.getChildren().add(UT3Radio);
		
		//This will return the value updated at run time imediately whenever any eradio button is changed
		groupEnv.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupEnv.getSelectedToggle() != null) {
//					Environment=groupEnv.getSelectedToggle().getUserData().toString();
					var.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());
					System.out.println(var.getEnvironment());

				}
			} 
		});

		//Label for User name  
		Text DBnameLabel = new Text("DB UserName"); 

		//Text field for name 
		TextField DBnameText = new TextField(); 

		//Label for User Password 
		Text DBPasswordLabel = new Text("DB Password"); 

		//Text field for Password 
		PasswordField  DBPasswordText = new PasswordField (); 

		// Connect to DB Button
		Button buttonDBConnect = new Button("Connect To Oracle DataBase");
		buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
		buttonDBConnect.setStyle("-fx-font: 20px Serif;");
		buttonDBConnect.setStyle("-fx-border-width: 5px;");

		final Label DBStatuslabel = new Label();

		HBox hbDBConnection = new HBox(15);
		hbDBConnection.setAlignment(Pos.CENTER);
		hbDBConnection.getChildren().add(buttonDBConnect);
		hbDBConnection.getChildren().add(DBStatuslabel);

		//Label for Generate Report 
		Button buttonRun = new Button("Generate Report"); 
		buttonRun.setMinHeight(40);
		buttonRun.setAlignment(Pos.CENTER_LEFT);
		buttonRun.setDisable(true);

		buttonDBConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				buttonDBConnect.setStyle("-fx-background-color: DARKORANGE");
//				DBnameText.setText("UHG_001303953");
//				DBPasswordText.setText("Jan%019J");
				final String DB_UserName=DBnameText.getText();
				final String DBPassword=DBPasswordText.getText();
				
				DetailCust_dataAnalyzer=new DataAnalyzer(var.getEnvironment(),DB_UserName, DBPassword);
				DBStatuslabel.setText(DetailCust_dataAnalyzer.connectToDB());
				DBStatuslabel.setTextFill(Color.CORAL);
				DBStatuslabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
				buttonDBConnect.setDisable(true);
				buttonRun.setDisable(false);
			}
		});


		//Label for LOB name 
		Text LOBLabel = new Text("LOB"); 

		//Toggle group of radio buttons for selecting LOB      
		ToggleGroup groupLOB = new ToggleGroup(); 
		
		RadioButton ACRadio = new RadioButton("ACIS"); 
		ACRadio.setToggleGroup(groupLOB); 
		ACRadio.setUserData("ACIS");

		RadioButton PRRadio = new RadioButton("PRIME"); 
		PRRadio.setUserData("PRIME");
		PRRadio.setToggleGroup(groupLOB); 

		RadioButton CRRadio = new RadioButton("CIRRUS"); 
		CRRadio.setUserData("CIRRUS");
		CRRadio.setToggleGroup(groupLOB); 

		//Select LOB as CDB by Default
		groupLOB.selectToggle(ACRadio);
//		LOB=groupLOB.getSelectedToggle().getUserData().toString();
		var.setLOB(groupLOB.getSelectedToggle().getUserData().toString());
		

		HBox hb_LOB_Radio = new HBox(15);
		hb_LOB_Radio.setAlignment(Pos.CENTER_LEFT);
		hb_LOB_Radio.getChildren().add(ACRadio);
		hb_LOB_Radio.getChildren().add(PRRadio);
		hb_LOB_Radio.getChildren().add(CRRadio);


		groupLOB.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupLOB.getSelectedToggle() != null) {
					var.setLOB(groupLOB.getSelectedToggle().getUserData().toString());
					System.out.println(var.getLOB());
				}

			} 
		});

		//Label for Creation Date  
		Text DateLabel = new Text("ILM Date Range");
		
		Text DateLabelFrom = new Text("From: ");
		Text DateLabelTo = new Text("To: ");

		//date picker to choose date 
		DatePicker fromDatePicker = new DatePicker(LocalDate.now(ZoneId.systemDefault())); 
		
		//Read the default value of From_DT
		LocalDate From_localDate = fromDatePicker.getValue();
		Calendar From_c =  Calendar.getInstance();
		From_c.set(From_localDate.getYear(), From_localDate.getMonthValue()-1, From_localDate.getDayOfMonth());
		Date From_date = From_c.getTime();
		var.setFromDate(DateUtil.getDate("dd-MMM-yy", From_date).toUpperCase());
		
		
		DatePicker toDatePicker = new DatePicker(LocalDate.now(ZoneId.systemDefault()));
		
		//Read the Default value for To_Date
		LocalDate localDate = toDatePicker.getValue();
		Calendar c =  Calendar.getInstance();
		c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
		Date date = c.getTime();
		var.setToDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());

		fromDatePicker.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				LocalDate localDate = fromDatePicker.getValue();
				Calendar c =  Calendar.getInstance();
				c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
				Date date = c.getTime();
				var.setFromDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());
				System.out.println(var.getFromDate());
			}
		});
		
		toDatePicker.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				LocalDate localDate = toDatePicker.getValue();
				Calendar c =  Calendar.getInstance();
				c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
				Date date = c.getTime();
				var.setToDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());
				System.out.println(var.getToDate());
			}
		});

		
		HBox hb_Date = new HBox(15);
		hb_Date.setAlignment(Pos.CENTER_LEFT);
		hb_Date.getChildren().add(DateLabelFrom);
		hb_Date.getChildren().add(fromDatePicker);
		hb_Date.getChildren().add(DateLabelTo);
		hb_Date.getChildren().add(toDatePicker);
		
		//Label for location/Directory 
		Text locationLabel = new Text("Report Location"); 

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		configuringDirectoryChooser(directoryChooser);

		TextArea DirectorytextArea = new TextArea();
		DirectorytextArea.setMaxHeight(3);
		DirectorytextArea.setMaxWidth(550);

		Button ReportPathbutton = new Button("Chooser Report Path");
		Button buttonclearAll = new Button("Clear All"); 
		Button buttonClearDate= new Button("Run For Next Date");

		ReportPathbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				File dir = directoryChooser.showDialog(stage);

				if (dir != null) {
					DirectorytextArea.setText(dir.getAbsolutePath());
					var.setReportPath(dir.getAbsolutePath());
//					ReportPath=dir.getAbsolutePath();

				} else {
					DirectorytextArea.setText(null);
				}
			}
		});


		//Progress Label
		final Label label = new Label("Analyzing Results:");
		final ProgressBar progressBar = new ProgressBar(0);
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);


		final Hyperlink statusLabel = new Hyperlink();
		statusLabel.setMinWidth(250);
		statusLabel.setTextFill(Color.BLUE);



		buttonRun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				//if ReportPath is empty generate pop up error
				if (var.getReportPath()!=null && !var.getReportPath().isEmpty()){

					buttonRun.setDisable(true);

					progressBar.setProgress(0);
					progressIndicator.setProgress(0);

					// Create a Task.
					cusDetailTask = new CustDetailTask(DetailCust_dataAnalyzer,var.getFromDate(),var.getToDate(),var.getReportPath(),var.getLOB());

					// Unbind progress property
					progressBar.progressProperty().unbind();

					// Bind progress property
					progressBar.progressProperty().bind(cusDetailTask.progressProperty());

					// UnBind progress property.
					progressIndicator.progressProperty().unbind();

					// Bind progress property.
					progressIndicator.progressProperty().bind(cusDetailTask.progressProperty());

					// Unbind text property for Label.
					statusLabel.textProperty().unbind();

					// Bind the text property of Label
					// with message property of Task
					statusLabel.textProperty().bind(cusDetailTask.messageProperty());

					// When completed tasks
					cusDetailTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //

							new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent t) {
							statusLabel.textProperty().unbind();
							statusLabel.setText(cusDetailTask.getValue());
						}
					});

					// Start the Task.
					new Thread(cusDetailTask).start();


					statusLabel.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Application a = new Application() {

								@Override
								public void start(Stage stage)
								{
								}
							};

							a.getHostServices().showDocument(cusDetailTask.getValue());

						}
					});

				}
				else{
					

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Please select a directiory to store Report!!");
					alert.showAndWait();
				}
			}
		});

		FlowPane root = new FlowPane();
		root.setPadding(new Insets(10));
		root.setHgap(10);
		root.getChildren().addAll(label, progressBar, progressIndicator, //
				statusLabel);
		root.setAlignment(Pos.CENTER);


		//Setting an action for the Clear button
		buttonclearAll.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				groupEnv.selectToggle(SIT1Radio);
				groupLOB.selectToggle(CRRadio);
				DBnameText.clear();
				DBPasswordText.clear();
				fromDatePicker.setValue(LocalDate.now());
				toDatePicker.setValue(LocalDate.now());
				DirectorytextArea.clear();
				buttonRun.setDisable(true);
				DBStatuslabel.setText(null);
				try{
					if (cusDetailTask.isRunning())
						cusDetailTask.cancel(true);
				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				statusLabel.setText(null);
				statusLabel.textProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);
				buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
				buttonDBConnect.setStyle("-fx-font: 20px Serif;");
				buttonDBConnect.setStyle("-fx-border-width: 5px;");
				buttonDBConnect.setDisable(false);

			}
		});


		//Setting an action for the buttonClearCustomerDetail button
		buttonClearDate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				fromDatePicker.setValue(LocalDate.now());
				toDatePicker.setValue(LocalDate.now());
				buttonRun.setDisable(false);
				groupLOB.selectToggle(ACRadio);

				try{
					if (cusDetailTask.isRunning())
						cusDetailTask.cancel(true);
				}
				catch(Exception ex){

				}
				try{


					statusLabel.setText(null);
					statusLabel.textProperty().unbind();

				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);

			}
		});




		//Creating a Grid Pane 
		GridPane gridPaneCustDetail = new GridPane();    

		//Setting size for the pane 
		gridPaneCustDetail.setMinSize(500, 500); 

		//Setting the padding    
		gridPaneCustDetail.setPadding(new Insets(10, 10, 10, 10));  

		//Setting the vertical and horizontal gaps between the columns 
		gridPaneCustDetail.setVgap(5); 
		gridPaneCustDetail.setHgap(5);       

		//Setting the Grid alignment 
		gridPaneCustDetail.setAlignment(Pos.CENTER); 

		//Arranging all the nodes in the grid 
		gridPaneCustDetail.add(EnvLabel, 0, 0); 
		gridPaneCustDetail.add(hbRadio, 1, 0);       

		gridPaneCustDetail.add(DBnameLabel, 0, 2); 
		gridPaneCustDetail.add(DBnameText, 1, 2); 

		gridPaneCustDetail.add(DBPasswordLabel, 0, 3); 
		gridPaneCustDetail.add(DBPasswordText, 1, 3); 

		gridPaneCustDetail.add(hbDBConnection, 1, 5);

		gridPaneCustDetail.add(LOBLabel, 0, 9); 
		gridPaneCustDetail.add(hb_LOB_Radio, 1, 9);

		gridPaneCustDetail.add(DateLabel, 0, 11);       
		gridPaneCustDetail.add(hb_Date, 1, 12); 

		gridPaneCustDetail.add(locationLabel, 0, 14); 
		gridPaneCustDetail.add(DirectorytextArea, 1, 14); 
		gridPaneCustDetail.add(ReportPathbutton, 1, 15); 

		buttonRun.setAlignment(Pos.BASELINE_LEFT);
		gridPaneCustDetail.add(buttonRun, 2, 16); 

		gridPaneCustDetail.add(root,1,18);

		gridPaneCustDetail.add(buttonclearAll, 0, 18); 
		gridPaneCustDetail.add(buttonClearDate, 0, 19);


		
		//Styling nodes   
		buttonRun.setStyle(
				"-fx-background-color: DARKORANGE;"); 

		EnvLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		EnvLabel.setFill(Color.SLATEGRAY); 
		DateLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DateLabel.setFill(Color.SLATEGRAY); 
		DBnameLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBnameLabel.setFill(Color.SLATEGRAY); 
		DBPasswordLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBPasswordLabel.setFill(Color.SLATEGRAY); 
		LOBLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		LOBLabel.setFill(Color.SLATEGRAY);
		locationLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		locationLabel.setFill(Color.SLATEGRAY); 

		//Setting the back ground color 
		gridPaneCustDetail.setStyle("-fx-background-color: BEIGE;");   


		return gridPaneCustDetail;
	}

	
	
	public GridPane getSummaryGridPane(Stage stage) {
		
		//Create an object for variables used for Summary detail
		Variables var= new Variables();
		
		//Label for Env name 
		Text EnvLabel = new Text("Environment"); 

		//Toggle group of radio buttons       
		ToggleGroup groupEnv = new ToggleGroup(); 
		
		RadioButton SIT1Radio = new RadioButton("SIT1"); 
		SIT1Radio.setToggleGroup(groupEnv); 
		SIT1Radio.setUserData("SIT1");

		RadioButton SIT2Radio = new RadioButton("SIT2"); 
		SIT2Radio.setUserData("SIT2");
		SIT2Radio.setToggleGroup(groupEnv); 

		RadioButton UATRadio = new RadioButton("UAT"); 
		UATRadio.setUserData("UAT");
		UATRadio.setToggleGroup(groupEnv); 

		RadioButton UT1Radio = new RadioButton("UT1"); 
		UT1Radio.setUserData("UT1");
		UT1Radio.setToggleGroup(groupEnv); 

		RadioButton UT2Radio = new RadioButton("UT2"); 
		UT2Radio.setUserData("UT2");
		UT2Radio.setToggleGroup(groupEnv); 

		RadioButton UT3Radio = new RadioButton("UT3");
		UT3Radio.setUserData("UT3");
		UT3Radio.setToggleGroup(groupEnv); 

		groupEnv.selectToggle(SIT1Radio);
		var.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());
		
		HBox hbRadio = new HBox(15);
		hbRadio.setAlignment(Pos.CENTER_LEFT);
		hbRadio.getChildren().add(SIT1Radio);
		hbRadio.getChildren().add(SIT2Radio);
		hbRadio.getChildren().add(UATRadio);
		hbRadio.getChildren().add(UT1Radio);
		hbRadio.getChildren().add(UT2Radio);
		hbRadio.getChildren().add(UT3Radio);
		
		//This will return the value updated at run time imediately whenever any eradio button is changed
		groupEnv.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupEnv.getSelectedToggle() != null) {
//					Environment=groupEnv.getSelectedToggle().getUserData().toString();
					var.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());
					System.out.println(var.getEnvironment());

				}
			} 
		});

		//Label for User name  
		Text DBnameLabel = new Text("DB UserName"); 

		//Text field for name 
		TextField DBnameText = new TextField(); 

		//Label for User Password 
		Text DBPasswordLabel = new Text("DB Password"); 

		//Text field for Password 
		PasswordField  DBPasswordText = new PasswordField (); 

		// Connect to DB Button
		Button buttonDBConnect = new Button("Connect To Oracle DataBase");
		buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
		buttonDBConnect.setStyle("-fx-font: 20px Serif;");
		buttonDBConnect.setStyle("-fx-border-width: 5px;");

		final Label DBStatuslabel = new Label();

		HBox hbDBConnection = new HBox(15);
		hbDBConnection.setAlignment(Pos.CENTER);
		hbDBConnection.getChildren().add(buttonDBConnect);
		hbDBConnection.getChildren().add(DBStatuslabel);

		//Label for Generate Report 
		Button buttonRun = new Button("Generate Report"); 
		buttonRun.setMinHeight(40);
		buttonRun.setAlignment(Pos.CENTER_LEFT);
		buttonRun.setDisable(true);

		buttonDBConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				buttonDBConnect.setStyle("-fx-background-color: DARKORANGE");
//				DBnameText.setText("UHG_001303953");
//				DBPasswordText.setText("Jan%019J");
				final String DB_UserName=DBnameText.getText();
				final String DBPassword=DBPasswordText.getText();
				
				SummaryMem_dataAnalyzer=new DataAnalyzer(var.getEnvironment(),DB_UserName, DBPassword);
				DBStatuslabel.setText(SummaryMem_dataAnalyzer.connectToDB());
				DBStatuslabel.setTextFill(Color.CORAL);
				DBStatuslabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
				buttonDBConnect.setDisable(true);
				buttonRun.setDisable(false);
			}
		});


		//Label for LOB name 
		Text LOBLabel = new Text("LOB"); 

		//Toggle group of radio buttons for selecting LOB      
		ToggleGroup groupLOB = new ToggleGroup(); 
		
		RadioButton CDBRadio = new RadioButton("CDB"); 
		CDBRadio.setToggleGroup(groupLOB); 
		CDBRadio.setUserData("CDB");

		RadioButton PRRadio = new RadioButton("PRIME"); 
		PRRadio.setUserData("PRIME");
		PRRadio.setToggleGroup(groupLOB); 

		RadioButton CRRadio = new RadioButton("CIRRUS"); 
		CRRadio.setUserData("CIRRUS");
		CRRadio.setToggleGroup(groupLOB); 

		//Select LOB as CDB by Default
		groupLOB.selectToggle(CDBRadio);
		var.setLOB(groupLOB.getSelectedToggle().getUserData().toString());
		

		HBox hb_LOB_Radio = new HBox(15);
		hb_LOB_Radio.setAlignment(Pos.CENTER_LEFT);
		hb_LOB_Radio.getChildren().add(CDBRadio);
		hb_LOB_Radio.getChildren().add(PRRadio);
		hb_LOB_Radio.getChildren().add(CRRadio);
		
		groupLOB.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupLOB.getSelectedToggle() != null) {
					var.setLOB(groupLOB.getSelectedToggle().getUserData().toString());
					System.out.println(var.getLOB());
				}

			} 
		});

		//Label for Creation Date  
		Text DateLabel = new Text("ILM Date Range");
		
		Text DateLabelFrom = new Text("From: ");
		Text DateLabelTo = new Text("To: ");

		//date picker to choose date 
		DatePicker fromDatePicker = new DatePicker(LocalDate.now(ZoneId.systemDefault())); 
		
		//read the default selected value of From Date
		LocalDate FromlocalDate = fromDatePicker.getValue();
		Calendar from_c =  Calendar.getInstance();
		from_c.set(FromlocalDate.getYear(), FromlocalDate.getMonthValue()-1, FromlocalDate.getDayOfMonth());
		Date from_date = from_c.getTime();
		var.setFromDate(DateUtil.getDate("dd-MMM-yy", from_date).toUpperCase());
		System.out.println(var.getFromDate());
		
		//Create Calendar for To Date
		DatePicker toDatePicker = new DatePicker(LocalDate.now(ZoneId.systemDefault()));
		
		//read the default selected value of To Date
		LocalDate localDate = toDatePicker.getValue();
		Calendar c =  Calendar.getInstance();
		c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
		Date date = c.getTime();
		var.setToDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());
		System.out.println(var.getToDate());
		
		
		
		fromDatePicker.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				LocalDate localDate = fromDatePicker.getValue();
				Calendar c =  Calendar.getInstance();
				c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
				Date date = c.getTime();
//				FromDate=DateUtil.getDate("dd-MMM-yy", date).toUpperCase();
				var.setFromDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());
				System.out.println(var.getFromDate());
			}
		});
		
		toDatePicker.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				LocalDate localDate = toDatePicker.getValue();
				Calendar c =  Calendar.getInstance();
				c.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
				Date date = c.getTime();
//				ToDate=DateUtil.getDate("dd-MMM-yy", date).toUpperCase();
				var.setToDate(DateUtil.getDate("dd-MMM-yy", date).toUpperCase());
				System.out.println(var.getToDate());
			}
		});


		HBox hb_Date = new HBox(15);
		hb_Date.setAlignment(Pos.CENTER_LEFT);
		hb_Date.getChildren().add(DateLabelFrom);
		hb_Date.getChildren().add(fromDatePicker);
		hb_Date.getChildren().add(DateLabelTo);
		hb_Date.getChildren().add(toDatePicker);
		
		//Label for location/Directory 
		Text locationLabel = new Text("Report Location"); 

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		configuringDirectoryChooser(directoryChooser);

		TextArea DirectorytextArea = new TextArea();
		DirectorytextArea.setMaxHeight(3);
		DirectorytextArea.setMaxWidth(550);

		Button ReportPathbutton = new Button("Chooser Report Path");
		Button buttonclearAll = new Button("Clear All"); 
		Button buttonClearDate= new Button("Run For Next Date");



		ReportPathbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				File dir = directoryChooser.showDialog(stage);

				if (dir != null) {
					DirectorytextArea.setText(dir.getAbsolutePath());
					var.setReportPath(dir.getAbsolutePath());
//					ReportPath=dir.getAbsolutePath();

				} else {
					DirectorytextArea.setText(null);
				}
			}
		});


		//Progress Label
		final Label label = new Label("Analyzing Results:");
		final ProgressBar progressBar = new ProgressBar(0);
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);


		final Hyperlink statusLabel = new Hyperlink();
		statusLabel.setMinWidth(250);
		statusLabel.setTextFill(Color.BLUE);



		buttonRun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				//if ReportPath is empty generate pop up error
				if (var.getReportPath()!=null && !var.getReportPath().isEmpty()){

					buttonRun.setDisable(true);

					progressBar.setProgress(0);
					progressIndicator.setProgress(0);

					// Create a Task.
					summaryTask = new SummaryTask(SummaryMem_dataAnalyzer,var.getFromDate(),var.getToDate(),var.getReportPath(),var.getLOB());

					// Unbind progress property
					progressBar.progressProperty().unbind();

					// Bind progress property
					progressBar.progressProperty().bind(summaryTask.progressProperty());

					// UnBind progress property.
					progressIndicator.progressProperty().unbind();

					// Bind progress property.
					progressIndicator.progressProperty().bind(summaryTask.progressProperty());

					// Unbind text property for Label.
					statusLabel.textProperty().unbind();

					// Bind the text property of Label
					// with message property of Task
					statusLabel.textProperty().bind(summaryTask.messageProperty());

					// When completed tasks
					summaryTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //

							new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent t) {
							statusLabel.textProperty().unbind();
							statusLabel.setText(summaryTask.getValue());
						}
					});

					// Start the Task.
					new Thread(summaryTask).start();


					statusLabel.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Application a = new Application() {

								@Override
								public void start(Stage stage)
								{
								}
							};

							a.getHostServices().showDocument(summaryTask.getValue());

						}
					});

				}
				else{
					

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Please select a directiory to store Report!!");
					alert.showAndWait();
				}
			}
		});

		FlowPane root = new FlowPane();
		root.setPadding(new Insets(10));
		root.setHgap(10);
		root.getChildren().addAll(label, progressBar, progressIndicator, //
				statusLabel);
		root.setAlignment(Pos.CENTER);


		//Setting an action for the Clear button
		buttonclearAll.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				groupEnv.selectToggle(SIT1Radio);
				groupLOB.selectToggle(CRRadio);
				DBnameText.clear();
				DBPasswordText.clear();
				fromDatePicker.setValue(LocalDate.now());
				toDatePicker.setValue(LocalDate.now());
				DirectorytextArea.clear();
				buttonRun.setDisable(true);
				DBStatuslabel.setText(null);
				try{
					if (summaryTask.isRunning())
						summaryTask.cancel(true);
				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				statusLabel.setText(null);
				statusLabel.textProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);
				buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
				buttonDBConnect.setStyle("-fx-font: 20px Serif;");
				buttonDBConnect.setStyle("-fx-border-width: 5px;");
				buttonDBConnect.setDisable(false);

			}
		});


		//Setting an action for the buttonClearCustomerDetail button
		buttonClearDate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				fromDatePicker.setValue(LocalDate.now());
				toDatePicker.setValue(LocalDate.now());
				buttonRun.setDisable(false);
				groupLOB.selectToggle(CDBRadio);

				try{
					if (summaryTask.isRunning())
						summaryTask.cancel(true);
				}
				catch(Exception ex){

				}
				try{


					statusLabel.setText(null);
					statusLabel.textProperty().unbind();

				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);

			}
		});




		//Creating a Grid Pane 
		GridPane gridPaneSummary = new GridPane();    

		//Setting size for the pane 
		gridPaneSummary.setMinSize(500, 500); 

		//Setting the padding    
		gridPaneSummary.setPadding(new Insets(10, 10, 10, 10));  

		//Setting the vertical and horizontal gaps between the columns 
		gridPaneSummary.setVgap(5); 
		gridPaneSummary.setHgap(5);       

		//Setting the Grid alignment 
		gridPaneSummary.setAlignment(Pos.CENTER); 

		//Arranging all the nodes in the grid 
		gridPaneSummary.add(EnvLabel, 0, 0); 
		gridPaneSummary.add(hbRadio, 1, 0);       

		gridPaneSummary.add(DBnameLabel, 0, 2); 
		gridPaneSummary.add(DBnameText, 1, 2); 

		gridPaneSummary.add(DBPasswordLabel, 0, 3); 
		gridPaneSummary.add(DBPasswordText, 1, 3); 

		gridPaneSummary.add(hbDBConnection, 1, 5);

		gridPaneSummary.add(LOBLabel, 0, 9); 
		gridPaneSummary.add(hb_LOB_Radio, 1, 9);

		gridPaneSummary.add(DateLabel, 0, 11);       
		gridPaneSummary.add(hb_Date, 1, 12); 

		gridPaneSummary.add(locationLabel, 0, 14); 
		gridPaneSummary.add(DirectorytextArea, 1, 14); 
		gridPaneSummary.add(ReportPathbutton, 1, 15); 

		buttonRun.setAlignment(Pos.BASELINE_LEFT);
		gridPaneSummary.add(buttonRun, 2, 16); 

		gridPaneSummary.add(root,1,18);

		gridPaneSummary.add(buttonclearAll, 0, 18); 
		gridPaneSummary.add(buttonClearDate, 0, 19);


		
		//Styling nodes   
		buttonRun.setStyle(
				"-fx-background-color: DARKORANGE;"); 

		EnvLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		EnvLabel.setFill(Color.SLATEGRAY); 
		DateLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DateLabel.setFill(Color.SLATEGRAY); 
		DBnameLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBnameLabel.setFill(Color.SLATEGRAY); 
		DBPasswordLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBPasswordLabel.setFill(Color.SLATEGRAY); 
		LOBLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		LOBLabel.setFill(Color.SLATEGRAY);
		locationLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		locationLabel.setFill(Color.SLATEGRAY); 

		//Setting the back ground color 
		gridPaneSummary.setStyle("-fx-background-color: BEIGE;");   


		return gridPaneSummary;
	}

	
	
	
	public GridPane getDetailGridPane(Stage stage){
		
		//Create an object for variables used for Summary detail
		Variables varDetail= new Variables();

		//Label for Env name 
		Text EnvLabel = new Text("Environment"); 

		//Toggle group of radio buttons       
		ToggleGroup groupEnv = new ToggleGroup(); 
		RadioButton SIT1Radio = new RadioButton("SIT1"); 
		SIT1Radio.setToggleGroup(groupEnv); 
		SIT1Radio.setUserData("SIT1");

		RadioButton SIT2Radio = new RadioButton("SIT2"); 
		SIT2Radio.setUserData("SIT2");
		SIT2Radio.setToggleGroup(groupEnv); 

		RadioButton UATRadio = new RadioButton("UAT"); 
		UATRadio.setUserData("UAT");
		UATRadio.setToggleGroup(groupEnv); 

		RadioButton UT1Radio = new RadioButton("UT1"); 
		UT1Radio.setUserData("UT1");
		UT1Radio.setToggleGroup(groupEnv); 

		RadioButton UT2Radio = new RadioButton("UT2"); 
		UT2Radio.setUserData("UT2");
		UT2Radio.setToggleGroup(groupEnv); 

		RadioButton UT3Radio = new RadioButton("UT3");
		UT3Radio.setUserData("UT3");
		UT3Radio.setToggleGroup(groupEnv); 

		groupEnv.selectToggle(SIT1Radio);
		varDetail.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());
		
		HBox hbRadio = new HBox(15);
		hbRadio.setAlignment(Pos.CENTER_LEFT);
		hbRadio.getChildren().add(SIT1Radio);
		hbRadio.getChildren().add(SIT2Radio);
		hbRadio.getChildren().add(UATRadio);
		hbRadio.getChildren().add(UT1Radio);
		hbRadio.getChildren().add(UT2Radio);
		hbRadio.getChildren().add(UT3Radio);

		groupEnv.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupEnv.getSelectedToggle() != null) {
					varDetail.setEnvironment(groupEnv.getSelectedToggle().getUserData().toString());
					System.out.println(varDetail.getEnvironment());


				}

			} 
		});

		//Label for User name  
		Text DBnameLabel = new Text("DB UserName"); 

		//Text field for name 
		TextField DBnameText = new TextField(); 

		//Label for User Password 
		Text DBPasswordLabel = new Text("DB Password"); 

		//Text field for Password 
		PasswordField  DBPasswordText = new PasswordField (); 

		// Connect to DB Button
		Button buttonDBConnect = new Button("Connect To Oracle DataBase");
		buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
		buttonDBConnect.setStyle("-fx-font: 20px Serif;");
		buttonDBConnect.setStyle("-fx-border-width: 5px;");

		final Label DBStatuslabel = new Label();

		HBox hbDBConnection = new HBox(15);
		hbDBConnection.setAlignment(Pos.CENTER);
		hbDBConnection.getChildren().add(buttonDBConnect);
		hbDBConnection.getChildren().add(DBStatuslabel);

		//Label for LOB name 
		Text LOBLabel = new Text("LOB"); 

		//Toggle group of radio buttons for selecting LOB      
		ToggleGroup groupLOB = new ToggleGroup(); 
		RadioButton CDBRadio = new RadioButton("CDB"); 
		CDBRadio.setToggleGroup(groupLOB); 
		CDBRadio.setUserData("CDB");

		
		RadioButton PRRadio = new RadioButton("PRIME"); 
		PRRadio.setUserData("PRIME");
		PRRadio.setToggleGroup(groupLOB); 

		RadioButton CRRadio = new RadioButton("CIRRUS"); 
		CRRadio.setUserData("CIRRUS");
		CRRadio.setToggleGroup(groupLOB); 

		//Select LOB as CDB by Default
		groupLOB.selectToggle(CDBRadio);
		varDetail.setLOB(groupLOB.getSelectedToggle().getUserData().toString());

		HBox hb_LOB_Radio = new HBox(15);
		hb_LOB_Radio.setAlignment(Pos.CENTER_LEFT);
		hb_LOB_Radio.getChildren().add(CDBRadio);
//		hb_LOB_Radio.getChildren().add(ACISRadio);
		hb_LOB_Radio.getChildren().add(PRRadio);
		hb_LOB_Radio.getChildren().add(CRRadio);


		groupLOB.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

				if (groupLOB.getSelectedToggle() != null) {
					varDetail.setLOB(groupLOB.getSelectedToggle().getUserData().toString());
					System.out.println(varDetail.getLOB());
				}
			} 
		});


		//Label for Generate Report 
		Button buttonReport = new Button("Generate Report"); 
		buttonReport.setMinHeight(40);
		buttonReport.setAlignment(Pos.CENTER_LEFT);
		buttonReport.setDisable(true);

		buttonDBConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				buttonDBConnect.setStyle("-fx-background-color: DARKORANGE");
//				DBnameText.setText("UHG_001303953");
//				DBPasswordText.setText("Jan%019J");
				final String DB_UserName=DBnameText.getText();
				final String DBPassword=DBPasswordText.getText();
				DetailMem_dataAnalyzer=new DataAnalyzer(varDetail.getEnvironment(),DB_UserName, DBPassword);
				DBStatuslabel.setText(DetailMem_dataAnalyzer.connectToDB());
				DBStatuslabel.setTextFill(Color.CORAL);
				DBStatuslabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
				buttonDBConnect.setDisable(true);
				buttonReport.setDisable(false);
			}
		});



		//Label for Customer number
		Text CustomerNumberLabel = new Text("Enter a Customer Number"); 
		
		//Text field for Customer number 
		TextField CustomerNumberText = new TextField(); 

		//Label for location 
		Text locationLabel = new Text("Report Location"); 

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		configuringDirectoryChooser(directoryChooser);

		TextArea textArea = new TextArea();
		textArea.setMaxHeight(3);
		textArea.setMaxWidth(550);

		Button button = new Button("Chooser Report Path");
		Button buttonclear = new Button("Clear All"); 
		Button buttonClearCustomerDetail= new Button("Run For Next Customer");

		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				File dir = directoryChooser.showDialog(stage);

				if (dir != null) {
					textArea.setText(dir.getAbsolutePath());
					varDetail.setReportPath(dir.getAbsolutePath());

				} else {
					textArea.setText(null);
				}
			}
		});


		//Progress Label
		final Label label = new Label("Analyzing Results:");
		final ProgressBar progressBar = new ProgressBar(0);
		final ProgressIndicator progressIndicator = new ProgressIndicator(0);


		final Hyperlink statusLabel = new Hyperlink();
		statusLabel.setMinWidth(250);
		statusLabel.setTextFill(Color.BLUE);

		buttonReport.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				//if ReportPath is empty generate pop up error
				if (varDetail.getReportPath()!=null && !varDetail.getReportPath().isEmpty()){

					buttonReport.setDisable(true);

					progressBar.setProgress(0);
					progressIndicator.setProgress(0);

					varDetail.setCustomerNumber(CustomerNumberText.getText());
					
					// Create a Task.
					detailTask = new DetailTask(DetailMem_dataAnalyzer,varDetail.getCustomerNumber(),varDetail.getReportPath(), varDetail.getLOB());

					// Unbind progress property
					progressBar.progressProperty().unbind();

					// Bind progress property
					progressBar.progressProperty().bind(detailTask.progressProperty());

					// UnBind progress property.
					progressIndicator.progressProperty().unbind();

					// Bind progress property.
					progressIndicator.progressProperty().bind(detailTask.progressProperty());

					// Unbind text property for Label.

					statusLabel.textProperty().unbind();

					// Bind the text property of Label
					// with message property of Task
					statusLabel.textProperty().bind(detailTask.messageProperty());

					// When completed tasks
					detailTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //

							new EventHandler<WorkerStateEvent>() {

						@Override
						public void handle(WorkerStateEvent t) {

							statusLabel.textProperty().unbind();
							statusLabel.setText(detailTask.getValue());
						}
					});

					// Start the Task.
					new Thread(detailTask).start();


					statusLabel.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							Application a = new Application() {

								@Override
								public void start(Stage stage)
								{
								}
							};

							a.getHostServices().showDocument(detailTask.getValue());


						}
					});

				}
				else{
					//						buttonReport.setDisable(true);

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Please select a directiory to store Report!!");
					alert.showAndWait();
				}

			}
		});

		FlowPane root = new FlowPane();
		root.setPadding(new Insets(10));
		root.setHgap(10);
		root.getChildren().addAll(label, progressBar, progressIndicator, //
				statusLabel);
		root.setAlignment(Pos.CENTER);


		//Setting an action for the Clear button
		buttonclear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				groupEnv.selectToggle(SIT1Radio);
				DBnameText.clear();
				DBPasswordText.clear();
				CustomerNumberText.clear();
				textArea.clear();
				buttonReport.setDisable(true);
				DBStatuslabel.setText(null);
				try{
					if (detailTask.isRunning())
						detailTask.cancel(true);
				}
				catch(Exception ex){

				}
				try{


					statusLabel.setText(null);
					statusLabel.textProperty().unbind();

				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);
				buttonDBConnect.setStyle("-fx-background-color: dodgerblue;");
				buttonDBConnect.setStyle("-fx-font: 20px Serif;");
				buttonDBConnect.setStyle("-fx-border-width: 5px;");
				buttonDBConnect.setDisable(false);

			}
		});


		//Setting an action for the buttonClearCustomerDetail button
		buttonClearCustomerDetail.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				CustomerNumberText.clear();
				varDetail.setCustomerNumber("");
				buttonReport.setDisable(false);
				groupLOB.selectToggle(CDBRadio);

				try{
					if (detailTask.isRunning())
						detailTask.cancel(true);
				}
				catch(Exception ex){

				}
				try{


					statusLabel.setText(null);
					statusLabel.textProperty().unbind();

				}
				catch(Exception ex){

				}
				progressBar.progressProperty().unbind();
				progressIndicator.progressProperty().unbind();
				progressBar.setProgress(0);
				progressIndicator.setProgress(0);

			}
		});


		//Creating a Grid Pane 
		GridPane gridPaneDetail = new GridPane();    

		//Setting size for the pane 
		gridPaneDetail.setMinSize(500, 500); 

		//Setting the padding    
		gridPaneDetail.setPadding(new Insets(10, 10, 10, 10));  

		//Setting the vertical and horizontal gaps between the columns 
		gridPaneDetail.setVgap(5); 
		gridPaneDetail.setHgap(5);       

		//Setting the Grid alignment 
		gridPaneDetail.setAlignment(Pos.CENTER); 

		//Arranging all the nodes in the grid 
		gridPaneDetail.add(EnvLabel, 0, 0); 
		gridPaneDetail.add(hbRadio, 1, 0);       

		gridPaneDetail.add(DBnameLabel, 0, 2); 
		gridPaneDetail.add(DBnameText, 1, 2); 

		gridPaneDetail.add(DBPasswordLabel, 0, 3); 
		gridPaneDetail.add(DBPasswordText, 1, 3); 

		gridPaneDetail.add(hbDBConnection, 1, 6);

		gridPaneDetail.add(LOBLabel, 0, 9); 
		gridPaneDetail.add(hb_LOB_Radio, 1, 9);

		gridPaneDetail.add(CustomerNumberLabel, 0, 12);       
		gridPaneDetail.add(CustomerNumberText, 1, 12); 

		gridPaneDetail.add(locationLabel, 0, 13); 
		gridPaneDetail.add(textArea, 1, 13); 
		gridPaneDetail.add(button, 1, 14); 

		buttonReport.setAlignment(Pos.BASELINE_LEFT);
		gridPaneDetail.add(buttonReport, 2, 17); 

		gridPaneDetail.add(root,1,19);

		gridPaneDetail.add(buttonclear, 0, 19); 
		gridPaneDetail.add(buttonClearCustomerDetail, 0, 20);





		//Styling nodes   
		buttonReport.setStyle(
				"-fx-background-color: DARKORANGE;"); 

		EnvLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		EnvLabel.setFill(Color.SLATEGRAY); 
		LOBLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		LOBLabel.setFill(Color.SLATEGRAY); 
		CustomerNumberLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		CustomerNumberLabel.setFill(Color.SLATEGRAY); 
		DBnameLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBnameLabel.setFill(Color.SLATEGRAY); 
		DBPasswordLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		DBPasswordLabel.setFill(Color.SLATEGRAY); 
		locationLabel.setStyle("-fx-font: normal bold 12px 'verdana' "); 
		locationLabel.setFill(Color.SLATEGRAY); 

		//Setting the back ground color 
		gridPaneDetail.setStyle("-fx-background-color: BEIGE;");   


		return gridPaneDetail;
	}

	/*public String getSummaryReportPath() {
		return SummaryReportPath;
	}

	public void setSummaryReportPath(String summaryReportPath) {
		SummaryReportPath = summaryReportPath;
	}*/
} 	

