package com.hzhan.util;

import com.hzhan.Enum.HttpStatusCode;
import com.hzhan.service.SessionHandler;
import com.hzhan.service.SessionHandlerImpl;
import com.hzhan.service.StakeHandlerImpl;
import com.hzhan.service.RouterHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class HttpUtil {
    public static void startServer(int port, int backlog) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        SessionHandler sessionHandler = new SessionHandlerImpl();
        server.createContext("/", new RouterHandler(sessionHandler, new StakeHandlerImpl()));
        server.setExecutor(Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 2));
        server.start();
    }

    public static void sendResponse(HttpExchange exchange, HttpStatusCode code, String response) {

        try(exchange) {
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(code.getValue(), response.getBytes(StandardCharsets.UTF_8).length);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
