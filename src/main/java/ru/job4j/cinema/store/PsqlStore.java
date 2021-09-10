package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.models.Account;
import ru.job4j.cinema.models.Place;
import ru.job4j.cinema.models.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db_job4j_cinema.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final PsqlStore INST = new PsqlStore();
    }

    public static PsqlStore instOf() {
        return Lazy.INST;
    }

    public void updatePlace(int id, boolean isBooked) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE hall SET isBooked = ? WHERE id = ?"
             )
        ) {
            ps.setBoolean(1, isBooked);
            ps.setInt(2, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in PsqlStore.updatePlace()", e);
        }
    }

    public List<Place> findAllPlaces() {
        List<Place> places = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM hall")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Place place = new Place(
                            it.getInt("id"),
                            it.getInt("row"),
                            it.getInt("cell"),
                            it.getBoolean("isBooked"));
                    places.add(place);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in PsqlStore.findAllPlaces()", e);
        }
        return places;
    }

    public void saveAccount(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO account(username, email, phone) VALUES (?, ?, ?) ON CONFLICT DO NOTHING"
             );
             PreparedStatement ps2 = cn.prepareStatement("SELECT id FROM account WHERE email = ?")
        ) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            ps2.setString(1, account.getEmail());
            try (ResultSet it = ps2.executeQuery()) {
                if (it.next()) {
                    account.setId(it.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in PsqlStore.saveAccount()", e);
        }
    }

    public void saveTicket(Ticket ticket) throws Exception {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO ticket(row, cell, account_id) VALUES (?, ?, ?)"
             );
             PreparedStatement ps2 = cn.prepareStatement("SELECT id FROM ticket WHERE row = ? AND cell = ?")
        ) {
            ps.setInt(1, ticket.getRow());
            ps.setInt(2, ticket.getCell());
            ps.setInt(3, ticket.getAccountId());
            ps.execute();
            ps2.setInt(1, ticket.getRow());
            ps2.setInt(2, ticket.getCell());
            try (ResultSet it = ps2.executeQuery()) {
                if (it.next()) {
                    ticket.setId(it.getInt("id"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in PsqlStore.saveTicket()", e);
            throw e;
        }
    }
}
