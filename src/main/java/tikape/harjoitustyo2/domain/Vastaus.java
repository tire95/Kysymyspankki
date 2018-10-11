/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.harjoitustyo2.domain;

/**
 *
 * @author tire
 */
public class Vastaus {
    private Integer id;
    private Integer kysymysId;
    private String vastausteksti;
    private boolean oikein;
    
    public Vastaus(Integer id, Integer kysymysId, String vastaus, boolean oikein) {
        this.id = id;
        this.kysymysId = kysymysId;
        this.vastausteksti = vastaus;
        this.oikein = oikein;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getKysymysId() {
        return this.kysymysId;
    }
    
    public String getVastausteksti() {
        return this.vastausteksti;
    }
    
    public boolean getOikein() {
        return this.oikein;
    }
    
}
