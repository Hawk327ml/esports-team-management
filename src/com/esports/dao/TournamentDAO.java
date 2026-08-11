package com.esports.dao;

import com.esports.model.TournamentResult;
import com.esports.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournamentDAO {
    
    // Get all tournament records (获取所有比赛记录)
    public List<TournamentResult> getAllTournaments() {
        List<TournamentResult> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM TOURNAMENT_RESULTS ORDER BY tournament_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TournamentResult tournament = new TournamentResult();
                tournament.setRecordId(rs.getInt("record_id"));
                tournament.setPlayerId(rs.getInt("player_id"));
                tournament.setTournamentName(rs.getString("tournament_name"));
                tournament.setTournamentDate(rs.getDate("tournament_date").toLocalDate());
                tournament.setRanking(rs.getString("ranking"));
                tournament.setPrizeMoney(rs.getBigDecimal("prize_money"));
                tournament.setTeam(rs.getString("team"));
                
                tournaments.add(tournament);
            }
            System.out.println("Retrieved " + tournaments.size() + " tournament records (获取到 " + tournaments.size() + " 条比赛记录)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }
    
    // Get tournament records by player ID (根据选手ID获取比赛记录)
    public List<TournamentResult> getTournamentsByPlayerId(int playerId) {
        List<TournamentResult> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM TOURNAMENT_RESULTS WHERE player_id = ? ORDER BY tournament_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TournamentResult tournament = new TournamentResult();
                    tournament.setRecordId(rs.getInt("record_id"));
                    tournament.setPlayerId(rs.getInt("player_id"));
                    tournament.setTournamentName(rs.getString("tournament_name"));
                    tournament.setTournamentDate(rs.getDate("tournament_date").toLocalDate());
                    tournament.setRanking(rs.getString("ranking"));
                    tournament.setPrizeMoney(rs.getBigDecimal("prize_money"));
                    tournament.setTeam(rs.getString("team"));
                    
                    tournaments.add(tournament);
                }
            }
            System.out.println("Retrieved " + tournaments.size() + " tournament records for player (获取到选手) " + playerId + " 的 " + tournaments.size() + " 条比赛记录");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournaments;
    }
    
    // Add tournament record (添加比赛记录)
    public boolean addTournament(TournamentResult tournament) {
        String sql = "INSERT INTO TOURNAMENT_RESULTS (player_id, tournament_name, tournament_date, " +
                    "ranking, prize_money, team) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tournament.getPlayerId());
            pstmt.setString(2, tournament.getTournamentName());
            pstmt.setDate(3, Date.valueOf(tournament.getTournamentDate()));
            pstmt.setString(4, tournament.getRanking());
            pstmt.setBigDecimal(5, tournament.getPrizeMoney());
            pstmt.setString(6, tournament.getTeam());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Tournament record added successfully, affected rows (添加比赛记录成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update tournament record (更新比赛记录)
    public boolean updateTournament(TournamentResult tournament) {
        String sql = "UPDATE TOURNAMENT_RESULTS SET player_id = ?, tournament_name = ?, tournament_date = ?, " +
                    "ranking = ?, prize_money = ?, team = ? WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tournament.getPlayerId());
            pstmt.setString(2, tournament.getTournamentName());
            pstmt.setDate(3, Date.valueOf(tournament.getTournamentDate()));
            pstmt.setString(4, tournament.getRanking());
            pstmt.setBigDecimal(5, tournament.getPrizeMoney());
            pstmt.setString(6, tournament.getTeam());
            pstmt.setInt(7, tournament.getRecordId());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Tournament record updated successfully, affected rows (更新比赛记录成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete tournament record (删除比赛记录)
    public boolean deleteTournament(int recordId) {
        String sql = "DELETE FROM TOURNAMENT_RESULTS WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Tournament record deleted successfully, affected rows (删除比赛记录成功，影响行数): " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Calculate total prize money for player (统计选手总奖金)
    public double getTotalPrizeByPlayer(int playerId) {
        String sql = "SELECT SUM(prize_money) as total_prize FROM TOURNAMENT_RESULTS WHERE player_id = ?";
        double totalPrize = 0.0;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalPrize = rs.getDouble("total_prize");
                }
            }
            System.out.println("Total prize for player (选手) " + playerId + " : " + totalPrize + " (的总奖金: " + totalPrize + ")");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPrize;
    }
}