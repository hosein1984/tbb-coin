package org.tbb.cli;

import org.tbb.node.Node;
import picocli.CommandLine.*;

import java.util.concurrent.CountDownLatch;

@Command(name = "serve", description = "Run the TBB node.")
public class ServeCommand implements Runnable {
    @Mixin
    CommonOptions commonOptions;

    @Option(names = {"-p", "--port"}, description = "Port to listen on", defaultValue = "8080")
    private int port;

    @Override
    public void run() {
        try {
            Node node = new Node(commonOptions.rootDir, port);
            node.Run();

            // Wait forever
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

