package org.tbb.cli;

import org.tbb.db.Account;
import org.tbb.db.State;
import org.tbb.db.Transaction;
import org.tbb.db.TransactionType;
import picocli.CommandLine;

import java.io.IOException;
import java.text.ParseException;

@CommandLine.Command(name = "reward", description = "Reward tokens to an account")
public class RewardCommand implements Runnable {
    @CommandLine.Option(names = {"-t", "--to"}, description = "To account", required = true)
    private String to;

    @CommandLine.Option(names = {"-a", "--amount"}, description = "Amount to transfer", required = true)
    private long amount;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk();
            Transaction tx = new Transaction(new Account("andrej"), new Account(to), amount, TransactionType.REWARD);
            state.addTransaction(tx);
            state.persist();
            System.out.printf("Rewarded %d to %s\n", amount, to);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
