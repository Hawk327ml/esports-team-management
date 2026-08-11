package com.esports.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EsportsApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load FXML using absolute path (使用绝对路径加载FXML)
            Parent root = FXMLLoader.load(getClass().getResource("/com/esports/view/MainView.fxml"));
            
            // Set scene (设置场景)
            Scene scene = new Scene(root, 1000, 700);
            
            // Set stage (设置舞台)
            primaryStage.setTitle("Esports Player Management System (电竞选手管理系统)");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML (加载FXML失败): " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Launch JavaFX application (启动JavaFX应用)
        launch(args);
    }
}