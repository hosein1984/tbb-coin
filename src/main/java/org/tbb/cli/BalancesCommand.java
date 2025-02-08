package org.tbb.cli;

import org.tbb.db.Account;
import org.tbb.db.State;
import picocli.CommandLine;

import java.io.IOException;
import java.text.ParseException;

@CommandLine.Command(name = "balances", description = "Prints the balances of all accounts or a specific account")
public class BalancesCommand implements Runnable {
    @CommandLine.Option(names = {"-a", "--account"}, description = "Account name", required = false)
    private String accountName;

    @Override
    public void run() {
        try {
            State state = State.initFromDisk();
            if (accountName != null) {
                Account account = new Account(accountName);
                long balance = state.getAccountBalance(account);
                System.out.printf("Account %s has a balance of %d\n", accountName, balance);
            } else {
                state.getBalances().forEach((account, balance) -> {
                    System.out.printf("Account %s has a balance of %d\n", account.name(), balance);
                });

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
