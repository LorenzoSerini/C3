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

package it.unicam.c3.View.Spring.Controllers;

import it.unicam.c3.Anagrafica.*;
import it.unicam.c3.Controller.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class SpringControllerAutenticazione {

    @GetMapping("auth")
    public String getAuth() {
        return "auth";
    }

    @PostMapping("auth")
    public ModelAndView doAuth(HttpSession session,
                               Model model,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("tipoUtente") String tipoUtente) {
        ControllerAutenticazione auth = null;

        try {
            auth = new ControllerAutenticazione();
        } catch (SQLException exception) {
            exception.printStackTrace();
            model.addAttribute("error", "Errore database");
            return new ModelAndView("/auth", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utente utente = null;
        Object controller = null;
        try {
            switch (tipoUtente) {
                case "cliente":
                    utente = auth.autenticaCliente(email, password);
                    if (utente != null)
                        controller = new ControllerCliente((Cliente) utente);
                    break;
                case "commerciante":
                    utente = auth.autenticaCommerciante(email, password);
                    if (utente != null) {
                        controller = new ControllerCommerciante((Commerciante) utente);
                    }
                    break;
                case "corriere":
                    utente = auth.autenticaCorriere(email, password);
                    if (utente != null)
                        controller = new ControllerCorriere((Corriere) utente);
                    break;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            model.addAttribute("error", "Errore database");
            return new ModelAndView("/auth", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (controller == null) {
            model.addAttribute("error", "Credenziali invalide");
            return new ModelAndView("/auth", HttpStatus.UNAUTHORIZED);
        }

        session.setAttribute("utente", utente);
        session.setAttribute("controller", controller);

        return new ModelAndView("redirect:/" + tipoUtente);
    }

    @GetMapping("/registra")
    public String getRegistra() {
        return "/registrazione";
    }

    @PostMapping("/registra")
    public ModelAndView postRegistra(Model model,
                                     @RequestParam("nome") String nome,
                                     @RequestParam("cognome") String cognome,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("tipoUtente") String tipoUtente) {
        ControllerAutenticazione auth = null;

        try {
            auth = new ControllerAutenticazione();
        } catch (SQLException exception) {
            exception.printStackTrace();
            model.addAttribute("error", "Errore database");
            return new ModelAndView("/registrazione", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ControllerAutenticazione.TipoUtente tipo;

        switch (tipoUtente) {
            case "cliente":
                tipo = ControllerAutenticazione.TipoUtente.CLIENTE;
                break;
            case "commerciante":
                tipo = ControllerAutenticazione.TipoUtente.COMMERCIANTE;
                break;
            case "corriere":
                tipo = ControllerAutenticazione.TipoUtente.CORRIERE;
                break;
            default:
                model.addAttribute("error", "Tipo utente non valido");
                return new ModelAndView("/registrazione", HttpStatus.BAD_REQUEST);
        }

        try {
            auth.registra(nome,
                    cognome,
                    email,
                    password,
                    tipo);
            model.addAttribute("success",
                    String.format("Utente %s %s iscritto", nome, cognome));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            model.addAttribute("error", "ERRORE: Email già presente!");
            return new ModelAndView("/registrazione", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            model.addAttribute("error", "ERRORE: "+e.getMessage());
        }

        return new ModelAndView("/registrazione");
    }

    @GetMapping("/disconnetti")
    public String disconnetti(HttpSession session) {
        session.removeAttribute("utente");
        session.removeAttribute("controller");
        return "redirect:/auth";
    }

    @GetMapping("/authAdmin")
    public String getAuthAdmin() {
        return "/authAdmin";
    }

    @PostMapping("/authAdmin")
    public ModelAndView getAuthAdmin(HttpSession session,
                                     Model model,
                                     @RequestParam("password") String password)
    {
        ControllerGestore controller = null;

        try {
            controller = new ControllerGestore();
            controller.autorizza(password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            model.addAttribute("error", "Errore database");
            return new ModelAndView("/authAdmin", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!controller.isAutorizzato()) {
            model.addAttribute("error", "Password errata");
            return new ModelAndView("/authAdmin", HttpStatus.UNAUTHORIZED);
        }

        session.setAttribute("controllerAdmin", controller);
        return new ModelAndView("redirect:/gestore");
    }

    @GetMapping("/authAdmin/disconnetti")
    public String disconnettiGestore(HttpSession session) {
        session.removeAttribute("controllerAdmin");
        return "redirect:/authAdmin";
    }
}
