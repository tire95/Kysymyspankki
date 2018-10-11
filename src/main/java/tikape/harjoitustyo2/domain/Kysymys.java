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
public class Kysymys {
    private Integer id;
    private String kurssi;
    private String aihe;
    private String kysymysteksti;
    
    public Kysymys(Integer id, String k, String a, String ky) {
        this.id = id;
        this.kurssi = k;
        this.aihe = a;
        this.kysymysteksti = ky;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getKurssi() {
        return this.kurssi;
    }
    
    public String getAihe() {
        return this.aihe;
    }
    
    public String getKysymysteksti() {
        return this.kysymysteksti;
    }
    
}
