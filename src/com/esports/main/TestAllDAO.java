package com.esports.main;

import com.esports.dao.*;
import com.esports.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestAllDAO {
    public static void main(String[] args) {
        PlayerDAO playerDAO = new PlayerDAO();
        ContractDAO contractDAO = new ContractDAO();
        TournamentDAO tournamentDAO = new TournamentDAO();
        
        System.out.println("=== Test all DAO classes ===\n");
        
        // 1. Test ContractDAO (测试ContractDAO)
        System.out.println("1. Test ContractDAO (测试ContractDAO)：");
        List<Contract> contracts = contractDAO.getAllContracts();
        for (Contract c : contracts) {
            System.out.println("Contract ID (合同ID): " + c.getContractId() + ", Player ID (选手ID): " + c.getPlayerId() + 
                             ", Type (类型): " + c.getContractType());
        }
        
        // Get Faker's contracts (using correct method name) (获取Faker的合同（使用正确的方法名）)
        System.out.println("\nFaker's contracts (Faker的合同)：");
        List<Contract> fakerContracts = contractDAO.getContractsByPlayerId(1);  // Method name correct (方法名正确)
        for (Contract c : fakerContracts) {
            System.out.println("Contract (合同): " + c.getStartDate() + " to (到) " + c.getEndDate() + 
                             ", Annual Salary (年薪): " + c.getAnnualSalary());
        }
        
        // 2. Test TournamentDAO (测试TournamentDAO)
        System.out.println("\n2. Test TournamentDAO (测试TournamentDAO)：");
        List<TournamentResult> tournaments = tournamentDAO.getAllTournaments();
        for (TournamentResult t : tournaments) {
            System.out.println("Tournament (比赛): " + t.getTournamentName() + ", Ranking (名次): " + t.getRanking() + 
                             ", Prize Money (奖金): " + t.getPrizeMoney());
        }
        
        // Calculate Faker's total prize money (统计Faker的总奖金)
        double fakerPrize = tournamentDAO.getTotalPrizeByPlayer(1);
        System.out.println("\nFaker's total prize money (Faker的总奖金): " + fakerPrize);
        
        // 3. Add new contract (添加新合同)
        System.out.println("\n3. Add new contract (for TestPlayer) (添加新合同（给TestPlayer）)：");
        Contract newContract = new Contract();
        newContract.setPlayerId(7);  // TestPlayer's ID (TestPlayer的ID)
        newContract.setStartDate(LocalDate.of(2024, 1, 1));
        newContract.setEndDate(LocalDate.of(2025, 12, 31));
        newContract.setAnnualSalary(new BigDecimal("1500000.00"));
        newContract.setContractType("Full-time");
        
        boolean contractAdded = contractDAO.addContract(newContract);  // Method name correct (方法名正确)
        System.out.println("Add contract result (添加合同结果): " + (contractAdded ? "Success (成功)" : "Failed (失败)"));
        
        // 4. Add new tournament record (添加新比赛记录)
        System.out.println("\n4. Add new tournament record (添加新比赛记录)：");
        TournamentResult newTournament = new TournamentResult();
        newTournament.setPlayerId(7);  // TestPlayer's ID (TestPlayer的ID)
        newTournament.setTournamentName("Test Tournament 2024");
        newTournament.setTournamentDate(LocalDate.of(2024, 3, 15));
        newTournament.setRanking("Champion");
        newTournament.setPrizeMoney(new BigDecimal("50000.00"));
        newTournament.setTeam("Test Team");
        
        boolean tournamentAdded = tournamentDAO.addTournament(newTournament);
        System.out.println("Add tournament record result (添加比赛记录结果): " + (tournamentAdded ? "Success (成功)" : "Failed (失败)"));
        
        // 5. Show all data again (再次显示所有数据)
        System.out.println("\n5. Final data statistics (最终数据统计)：");
        System.out.println("Total players (选手总数): " + playerDAO.getAllPlayers().size());
        System.out.println("Total contracts (合同总数): " + contractDAO.getAllContracts().size());
        System.out.println("Total tournament records (比赛记录总数): " + tournamentDAO.getAllTournaments().size());
        
        System.out.println("\n=== Test completed ===\n=== 测试完成 ===");
    }
}