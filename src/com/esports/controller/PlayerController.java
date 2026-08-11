package com.esports.controller;

import com.esports.dao.PlayerDAO;
import com.esports.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;  // Add this line (添加这一行)
import java.util.List;

public class PlayerController {
    
    @FXML private TableView<Player> tblPlayers;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbPosition;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblTotalPlayers;
    @FXML private Label lblAverageAge;
    @FXML private Label lblTotalSalary;
    @FXML private Label lblPlayerInfo;
    
    private PlayerDAO playerDAO;
    private ObservableList<Player> playerList;
    
    @FXML
    public void initialize() {
        System.out.println("PlayerController initialization (PlayerController初始化)");
        playerDAO = new PlayerDAO();
        playerList = FXCollections.observableArrayList();
        
        // Configure filter boxes (配置筛选框)
        if (cmbPosition != null) {
            cmbPosition.getItems().addAll("All Positions (所有位置)", "Mid", "ADC", "Top", "Support", "Jungle");
            cmbPosition.setValue("All Positions (所有位置)");
        }
        
        if (cmbStatus != null) {
            cmbStatus.getItems().addAll("All Status (所有状态)", "Active", "Inactive", "Retired");
            cmbStatus.setValue("All Status (所有状态)");
        }
        
        // Configure table columns (配置表格列)
        setupTableColumns();
        
        // Load data (加载数据)
        loadPlayers();
    }
    
    private void setupTableColumns() {
        try {
            // Fix: Use correct column definition method (修复：使用正确的列定义方式)
            // Clear existing columns (清空现有列)
            tblPlayers.getColumns().clear();
            
            // Create table columns (创建表格列)
            TableColumn<Player, Integer> colId = new TableColumn<>("ID");
            TableColumn<Player, String> colName = new TableColumn<>("Player (选手)");
            TableColumn<Player, Integer> colAge = new TableColumn<>("Age (年龄)");
            TableColumn<Player, String> colPosition = new TableColumn<>("Position (位置)");
            TableColumn<Player, String> colNationality = new TableColumn<>("Nationality (国籍)");
            TableColumn<Player, String> colJoinDate = new TableColumn<>("Join Date (加入日期)");
            TableColumn<Player, String> colSalary = new TableColumn<>("Monthly Salary (月薪)");
            TableColumn<Player, String> colStatus = new TableColumn<>("Status (状态)");
            
            // Set column property binding (设置列属性绑定)
            colId.setCellValueFactory(new PropertyValueFactory<>("playerId"));
            
            // Fix: Use simple way to handle player name (修复：使用简单的方式处理选手名称)
            colName.setCellValueFactory(cellData -> {
                Player player = cellData.getValue();
                String displayName = player.getUsername();
                if (player.getRealName() != null && !player.getRealName().isEmpty()) {
                    displayName = player.getRealName() + " (" + player.getUsername() + ")";
                }
                // Use javafx.beans.property.SimpleObjectProperty (使用javafx.beans.property.SimpleObjectProperty)
                return new javafx.beans.property.SimpleObjectProperty<>(displayName);
            });
            
            colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
            colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
            colNationality.setCellValueFactory(new PropertyValueFactory<>("nationality"));
            colJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
            
            // Fix: Salary column uses ObjectProperty (修复：薪资列使用ObjectProperty)
            colSalary.setCellValueFactory(cellData -> {
                java.math.BigDecimal salary = cellData.getValue().getSalary();
                String salaryStr = "¥0.00";
                if (salary != null) {
                    salaryStr = String.format("¥%.2f", salary);
                }
                return new javafx.beans.property.SimpleObjectProperty<>(salaryStr);
            });
            
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            
            // Set column widths (设置列宽度)
            colId.setPrefWidth(50);
            colName.setPrefWidth(150);
            colAge.setPrefWidth(60);
            colPosition.setPrefWidth(80);
            colNationality.setPrefWidth(100);
            colJoinDate.setPrefWidth(100);
            colSalary.setPrefWidth(100);
            colStatus.setPrefWidth(80);
            
            // Fix: Correctly add columns to table (修复：正确添加列到表格)
            tblPlayers.getColumns().add(colId);
            tblPlayers.getColumns().add(colName);
            tblPlayers.getColumns().add(colAge);
            tblPlayers.getColumns().add(colPosition);
            tblPlayers.getColumns().add(colNationality);
            tblPlayers.getColumns().add(colJoinDate);
            tblPlayers.getColumns().add(colSalary);
            tblPlayers.getColumns().add(colStatus);
            
            System.out.println("Player table columns configured (选手表格列配置完成)");
            
        } catch (Exception e) {
            System.out.println("Error configuring table columns (配置表格列时出错): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadPlayers() {
        try {
            System.out.println("Loading player data... (加载选手数据...)");
            List<Player> players = playerDAO.getAllPlayers();
            
            if (players == null) {
                System.out.println("Database query returned null (数据库查询返回null)");
                return;
            }
            
            playerList.setAll(players);
            tblPlayers.setItems(playerList);
            updateStatistics();
            
            System.out.println("Loaded " + players.size() + " players (加载了 " + players.size() + " 名选手)");
            
        } catch (Exception e) {
            System.out.println("Failed to load data (加载数据失败): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateStatistics() {
        if (playerList == null || playerList.isEmpty()) {
            lblTotalPlayers.setText("Total Players (选手总数): 0");
            lblAverageAge.setText("Average Age (平均年龄): 0.0");
            lblTotalSalary.setText("Total Monthly Salary (总月薪): ¥0.00");
            lblPlayerInfo.setText("No player selected (没有选中任何选手)");
            return;
        }
        
        int total = playerList.size();
        double avgAge = 0;
        double totalSalary = 0;
        int activePlayers = 0;
        
        for (Player p : playerList) {
            avgAge += p.getAge();
            if (p.getSalary() != null) {
                totalSalary += p.getSalary().doubleValue();
            }
            if ("Active".equalsIgnoreCase(p.getStatus())) {
                activePlayers++;
            }
        }
        avgAge /= total;
        
        lblTotalPlayers.setText(String.format("Total Players (选手总数): %d (Active (活跃): %d)", total, activePlayers));
        lblAverageAge.setText(String.format("Average Age (平均年龄): %.1f years (岁)", avgAge));
        lblTotalSalary.setText(String.format("Total Monthly Salary (总月薪): ¥%.2f", totalSalary));
    }
    
    private void showPlayerDetails(Player player) {
        if (player == null) {
            lblPlayerInfo.setText("No player selected (没有选中任何选手)");
            return;
        }
        
        String info = String.format(
            "Player ID (选手ID): %d | Real Name (真实姓名): %s | Game ID (游戏ID): %s | Age (年龄): %d | Position (位置): %s | Nationality (国籍): %s | Status (状态): %s",
            player.getPlayerId(),
            player.getRealName() != null ? player.getRealName() : "N/A",
            player.getUsername(),
            player.getAge(),
            player.getPosition(),
            player.getNationality() != null ? player.getNationality() : "Unknown",
            player.getStatus()
        );
        lblPlayerInfo.setText(info);
    }
    
    // Button events (按钮事件)
    @FXML private void refreshPlayers() { 
        loadPlayers(); 
        showAlert("Refresh Successful (刷新成功)", "Player data has been updated! (选手数据已更新！)");
    }
    
    @FXML 
    private void showAddPlayerDialog() { 
        boolean okClicked = PlayerDialogController.showPlayerDialog(null, 
            (Stage) tblPlayers.getScene().getWindow());
        
        if (okClicked) {
            // Refresh data (刷新数据)
            loadPlayers();
            showAlert("Operation Successful (操作成功)", "Player successfully added! (选手已成功添加！)");
        }
    }
    
    @FXML 
    private void showEditPlayerDialog() { 
        Player selected = tblPlayers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a player to edit first! (请先选择一名选手进行编辑！)");
            return;
        }
        
        boolean okClicked = PlayerDialogController.showPlayerDialog(selected, 
            (Stage) tblPlayers.getScene().getWindow());
        
        if (okClicked) {
            // Refresh data (刷新数据)
            loadPlayers();
            showAlert("Operation Successful (操作成功)", "Player information updated! (选手信息已更新！)");
        }
    }
    
    @FXML 
    private void deleteSelectedPlayer() { 
        Player selected = tblPlayers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a player to delete first! (请先选择一名选手进行删除！)");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion (确认删除)");
        confirm.setHeaderText("Delete Player (删除选手)");
        confirm.setContentText("Are you sure you want to delete player (确定要删除选手) " + selected.getUsername() + " ?");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = playerDAO.deletePlayer(selected.getPlayerId());
                if (success) {
                    showAlert("Deletion Successful (删除成功)", "Player (选手) " + selected.getUsername() + " has been deleted! (已删除！)");
                    loadPlayers();
                } else {
                    showAlert("Deletion Failed (删除失败)", "Unable to delete player, please try again! (无法删除选手，请重试！)");
                }
            }
        });
    }
    
    @FXML private void searchPlayers() { 
        String searchText = txtSearch.getText().trim();
        String position = cmbPosition.getValue();
        String status = cmbStatus.getValue();
        
        if (searchText.isEmpty() && "All Positions (所有位置)".equals(position) && "All Status (所有状态)".equals(status)) {
            loadPlayers();
            return;
        }
        
        ObservableList<Player> filteredList = FXCollections.observableArrayList();
        for (Player player : playerList) {
            boolean matches = true;
            
            // Search text (搜索文本)
            if (!searchText.isEmpty()) {
                String searchLower = searchText.toLowerCase();
                if (!player.getUsername().toLowerCase().contains(searchLower) &&
                    (player.getRealName() == null || !player.getRealName().toLowerCase().contains(searchLower))) {
                    matches = false;
                }
            }
            
            // Position filter (位置筛选)
            if (!"All Positions (所有位置)".equals(position) && !position.equals(player.getPosition())) {
                matches = false;
            }
            
            // Status filter (状态筛选)
            if (!"All Status (所有状态)".equals(status) && !status.equalsIgnoreCase(player.getStatus())) {
                matches = false;
            }
            
            if (matches) {
                filteredList.add(player);
            }
        }
        
        tblPlayers.setItems(filteredList);
        updateStatistics();
        showAlert("Search Complete (搜索完成)", "Found " + filteredList.size() + " matching players (找到 " + filteredList.size() + " 名符合条件的选手)");
    }
    
    @FXML private void showAllPlayers() { 
        loadPlayers(); 
        txtSearch.clear();
        cmbPosition.setValue("All Positions (所有位置)");
        cmbStatus.setValue("All Status (所有状态)");
    }
    
    @FXML private void showPlayerContracts() {
        Player selected = tblPlayers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a player to view contracts first! (请先选择一名选手查看合同！)");
            return;
        }
        showAlert("View Contracts (合同查看)", "View contracts for player (查看选手) " + selected.getUsername() + " function pending implementation (的合同功能待实现)");
    }
    
    @FXML private void showPlayerTournaments() {
        Player selected = tblPlayers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a player to view tournament records first! (请先选择一名选手查看比赛记录！)");
            return;
        }
        showAlert("Tournament Records (比赛记录)", "View tournament records for player (查看选手) " + selected.getUsername() + " function pending implementation (的比赛记录功能待实现)");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}