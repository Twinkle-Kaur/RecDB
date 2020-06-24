package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import DBConnection.DBcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableController implements Initializable {

	ObservableList<ModelTable> oblist=FXCollections.observableArrayList() ;
	
	@FXML
    private TableView<ModelTable> table;

	@FXML
    private TableColumn<ModelTable, String> col_loc;

    @FXML
    private TableColumn<ModelTable, String> col_drug;

    @FXML
    private TableColumn<ModelTable, String> col_id;

    @FXML
    private TableColumn<ModelTable, String> col_treat;

    @FXML
    private TableColumn<ModelTable, String> col_name;

    @FXML
    private TableColumn<ModelTable, String> col_status;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		DBcon handler=new DBcon();
		try {
			Connection con=handler.dbcon();
			ResultSet rs=con.createStatement().executeQuery("select * from data");
			
			while(rs.next()) 
			{
				oblist.add(new ModelTable(rs.getString("id"),rs.getString("name"),
						rs.getString("loc"),rs.getString("status"),rs.getString("drug"),
						rs.getString("treat")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_loc.setCellValueFactory(new PropertyValueFactory<>("loc"));
		col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		col_drug.setCellValueFactory(new PropertyValueFactory<>("drug"));
		col_treat.setCellValueFactory(new PropertyValueFactory<>("treat"));
		
		table.setItems(oblist);
	}

}
