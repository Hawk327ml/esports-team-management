-- Schema inferred from DAO SQL (MySQL 8+)
CREATE DATABASE IF NOT EXISTS esports_manager
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE esports_manager;

CREATE TABLE IF NOT EXISTS MEMBERS (
  player_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  real_name VARCHAR(100),
  age INT,
  position VARCHAR(100),
  nationality VARCHAR(100),
  join_date DATE,
  salary DECIMAL(12, 2),
  status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS CONTRACTS (
  contract_id INT PRIMARY KEY AUTO_INCREMENT,
  player_id INT,
  team_name VARCHAR(150),
  start_date DATE,
  end_date DATE,
  annual_salary DECIMAL(12, 2),
  contract_type VARCHAR(100),
  CONSTRAINT fk_contracts_member FOREIGN KEY (player_id) REFERENCES MEMBERS(player_id)
);

CREATE TABLE IF NOT EXISTS TOURNAMENT_RESULTS (
  record_id INT PRIMARY KEY AUTO_INCREMENT,
  player_id INT,
  tournament_name VARCHAR(150) NOT NULL,
  tournament_date DATE,
  ranking VARCHAR(50),
  prize_money DECIMAL(12, 2),
  team VARCHAR(150),
  CONSTRAINT fk_tournament_member FOREIGN KEY (player_id) REFERENCES MEMBERS(player_id)
);
