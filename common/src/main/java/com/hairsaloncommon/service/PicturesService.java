package com.hairsaloncommon.service;

import com.hairsaloncommon.repository.PicturesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PicturesService {

    private final PicturesRepository picturesRepo;
}
