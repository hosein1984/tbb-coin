package org.tbb.cli;

import org.tbb.db.Block;
import org.tbb.db.State;
import org.tbb.db.Transaction;
import org.tbb.db.TransactionType;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.io.IOException;
import java.text.ParseException;
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

            Block block0 = new Block(new ArrayList<>(List.of(
                    new Transaction("andrej", "andrej", 3),
                    new Transaction("andrej", "andrej", 700, TransactionType.REWARD)
            )));
            state.addBlock(block0);
            String block0Hash = state.persist();

            Block block1 = new Block(
                    block0Hash,
                    new ArrayList<>(List.of(
                            new Transaction("andrej", "babayaga", 2000),
                            new Transaction("andrej", "andrej", 100, TransactionType.REWARD),
                            new Transaction("babayaga", "andrej", 1),
                            new Transaction("babayaga", "caesar", 1000),
                            new Transaction("babayaga", "andrej", 50),
                            new Transaction("andrej", "andrej", 600, TransactionType.REWARD)
                    ))
            );
            state.addBlock(block1);
            state.persist();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
