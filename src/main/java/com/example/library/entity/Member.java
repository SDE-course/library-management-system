package com.example.library.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private LocalDate membershipDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.membershipDate = LocalDate.now();
    }
}