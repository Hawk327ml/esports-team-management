package com.esports.main;

import com.esports.dao.PlayerDAO;
import com.esports.model.Player;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestPlayerDAO {
    public static void main(String[] args) {
        PlayerDAO playerDAO = new PlayerDAO();
        
        System.out.println("=== Test PlayerDAO ===");
        
        // 1. Test getting all players (测试获取所有选手)
        System.out.println("\n1. Get all players (获取所有选手)：");
        List<Player> allPlayers = playerDAO.getAllPlayers();
        for (Player p : allPlayers) {
            System.out.println(p);
        }
        
        // 2. Test getting player by ID (测试根据ID获取选手)
        System.out.println("\n2. Get player by ID (ID=1) (根据ID获取选手（ID=1）)：");
        Player player = playerDAO.getPlayerById(1);
        if (player != null) {
            System.out.println("Found player (找到选手): " + player.getUsername() + ", Position (位置): " + player.getPosition());
        }
        
        // 3. Test filtering by position (using correct method name) (测试根据位置筛选（使用正确的方法名）)
        System.out.println("\n3. Get all Mid Laner players (获取所有Mid Laner选手)：");
        List<Player> midLaners = playerDAO.getPlayersByPosition("Mid");  // Simplified position name (简化位置名称)
        for (Player p : midLaners) {
            System.out.println(p.getUsername() + " - " + p.getSalary());
        }
        
        // 4. Test adding new player (测试添加新选手)
        System.out.println("\n4. Add new player (添加新选手)：");
        Player newPlayer = new Player();
        newPlayer.setUsername("TestPlayer2");  // Use different username (使用不同的用户名)
        newPlayer.setAge(21);
        newPlayer.setPosition("Jungle");
        newPlayer.setJoinDate(LocalDate.now());
        newPlayer.setSalary(new BigDecimal("800000.00"));
        newPlayer.setStatus("Active");
        
        boolean added = playerDAO.addPlayer(newPlayer);  // Method name correct (方法名正确)
        System.out.println("Add result (添加结果): " + (added ? "Success (成功)" : "Failed (失败)"));
        
        // Show all players again (再次显示所有选手)
        System.out.println("\n5. Show all players again (再次显示所有选手)：");
        allPlayers = playerDAO.getAllPlayers();
        for (Player p : allPlayers) {
            System.out.println(p);
        }
        
        System.out.println("\n=== Test completed ===\n=== 测试完成 ===");
    }
}