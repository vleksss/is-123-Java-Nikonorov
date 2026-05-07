package com.auction.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DatabaseBootstrap {
    private DatabaseBootstrap() {
    }

    public static void initialize() {
        try {
            String host = getEnv("DB_HOST", "localhost");
            String port = getEnv("DB_PORT", "3050");
            String username = getEnv("DB_USERNAME", "SYSDBA");
            String password = getEnv("DB_PASSWORD", "masterkey");
            String defaultDbPath = Paths.get(System.getProperty("user.dir"), "database", "auction.fdb").toString();
            String dbPathValue = getEnv("DB_PATH", defaultDbPath);

            Path dbPath = Paths.get(dbPathValue).toAbsolutePath().normalize();
            if (Files.exists(dbPath)) {
                return;
            }

            Path parent = dbPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String isqlPath = resolveIsqlPath();

            Path createScript = Files.createTempFile("auction-create-db", ".sql");
            Path schemaScript = copyResourceToTempFile("db/schema-red.sql", "auction-schema", ".sql");
            Path demoScript = copyResourceToTempFile("db/demo-data.sql", "auction-demo", ".sql");

            String createSql = ""
                    + "CREATE DATABASE 'localhost:" + dbPath.toString().replace("\\", "/") + "'\n"
                    + "USER '" + username + "' PASSWORD '" + password + "'\n"
                    + "PAGE_SIZE 8192\n"
                    + "DEFAULT CHARACTER SET UTF8;\n"
                    + "QUIT;\n";

            Files.writeString(createScript, createSql, StandardCharsets.UTF_8);

            runProcess(new ProcessBuilder(isqlPath, "-i", createScript.toString()), "Не удалось создать базу данных");
            runProcess(new ProcessBuilder(
                    isqlPath,
                    "-bail",
                    "-user", username,
                    "-password", password,
                    dbPath.toString(),
                    "-i", schemaScript.toString()
            ), "Не удалось применить schema-red.sql");
            runProcess(new ProcessBuilder(
                    isqlPath,
                    "-bail",
                    "-user", username,
                    "-password", password,
                    dbPath.toString(),
                    "-i", demoScript.toString()
            ), "Не удалось применить demo-data.sql");

            Files.deleteIfExists(createScript);
            Files.deleteIfExists(schemaScript);
            Files.deleteIfExists(demoScript);
        } catch (Exception ex) {
            throw new IllegalStateException("Не удалось автоматически создать и инициализировать базу данных", ex);
        }
    }

    private static String resolveIsqlPath() {
        String envValue = System.getenv("ISQL_PATH");
        if (envValue != null && !envValue.isBlank() && Files.exists(Paths.get(envValue))) {
            return envValue;
        }

        Path redDatabase = Paths.get("C:/Program Files/RedDatabase/isql.exe");
        if (Files.exists(redDatabase)) {
            return redDatabase.toString();
        }

        Path redDatabaseBin = Paths.get("C:/Program Files/RedDatabase/bin/isql.exe");
        if (Files.exists(redDatabaseBin)) {
            return redDatabaseBin.toString();
        }

        Path firebird = Paths.get("C:/Program Files/Firebird/Firebird_3_0/isql.exe");
        if (Files.exists(firebird)) {
            return firebird.toString();
        }

        Path firebirdBin = Paths.get("C:/Program Files/Firebird/Firebird_3_0/bin/isql.exe");
        if (Files.exists(firebirdBin)) {
            return firebirdBin.toString();
        }

        return "isql";
    }

    private static Path copyResourceToTempFile(String resourcePath, String prefix, String suffix) throws IOException {
        Path tempFile = Files.createTempFile(prefix, suffix);
        try (InputStream inputStream = DatabaseBootstrap.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Ресурс не найден: " + resourcePath);
            }
            Files.write(tempFile, inputStream.readAllBytes());
        }
        return tempFile;
    }

    private static void runProcess(ProcessBuilder processBuilder, String errorMessage) throws IOException, InterruptedException {
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException(errorMessage + System.lineSeparator() + output);
        }
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}