package org.tbb.cli;

import org.tbb.crypto.Hash;
import org.tbb.core.Block;
import org.tbb.core.State;
import org.tbb.core.Transaction;
import org.tbb.core.TransactionType;
import org.tbb.crypto.HashUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.util.ArrayList;
import java.util.List;

@Command(name = "migrate", description = "Migrate the database to a new version")
public class MigrateCommand implements Runnable {
    @Mixin
    CommonOptions commonOptions;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk(commonOptions.rootDir);

            List<List<Transaction>> blockTransactions = new ArrayList<>();
            blockTransactions.add(new ArrayList<>(List.of(
                    new Transaction("andrej", "andrej", 3),
                    new Transaction("andrej", "andrej", 700, TransactionType.REWARD)
            )));
            blockTransactions.add(new ArrayList<>(List.of(
                    new Transaction("andrej", "babayaga", 2000),
                    new Transaction("andrej", "andrej", 100, TransactionType.REWARD),
                    new Transaction("babayaga", "andrej", 1),
                    new Transaction("babayaga", "caesar", 1000),
                    new Transaction("babayaga", "andrej", 50),
                    new Transaction("andrej", "andrej", 600, TransactionType.REWARD)
            )));
            blockTransactions.add(new ArrayList<>(List.of(
                    new Transaction("andrej", "andrej", 24700, TransactionType.REWARD)
            )));

            Hash blockHash = HashUtils.empty();
            for (int i = 0; i < blockTransactions.size(); i++) {
                List<Transaction> transactions = blockTransactions.get(i);
                Block block = new Block(blockHash, i, transactions
                );
                state.addBlock(block);
                blockHash = state.persist();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
