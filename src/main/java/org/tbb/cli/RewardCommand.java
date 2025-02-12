package org.tbb.cli;

import org.tbb.core.Account;
import org.tbb.core.State;
import org.tbb.core.Transaction;
import org.tbb.core.TransactionType;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.text.ParseException;

@Command(name = "reward", description = "Reward tokens to an account")
public class RewardCommand implements Runnable {
    @Mixin
    CommonOptions commonOptions;

    @Option(names = {"-t", "--to"}, description = "To account", required = true)
    private String to;

    @Option(names = {"-a", "--amount"}, description = "Amount to transfer", required = true)
    private long amount;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk(commonOptions.rootDir);
            Transaction tx = new Transaction(new Account("andrej"), new Account(to), amount, TransactionType.REWARD);
            state.addTransaction(tx);
            state.persist();
            System.out.printf("Rewarded %d to %s\n", amount, to);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
