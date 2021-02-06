package com.spring.project1.demo.controller;

import com.spring.project1.demo.data.LocationStats;
import com.spring.project1.demo.services.NewCasesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CaseController {

    @Autowired
    NewCasesDataService newCasesDataService;

    @GetMapping("/new")
    public String newcase(Model model) {
        prepareCasesData(model);
        return "cases";
    }

    private void prepareCasesData(Model model) {
        List<LocationStats> allStats = newCasesDataService.getAllStats();
        int totalReportedCases1 = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases1 = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        String totalReportedCases = String.format("%,d", totalReportedCases1);
        String totalNewCases = String.format("%,d", totalNewCases1);

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
    }
}
