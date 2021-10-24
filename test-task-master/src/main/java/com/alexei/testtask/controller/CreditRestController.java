package com.alexei.testtask.controller;

import com.alexei.testtask.DTO.AсkDto;
import com.alexei.testtask.DTO.CreditDto;
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
    public static final String GET_CREDITS = "/api/v1/credits";
    public static final String GET_CREDIT = "/api/v1/credits/{id}";
    public static final String CREATE_CREDITS = "/api/v1/credits";
    public static final String EDIT_CREDITS = "/api/v1/credits/{id}";
    public static final String DELETE_CREDITS = "/api/v1/credits/{id}";
    private final CreditDtoFactory creditDtoFactory;
    private final BankService bankService;

    public CreditRestController(CreditDtoFactory creditDtoFactory, BankService bankService) {
        this.creditDtoFactory = creditDtoFactory;
        this.bankService = bankService;
    }

    @GetMapping(GET_CREDITS)
    public List<CreditDto> getCredits(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dir", required = false) String direction
    ) {
        List<CreditSorting> sortingProperties = bankService.getSortingCreditsProperties();
        CreditSorting selectedSorting = getSelectedSorting(sortingProperties, order, direction);
        List<Credit> creditsEntity = bankService.findAllCredits(selectedSorting);
        List<CreditDto> credits = new ArrayList<>();
        for (Credit credit : creditsEntity) {
            credits.add(creditDtoFactory.makeCreditDto(credit));
        }
        return credits;
    }

    @GetMapping(GET_CREDIT)
    public CreditDto getCredit(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("кредит с таким Id %s не найден", id));
        }
        Credit credit = bankService.findCreditById(UUID.fromString(id));
        return creditDtoFactory.makeCreditDto(credit);
    }

    @PostMapping(CREATE_CREDITS)
    public CreditDto createCredit(@RequestParam Integer creditLimit,
                                  @RequestParam Integer interestRate) {
        Credit credit = new Credit(null, creditLimit, interestRate, null);
        bankService.saveCredit(credit);
        return creditDtoFactory.makeCreditDto(credit);
    }

    @PatchMapping(EDIT_CREDITS)
    public CreditDto updateCredit(@PathVariable String id,
                                  @RequestParam Integer creditLimit,
                                  @RequestParam Integer interestRate) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("кредит с таким Id %s не найден", id));
        }
        Credit credit = new Credit(UUID.fromString(id), creditLimit, interestRate,
                bankService.findCreditById(UUID.fromString(id)).getBank());
        bankService.saveCredit(credit);
        return creditDtoFactory.makeCreditDto(credit);
    }

    @DeleteMapping(DELETE_CREDITS)
    public AсkDto deleteCredit(@PathVariable String id) {
        if (!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            throw new IllegalArgumentException(String.format("кредит с таким Id %s не найден", id));
        }
        bankService.deleteCreditById(UUID.fromString(id));
        return AсkDto.makeAnswer(true);
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
