package org.tbb.utils;

import org.tbb.db.Genesis;
import org.tbb.json.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static final String DATABASE_DIRECTORY_NAME = "database";
    public static final String GENESIS_FILE_NAME = "genesis.json";
    public static final String BLOCKS_FILE_NAME = "blocks.db";

    public static Path getDatabaseDirPath(String rootDir) {
        return Paths.get(rootDir, DATABASE_DIRECTORY_NAME);
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
