package ru.job4j.cinema.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.models.Account;
import ru.job4j.cinema.models.Ticket;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PaymentServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Ticket ticket = GSON.fromJson(req.getReader(), Ticket.class);
        Account account = new Account(0, ticket.getName(), ticket.getEmail(), ticket.getPhone());
        PsqlStore.instOf().saveAccount(account);
        System.out.println(account.getId());
        ticket.setAccountId(account.getId());

        resp.setContentType("application/json; charset=utf-8");
        boolean response = PsqlStore.instOf().saveTicket(ticket);
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(response);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
