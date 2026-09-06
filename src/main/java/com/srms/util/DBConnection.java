package com.srms.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection - HikariCP Connection Pool Manager.
 * Supports H2 (Embedded zero-setup default) and MySQL 8.x.
 * Auto-initializes schema and seed data if tables do not exist.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static synchronized void initDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        try {
            HikariConfig config = new HikariConfig();

            String dbUrl = System.getProperty("db.url", System.getenv("DB_URL"));
            String dbUser = System.getProperty("db.user", System.getenv("DB_USER"));
            String dbPass = System.getProperty("db.password", System.getenv("DB_PASSWORD"));

            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                LOGGER.info("Initializing HikariCP Pool for external DB: " + dbUrl);
                config.setJdbcUrl(dbUrl);
                config.setUsername(dbUser != null ? dbUser : "root");
                config.setPassword(dbPass != null ? dbPass : "");
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            } else {
                LOGGER.info("No external DB configured. Using embedded H2 Database for instant execution.");
                config.setJdbcUrl("jdbc:h2:mem:srmsdb;DB_CLOSE_DELAY=-1;MODE=MySQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
                config.setUsername("sa");
                config.setPassword("");
                config.setDriverClassName("org.h2.Driver");
            }

            config.setMaximumPoolSize(20);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setLeakDetectionThreshold(15000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);

            // Execute auto-initialization of schema and seed data
            autoInitSchemaAndSeed();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize DB connection pool", e);
            throw new RuntimeException("Database Connection Pool Initialization Error", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initDataSource();
        }
        return dataSource.getConnection();
    }

    private static void autoInitSchemaAndSeed() {
        try (Connection conn = dataSource.getConnection()) {
            boolean tableExists = false;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1 FROM users LIMIT 1")) {
                tableExists = true;
            } catch (SQLException e) {
                tableExists = false;
            }

            if (!tableExists) {
                LOGGER.info("Executing schema.sql and seed_data.sql for initial database population...");
                executeSqlScript(conn, "schema.sql");
                executeSqlScript(conn, "seed_data.sql");
                LOGGER.info("Database schema and seed data initialized successfully!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Auto-initialization of schema/seed data encountered an issue: " + e.getMessage(), e);
        }
    }

    private static void executeSqlScript(Connection conn, String scriptName) {
        InputStream is = DBConnection.class.getClassLoader().getResourceAsStream(scriptName);
        if (is == null) {
            LOGGER.warning("Could not find script file: " + scriptName);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Statement stmt = conn.createStatement()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sb.toString().replace(";", "").trim();
                    if (!sql.isEmpty()) {
                        try {
                            stmt.execute(sql);
                        } catch (Exception ex) {
                            LOGGER.log(Level.WARNING, "SQL Execution Note for script [" + scriptName + "]: " + ex.getMessage());
                        }
                    }
                    sb.setLength(0);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL script " + scriptName, e);
        }
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
