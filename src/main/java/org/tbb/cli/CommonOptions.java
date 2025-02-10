package org.tbb.cli;

import picocli.CommandLine.Option;

class CommonOptions {
    @Option(names = {"-v", "--verbose"}, description = "Enable verbose mode.")
    boolean verbose;

    @Option(names = {"-d", "--dir"}, description = "Root directory for the database.", defaultValue = "data")
    String rootDir;
}
