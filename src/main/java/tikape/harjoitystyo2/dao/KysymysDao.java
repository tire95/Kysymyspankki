/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.harjoitystyo2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.harjoitustyo2.database.Database;
import tikape.harjoitustyo2.domain.Kysymys;

/**
 *
 * @author tire
 */
public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }

    @Override
    public Kysymys findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Kysymys a = new Kysymys(rs.getInt("id"), rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));
  
        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT id, kurssi, aihe, kysymysteksti FROM Kysymys").executeQuery()) {

            while (result.next()) {
                kysymykset.add(new Kysymys(result.getInt("id"), result.getString("kurssi"), result.getString("aihe"), result.getString("kysymysteksti")));
            }
        }

        return kysymykset;
    }

    @Override
    public Kysymys saveOrUpdate(Kysymys object) throws SQLException {
        Kysymys byTeksti = findByTeksti(object.getKysymysteksti());

        if (byTeksti != null) {
            return byTeksti;
        }

        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (id, kurssi, aihe, kysymysteksti) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, object.getId());
            stmt.setString(2, object.getKurssi());
            stmt.setString(3, object.getAihe());
            stmt.setString(4, object.getKysymysteksti());
            stmt.executeUpdate();
        }
        return findByTeksti(object.getKysymysteksti());
    }


@Override
        public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    private Kysymys findByTeksti(String teksti) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kurssi, aihe, kysymysteksti FROM Kysymys WHERE kysymysteksti = ?");
            stmt.setString(1, teksti);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Kysymys(result.getInt("id"), result.getString("kurssi"), result.getString("aihe"), result.getString("kysymysteksti"));
        }
    }

}
