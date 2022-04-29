package com.example.finance.api.income;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/incomes")
public class IncomeResource {

    private final IncomeService incomeService;
    private final IncomeRepository incomeRepository;

    @GetMapping
    public ResponseEntity<Page<IncomeResponse>> findByFilter(IncomeFilter filter, Pageable pageable) {
        Page<Income> page = incomeRepository.findByFilter(filter, pageable);
        Page<IncomeResponse> response = page.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> create(@Valid @RequestBody IncomeRequest request) {
        var income = incomeService.create(toEntity(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(income.getId())
                .toUri();

        return ResponseEntity.created(location).body(toResponse(income));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody IncomeRequest request) {
        Income income = incomeService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(income));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incomeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> findById(@PathVariable Long id) {
        Income income = incomeService.findById(id);
        return ResponseEntity.ok(toResponse(income));
    }

    @PutMapping("/{id}/receipt")
    public ResponseEntity<Void> receive(@PathVariable Long id) {
        incomeService.receive(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/receipt")
    public ResponseEntity<Void> cancelReceipt(@PathVariable Long id) {
        incomeService.cancelReceipt(id);
        return ResponseEntity.noContent().build();
    }

    private Income toEntity(IncomeRequest request) {
        return Income.builder()
                .description(request.description())
                .amount(request.amount())
                .dateDue(request.dateDue())
                .dateReceipt(request.dateReceipt())
                .build();
    }

    public IncomeResponse toResponse(Income income) {
        return new IncomeResponse(
                income.getId(),
                income.getDescription(),
                income.getAmount(),
                income.getDateDue(),
                income.getDateReceipt(),
                income.getStatus());
    }

}
