package com.esports.dao;

import com.esports.model.Player;
import com.esports.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    
    // Get all players (include new fields) (获取所有选手（包含新字段）)
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT player_id, " +
                    "COALESCE(real_name, username) as real_name, " +
                    "username, age, position, " +
                    "COALESCE(nationality, 'Unknown') as nationality, " +
                    "join_date, salary, status " +
                    "FROM MEMBERS ORDER BY player_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setRealName(rs.getString("real_name"));
                player.setUsername(rs.getString("username"));
                player.setAge(rs.getInt("age"));
                player.setPosition(rs.getString("position"));
                player.setNationality(rs.getString("nationality"));
                player.setJoinDate(rs.getDate("join_date").toLocalDate());
                player.setSalary(rs.getBigDecimal("salary"));
                player.setStatus(rs.getString("status"));
                
                players.add(player);
            }
            System.out.println("Retrieved " + players.size() + " players (获取到 " + players.size() + " 名选手)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
    
    // Get player by ID (根据ID获取选手)
    public Player getPlayerById(int playerId) {
        String sql = "SELECT player_id, " +
                    "COALESCE(real_name, username) as real_name, " +
                    "username, age, position, " +
                    "COALESCE(nationality, 'Unknown') as nationality, " +
                    "join_date, salary, status " +
                    "FROM MEMBERS WHERE player_id = ?";
        Player player = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    player = new Player();
                    player.setPlayerId(rs.getInt("player_id"));
                    player.setRealName(rs.getString("real_name"));
                    player.setUsername(rs.getString("username"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(rs.getString("position"));
                    player.setNationality(rs.getString("nationality"));
                    player.setJoinDate(rs.getDate("join_date").toLocalDate());
                    player.setSalary(rs.getBigDecimal("salary"));
                    player.setStatus(rs.getString("status"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }
    
    // Add new player (complete version - supports all fields) (添加新选手（完整版本 - 支持所有字段）)
    public boolean addPlayer(Player player) {
        String sql = "INSERT INTO MEMBERS (username, real_name, age, position, nationality, join_date, salary, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, player.getUsername());
            pstmt.setString(2, player.getRealName());
            pstmt.setInt(3, player.getAge());
            pstmt.setString(4, player.getPosition());
            pstmt.setString(5, player.getNationality());
            pstmt.setDate(6, Date.valueOf(player.getJoinDate()));
            pstmt.setBigDecimal(7, player.getSalary());
            pstmt.setString(8, player.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Player added successfully, affected rows (添加选手成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Failed to add player (添加选手失败): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Update player information (complete version - supports all fields) (更新选手信息（完整版本 - 支持所有字段）)
    public boolean updatePlayer(Player player) {
        String sql = "UPDATE MEMBERS SET username = ?, real_name = ?, age = ?, position = ?, " +
                    "nationality = ?, join_date = ?, salary = ?, status = ? WHERE player_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, player.getUsername());
            pstmt.setString(2, player.getRealName());
            pstmt.setInt(3, player.getAge());
            pstmt.setString(4, player.getPosition());
            pstmt.setString(5, player.getNationality());
            pstmt.setDate(6, Date.valueOf(player.getJoinDate()));
            pstmt.setBigDecimal(7, player.getSalary());
            pstmt.setString(8, player.getStatus());
            pstmt.setInt(9, player.getPlayerId());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Player updated successfully, affected rows (更新选手成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Failed to update player (更新选手失败): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete player (删除选手)
    public boolean deletePlayer(int playerId) {
        String sql = "DELETE FROM MEMBERS WHERE player_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Player deleted successfully, affected rows (删除选手成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Failed to delete player (删除选手失败): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Filter players by position (根据位置筛选选手)
    public List<Player> getPlayersByPosition(String position) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT player_id, " +
                    "COALESCE(real_name, username) as real_name, " +
                    "username, age, position, " +
                    "COALESCE(nationality, 'Unknown') as nationality, " +
                    "join_date, salary, status " +
                    "FROM MEMBERS WHERE position = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, position);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setPlayerId(rs.getInt("player_id"));
                    player.setRealName(rs.getString("real_name"));
                    player.setUsername(rs.getString("username"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(rs.getString("position"));
                    player.setNationality(rs.getString("nationality"));
                    player.setJoinDate(rs.getDate("join_date").toLocalDate());
                    player.setSalary(rs.getBigDecimal("salary"));
                    player.setStatus(rs.getString("status"));
                    
                    players.add(player);
                }
            }
            System.out.println("Retrieved " + players.size() + " " + position + " players (获取到 " + players.size() + " 名" + position + "选手)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
    
    // Search players (by username or real name) (搜索选手（根据用户名或真实姓名）)
    public List<Player> searchPlayers(String keyword) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT player_id, " +
                    "COALESCE(real_name, username) as real_name, " +
                    "username, age, position, " +
                    "COALESCE(nationality, 'Unknown') as nationality, " +
                    "join_date, salary, status " +
                    "FROM MEMBERS WHERE username LIKE ? OR real_name LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setPlayerId(rs.getInt("player_id"));
                    player.setRealName(rs.getString("real_name"));
                    player.setUsername(rs.getString("username"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(rs.getString("position"));
                    player.setNationality(rs.getString("nationality"));
                    player.setJoinDate(rs.getDate("join_date").toLocalDate());
                    player.setSalary(rs.getBigDecimal("salary"));
                    player.setStatus(rs.getString("status"));
                    
                    players.add(player);
                }
            }
            System.out.println("Found " + players.size() + " matching players (搜索到 " + players.size() + " 名符合条件的选手)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
}