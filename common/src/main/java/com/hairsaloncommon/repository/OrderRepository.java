package com.hairsaloncommon.repository;

import com.hairsaloncommon.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {

}
