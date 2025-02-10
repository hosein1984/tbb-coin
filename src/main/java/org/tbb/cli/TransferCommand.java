package org.tbb.cli;

import org.tbb.db.Account;
import org.tbb.db.State;
import org.tbb.db.Transaction;
import picocli.CommandLine.*;

import java.io.IOException;
import java.text.ParseException;

@Command(name = "transfer", description = "Transfer tokens between accounts")
public class TransferCommand implements Runnable {
    @Mixin
    CommonOptions commonOptions;

    @Option(names = {"-f", "--from"}, description = "From account", required = true)
    private String from;

    @Option(names = {"-t", "--to"}, description = "To account", required = true)
    private String to;

    @Option(names = {"-a", "--amount"}, description = "Amount to transfer", required = true)
    private long amount;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk(commonOptions.rootDir);
            Transaction tx = new Transaction(new Account(from), new Account(to), amount);
            state.addTransaction(tx);
            state.persist();
            System.out.printf("Transferred %d from %s to %s\n", amount, from, to);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
