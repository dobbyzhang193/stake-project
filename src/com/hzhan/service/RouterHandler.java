package com.hzhan.service;

import com.hzhan.Enum.Action;
import com.hzhan.Enum.HttpMethod;
import com.hzhan.Enum.HttpStatusCode;
import com.hzhan.model.Session;
import com.hzhan.util.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this class manage the incoming http request, and dispatch the url path to the specific handler
 */
public class RouterHandler implements HttpHandler {
    private static final String error = "bad request, method not allowed";
    private final SessionHandler sessionHandler;
    private final StakeHandler stakeHandler;

    public RouterHandler(SessionHandler sessionHandler, StakeHandler stakeHandler) {
        this.sessionHandler = sessionHandler;
        this.stakeHandler = stakeHandler;
    }
    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     *
     * @throws NullPointerException if exchange is {@code null}
     * @throws IOException          if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println("method: " + method + ", path:" + path);

        //remove the first slash ('/') and split the path url
        String[] splitPath = path.substring(1).split("/");

        if(splitPath.length != 2) {
            String error = "bad request, please verify the url path";
            System.out.println(error);
            HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_FOUND, error);
            return;
        }

        int id = Integer.parseInt(splitPath[0]);
        String action = splitPath[1];

        if(Action.SESSION.getValue().equals(action)) {
            if(HttpMethod.POST.name().equals(method)) {
                HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_FOUND, error);
                return;
            }
            sessionHandler(exchange, id, sessionHandler);

        }else if(Action.STAKE.getValue().equals(action)) {
            if(HttpMethod.GET.name().equals(method)) {
                HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_FOUND, error);
                return;
            }
            stakeHandler(exchange, id);

        }else if(Action.HIGHSTAKES.getValue().equals(action)) {
            if(HttpMethod.POST.name().equals(method)) {
                HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_FOUND, error);
                return;
            }
            highStakesHandler(exchange, id);
        }
    }

    public void sessionHandler(HttpExchange exchange, int customerId, SessionHandler sessionHandler) {
        Session session = sessionHandler.getOrCreatSession(customerId);
        HttpUtil.sendResponse(exchange, HttpStatusCode.OK, session.getKey());
        //sessionHandler.testPrint();
    }

    public void stakeHandler(HttpExchange exchange, int betOfferId) {
        String[] parameter = exchange.getRequestURI().getQuery().split("=");

        try {
            String body = new String(
                    exchange.getRequestBody()
                            .readAllBytes(), StandardCharsets.UTF_8);

            int stake = Integer.parseInt(body);

            //System.out.println("betOfferId:" + betOfferId + ", stake:" + stake + ", sessionKey:" + parameter[1]);

            if(!sessionHandler.isValidSession(parameter[1])) {
                String error = "session expired or not found";
                HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_ALLOWED, error);
                return;
            }

            stakeHandler.postStake(betOfferId, stake, sessionHandler.getCustomerIdBySessionKey(parameter[1]));

            HttpUtil.sendResponse(exchange, HttpStatusCode.OK, "");

        } catch(IOException | NumberFormatException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void highStakesHandler(HttpExchange exchange, int betOfferId) {
        //System.out.println("the bet offer id is:" + betOfferId);
        List<Map.Entry<Integer, Integer>> res = stakeHandler.getHighestStakes(betOfferId);

        if(res == null) {
            HttpUtil.sendResponse(exchange, HttpStatusCode.NOT_FOUND, betOfferId + "not found");
            return;
        }

        String response = res.stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(","));

        HttpUtil.sendResponse(exchange, HttpStatusCode.OK, response);
    }
}
