package ua.kpi.fict.acts.it03;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Controller {

    @FXML
    private TableView<KeyValue> dataTable;

    @FXML
    private Button delButton;

    @FXML
    private Label infoLabel;

    @FXML
    private Button getButton;

    @FXML
    private TableView<KeyValue> indexTable;

    @FXML
    private TextField keyForm;

    @FXML
    private Button setButton;

    @FXML
    private TextArea valueForm;

    @FXML
    void initialize()
    {
        try
        {
            IndexStructure indexSt = new IndexStructure("src/ua/kpi/fict/acts/it03/Files/");

            setButton.setOnAction(event  ->
            {
                infoLabel.setText("");

                String key = keyForm.getText();
                String value = valueForm.getText();
                if(key.length()>indexSt.KEY_LENGTH
                        || value.length() > indexSt.DATA_LENGTH
                        || key.length() == 0
                        || value.length() == 0)
                {
                    infoLabel.setTextFill(Color.RED);
                    infoLabel.setText("Exceeded the number of characters/ Empty field");
                }
                else
                {
                    try{
                        indexSt.set(key, value);
                        infoLabel.setTextFill(Color.GREEN);
                        infoLabel.setText("Added/Changed successfully");
                        fillTables(indexSt);
                    }
                    catch(IOException err)
                    {
                        infoLabel.setTextFill(Color.RED);
                        infoLabel.setText("Something went wrong");
                    }
                }
                keyForm.setText("");
                valueForm.setText("");
            });


            getButton.setOnAction(event ->
            {
                infoLabel.setText("");

                String key = keyForm.getText();
                if(key.length()>indexSt.KEY_LENGTH
                        || key.length() == 0)
                {
                    infoLabel.setTextFill(Color.RED);
                    infoLabel.setText("Exceeded the number of characters/ Empty field");
                }
                else
                {
                    try{
                        infoLabel.setTextFill(Color.GREEN);
                        infoLabel.setText("Your value: "+ indexSt.get(key));
                    }
                    catch(IOException err)
                    {
                        infoLabel.setTextFill(Color.RED);
                        infoLabel.setText("Something went wrong");
                    }
                }
                keyForm.setText("");

            });

            delButton.setOnAction(event ->
            {
                infoLabel.setText("");

                String key = keyForm.getText();
                if(key.length()>indexSt.KEY_LENGTH
                        || key.length() == 0)
                {
                    infoLabel.setTextFill(Color.RED);
                    infoLabel.setText("Exceeded the number of characters/ Empty field");
                }
                else
                {
                    try{
                        indexSt.delete(key);
                        infoLabel.setTextFill(Color.GREEN);
                        infoLabel.setText("Deleted successfully/Key not found");
                        fillTables(indexSt);
                    }
                    catch(IOException err)
                    {
                        infoLabel.setTextFill(Color.RED);
                        infoLabel.setText("Something went wrong");
                    }
                }
                keyForm.setText("");

            });


            fillTables(indexSt);
        }
        catch (IOException err)
        { System.out.println(err.getMessage()); }


    }

    private void fillTables(IndexStructure indexSt) throws IOException
    {
        indexTable.getItems().clear();
        dataTable.getItems().clear();
        LinkedList<String> indexList = indexSt.getIndexList();
        Iterator<String> it = indexList.iterator();
        while(it.hasNext())
        {
            indexTable.getItems().add(new KeyValue(it.next(), it.next()));
        }
        for (int i = 1; i <= indexSt.blocksAmount; i++)
        {
            dataTable.getItems().add(new KeyValue("-".repeat(35), "-".repeat(50)+"Block "+i+"-".repeat(50)));
            LinkedList<String> dataList = indexSt.getDataList(i);
            it = dataList.iterator();
            while(it.hasNext())
            {
                dataTable.getItems().add(new KeyValue(it.next(), it.next()));
            }
            dataTable.getItems().add(new KeyValue("",""));
        }
    }

}
