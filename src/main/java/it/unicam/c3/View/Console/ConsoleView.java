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
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Controller.ControllerAutenticazione;
import it.unicam.c3.View.View;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ConsoleView implements View {
    private static final String ACCOUNT_CLIENTE="1";
    private static final String ACCOUNT_COMMERCIANTE="2";
    private static final String ACCOUNT_CORRIERE="3";
    private static final String AMMINISTRAZIONE="4";
    private static final String REGISTRAZIONE="1";
    private static final String LOGIN="2";
    private static final String CLOSE_APPLICATION="exit";
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private ControllerAutenticazione controllerAutenticazione;
    ConsoleAccountCliente clienteView;
    ConsoleAccountCorriere corriereView;
    ConsoleAccountCommerciante commercianteView;
    ConsoleAmministrazione amministrazioneView;

    public ConsoleView() throws Exception {
        controllerAutenticazione= new ControllerAutenticazione();
    }

    @Override
    public void start() throws Exception {
        String line;
        do {
            choiceInit();
            line= br.readLine();
            if(line.equals(AMMINISTRAZIONE)){
                    amministrazioneView = new ConsoleAmministrazione();
                    amministrazioneView.amministrazioneView();
            }else if(!line.equals(CLOSE_APPLICATION)) choiceLoginOrRegistration(line);
        }while(!line.equals(CLOSE_APPLICATION));
        br.close();
    }

    private void choiceInit(){
        System.out.println("SCEGLI ACCOUNT:");
        System.out.println(ACCOUNT_CLIENTE+") Account Cliente");
        System.out.println(ACCOUNT_COMMERCIANTE+") Account Commerciante");
        System.out.println(ACCOUNT_CORRIERE+") Account Corriere");
        System.out.println(AMMINISTRAZIONE+") Area Amministrazione");
        System.out.println("\n"+CLOSE_APPLICATION+") CLOSE APPLICATION");
    }

    private void choiceLoginOrRegistration(String tipo) throws IOException, SQLException, MessagingException {
        String line;
        System.out.println(REGISTRAZIONE + ") Registrazione");
        System.out.println(LOGIN + ") Login");
        line = br.readLine();
        switch (line) {
            case REGISTRAZIONE:
                registration(tipo);
                break;
            case LOGIN:
                System.out.println("Inserisci una e-mail:");
                String email = br.readLine();
                System.out.println("Inserisci una password:");
                String password = br.readLine();
                switch (tipo) {
                    case ACCOUNT_CLIENTE:
                        if(this.autenticaCliente(email, password)!=null){
                            clienteView = new ConsoleAccountCliente(this.autenticaCliente(email,password));
                            clienteView.clienteView();
                        }else System.out.println("AUTENTICAZIONE FALLITA!");
                        break;
                    case ACCOUNT_COMMERCIANTE:
                        if(this.autenticaCommerciante(email, password)!=null){
                            commercianteView = new ConsoleAccountCommerciante(this.autenticaCommerciante(email,password));
                            commercianteView.commercianteView();
                        }else System.out.println("AUTENTICAZIONE FALLITA!");
                        break;
                    case ACCOUNT_CORRIERE:
                        if(this.autenticaCorriere(email, password)!=null){
                            corriereView = new ConsoleAccountCorriere(this.autenticaCorriere(email,password));
                            corriereView.corriereView();
                        }else System.out.println("AUTENTICAZIONE FALLITA!");
                        break;
                }
                break;

        }
    }

    private void registration(String tipo) {
        String name;
        String cognome;
        String mail;
        String password;
        try {
            System.out.println("Inserisci nome:");
            name = br.readLine();
            System.out.println("Inserisci cognome:");
            cognome = br.readLine();
            System.out.println("Inserisci una e-mail:");
            mail = br.readLine();
            System.out.println("Inserisci una password:");
            password = br.readLine();
            try {
                switch (tipo) {
                    case ACCOUNT_CLIENTE:
                        this.controllerAutenticazione.registra(name, cognome, mail, password, ControllerAutenticazione.TipoUtente.CLIENTE);
                        break;
                    case ACCOUNT_COMMERCIANTE:
                        this.controllerAutenticazione.registra(name, cognome, mail, password, ControllerAutenticazione.TipoUtente.COMMERCIANTE);
                        break;
                    case ACCOUNT_CORRIERE:
                        this.controllerAutenticazione.registra(name, cognome, mail, password, ControllerAutenticazione.TipoUtente.CORRIERE);
                        break;
                }
            }catch (SQLException e){
                System.out.println("ERROR: ERRORE DATABASE!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Cliente autenticaCliente(String email, String password){
       return this.controllerAutenticazione.autenticaCliente(email, password);
    }

    private Commerciante autenticaCommerciante(String email, String password){
        return this.controllerAutenticazione.autenticaCommerciante(email,password);
    }

    private Corriere autenticaCorriere(String email, String password){
        return this.controllerAutenticazione.autenticaCorriere(email,password);
    }




}
