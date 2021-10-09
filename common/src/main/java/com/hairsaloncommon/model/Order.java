package com.hairsaloncommon.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date startDatetime;
    private Date endDatetime;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Service> services;
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToOne
    private User user;
}
