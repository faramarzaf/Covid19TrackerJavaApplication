package com.spring.project1.demo.controller;

import com.spring.project1.demo.data.LocationStats;
import com.spring.project1.demo.services.RecoveredDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecoveredController {

    @Autowired
    RecoveredDataService recoveredDataService;

    @GetMapping("/recovered")
    public String recovered(Model model) {
        prepareReCoveredData(model);
        return "recovered";
    }

    private void prepareReCoveredData(Model model) {
        List<LocationStats> allStats = recoveredDataService.getAllStats();
        int totalReportedRecovered1 = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalRecoveredToday1 = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        String totalReportedRecovered = String.format("%,d", totalReportedRecovered1);
        String totalRecoveredToday = String.format("%,d", totalRecoveredToday1);

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedRecovered", totalReportedRecovered);
        model.addAttribute("totalRecoveredToday", totalRecoveredToday);
    }
}
