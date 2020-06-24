package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import Controller.Login;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import DBConnection.DBcon;
public class Login implements Initializable{
    @FXML
    private ImageView load;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXCheckBox checkbox;

    @FXML
    private JFXTextField userName;

    @FXML
    private AnchorPane frame;

    @FXML
    private JFXButton logBtn;
    
    private static Login instance;

    private DBcon handler;
    private Connection con;
    private PreparedStatement pst;
   
    public Login()
    {
    	instance = this;
    }
    
    public static Login getInstance()
    {
    	return instance;
    }
    
    
    public String username()
    {
    	return userName.getText();
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		load.setVisible(false);
		handler=new DBcon();
	}
	 @FXML
	 public void logIn(ActionEvent event)
	 {
		 load.setVisible(true);
		 PauseTransition pt=new PauseTransition();
		 pt.setDuration(Duration.seconds(5));
		 pt.play();
		 
		
			try {
				con=handler.dbcon();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		 String query="Select * from credentials where username=? AND password=?";
		 try {
			pst=con.prepareStatement(query);
			pst.setString(1, userName.getText());
			pst.setString(2, pass.getText());
			ResultSet rs=pst.executeQuery();
			 
			int count=0;
			while(rs.next())
			{
				count++;
			}
			if(count==1)
			{	try {
				Parent root =FXMLLoader.load(getClass().getResource("/FXML/HomePage.fxml"));
				Stage home=new Stage(); 
				Scene s=new Scene(root,800,480);
				home.setScene(s);
				home.show();
				home.setResizable(false);
				Stage nStage = (Stage) logBtn.getScene().getWindow();
                nStage.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
								
			}
			else
			{
				Alert alert=new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Username and/or password is incorrect");
				alert.show();
				load.setVisible(false);			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
			 try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
	  }
}
