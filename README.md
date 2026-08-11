# Esports Team Management

JavaFX + MySQL desktop app for esports team ops (players / members, contracts, tournament results).

**Portfolio:** https://hawk327ml.github.io/

## Stack

- Java + JavaFX (FXML views under `src/com/esports/view/`)
- MySQL database `esports_manager`
- Package root: `com.esports`

## Setup

1. Install JDK 17+ with JavaFX support (or add JavaFX modules / SDK).
2. Create DB schema:

```bash
mysql -u root -p < sql/schema.sql
```

3. Set DB password via environment variable (do **not** hardcode secrets):

```bash
# PowerShell
$env:ESPORTS_DB_PASSWORD = "your_password"
```

4. Add MySQL Connector/J to the classpath.
5. Run main class: `com.esports.main.EsportsApp`

Connection defaults (`DatabaseConnection.java`):

- URL: `jdbc:mysql://localhost:3306/esports_manager`
- User: `root`
- Password: `ESPORTS_DB_PASSWORD` env (fallback `changeme` for local only)

## Project layout

```text
src/com/esports/
  main/          # EsportsApp + DAO smoke tests
  controller/    # JavaFX controllers
  dao/           # MEMBERS / CONTRACTS / TOURNAMENT_RESULTS
  model/
  view/          # FXML
  util/          # DatabaseConnection
docs/screenshots/
sql/schema.sql
```

## Screenshots

See `docs/screenshots/`.

## Security

Local MySQL credentials were redacted before publish. Rotate any password that was previously committed in drafts.
