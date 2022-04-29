package com.example.finance.api.expense;

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
@RequestMapping("/v1/expenses")
public class ExpenseResource {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> findByFilter(ExpenseFilter filter, Pageable pageable) {
        Page<Expense> page = expenseRepository.findByFilter(filter, pageable);
        Page<ExpenseResponse> response = page.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request) {
        var expense = expenseService.create(toEntity(request));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(expense.getId())
                .toUri();

        return ResponseEntity.created(location).body(toResponse(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.update(id, toEntity(request));
        return ResponseEntity.ok(toResponse(expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Long id) {
        Expense expense = expenseService.findById(id);
        return ResponseEntity.ok(toResponse(expense));
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        expenseService.pay(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/payment")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        expenseService.cancelPayment(id);
        return ResponseEntity.noContent().build();
    }

    private Expense toEntity(ExpenseRequest request) {
        return Expense.builder()
                .description(request.description())
                .amount(request.amount())
                .dateDue(request.dateDue())
                .datePayment(request.datePayment())
                .build();
    }

    public ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDateDue(),
                expense.getDatePayment(),
                expense.getStatus());
    }

}
