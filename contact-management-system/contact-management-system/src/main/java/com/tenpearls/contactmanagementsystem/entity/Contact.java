package com.tenpearls.contactmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String title;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private String emailType = "Personal";

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private String phoneType = "Personal";

    @Column(nullable = false)
    @Builder.Default
    private Boolean favorite = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}