package com.example.finance.api.expense;

import com.example.finance.api.common.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Expenses", description = "Endpoints for managing expenses")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/expenses")
public class ExpenseResource {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Find expenses by filter")
    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> findByFilter(ExpenseFilter filter, Pageable pageable) {
        Page<Expense> page = expenseRepository.findByFilter(filter, pageable, securityUtils.getUserId());
        Page<ExpenseResponse> response = page.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create expense")
    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request) {
        var expense = expenseService.create(toEntity(request), securityUtils.getUserId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(expense.getId())
                .toUri();

        return ResponseEntity.created(location).body(toResponse(expense));
    }

    @Operation(summary = "Update expense")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.update(id, toEntity(request), securityUtils.getUserId());
        return ResponseEntity.ok(toResponse(expense));
    }

    @Operation(summary = "Delete expense by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.deleteById(id, securityUtils.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get expense by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> findById(@PathVariable Long id) {
        Expense expense = expenseService.findByIdAndUser(id, securityUtils.getUserId());
        return ResponseEntity.ok(toResponse(expense));
    }

    @Operation(summary = "Pay expense")
    @PutMapping("/{id}/payment")
    public ResponseEntity<Void> pay(@PathVariable Long id) {
        expenseService.pay(id, securityUtils.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel payment of expense")
    @DeleteMapping("/{id}/payment")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long id) {
        expenseService.cancelPayment(id, securityUtils.getUserId());
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
