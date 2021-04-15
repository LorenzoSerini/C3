package it.unicam.c3.test;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Citta.CentroCittadino;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CentroCittadinoTest {
    private CentroCittadino cc;
    private Commerciante comm;



    @Test
    public void test(){
        this.CentroCittadinoInit();
        this.numberUtenti();
        this.numberPuntiVendita();
    }


    @Order(1)
    public void CentroCittadinoInit(){
        cc=CentroCittadino.getInstance();

        cc.addPuntoRitiro("PROVA", 400);

        comm = new Commerciante("Name","Surname","email","password");


        comm.addPuntoVendita("nome1","posizione1");
        comm.addPuntoVendita("nome2","posizione2");
        comm.addPuntoVendita("nome3","posizione3");

        cc.addCommerciante(comm);

        cc.addCliente(new Cliente("Name","Surname","email","password"));

        cc.addCorriere(new Corriere("Name","Surname","email","password"));
        cc.addCorriere(new Corriere("Name2","Surname2","email2","password2"));

    }

    @Order(2)
    public void numberUtenti(){
       assertEquals(1,cc.getCommercianti().size());
       assertEquals(1,cc.getClienti().size());
       assertEquals(2,cc.getCorrieri().size());
    }

    @Order(3)
    public void numberPuntiVendita(){
        assertEquals(3,cc.getCommercianti().get(0).getPuntiVendita().size());
    }


}
