package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AskDto;
import com.alexei.testtask.DTO.CreditDTO;
import com.alexei.testtask.entity.Credit;
import com.alexei.testtask.factories.CreditDtoFactory;
import com.alexei.testtask.service.BankService;
import com.alexei.testtask.service.CreditSorting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RestController
public class CreditRestController {
    public static final String FETCH_CREDITS = "/api/v1/credits";
    public static final String CREATE_CREDITS = "/api/v1/credits";
    public static final String EDIT_CREDITS = "/api/v1/credits/{id}";
    public static final String DELETE_CREDITS = "/api/v1/credits/{id}";
    private final CreditDtoFactory creditDtoFactory;
    private final BankService bankService;

    public CreditRestController(CreditDtoFactory creditDtoFactory, BankService bankService) {
        this.creditDtoFactory = creditDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(FETCH_CREDITS)
    public List<CreditDTO> getCredits(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dir", required = false) String direction
    ) {
        List<CreditSorting> sortingProperties = bankService.getSortingCreditsProperties();
        CreditSorting selectedSorting = getSelectedSorting(sortingProperties, order, direction);
        List<Credit> creditsEntity = bankService.findAllCredits(selectedSorting);
        List<CreditDTO> credits = new ArrayList<>();
        for (Credit credit : creditsEntity) {
            credits.add(creditDtoFactory.makeCreditDto(credit));
        }
        return credits;
    }

    @PostMapping(CREATE_CREDITS)
    public CreditDTO createCredit(@RequestParam Integer creditLimit,
                                  @RequestParam Integer interestRate) {
        Credit credit = new Credit(null, creditLimit, interestRate, null);
        bankService.saveCredit(credit);
        return creditDtoFactory.makeCreditDto(credit);
    }

    @PatchMapping(EDIT_CREDITS)
    public CreditDTO updateCredit(@PathVariable String id,
                                  @RequestParam Integer creditLimit,
                                  @RequestParam Integer interestRate) {
        Credit credit = new Credit(UUID.fromString(id), creditLimit, interestRate,
                bankService.findCreditById(UUID.fromString(id)).getBank());
        bankService.saveCredit(credit);
        return creditDtoFactory.makeCreditDto(credit);
    }

    @DeleteMapping(DELETE_CREDITS)
    public AskDto deleteCredit(@PathVariable String id) {
        bankService.deleteCreditById(UUID.fromString(id));
        return AskDto.makeAnswer(true);
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
