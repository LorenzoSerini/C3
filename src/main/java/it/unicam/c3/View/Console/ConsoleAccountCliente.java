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

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Controller.ControllerCliente;
import it.unicam.c3.Ordini.Ordine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ConsoleAccountCliente {
    private Cliente cliente;
    private ControllerCliente controller;
    private BufferedReader br;
    private static final String VISUALIZZA_PUNTI_VENDITA = "0";
    private static final String VISUALIZZA_OFFERTE="1";
    private static final String VISUALIZZA_CARRELLO = "2";
    private static final String CREA_ORDINE = "3";
    private static final String VISUALIZZA_ORDINI = "4";
    private static final String VISUALIZZA_CONSEGNE = "5";
    private static final String RIMUOVI_TUTTO = "0";
    private static final String RIMUOVI_SINGOLO_PRODOTTO = "1";
    private static final String VISUALIZZA_TUTTE_LE_OFFERTE = "0";
    private static final String VISUALIZZA_OFFERTE_PUNTO_VENDITA = "1";
    private static final String RETURN = "u";
    private static final String LOGOUT = "L";


    public ConsoleAccountCliente(Cliente cliente){
        this.cliente=cliente;
        try {
            this.controller=new ControllerCliente(cliente);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private void initChoice() {
        System.out.println("COSA VUOI FARE:");
        System.out.println(VISUALIZZA_PUNTI_VENDITA + ") Visualizza Punti Vendita");
        System.out.println(VISUALIZZA_OFFERTE + ") Visualizza Offerte");
        System.out.println(VISUALIZZA_CARRELLO + ") Visualizza Carrello");
        System.out.println(CREA_ORDINE+") Crea Ordine");
        System.out.println(VISUALIZZA_ORDINI+") Visualizza Ordini");
        System.out.println(VISUALIZZA_CONSEGNE+") Visualizza Consegne");
        System.out.println(LOGOUT + ") Logout");
    }

    private void carrelloChoice() {
        System.out.println(RIMUOVI_TUTTO+") Rimuovi tutti i prodotti");
        System.out.println(RIMUOVI_SINGOLO_PRODOTTO+") Rimuovi singolo prodotto");
        System.out.println("[u+enter to return]");
    }

    private void mostraPuntiVendita() {
        System.out.println("PUNTI VENDITA:");
        for (int i = 0; i < controller.getPuntiVendita().size(); i++) {
            System.out.println(i + ") " + controller.getPuntiVendita().get(i));
        }
    }

    private void mostraProdotti(PuntoVendita pv) {
        System.out.println("PRODOTTI DISPONIBILI:");
        for (int i = 0; i < pv.getProdottiDisponibili().size(); i++) {
            System.out.println(i + ") " + pv.getProdottiDisponibili().get(i));
        }
    }



    private void mostraCarrello() {
        System.out.println("CARRELLO:");
        int count = 0;
        for(PuntoVendita pv:controller.getCarrello().keySet()) {
            System.out.println("\n----------"+count+") "+pv.getNome()+"----------");
            count++;
            double price =0;
            for (int i = 0; i < controller.getCarrello().get(pv).size(); i++) {
                System.out.println(i + ") " + controller.getCarrello().get(pv).get(i));
                price+=controller.getCarrello().get(pv).get(i).getPrezzo();
            }
            System.out.println("-----------------------");
            System.out.println("Prezzo alla cassa: "+price+"\n");
        }
    }

    private void carrelloView() throws IOException {
        String line = null;
        mostraCarrello();
        System.out.println();
        carrelloChoice();
        line = br.readLine();
        switch (line){
            case RIMUOVI_TUTTO:
                controller.clearCarrello();
                break;
            case RIMUOVI_SINGOLO_PRODOTTO:
                System.out.println("[number+invio to select Punto Vendita]");
                String linepv = br.readLine();
                System.out.println("[number+invio to select Product]");
                String lineprodotto = br.readLine();
                controller.removeFromCarrello(Integer.parseInt(linepv),Integer.parseInt(lineprodotto));
                break;
        }
    }


    private void creaOrdini() throws IOException {
        String line;
        System.out.println("Vuoi davvero creare un ordine con gli oggetti del carrello?\n[y+enter per creare l'ordine]\n[n+enter per annullare l'operazione]");
        line=br.readLine();
        if(line.equals("y")) {
            try {
                controller.ordinaProdotti();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR: DataBase Error!");
            }
        }
    }

    private void visualizzaOrdini(){
        System.out.println("ORDINI:");
        for(int i=0; i<controller.getOrdini().size();i++){
            System.out.println(i+") "+controller.getOrdini().get(i));
        }
    }

    private void visualizzaConsegne(){
        System.out.println("CONSEGNE:");
        for(int i=0; i<controller.getConsegne().size();i++){
            System.out.println(i+") "+controller.getConsegne().get(i).toString());
        }
    }

    private void visualizzaOfferte(){
        System.out.println("OFFERTE:");
        for(PuntoVendita pv:controller.getOfferte().keySet()) {
            System.out.println("\n----------)"+pv.getNome()+"----------");
            for (int i = 0; i < controller.getOfferte().get(pv).size(); i++) {
                System.out.println("--------------------------------");
                System.out.println("++++++++++OFFERTA "+i+"+++++++++");
                System.out.println(controller.getOfferte().get(pv).get(i).getDescrizione());
                System.out.println("Valore offerta: "+controller.getOfferte().get(pv).get(i).getImporto());
                System.out.println("--------------------------------");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void visualizzaOfferta(PuntoVendita pv){
        System.out.println("OFFERTE:");
            System.out.println("\n----------)"+pv.getNome()+"----------");
            for (int i = 0; i < pv.getOfferte().size(); i++) {
                System.out.println("--------------------------------");
                System.out.println("++++++++++OFFERTA "+i+"++++++++++");
                System.out.println(pv.getOfferte().get(i).getDescrizione());
                System.out.println("Valore offerta: "+pv.getOfferte().get(i).getImporto());
                System.out.println("--------------------------------");
            }
            System.out.println();
    }

    private void visualizzaDettagliOrdine(Ordine ordine){
        System.out.println("DETTAGLI ORDINE:");
        System.out.println("-----------------");
        System.out.println("Punto Vendita: "+ordine.getPuntoVendita().getNome()+" "+ordine.getPuntoVendita().getPosizione());
        System.out.println("Cliente: "+ordine.getCliente());
        System.out.println("Stato: "+ordine.getStato());
        System.out.println("Prodotti:");
        for(int i=0; i<ordine.getProdotti().size();i++) {
            System.out.println(i+") " + ordine.getProdotti().get(i));
        }
    }

    private void consegneView() throws IOException {
        String line;
        visualizzaConsegne();
        System.out.println("[u+enter to return]");
        line = br.readLine();
    }

    private void offerteView() throws IOException {
        String line;
        System.out.println("OPZIONI DISPONIBILI: ");
        System.out.println(VISUALIZZA_TUTTE_LE_OFFERTE+") Visualizza Tutte Le Offerte Disponibili");
        System.out.println(VISUALIZZA_OFFERTE_PUNTO_VENDITA+") Visualizza Offerte Singolo Punto Vendita");
        System.out.println("["+RETURN+"+enter to return]");
        System.out.println("---------------------------------------------------|");
        System.out.println("NB: L'APPLICAZIONE DELLE OFFERTE E' DA RICHIEDERE  |");
        System.out.println("AL COMMERCIANTE NEL MOMENTO DEL PAGAMENTO IN CASSA |");
        System.out.println("DEI PRODOTTI ORDINATI                              |");
        System.out.println("---------------------------------------------------|");
        line = br.readLine();
        switch (line){
            case VISUALIZZA_TUTTE_LE_OFFERTE:
                visualizzaOfferte();
                break;
            case VISUALIZZA_OFFERTE_PUNTO_VENDITA:
                mostraPuntiVendita();
                System.out.println("Seleziona punto vendita di cui vuoi visualizzare le offerte");
                line = br.readLine();
                PuntoVendita pv = CentroCittadino.getInstance().getPuntiVendita().get(Integer.parseInt(line));
                visualizzaOfferta(pv);
                break;
            case RETURN:
                break;
        }
    }

    private void ordiniView() throws IOException {
        String line;
        visualizzaOrdini();
        System.out.println("[number+enter per dettagli ordine]");
        System.out.println("[u+enter to return]");
        line = br.readLine();
        if(!line.equals(RETURN)){
            visualizzaDettagliOrdine(controller.getOrdini().get(Integer.parseInt(line)));
            System.out.println();
        }
    }



    private void puntiVenditaView() throws IOException {
        String line;
        mostraPuntiVendita();
        System.out.println("[u+enter to return]");
        line = br.readLine();
        if (!line.equals(RETURN)) {
            PuntoVendita pv = controller.getPuntiVendita().get(Integer.parseInt(line));
            do {
                mostraProdotti(pv);
                System.out.println("[number+enter per aggiungere un prodotto al carrello]");
                System.out.println("[u+enter to return]");
                line = br.readLine();
                if (!line.equals("u")) {
                    Prodotto prodotto = pv.getProdottiDisponibili().get(Integer.parseInt(line));
                    controller.addInCarrello(pv, prodotto);
                }
            }while(!line.equals(RETURN));
        }
    }


    public void clienteView() throws IOException {
        String line;
        do {
            initChoice();
            line= br.readLine();
            if (line.equals(VISUALIZZA_PUNTI_VENDITA)) {
                puntiVenditaView();
            }else if(line.equals(VISUALIZZA_OFFERTE)){
                offerteView();
            }
            else if(line.equals(VISUALIZZA_CARRELLO)){
                carrelloView();
            }else if(line.equals(CREA_ORDINE)){
                creaOrdini();
            }else if(line.equals(VISUALIZZA_ORDINI)){
                ordiniView();
            }else if(line.equals(VISUALIZZA_CONSEGNE)){
                consegneView();
            }
        } while (!line.equals(LOGOUT));
    }



}
