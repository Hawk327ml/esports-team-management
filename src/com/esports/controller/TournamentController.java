package com.esports.controller;

import com.esports.dao.PlayerDAO;
import com.esports.dao.TournamentDAO;
import com.esports.model.TournamentResult;
import com.esports.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TournamentController {
    
    @FXML
    private TableView<TournamentResult> tournamentTable;
    
    @FXML
    private TableColumn<TournamentResult, Integer> recordIdColumn;
    
    @FXML
    private TableColumn<TournamentResult, Integer> playerIdColumn;
    
    @FXML
    private TableColumn<TournamentResult, String> tournamentNameColumn;
    
    @FXML
    private TableColumn<TournamentResult, LocalDate> tournamentDateColumn;
    
    @FXML
    private TableColumn<TournamentResult, String> rankingColumn;
    
    @FXML
    private TableColumn<TournamentResult, BigDecimal> prizeMoneyColumn;
    
    @FXML
    private TableColumn<TournamentResult, String> teamColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Label totalTournamentsLabel;
    
    @FXML
    private Label totalPrizeMoneyLabel;
    
    @FXML
    private Label averagePrizeLabel;
    
    @FXML
    private Label championCountLabel;
    
    @FXML
    private Label tournamentInfoLabel;
    
    private ObservableList<TournamentResult> tournamentList = FXCollections.observableArrayList();
    private TournamentDAO tournamentDAO = new TournamentDAO();
    private PlayerDAO playerDAO = new PlayerDAO();
    
    @FXML
    private void initialize() {
        System.out.println("TournamentController initialization (TournamentController初始化)");
        setupTableColumns();
        loadTournaments();
        updateStatistics();
        
        // Add table selection listener (添加表格选择监听器)
        tournamentTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showTournamentDetails(newValue));
    }
    
    private void setupTableColumns() {
        // Use PropertyValueFactory (使用PropertyValueFactory)
        recordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        tournamentNameColumn.setCellValueFactory(new PropertyValueFactory<>("tournamentName"));
        tournamentDateColumn.setCellValueFactory(new PropertyValueFactory<>("tournamentDate"));
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        prizeMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("prizeMoney"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));
        
        // Format prize money column (格式化奖金列)
        prizeMoneyColumn.setCellFactory(column -> {
            return new TableCell<TournamentResult, BigDecimal>() {
                @Override
                protected void updateItem(BigDecimal prize, boolean empty) {
                    super.updateItem(prize, empty);
                    if (empty || prize == null) {
                        setText(null);
                    } else {
                        setText(String.format("¥%,.2f", prize));
                        if (prize.compareTo(new BigDecimal("100000")) > 0) {
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        } else if (prize.compareTo(new BigDecimal("50000")) > 0) {
                            setStyle("-fx-text-fill: orange;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
        
        // Ranking column colors (名次列颜色)
        rankingColumn.setCellFactory(column -> {
            return new TableCell<TournamentResult, String>() {
                @Override
                protected void updateItem(String ranking, boolean empty) {
                    super.updateItem(ranking, empty);
                    if (empty || ranking == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(ranking);
                        if (ranking.contains("Champion") || ranking.contains("1")) {
                            setStyle("-fx-text-fill: gold; -fx-font-weight: bold;");
                        } else if (ranking.contains("Runner-up") || ranking.contains("2")) {
                            setStyle("-fx-text-fill: silver; -fx-font-weight: bold;");
                        } else if (ranking.contains("3") || ranking.contains("Third")) {
                            setStyle("-fx-text-fill: #cd7f32; -fx-font-weight: bold;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
        
        System.out.println("Tournament table columns configured (比赛记录表格列配置完成)");
    }
    
    private void loadTournaments() {
        System.out.println("Loading tournament record data... (加载比赛记录数据...)");
        tournamentList.clear();
        tournamentList.addAll(tournamentDAO.getAllTournaments());
        tournamentTable.setItems(tournamentList);
        System.out.println("Loaded " + tournamentList.size() + " tournament records (加载了 " + tournamentList.size() + " 条比赛记录)");
    }
    
    private void updateStatistics() {
        int total = tournamentList.size();
        BigDecimal totalPrize = BigDecimal.ZERO;
        int championCount = 0;
        
        for (TournamentResult tournament : tournamentList) {
            if (tournament.getPrizeMoney() != null) {
                totalPrize = totalPrize.add(tournament.getPrizeMoney());
            }
            
            if (tournament.getRanking() != null && 
                (tournament.getRanking().contains("Champion") || tournament.getRanking().contains("1"))) {
                championCount++;
            }
        }
        
        BigDecimal averagePrize = total > 0 ? totalPrize.divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        
        totalTournamentsLabel.setText("Total Tournament Records (比赛记录总数): " + total);
        totalPrizeMoneyLabel.setText(String.format("Total Prize Money (总奖金): ¥%,.2f", totalPrize));
        averagePrizeLabel.setText(String.format("Average Prize (平均奖金): ¥%,.2f", averagePrize));
        championCountLabel.setText("Champion Count (冠军次数): " + championCount);
    }
    
    private void showTournamentDetails(TournamentResult tournament) {
        if (tournament == null) {
            tournamentInfoLabel.setText("No tournament record selected (没有选中任何比赛记录)");
            return;
        }
        
        try {
            Player player = playerDAO.getPlayerById(tournament.getPlayerId());
            String playerName = (player != null) ? player.getUsername() : "Unknown Player (未知选手)";
            
            String info = String.format(
                "Record ID (记录ID): %d | Player (选手): %s (ID: %d) | Tournament (比赛): %s | Date (日期): %s | Ranking (名次): %s | Prize (奖金): ¥%,.2f | Team (战队): %s",
                tournament.getRecordId(),
                playerName,
                tournament.getPlayerId(),
                tournament.getTournamentName(),
                tournament.getTournamentDate(),
                tournament.getRanking(),
                tournament.getPrizeMoney(),
                tournament.getTeam() != null ? tournament.getTeam() : "N/A"
            );
            tournamentInfoLabel.setText(info);
        } catch (Exception e) {
            tournamentInfoLabel.setText("Record ID (记录ID): " + tournament.getRecordId() + " | Tournament (比赛): " + tournament.getTournamentName());
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadTournaments();
        updateStatistics();
        showAlert("Refresh Successful (刷新成功)", "Tournament record data has been refreshed! (比赛记录数据已刷新！)");
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadTournaments();
            updateStatistics();
            return;
        }
        
        ObservableList<TournamentResult> filteredList = FXCollections.observableArrayList();
        for (TournamentResult tournament : tournamentList) {
            boolean matches = false;
            
            // Search player ID (搜索选手ID)
            if (String.valueOf(tournament.getPlayerId()).contains(searchText)) {
                matches = true;
            }
            
            // Search tournament name (搜索比赛名称)
            if (tournament.getTournamentName() != null && 
                tournament.getTournamentName().toLowerCase().contains(searchText.toLowerCase())) {
                matches = true;
            }
            
            // Search team (搜索战队)
            if (tournament.getTeam() != null && 
                tournament.getTeam().toLowerCase().contains(searchText.toLowerCase())) {
                matches = true;
            }
            
            // Search ranking (搜索名次)
            if (tournament.getRanking() != null && 
                tournament.getRanking().toLowerCase().contains(searchText.toLowerCase())) {
                matches = true;
            }
            
            if (matches) {
                filteredList.add(tournament);
            }
        }
        
        tournamentTable.setItems(filteredList);
        updateStatistics();
        
        if (filteredList.isEmpty()) {
            showAlert("Search Results (搜索结果)", "No matching tournament records found (没有找到符合条件的比赛记录)");
        }
    }
    
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        loadTournaments();
    }
    
    @FXML
    private void handleAddTournament() {
        showAlert("Function Hint (功能提示)", "Add tournament record function pending implementation (添加比赛记录功能待实现)");
        // Can add dialog to input new tournament record later (后续可以添加对话框来输入新比赛记录)
    }
    
    @FXML
    private void handleEditTournament() {
        TournamentResult selected = tournamentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a tournament record to edit first! (请先选择一条比赛记录进行编辑！)");
            return;
        }
        showAlert("Function Hint (功能提示)", "Edit tournament record function pending implementation - Selected Record ID (编辑比赛记录功能待实现 - 选择记录ID): " + selected.getRecordId());
        // Can add edit dialog later (后续可以添加编辑对话框)
    }
    
    @FXML
    private void handleDeleteTournament() {
        TournamentResult selected = tournamentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a tournament record to delete first! (请先选择一条比赛记录进行删除！)");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion (确认删除)");
        confirm.setHeaderText("Delete Tournament Record (删除比赛记录)");
        confirm.setContentText("Are you sure you want to delete tournament record ID (确定要删除比赛记录ID) " + selected.getRecordId() + " ?\nTournament (比赛): " + selected.getTournamentName());
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            boolean success = tournamentDAO.deleteTournament(selected.getRecordId());
            if (success) {
                showAlert("Deletion Successful (删除成功)", "Tournament record successfully deleted! (比赛记录已成功删除！)");
                loadTournaments();
                updateStatistics();
            } else {
                showAlert("Deletion Failed (删除失败)", "Unable to delete tournament record, please try again! (无法删除比赛记录，请重试！)");
            }
        }
    }
    
    @FXML
    private void handlePlayerStats() {
        TournamentResult selected = tournamentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error (选择错误)", "Please select a tournament record to view player statistics first! (请先选择一条比赛记录查看选手统计！)");
            return;
        }
        
        double totalPrize = tournamentDAO.getTotalPrizeByPlayer(selected.getPlayerId());
        Player player = playerDAO.getPlayerById(selected.getPlayerId());
        String playerName = (player != null) ? player.getUsername() : "Unknown Player (未知选手)";
        
        showAlert("Player Statistics (选手统计)", 
            "Player (选手): " + playerName + " (ID: " + selected.getPlayerId() + ")\n" +
            "Total Prize (总奖金): ¥" + String.format("%,.2f", totalPrize) + "\n" +
            "(This function can be expanded to show all tournament records for the player) （此功能可扩展为显示选手所有比赛记录）");
    }
    
    @FXML
    private void handleBackToMain() {
        try {
            // Get current scene and return to main interface (获取当前场景并返回主界面)
            Stage stage = (Stage) tournamentTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/esports/view/MainView.fxml"));
            stage.setScene(new Scene(root, 1000, 700));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error (错误)", "Unable to return to main menu (无法返回主菜单): " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}