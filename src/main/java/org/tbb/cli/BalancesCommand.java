package org.tbb.cli;

import org.tbb.crypto.Hash;
import org.tbb.core.Account;
import org.tbb.core.State;
import picocli.CommandLine.*;

import java.io.IOException;
import java.text.ParseException;

@Command(name = "balances", description = "Prints the balances of all accounts or a specific account")
public class BalancesCommand implements Runnable {
    @Mixin
    CommonOptions commonOptions;

    @Option(names = {"-a", "--account"}, description = "Account name", required = false)
    private String accountName;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk(commonOptions.rootDir);
            Hash blockHash = state.getLatestBlockHash();
            if (accountName != null) {
                Account account = new Account(accountName);
                long balance = state.getAccountBalance(account);

                System.out.printf("Account %s has a balance of %d at: %s\n", account.name(), balance, blockHash);
            } else {
                System.out.println("Account balances at: " + blockHash);
                state.getBalances().forEach((account, balance) -> {
                    System.out.printf("Account %s has a balance of %d\n", account.name(), balance);
                });

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
