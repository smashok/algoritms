package ua.kpi.fict.acts.it03;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends  Application{

    public static void main(String[] args)
    {
        try {
            IndexStructure indexSt = new IndexStructure("src/ua/kpi/fict/acts/it03/Files/");
//            for (int i = 0; i < 10000; i++) {
//                indexSt.set("key"+i, "value"+i);
//            }
//            for (int i = 0; i < 15; i++) {
//                String key = "key"+ (int)(Math.random()*9999);
//                System.out.println(key);
//                indexSt.get(key);
//            }
        }catch (IOException err)
        {
            System.out.println(err.getMessage());
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Laboratory Work 2");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}
