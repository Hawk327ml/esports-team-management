-- Optional demo rows for local walkthrough (run after schema.sql)
USE esports_manager;

INSERT INTO MEMBERS (username, real_name, age, position, nationality, join_date, salary, status) VALUES
  ('ViperX', 'Alex Chen', 21, 'Duelist', 'MY', '2024-03-01', 4500.00, 'Active'),
  ('Nova', 'Sara Lim', 23, 'Controller', 'SG', '2023-11-15', 5200.00, 'Active'),
  ('Anchor', 'Kim Jae', 25, 'Sentinel', 'KR', '2022-08-20', 6100.00, 'Active');

INSERT INTO CONTRACTS (player_id, team_name, start_date, end_date, annual_salary, contract_type) VALUES
  (1, 'HAWK Academy', '2024-03-01', '2025-03-01', 54000.00, 'Pro'),
  (2, 'HAWK Academy', '2023-11-15', '2025-11-15', 62400.00, 'Pro'),
  (3, 'HAWK Academy', '2022-08-20', '2024-08-20', 73200.00, 'Pro');

INSERT INTO TOURNAMENT_RESULTS (player_id, tournament_name, tournament_date, ranking, prize_money, team) VALUES
  (1, 'SEA Challengers 2024', '2024-06-12', '2nd', 8000.00, 'HAWK Academy'),
  (2, 'SEA Challengers 2024', '2024-06-12', '2nd', 8000.00, 'HAWK Academy'),
  (3, 'Campus Cup 2023', '2023-12-03', '1st', 12000.00, 'HAWK Academy');
