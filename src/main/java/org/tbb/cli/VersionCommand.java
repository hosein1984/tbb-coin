package org.tbb.cli;

import org.tbb.utils.AppInfo;
import picocli.CommandLine;

@CommandLine.Command(name = "version", description = "Prints the version of the application")
public class VersionCommand implements Runnable {
    @Override
    public void run() {
        System.out.printf("Running %s version %s\n", AppInfo.NAME, AppInfo.getVersion());
    }
}
