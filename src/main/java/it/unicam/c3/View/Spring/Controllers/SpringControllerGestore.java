package it.unicam.c3.View.Spring.Controllers;

import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Controller.ControllerGestore;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("gestore")
public class SpringControllerGestore {
    private ControllerGestore controller;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean authorize(HttpSession session) {
        Object o = session.getAttribute("controllerAdmin");
        if (!(o instanceof ControllerGestore) || !((ControllerGestore) o).isAutorizzato())
            return false;

        controller = (ControllerGestore) o;
        return true;
    }

    @GetMapping
    public ModelAndView getHome(HttpSession session, Model model) {
        if (!authorize(session))
            return new ModelAndView("redirect:/authAdmin", HttpStatus.UNAUTHORIZED);

        model.addAttribute("puntiRitiro", controller.getPuntiRitiro());
        return new ModelAndView("/gestore/home");
    }

    @GetMapping("eliminaPuntoRitiro")
    public ModelAndView eliminaPuntoRitiro(HttpSession session, @RequestParam("id") String id) {
        if (!authorize(session))
            return new ModelAndView("redirect:/authAdmin", HttpStatus.UNAUTHORIZED);

        PuntoRitiro puntoRitiro = controller.getPuntiRitiro()
                .stream()
                .filter(pr -> pr.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (puntoRitiro == null)
            return new ModelAndView("/not-found", HttpStatus.NOT_FOUND);

        try {
            controller.removePuntoRitiro(puntoRitiro);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ModelAndView("redirect:/gestore");
    }

    @GetMapping("aggiungiPuntoRitiro")
    public ModelAndView getAggiungiPuntoRitiro(HttpSession session) {
        if (!authorize(session))
            return new ModelAndView("redirect:/authAdmin", HttpStatus.UNAUTHORIZED);

        return new ModelAndView("/gestore/aggiungiPuntoRitiro");
    }

    @PostMapping("aggiungiPuntoRitiro")
    public ModelAndView doAggiungiPuntoRitiro(HttpSession session,
                                              @RequestParam("indirizzo") String indirizzo,
                                              @RequestParam("capienza") int capienza)
    {
        if (!authorize(session))
            return new ModelAndView("redirect:/authAdmin", HttpStatus.UNAUTHORIZED);

        try {
            controller.addPuntoRitiro(indirizzo, capienza);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        return new ModelAndView("redirect:/gestore");
    }
}
