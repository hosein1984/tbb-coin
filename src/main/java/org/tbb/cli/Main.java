package org.tbb.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "tbb",
        mixinStandardHelpOptions = true,
        subcommands = {
                VersionCommand.class,
                BalancesCommand.class,
                TransferCommand.class,
                RewardCommand.class,
        })
public class Main implements Runnable {

    @Override
    public void run() {
        // user must use a subcommand
        throw new CommandLine.ParameterException(new CommandLine(this), "Missing required command");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}