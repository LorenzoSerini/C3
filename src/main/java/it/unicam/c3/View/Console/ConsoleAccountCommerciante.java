/*******************************************************************************
 * MIT License

 * Copyright (c) 2021 Lorenzo Serini and Alessandro Pecugi
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
/**
 *
 */

package it.unicam.c3.View.Console;


import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Controller.ControllerCommerciante;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ConsoleAccountCommerciante {
    private Commerciante commerciante;
    private ControllerCommerciante controller;
    private BufferedReader br;

    private static final String VISUALIZZA_PUNTI_VENDITA = "1";
    private static final String AGGIUNGI_PUNTO_VENDITA = "2";
    private static final String VISUALIZZA_PRODOTTI = "1";
    private static final String AGGIUNGI_PRODOTTO = "2";
    private static final String VISUALIZZA_ORDINI_IN_ATTESA = "3";
    private static final String ABILITA_RITIRO_CONSEGNA = "4";
    private static final String RETURN = "u";
    private static final String LOGOUT = "L";

    public ConsoleAccountCommerciante(Commerciante commerciante) {
        this.commerciante = commerciante;
        try {
            controller = new ControllerCommerciante(commerciante);
        } catch (SQLException exception) {
            System.out.println("ERROR: Errore Database!");
        }
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void initChoice() {
        System.out.println("COSA VUOI FARE:");
        System.out.println(VISUALIZZA_PUNTI_VENDITA + ") Visualizza punti vendita");
        System.out.println(AGGIUNGI_PUNTO_VENDITA + ") Aggiungi punto vendita");
        System.out.println(VISUALIZZA_ORDINI_IN_ATTESA + ") Visualizza ordini in attesa");
        System.out.println(ABILITA_RITIRO_CONSEGNA + ") Abilita ritiro consegna");
        System.out.println(LOGOUT + ") Logout");
    }

    public void commercianteView() throws IOException {
        String line;
        do {
            initChoice();
            line= br.readLine();
            switch (line) {
                case VISUALIZZA_PUNTI_VENDITA:
                    puntiVenditaView();
                    System.out.println();
                    break;
                case AGGIUNGI_PUNTO_VENDITA:
                    aggiungiPuntoVenditaView();
                    System.out.println();
                    break;
                case VISUALIZZA_ORDINI_IN_ATTESA:
                    ordiniInAttesaView();
                    System.out.println();
                    break;
                case ABILITA_RITIRO_CONSEGNA:
                    abilitaRitiroView();
                    System.out.println();
                    break;
            }
        }
        while (!line.equals(LOGOUT));
    }

    private void visualizzaConsegneDaAbilitare(){
        System.out.println("CONSEGNE DA ABILITARE PER IL RITIRO:");
        for(int i=0; i<controller.getConsegneDaAbilitareAlRitiro().size();i++){
            System.out.println("-------- CONSEGNA "+i+" --------");
            System.out.println("--------------------------------");
            System.out.println("Id Consegna: "+controller.getConsegneDaAbilitareAlRitiro().get(i).getId());
            System.out.println("Nome Ordine Cliente: "+controller.getConsegneDaAbilitareAlRitiro().get(i).getOrdine().getCliente());
            System.out.println("Email Ordine Cliente: "+controller.getConsegneDaAbilitareAlRitiro().get(i).getOrdine().getCliente().getEmail());
            System.out.println("N. Prodotti Ordinati: "+controller.getConsegneDaAbilitareAlRitiro().get(i).getOrdine().getProdotti().size());
        }
    }

    private void visualizzaDettagliConsegna(Consegna consegna){
        System.out.println("DETTAGLI CONSEGNA:");
        System.out.println("Id Consegna: "+consegna.getId());
        System.out.println("Punto Ritiro: "+consegna.getPuntoRitiro().getIndirizzo());
        System.out.println("Prodotti: ");
        System.out.println("----------");
        for(Prodotto p:consegna.getOrdine().getProdotti()){
            System.out.println(p.getDescrizione()+" "+p.getPrezzo());
        }
        System.out.println("---------");
        System.out.println("TOTALE: "+consegna.getOrdine().getPrice());
    }

    private void visualizzaDettagliOrdine(Ordine ordine){
        System.out.println("DETTAGLI ORDINE:");
        System.out.println("Nominativo Cliente: "+ordine.getCliente().getNome()+" "+ordine.getCliente().getCognome());
        System.out.println("Email Cliente: "+ordine.getCliente().getEmail());
        System.out.println("Prodotti: ");
        System.out.println("----------");
        for(Prodotto p: ordine.getProdotti()){
            System.out.println(p.getDescrizione()+" "+p.getPrezzo());
        }
        System.out.println("---------");
        System.out.println("TOTALE: "+ordine.getPrice());
    }

    private void abilitaRitiroView() throws IOException {
        String line;
        this.visualizzaConsegneDaAbilitare();
        System.out.println("[number+enter per visualizzare una consegna nel dettaglio]");
        System.out.println("["+RETURN+"+enter per tornare indietro]");
        line=br.readLine();
        if (!line.equals(RETURN)) {
            this.visualizzaDettagliConsegna(controller.getConsegneDaAbilitareAlRitiro().get(Integer.parseInt(line)));
            System.out.println();
            System.out.println("Vuoi abilitare la consegna al ritiro?");
            System.out.println("[y+enter per abilitare la consegna al ritiro]");
            System.out.println("[n+enter per annullare l'operazione]");
            String lineChoice =br.readLine();
            if(lineChoice.equals("y")) {
                try {
                    controller.abilitaRitiro(controller.getConsegneDaAbilitareAlRitiro().get(Integer.parseInt(line)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void visualizzaOrdiniInAttesa(){
        System.out.println("LISTA ORDINI IN ATTESA DI CONFERMA:\n");
        for(int i = 0; i<controller.getOrdini(StatoOrdine.IN_ATTESA).size(); i++){
            System.out.println("-------- ORDINE "+i+" --------");
            System.out.println("Nome Cliente: "+controller.getOrdini(StatoOrdine.IN_ATTESA).get(i).getCliente().getNome()+" "+controller.getOrdini(StatoOrdine.IN_ATTESA).get(i).getCliente().getCognome());
            System.out.println("Email Cliente: "+controller.getOrdini(StatoOrdine.IN_ATTESA).get(i).getCliente().getEmail());
            System.out.println("N. prodotti ordinati: "+controller.getOrdini(StatoOrdine.IN_ATTESA).get(i).getProdotti().size());
        }
    }

    private void visualizzaPuntiVendita(){
        System.out.println("LISTA PUNTI VENDITA:\n");
        for(int i=0; i<controller.getPuntiVendita().size(); i++){
            System.out.println("-------- PUNTO VENDITA "+i+" --------");
            System.out.println("Nome: "+controller.getPuntiVendita().get(i).getNome());
            System.out.println("Posizione: "+controller.getPuntiVendita().get(i).getPosizione());
            System.out.println();
        }
    }

    private PuntoRitiro scegliPuntoRitiroView() throws IOException {
        String line;
        if(controller.getPuntiRitiroDisponibili().size()>0) {
            System.out.println("PUNTI RITIRO DISPONIBILI:");
            System.out.println("-------------------------");
            for (int i = 0; i < controller.getPuntiRitiroDisponibili().size(); i++) {
                System.out.println();
                System.out.println("-------- Punto Ritiro " + i + " --------");
                System.out.println("Posizione: "+controller.getPuntiRitiroDisponibili().get(i).getIndirizzo());
                System.out.println("Slot Disponibili: "+controller.getPuntiRitiroDisponibili().get(i).getSlotDisponibili());
            }
            System.out.println("\nScegli il punto di ritiro dove verr\u00E0 effettuata la consegna:");
            System.out.println("[number+enter per scegliere il punto di ritiro]");
            System.out.println("[" + RETURN + "+enter per annullare l'operazione]");
            line = br.readLine();
            if (!line.equals(RETURN)) return controller.getPuntiRitiroDisponibili().get(Integer.parseInt(line));
        } else{
            System.out.println("MOMENTANEAMENTE TUTTI I PUNTI DI RITIRO SONO OCCUPATI");
            System.out.println("LA PREGHIAMO DI RIPROVARE PIU TARDI!");
            return null;
        }
        return null;
    }


    private void ordiniInAttesaView() throws IOException {
        String line;
        this.visualizzaOrdiniInAttesa();
        System.out.println("[number+enter per visualizzare un ordine nel dettaglio]");
        System.out.println("["+RETURN+"+enter per tornare indietro]");
        line=br.readLine();
        if (!line.equals(RETURN)) {
            this.visualizzaDettagliOrdine(controller.getOrdini(StatoOrdine.IN_ATTESA).get(Integer.parseInt(line)));
            System.out.println("\nVuoi accettare l'ordine?");
            System.out.println("[y+enter per accettare l'ordine]");
            System.out.println("[n+enter per rifiutare l'ordine]");
            System.out.println("["+RETURN+"+enter per tornare indietro]");
            String lineChoice=br.readLine();
            if(lineChoice.equals("y")) {
                PuntoRitiro pv =  this.scegliPuntoRitiroView();
                if(!pv.equals(null)) {
                    try {
                        controller.accettaOrdine(controller.getOrdini(StatoOrdine.IN_ATTESA).get(Integer.parseInt(line)), pv);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(lineChoice.equals("n")) {
                try {
                    controller.rifiutaOrdine(controller.getOrdini(StatoOrdine.IN_ATTESA).get(Integer.parseInt(line)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void aggiungiPuntoVenditaView() throws IOException {
        String line;
        System.out.println("Inserisci il nome del punto vendita da creare: ");
        String nameLine = br.readLine();
        System.out.println("Inserisci vie e numero civico (es: Via Prova, 1000) \ndel punto vendita da creare: ");
        String positionLine = br.readLine();
        System.out.println("Vuoi davvero creare il nuovo punto vendita?");
        System.out.println("[y+enter per crearlo]");
        System.out.println("[n+enter per annullare l'operazione]");
        line = br.readLine();
        if(line.equals("y")) {
            try {
                controller.addPuntoVendita(positionLine,nameLine);
            } catch (Exception exception) {
                System.out.println("ERROR: ERRORE SALVATAGGIO PUNTO VENDITA!!");
            }
        }
    }

    private void aggiungiProdotto(PuntoVendita pv) throws IOException {
        String line;
        System.out.println("Inserisci la descrizione del prodotto");
        String descriptionLine= br.readLine();
        System.out.println("Inserisci il prezzo del prodotto");
        String priceLine= br.readLine();
        System.out.println("Sei sicuro di voler aggiungere il prodotto?");
        System.out.println("[y+enter per aggiungere il prodotto]");
        System.out.println("[n+enter per annullare l'operazione]");
        line=br.readLine();
        if(line.equals("y")) {
            try {
                controller.addProdotto(pv,descriptionLine, Double.parseDouble(priceLine));
            } catch (Exception e) {
                System.out.println("ERROR: ERRORE DATABASE!");
            }
        }
    }

    private void visualizzaProdotti(PuntoVendita pv){
        System.out.println("PRODOTTI:");
        System.out.println("------------------");
        for(int i=0; i<pv.getProdotti().size();i++){
            System.out.println(pv.getProdotti().get(i).getDescrizione());
            System.out.println("Disponibilit\u00E0: "+pv.getProdotti().get(i).getDisponibilita());
            System.out.println("Prezzo: "+pv.getProdotti().get(i).getPrezzo());
            System.out.println();
        }
    }

    private void puntoVenditaChoice(PuntoVendita pv) throws IOException {
        String line;
        System.out.println("Nome: "+pv.getNome());
        System.out.println("Posizione: "+pv.getPosizione());
        System.out.println();
        System.out.println(VISUALIZZA_PRODOTTI+") Visualizza Prodotti");
        System.out.println(AGGIUNGI_PRODOTTO+") Aggiungi Prodotto");
        System.out.println(RETURN+") Tornare indietro");
        line= br.readLine();
        switch(line){
            case VISUALIZZA_PRODOTTI:
                this.visualizzaProdotti(pv);
                break;
            case AGGIUNGI_PRODOTTO:
                this.aggiungiProdotto(pv);
                break;
        }
    }

    private void puntiVenditaView() throws IOException {
        String line;
        this.visualizzaPuntiVendita();
        System.out.println("[number+enter per aprire nel dettaglio un punto vendita]");
        System.out.println("["+RETURN+" per tornare indietro]");
        line = br.readLine();
        if(!line.equals(RETURN)) this.puntoVenditaChoice(controller.getPuntiVendita().get(Integer.parseInt(line)));

    }
}
