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
public class VastausDao implements Dao<Vastaus, Integer>{
    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    public List<Vastaus> findAllForKysymys(Kysymys k) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        
        try (Connection conn = database.getConnection()) {
            
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kysymys_id, vastausteksti, oikein FROM Vastaus WHERE kysymys_id = ?");
            stmt.setInt(1, k.getId());
            ResultSet result = stmt.executeQuery();
            
            while(result.next()) {
                vastaukset.add(new Vastaus(result.getInt("id"), result.getInt("kysymys_id"), result.getString("vastausteksti"), result.getBoolean("oikein")));
            }
        }
        
        return vastaukset;
    }

    @Override
    public Vastaus saveOrUpdate(Vastaus object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            
            PreparedStatement stmt = conn.prepareStatement("SELECT id, kysymys_id, vastausteksti, oikein FROM Vastaus WHERE id = ?");
            stmt.setInt(1, object.getId());
            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
                stmt2.setInt(1, object.getKysymysId());
                stmt2.setString(2, object.getVastausteksti());
                stmt2.setBoolean(3, object.getOikein());
                stmt2.executeUpdate();
            }
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
    
}
