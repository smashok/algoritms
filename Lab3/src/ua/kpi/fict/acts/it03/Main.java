package ua.kpi.fict.acts.it03;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        final int MIN_PRICE = 2;
        final int MAX_PRICE = 30;
        final int MIN_WEIGHT = 1;
        final int MAX_WEIGHT = 25;
        final int MAX_BAG_WEIGHT = 250;
        final int ITEMS_AMOUNT = 100;
        final int POPULATION_SIZE = 100;
        final int POPULATION_AMOUNT = 1000;


        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < ITEMS_AMOUNT; i++) {
            int price = (int)(Math.random()*(MAX_PRICE-MIN_PRICE+1)+MIN_PRICE);
            int weight = (int)(Math.random()*(MAX_WEIGHT-MIN_WEIGHT+1)+MIN_WEIGHT);
            items.add(new Item(price, weight));
        }

        GeneticAlgorithm ga = new GeneticAlgorithm(MAX_BAG_WEIGHT, POPULATION_SIZE,items);
        System.out.println(ga.start(POPULATION_AMOUNT));
        ArrayList<Integer> bestArr = ga.bestArr;
        ArrayList<Integer> midArr = ga.midArr;


        //построение графиков
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> mainChart = new LineChart<Number, Number>(x,y);

        ObservableList<XYChart.Data> lineChartData1 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> lineChartData2 = FXCollections.observableArrayList();

        for (int i = 0; i < bestArr.size(); i++)
        {
            lineChartData1.add(new XYChart.Data(i,bestArr.get(i)));
            lineChartData2.add(new XYChart.Data(i,midArr.get(i)));
        }
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        series1.setData(lineChartData1);
        series1.setName("Best");
        series2.setData(lineChartData2);
        series2.setName("Middle");
        mainChart.getData().add(series1);
//        mainChart.getData().add(series2);
        mainChart.setCreateSymbols(false);

        Scene scene = new Scene(mainChart,1000, 600);
        primaryStage.setTitle("Laboratory Work 3");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
