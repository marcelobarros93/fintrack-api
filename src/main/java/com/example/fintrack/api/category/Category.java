package com.example.fintrack.api.category;

import com.example.fintrack.api.common.entity.AbstractEntity;
import com.example.fintrack.api.common.enums.BillType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_category")
public class Category extends AbstractEntity {

    @NotBlank
    private String name;

    @NotNull
    private Boolean active;

    @Enumerated(EnumType.STRING)
    private BillType type;

    @NotBlank
    @Column(name = "user_id", nullable = false)
    private String userId;

    public Category(Long id) {
        super(id);
    }

    public void create(String userId) {
        this.userId = userId;
        this.active = true;
    }
}