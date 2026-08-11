# Esports Team Management

JavaFX + MySQL desktop app for esports team ops (players / members, contracts, tournament results).

**Portfolio:** https://hawk327ml.github.io/

## Stack

- Java 17 + JavaFX (FXML under `src/com/esports/view/`)
- MySQL database `esports_manager`
- Maven (`pom.xml`)

## Quick start

1. Install **JDK 17+** and **Maven**.
2. Create schema:

```bash
mysql -u root -p < sql/schema.sql
```

3. Set DB password (do **not** hardcode secrets):

```powershell
$env:ESPORTS_DB_PASSWORD = "your_password"
```

4. Run:

```powershell
.\run.bat
```

or:

```bash
mvn clean javafx:run
```

Main class: `com.esports.main.EsportsApp`

Connection defaults (`DatabaseConnection.java`):

- URL: `jdbc:mysql://localhost:3306/esports_manager`
- User: `root`
- Password: env `ESPORTS_DB_PASSWORD` (local fallback `changeme`)

## Layout

```text
pom.xml
run.bat
sql/schema.sql
src/com/esports/
  main/          # EsportsApp + DAO smoke tests
  controller/
  dao/           # MEMBERS / CONTRACTS / TOURNAMENT_RESULTS
  model/
  view/          # FXML
  util/
docs/screenshots/
```

## Screenshots

See `docs/screenshots/`.

## Security

Local MySQL credentials were redacted before publish. Rotate any password that was previously committed in drafts.
