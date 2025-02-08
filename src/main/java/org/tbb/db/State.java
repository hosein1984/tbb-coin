package org.tbb.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class State {
    private final Map<Account, Long> balances = new HashMap<>();
    private final List<Transaction> txMempool = new ArrayList<>();

    private File dbFile;

    private State(File dbFile, Map<Account, Long> balances) {
        this.dbFile = dbFile;
        this.balances.putAll(balances);
    }

    public static State initFromDisk() throws IOException, ParseException {
        String cwd = System.getProperty("user.dir");

        Path databasePath = Paths.get(cwd, "database");

        Path genesisFilePath = databasePath.resolve("genesis.json");
        Genesis genesis = Genesis.initFromDisk(genesisFilePath);

        Path dbFilePath = databasePath.resolve("tx.db");
        File dbFile = dbFilePath.toFile();
        if (!dbFile.exists()) {
            throw new IOException("Database file does not exist");
        }

        State state = new State(dbFile, genesis.getBalances());

        try (BufferedReader reader = Files.newBufferedReader(dbFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction tx = Transaction.parseJson(line);
                state.applyTransaction(tx);
            }
        }

        return state;
    }

    public void addTransaction(Transaction tx) {
        this.applyTransaction(tx);
        this.txMempool.add(tx);
    }

    public void applyTransaction(Transaction tx) {
        if (tx.isReward()) {
            long balance = this.balances.getOrDefault(tx.to(), 0L);
            this.balances.put(tx.to(), balance + tx.amount());
        } else {
            long fromBalance = this.balances.getOrDefault(tx.from(), 0L);
            if (fromBalance < tx.amount()) {
                throw new IllegalArgumentException("Insufficient balance");
            }

            this.balances.merge(tx.from(), -tx.amount(), Long::sum);
            this.balances.merge(tx.to(), tx.amount(), Long::sum);
        }
    }

    public void persist() {
        try (FileWriter writer = new FileWriter(this.dbFile, true)) {
            Iterator<Transaction> it = this.txMempool.iterator();
            while (it.hasNext()) {
                Transaction tx = it.next();
                writer.write(tx.toJson());
                writer.write("\n");
                writer.flush();
                it.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getAccountBalance(Account account) {
        return this.balances.getOrDefault(account, 0L);
    }

    public Map<Account, Long> getBalances() {
        return this.balances;
    }
}
