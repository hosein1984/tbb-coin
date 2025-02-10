package org.tbb.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.util.List;

@Command(
        name = "tbb",
        mixinStandardHelpOptions = true,
        subcommands = {
                VersionCommand.class,
                BalancesCommand.class,
                TransferCommand.class,
                RewardCommand.class,
                MigrateCommand.class
        })
public class Main implements Runnable {
    @Mixin
    CommonOptions commonOptions;

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