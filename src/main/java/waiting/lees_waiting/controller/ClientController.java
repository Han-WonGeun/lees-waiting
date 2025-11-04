package waiting.lees_waiting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import waiting.lees_waiting.service.ClientService;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/notice")
    public String notice() {
        return "client/notice";
    }

    @GetMapping("/people")
    public String people() {
        return "client/people";
    }

    @PostMapping("/phone")
    public String phone(@RequestParam int people, Model model) {
        model.addAttribute("people", people);
        return "client/phone";
    }

    @PostMapping("/register")
    public String register(@RequestParam int people, @RequestParam String phone, Model model) {
        clientService.registerWaiting(people, phone);
        String estimatedTime = clientService.getEstimatedWaitingTime(people);

        // TODO: SMS 발송 로직

        model.addAttribute("estimatedTime", estimatedTime);
        return "client/waiting";
    }
}
