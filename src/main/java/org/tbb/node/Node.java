package org.tbb.node;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.tbb.core.Account;
import org.tbb.core.State;
import org.tbb.core.Transaction;
import org.tbb.core.TransactionType;
import org.tbb.crypto.Hash;
import org.tbb.json.JsonUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Map;

public class Node {
    private final int port;
    private final State state;
    private final Javalin router;

    public Node(String rootDir, int port) throws IOException, ParseException {
        this.port = port;
        this.state = State.initFromDisk(rootDir);
        this.router = this.setupRouter();
        this.registerRoutes();
    }

    public void Run() {
        System.out.println("Running TBB node on port " + port);
        router.start(port);
    }

    public Javalin setupRouter() {
        JsonMapper jsonMapper = new JsonMapper() {
            @NotNull
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return JsonUtils.toJson(obj);
            }

            @NotNull
            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return JsonUtils.fromJson(json, targetType);
            }
        };

        return Javalin.create(
                config -> {
                    config.jsonMapper(jsonMapper);
                }
        );
    }

    public void registerRoutes() {
        router.get("/balances/list", this::listBalancesHandler);
        router.get("/balances/{account}", this::getAccountBalanceHandler);
        router.post("/transactions/add", this::addTransactionHandler);
    }

    private void listBalancesHandler(Context ctx) {
        record Response(String status, Map<Account, Long> balances, Hash latestBlockHash) {
        }

        Response response = new Response("ok", state.getBalances(), state.getLatestBlockHash());
        ctx.json(response);
    }

    private void getAccountBalanceHandler(Context ctx) {
        record Response(String status, String account, long balance, Hash latestBlockHash) {
        }

        String accountName = ctx.pathParam("account");
        Account account = new Account(accountName);
        long balance = state.getAccountBalance(account);

        Response response = new Response("ok", accountName, balance, state.getLatestBlockHash());
        ctx.json(response);
    }

    private void addTransactionHandler(Context ctx) {
        record Request(String from, String to, long amount, String type) {
        }
        record Response(String status, Hash latestBlockHash) {
        }

        Request request = ctx.bodyValidator(Request.class)
                .check(req -> req.from() != null && !req.from().isBlank(), "Sender (from) cannot be empty")
                .check(req -> req.to() != null && !req.to().isBlank(), "Receiver (to) cannot be empty")
                .check(req -> req.amount() > 0, "Amount must be greater than 0")
                .check(req -> req.type() == null || "transfer".equals(req.type()) || "reward".equals(req.type()), "Invalid transaction type")
                .get();
        Transaction tx = new Transaction(
                request.from(),
                request.to(),
                request.amount(),
                request.type() == null ? TransactionType.TRANSFER : TransactionType.parse(request.type())
        );
        state.addTransaction(tx);
        state.persist();

        Response response = new Response("ok", state.getLatestBlockHash());
        ctx.json(response);
    }
}
