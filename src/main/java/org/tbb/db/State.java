package org.tbb.db;

import org.tbb.json.JsonUtils;
import org.tbb.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

public class State {
    private final Map<Account, Long> balances = new HashMap<>();
    private final List<Transaction> txMempool = new ArrayList<>();
    private String latestBlockHash;

    private File dbFile;

    private State(File dbFile, Map<Account, Long> balances) {
        this.dbFile = dbFile;
        this.balances.putAll(balances);
        this.latestBlockHash = BlockHeader.EMPTY_HASH;
    }

    public static State initFromDisk(String rootDir) throws IOException, ParseException {
        FileUtils.initDatabaseDir(rootDir);

        Genesis genesis = Genesis.initFromDisk(FileUtils.getGenesisFilePath(rootDir));
        Path dbFilePath = FileUtils.getBlocksFilePath(rootDir);
        File dbFile = dbFilePath.toFile();
        State state = new State(dbFile, genesis.getBalances());

        try (BufferedReader reader = Files.newBufferedReader(dbFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                BlockFs blockFs = JsonUtils.fromJson(line, BlockFs.class);
                state.applyBlock(blockFs.block());
                state.latestBlockHash = blockFs.hash();
            }
        }


        return state;
    }

    public void addBlock(Block block) {
        for (Transaction tx : block.body().transactions()) {
            this.addTransaction(tx);
        }
    }

    private void applyBlock(Block block) {
        for (Transaction tx : block.body().transactions()) {
            this.applyTransaction(tx);
        }
    }

    public void addTransaction(Transaction tx) {
        this.applyTransaction(tx);
        this.txMempool.add(tx);
    }

    public void applyTransaction(Transaction tx) {
        if (tx.isReward()) {
            this.balances.merge(tx.to(), tx.amount(), Long::sum);
        } else {
            long fromBalance = this.balances.getOrDefault(tx.from(), 0L);
            if (fromBalance < tx.amount()) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            this.balances.merge(tx.from(), -tx.amount(), Long::sum);
            this.balances.merge(tx.to(), tx.amount(), Long::sum);
        }
    }

    public String persist() {
        Block block = new Block(this.latestBlockHash, this.txMempool);
        String blockHash = block.hash();
        BlockFs blockFs = new BlockFs(blockHash, block);

        try (FileWriter writer = new FileWriter(this.dbFile, true)) {
            String json = JsonUtils.toJson(blockFs);
            writer.write(json);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.latestBlockHash = blockHash;
        this.txMempool.clear();

        return blockHash;
    }


    public String getLatestBlockHash() {
        return this.latestBlockHash;
    }

    public long getAccountBalance(Account account) {
        return this.balances.getOrDefault(account, 0L);
    }

    public Map<Account, Long> getBalances() {
        return this.balances;
    }
}
