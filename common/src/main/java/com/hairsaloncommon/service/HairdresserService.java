package com.hairsaloncommon.service;

import com.hairsaloncommon.repository.HairdresserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HairdresserService {

    private final HairdresserRepository hairdresserRepo;
}
