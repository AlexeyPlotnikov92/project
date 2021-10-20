package com.alexei.testtask.controller;

import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.ClientSorting;
import com.alexei.testtask.service.CreditSorting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/credits")
public class CreditController {
    private final BankService bankService;

    public CreditController(BankService bankService) {
        this.bankService = bankService;
    }


    @GetMapping
    public ModelAndView getCredits(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dir", required = false) String direction
    ) {
        List<CreditSorting> sortingProperties = bankService.getSortingCreditsProperties();
        CreditSorting selectedSorting = getSelectedSorting(sortingProperties, order, direction);
        ModelAndView modelAndView = new ModelAndView("credits");
        modelAndView.addObject("credits", bankService.findAllCredits(selectedSorting));
        modelAndView.addObject("sorting", sortingProperties);
        modelAndView.addObject("selectedSorting", selectedSorting);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getCreditById(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("credit");
        modelAndView.addObject("credit", bankService.findCreditById(UUID.fromString(id)));
        modelAndView.addObject("creditId", id);
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createCredit(@RequestParam Integer creditLimit,
                                     @RequestParam Integer interestRate) {
        if (creditLimit != null && interestRate != null) {
            Credit credit = new Credit(null, creditLimit, interestRate, null);
            bankService.saveCredit(credit);
        }
        return new ModelAndView("redirect:/admin/credits");
    }

    @PostMapping("/{id}")
    public ModelAndView updateCredit(@PathVariable String id,
                                     @RequestParam Integer creditLimit,
                                     @RequestParam Integer interestRate) {
        Credit credit = new Credit(UUID.fromString(id), creditLimit, interestRate,
                bankService.findCreditById(UUID.fromString(id)).getBank());
        bankService.saveCredit(credit);
        return new ModelAndView("redirect:/admin/credits");
    }

    @PostMapping("/{id}/remove")
    public ModelAndView delete(@PathVariable String id) {
        bankService.deleteCreditById(UUID.fromString(id));
        return new ModelAndView("redirect:/admin/credits");
    }

    private CreditSorting getSelectedSorting(List<CreditSorting> sortingProperties, String order, String dir) {
        if (StringUtils.isNotEmpty(order)) {
            CreditSorting.Direction direction;
            try {
                direction = CreditSorting.Direction.valueOf(dir);
            } catch (IllegalArgumentException e) {
                direction = CreditSorting.Direction.ASC;
            }
            CreditSorting creditSorting = sortingProperties.stream()
                    .filter(sp -> sp.getColumn().equals(order))
                    .findFirst()
                    .orElse(null);
            if (creditSorting != null) {
                creditSorting.setDirection(direction);
            } else {
                creditSorting = new CreditSorting("id", "id",
                        CreditSorting.Direction.ASC);
            }
            return creditSorting;
        } else {
            return new CreditSorting("id", "id", CreditSorting.Direction.ASC);
        }
    }
}
