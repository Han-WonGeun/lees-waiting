package waiting.lees_waiting.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import waiting.lees_waiting.entity.Admin;
import waiting.lees_waiting.entity.RestaurantTable;
import waiting.lees_waiting.entity.WaitingInfo;
import waiting.lees_waiting.entity.WaitingStatus;
import waiting.lees_waiting.service.AdminService;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Optional<Admin> loginResult = adminService.login(username, password);
        if (loginResult.isPresent()) {
            HttpSession session = request.getSession();
            session.setAttribute("loginAdmin", loginResult.get());
            return "redirect:/admin/management";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        adminService.clearAllData();
        return "redirect:/";
    }

    @GetMapping("/management")
    public String management(Model model) {
        Map<String, Long> tableCounts = adminService.getTableCounts();
        List<RestaurantTable> tables = adminService.getAllTables();
        model.addAttribute("tableCounts", tableCounts);
        model.addAttribute("tables", tables);
        model.addAttribute("waitingList", adminService.getWaitingList()); 
        return "admin/management";
    }

    @PostMapping("/management/tables")
    public String setTables(@RequestParam int twoPersonTables, @RequestParam int fourPersonTables) {
        adminService.createTables(twoPersonTables, fourPersonTables);
        return "redirect:/admin/management";
    }

    @PostMapping("/management/entry")
    public String manageTime(@RequestParam Long tableId, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime entryTime) {
        adminService.updateTableEntryTime(tableId, entryTime);
        return "redirect:/admin/management";
    }

    @PostMapping("/management/update")
    public String updateWaitingStatus(@RequestParam Long waitingId, @RequestParam WaitingStatus status) {
        adminService.updateWaitingStatus(waitingId, status);
        return "redirect:/admin/management";
    }

    @PostMapping("/management/clear")
    public String clearTable(@RequestParam Long tableId) {
        adminService.clearTable(tableId);
        return "redirect:/admin/management";
    }

    @GetMapping("/api/waiting-list")
    @ResponseBody
    public List<WaitingInfo> getWaitingListApi() {
        return adminService.getWaitingList();
    }

    @GetMapping("/api/tables")
    @ResponseBody
    public List<RestaurantTable> getTablesApi() {
        return adminService.getAllTables();
    }
}
