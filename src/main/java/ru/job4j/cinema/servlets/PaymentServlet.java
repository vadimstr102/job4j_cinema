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
import java.sql.SQLException;

public class PaymentServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Ticket ticket = GSON.fromJson(req.getReader(), Ticket.class);
        Account account = new Account(0, ticket.getName(), ticket.getEmail(), ticket.getPhone());
        PsqlStore.instOf().saveAccount(account);
        ticket.setAccountId(account.getId());
        try {
            PsqlStore.instOf().saveTicket(ticket);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }
}
