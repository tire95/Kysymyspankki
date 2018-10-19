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
import tikape.harjoitustyo2.domain.Vastaus;

/**
 *
 * @author tire
 */
public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Vastaus a = new Vastaus(rs.getInt("id"), rs.getInt("kysymys_id"), rs.getString("vastausTeksti"), rs.getBoolean("oikein"));

        stmt.close();
        rs.close();

        conn.close();

        return a;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT id, kysymys_id, vastausteksti, oikein FROM Vastaus").executeQuery()) {

            while (result.next()) {
                vastaukset.add(new Vastaus(result.getInt("id"), result.getInt("kysymys_id"), result.getString("vastausteksti"), result.getBoolean("oikein")));
            }
        }

        return vastaukset;
    }

//    tällä metodilla etsitään kaikki vastaukset, jotka on liitetty haluttuun kysymykseen
    public List<Vastaus> findAllForKysymys(Kysymys k) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();

        try (Connection conn = database.getConnection()) {

//            etsitään kaikki vastaukset tietylle kysymykselle kysymys_id:n perusteella
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kysymys_id, vastausteksti, oikein FROM Vastaus WHERE kysymys_id = ?");
            stmt.setInt(1, k.getId());
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                vastaukset.add(new Vastaus(result.getInt("id"), result.getInt("kysymys_id"), result.getString("vastausteksti"), result.getBoolean("oikein")));
            }
        }

        return vastaukset;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {

//          katsotaan, löytyykö kysymyspankista tietylle kysymykselle vastausta jolla on sama vastausteksti
//          ts. saman vastaustekstin voi antaa useammalle eri kysymykselle, jos haluaa
        Vastaus byTeksti = findByTekstiAndKysymysId(object.getVastausteksti(), object.getKysymysId());

//         jos tietylle vastaukselle löytyy vastaus samalla vastaustekstillä, ei tallenneta uutta vastausta
        if (byTeksti != null) {
            return byTeksti;
        }

        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
            stmt.setInt(1, object.getKysymysId());
            stmt.setString(2, object.getVastausteksti());
            stmt.setBoolean(3, object.getOikein());
            stmt.executeUpdate();
        }
        return object;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

//    metodi poistaa kaikki vastaukset tietyn kysymyksen id:n perusteella
//    ts. jos tietty kysymys poistetaan tietokannasta, tällä metodilla voidaan poistaa tähän kysymykseen liitetyt vastaukset
    public void deleteForKysymys(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }

    private Vastaus findByTekstiAndKysymysId(String teksti, Integer id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kysymys_id, vastausteksti, oikein FROM Vastaus WHERE vastausteksti = ? AND kysymys_id = ?");
            stmt.setString(1, teksti);
            stmt.setInt(2, id);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                return null;
            }

            return new Vastaus(result.getInt("id"), result.getInt("kysymys_id"), result.getString("vastausteksti"), result.getBoolean("oikein"));
        }
    }

}
