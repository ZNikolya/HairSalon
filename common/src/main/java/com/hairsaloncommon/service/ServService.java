package com.hairsaloncommon.service;

import com.hairsaloncommon.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServService {

    private final ServiceRepository serviceRepo;
}
