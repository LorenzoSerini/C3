package it.unicam.c3.View.Spring.Controllers;

import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Controller.ControllerCorriere;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
@Controller
@RequestMapping("corriere")
public class SpringControllerCorriere extends SpringControllerBase {

    private Corriere corriere;
    private ControllerCorriere controller;

    private boolean isUnauthorized(HttpSession session) {
        corriere = getCorriere(session);
        controller = getControllerCorriere(session);

        return corriere == null || controller == null;
    }

    @GetMapping
    public String getHome(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        model.addAttribute("emailCorriere", corriere.getEmail());

        List<Consegna> consegneInCarico = controller.getConsegneInCarico();
        model.addAttribute("consegneInCarico", consegneInCarico);

        List<Consegna> consegneInAttesa = controller.getConsegneInAttesa();
        model.addAttribute("consegneInAttesa", consegneInAttesa);

        return "/corriere/home";
    }

    @GetMapping("prendiInCarico")
    public String prendiInCaricoConsegna(HttpSession session,
                                         @RequestParam("idConsegna") String idConsegna)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Consegna consegna = controller.getConsegneInAttesa()
                .stream()
                .filter(c -> c.getId().equals(idConsegna))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        try {
            controller.prendiInCarico(consegna);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/corriere";
    }

    @GetMapping("annulla")
    public String annullaPresaInCarico(HttpSession session,
                                       @RequestParam("idConsegna") String idConsegna)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Consegna consegna = controller.getConsegneInCarico()
                .stream()
                .filter(c -> c.getId().equals(idConsegna))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        try {
            controller.annullaPresaInCarico(consegna);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/corriere";
    }

    @GetMapping("effettuata")
    public String aggiornaStatoPuntoVendita(HttpSession session,
                                            @RequestParam("idConsegna") String idConsegna)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Consegna consegna = controller.getConsegneInCarico()
                .stream()
                .filter(c -> c.getId().equals(idConsegna))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        try {
            controller.effettuaConsegna(consegna);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/corriere";
    }
}
