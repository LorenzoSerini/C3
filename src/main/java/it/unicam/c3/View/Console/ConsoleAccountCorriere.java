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

import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Controller.ControllerCorriere;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleAccountCorriere {
    private Corriere corriere;
    private ControllerCorriere controller;
    private BufferedReader br;
    private final String VISUALIZZA_CONSEGNE_IN_ATTESA = "0";
    private final String VISUALIZZA_CONSEGNE_PRESE_IN_CARICO = "1";
    private final String CONSEGNA_EFFETTUATA = "0";
    private static final String RETURN = "u";
    private final String LOGOUT = "L";

    public ConsoleAccountCorriere(Corriere corriere){
        this.corriere=corriere;
        try {
            controller=new ControllerCorriere(corriere);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private void initChoice(){
        System.out.println("COSA VUOI FARE:");
        System.out.println(VISUALIZZA_CONSEGNE_IN_ATTESA+") Visualizza Consegne In Attesa Di Consegna");
        System.out.println(VISUALIZZA_CONSEGNE_PRESE_IN_CARICO+") Visualizza Consegne Prese In Carico");
        System.out.println(LOGOUT + ") Logout");
    }

    private void visualizzaConsegneInAttesa(){
        System.out.println("CONSEGNE IN ATTESA:\n");
        for(int i=0; i<controller.getConsegneInAttesa().size();i++){
            System.out.println(i+") DETTAGLI CONSEGNA:");
            System.out.println("---------------");
            System.out.println("Id: "+controller.getConsegneInAttesa().get(i).getId());
            System.out.println("Nome Punto Vendita: "+controller.getConsegneInAttesa().get(i).getOrdine().getPuntoVendita().getNome());
            System.out.println("Posizione Punto Vendita: "+controller.getConsegneInAttesa().get(i).getOrdine().getPuntoVendita().getPosizione());
            System.out.println("Nome Commerciante: "+controller.getConsegneInAttesa().get(i).getCommerciante().getNome()+" "+controller.getConsegneInAttesa().get(i).getCommerciante().getCognome());
            System.out.println("Email Commerciante: "+controller.getConsegneInAttesa().get(i).getCommerciante().getEmail());
            System.out.println("Punto Ritiro: "+controller.getConsegneInAttesa().get(i).getPuntoRitiro().getIndirizzo());
            System.out.println("N. Slot Disponibili Punto Ritiro: "+controller.getConsegneInAttesa().get(i).getPuntoRitiro().getSlotDisponibili());
            System.out.println("Stato: "+controller.getConsegneInAttesa().get(i).getStato());
            System.out.println("Numero Prodotti: "+controller.getConsegneInAttesa().get(i).getOrdine().getProdotti().size());
            //Potrei mettere il visualizza dettaglio consegna in cui vedo i prodotti dell'ordine
        }
    }

    private void visualizzaConsegneInCarico(){
        System.out.println("CONSEGNE PRESE IN CARICO:\n");
        for(int i=0; i<controller.getConsegneInCarico().size();i++){
            System.out.println(i+") DETTAGLI CONSEGNA:");
            System.out.println("---------------");
            System.out.println("Id: "+controller.getConsegneInCarico().get(i).getId());
            System.out.println("Nome Punto Vendita: "+controller.getConsegneInCarico().get(i).getOrdine().getPuntoVendita().getNome());
            System.out.println("Posizione Punto Vendita: "+controller.getConsegneInCarico().get(i).getOrdine().getPuntoVendita().getPosizione());
            System.out.println("Nome Commerciante: "+controller.getConsegneInCarico().get(i).getCommerciante().getNome()+" "+controller.getConsegneInCarico().get(i).getCommerciante().getCognome());
            System.out.println("Email Commerciante: "+controller.getConsegneInCarico().get(i).getCommerciante().getEmail());
            System.out.println("Punto Ritiro: "+controller.getConsegneInCarico().get(i).getPuntoRitiro().getIndirizzo());
            System.out.println("Stato: "+controller.getConsegneInCarico().get(i).getStato());
            System.out.println("Numero Prodotti: "+controller.getConsegneInCarico().get(i).getOrdine().getProdotti().size());
        }
    }

    private void consegneInAttesaView() throws IOException {
        String line;
        visualizzaConsegneInAttesa();
        System.out.println("[number+enter per prendere in carico la consegna]");
        System.out.println("[u+enter to return]");
        line = br.readLine();
        if(!line.equals(RETURN)){
            try {
                controller.prendiInCarico(Integer.parseInt(line));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void consegnePreseInCaricoView() throws IOException {
        String line;
        visualizzaConsegneInCarico();
        System.out.println("[number+enter per segnalare che la consegna è stata effettuata]");
        System.out.println("[u+enter to return]");
        line = br.readLine();
        if(!line.equals(RETURN)) {
            try {
                controller.effettuaConsegna(controller.getConsegneInCarico().get(Integer.parseInt(line)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void corriereView() throws IOException, MessagingException {
        String line;
        do {
            initChoice();
            line= br.readLine();
            if (line.equals(VISUALIZZA_CONSEGNE_IN_ATTESA)) {
                consegneInAttesaView();
            }else if(line.equals(VISUALIZZA_CONSEGNE_PRESE_IN_CARICO)){
                consegnePreseInCaricoView();
            }
        } while (!line.equals(LOGOUT));
    }


}
