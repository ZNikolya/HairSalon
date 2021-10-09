package com.hairsaloncommon.repository;

import com.hairsaloncommon.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HairdresserRepository extends JpaRepository<Worker,Integer> {

}
