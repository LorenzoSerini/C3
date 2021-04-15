package it.unicam.c3.test;

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Ordini.Ordine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GestoreConsegneTest {
    private Ordine ordine1;
    private Ordine ordine2;
    private Ordine ordine3;

    @BeforeEach
    public void initGestoreConsegne(){
            CentroCittadino.getInstance().addPuntoRitiro("Via Panfilo, 16", 10);
            CentroCittadino.getInstance().addPuntoRitiro("Corso Cavour, 18", 10);
            CentroCittadino.getInstance().addCommerciante(new Commerciante("Paolo", "Rossi", "paolorossi@gmail.com", "prova"));
            CentroCittadino.getInstance().addCorriere(new Corriere());
            CentroCittadino.getInstance().addCorriere(new Corriere());
            ordine1 = new Ordine();
            ordine2 = new Ordine();
            ordine3 = new Ordine();
    }

    @Test
    @Order(1)
    public void numberConsegneInAttesa(){
        GestoreConsegne.getInstance().addConsegna(ordine1,CentroCittadino.getInstance().getCommercianti().get(0),CentroCittadino.getInstance().getPuntiRitiro().get(0));
        GestoreConsegne.getInstance().addConsegna(ordine2,CentroCittadino.getInstance().getCommercianti().get(0),CentroCittadino.getInstance().getPuntiRitiro().get(0));
        GestoreConsegne.getInstance().addConsegna(ordine3,CentroCittadino.getInstance().getCommercianti().get(0),CentroCittadino.getInstance().getPuntiRitiro().get(1));
        assertEquals(3,GestoreConsegne.getInstance().getConsegne(StatoConsegna.IN_ATTESA).size());
        assertEquals(0,GestoreConsegne.getInstance().getConsegne(StatoConsegna.PRESA_IN_CARICO).size());
        assertEquals(0,GestoreConsegne.getInstance().getConsegne(StatoConsegna.EFFETTUATA).size());
    }

    @Test
    @Order(2)
    public void numberConsegnePresenInCarico() throws Exception {
        GestoreConsegne.getInstance().prendiInCaricoConsegna(0,CentroCittadino.getInstance().getCorrieri().get(0));
        GestoreConsegne.getInstance().prendiInCaricoConsegna(2,CentroCittadino.getInstance().getCorrieri().get(0));
        assertEquals(1,GestoreConsegne.getInstance().getConsegne(StatoConsegna.IN_ATTESA).size());
        assertEquals(2,GestoreConsegne.getInstance().getConsegne(StatoConsegna.PRESA_IN_CARICO).size());
        assertEquals(0,GestoreConsegne.getInstance().getConsegne(StatoConsegna.EFFETTUATA).size());
    }


    @Test
    @Order(3)
    public void numberConsegnePresenInCaricoDa() throws Exception {
        GestoreConsegne.getInstance().prendiInCaricoConsegna(0,CentroCittadino.getInstance().getCorrieri().get(0));
        GestoreConsegne.getInstance().prendiInCaricoConsegna(2,CentroCittadino.getInstance().getCorrieri().get(0));
        assertEquals(2,GestoreConsegne.getInstance().getConsegnePreseInCaricoDa(CentroCittadino.getInstance().getCorrieri().get(0)).size());
        assertEquals(0,GestoreConsegne.getInstance().getConsegnePreseInCaricoDa(CentroCittadino.getInstance().getCorrieri().get(1)).size());
    }


    @Test
    @Order(4)
    public void numberConsegneEffettuate() throws Exception {
        GestoreConsegne.getInstance().prendiInCaricoConsegna(0,CentroCittadino.getInstance().getCorrieri().get(0));
        GestoreConsegne.getInstance().prendiInCaricoConsegna(2,CentroCittadino.getInstance().getCorrieri().get(0));
        GestoreConsegne.getInstance().consegnaEffettuata(0,CentroCittadino.getInstance().getCorrieri().get(0));
        GestoreConsegne.getInstance().consegnaEffettuata(2,CentroCittadino.getInstance().getCorrieri().get(0));
        assertEquals(2,GestoreConsegne.getInstance().getConsegne(StatoConsegna.EFFETTUATA).size());
        assertEquals(1,GestoreConsegne.getInstance().getConsegne(StatoConsegna.IN_ATTESA).size());
    }


}