package com.spring.project1.demo.controller;

import com.spring.project1.demo.data.LocationStats;
import com.spring.project1.demo.services.DeadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DeadController {

    @Autowired
    DeadDataService deadDataService;

    @GetMapping("/dead")
    public String dead(Model model) {
        prepareDeadData(model);
        return "dead";
    }


    private void prepareDeadData(Model model) {
        List<LocationStats> allStats = deadDataService.getAllStats();
        int totalReportedDead1 = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalDeadToday1 = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        String totalReportedDead = String.format("%,d", totalReportedDead1);
        String totalDeadToday = String.format("%,d", totalDeadToday1);

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedDead", totalReportedDead);
        model.addAttribute("totalDeadToday", totalDeadToday);
    }
}
