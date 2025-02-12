package org.tbb.utils;

import org.tbb.core.Genesis;
import org.tbb.json.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    public static final String DATABASE_DIRECTORY_NAME = "database";
    public static final String GENESIS_FILE_NAME = "genesis.json";
    public static final String BLOCKS_FILE_NAME = "blocks.db";

    public static Path getDatabaseDirPath(String rootDir) {
        return expandPath(rootDir).resolve(DATABASE_DIRECTORY_NAME);
    }

    public static Path getGenesisFilePath(String rootDir) {
        return getDatabaseDirPath(rootDir).resolve(GENESIS_FILE_NAME);
    }

    public static Path getBlocksFilePath(String rootDir) {
        return getDatabaseDirPath(rootDir).resolve(BLOCKS_FILE_NAME);
    }

    public static boolean fileExists(Path path) {
        return path.toFile().exists();
    }

    // Expands a file path
// 1. replace tilde with users home dir
// 2. expands embedded environment variables
// 3. cleans the path, e.g. /a/b/../c -> /a/c
// Note, it has limitations, e.g. ~someuser/tmp will not be expanded
    public static Path expandPath(String path) {
        if (path.contains(":") || path.contains("~")) {
            return Paths.get(path);
        }

        if (path.startsWith("~/") || path.startsWith("~\\")) {
            String home = System.getProperty("user.home");
            path = home + path.substring(1);
        }

        path = expandEnvVars(path);

        return Paths.get(path).normalize();
    }

    private static String expandEnvVars(String path) {
        Pattern envPattern = Pattern.compile("\\$\\{([^}]+)\\}|\\$(\\w+)");
        Matcher matcher = envPattern.matcher(path);

        StringBuilder expandedPath = new StringBuilder();
        while (matcher.find()) {
            String varName = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            String value = System.getenv(varName);
            matcher.appendReplacement(expandedPath, value != null ? value : "");
        }
        matcher.appendTail(expandedPath);

        return expandedPath.toString();
    }

    public static boolean directoryExists(Path path) {
        return path.toFile().isDirectory();
    }

    public static void initDatabaseDir(String rootDir) {
        Path databaseDir = getDatabaseDirPath(rootDir);
        if (!directoryExists(databaseDir)) {
            boolean success = databaseDir.toFile().mkdirs();
            if (!success) {
                throw new RuntimeException("Failed to create database directory");
            }
        }

        Path genesisFilePath = getGenesisFilePath(rootDir);
        if (!fileExists(genesisFilePath)) {
            initGenesisFile(genesisFilePath);
        }

        Path blocksFilePath = getBlocksFilePath(rootDir);
        if (!fileExists(blocksFilePath)) {
            initBlocksFile(blocksFilePath);
        }
    }

    private static void initGenesisFile(Path path) {
        String json = JsonUtils.toJson(Genesis.DEFAULT);
        try {
            Files.createFile(path);
            Files.writeString(path, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write genesis file", e);
        }
    }

    private static void initBlocksFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create blocks file", e);
        }
    }


}
