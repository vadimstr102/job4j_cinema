package ru.job4j.cinema.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.models.Place;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HallServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        List<Place> places = PsqlStore.instOf().findAllPlaces();
        String json = GSON.toJson(places);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Place place = GSON.fromJson(req.getReader(), Place.class);
        PsqlStore.instOf().updatePlace(place.getId(), true);
    }
}
