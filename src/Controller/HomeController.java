package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.opencv.core.Core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToolbar;

import Controller.Login;
import Controller.RecController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class HomeController implements Initializable{

	 @FXML
	    private Pane toolBar;
	    @FXML
	    private JFXButton  cntBtn;

	    @FXML
	    private JFXButton abtBtn;
	    @FXML
	    private JFXButton  recBtn;
	    @FXML
	    private JFXButton logOut;
	    @FXML
	    private AnchorPane frame;
	    @FXML
	    private AnchorPane data;
	    @FXML
	    private Text text;
	    
	    AnchorPane home;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		setUsername(Login.getInstance().username());
		createPage();
	}
	
	private void setNode(Node node) {
		
		data.getChildren().clear();
		data.getChildren().add((Node) node);
		FadeTransition ft=new FadeTransition(Duration.millis(10000));
		ft.setNode(node);
		ft.setFromValue(0.1);
		ft.setToValue(1);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);
		ft.play();
	}
	private void createPage() 
	{
		try {
			home=FXMLLoader.load(getClass().getResource("/FXML/Table.fxml"));
			setNode(home);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setUsername(String user)
	{
		this.text.setText("Welcome, "+user);
	}
	 @FXML
	    void logOut(ActionEvent event) 
	 	{
		 	try {
				Parent root=FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
				Stage login=new Stage();
				root.setStyle("-fx-background-color:#ffffff;");
				Scene s=new Scene(root,390,314);
				login.setScene(s);
				login.show();
				login.setResizable(false);
				Stage nStage = (Stage) abtBtn.getScene().getWindow();
                nStage.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	@FXML
   public void contact(ActionEvent event) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/FXML/Contact.fxml"));
			Stage contact=new Stage(); 
			Scene s=new Scene(root,300,176);
			contact.setScene(s);
			contact.show();
			contact.setResizable(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	@FXML
	   public void aboutUs(ActionEvent event) {
		Parent root;
		try {
			root=FXMLLoader.load(getClass().getResource("/FXML/aboutUs.fxml")); ;
			Stage about=new Stage(); 
			Scene s=new Scene(root,602,440);
			about.setScene(s);
			about.show();
			about.setResizable(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		@FXML
		   public void rec(ActionEvent event) {
			
			Parent root;
			try {
				
				root= FXMLLoader.load(getClass().getResource("/FXML/Recognation.fxml"));;
				Stage r=new Stage(); 
				Scene s=new Scene(root,510,566);
				r.setScene(s);
				r.show();
				r.setResizable(false);
			
				
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
}
