package com.mrs.app.hall.service;

import com.mrs.app.hall.dto.HallCreateRequest;
import com.mrs.app.hall.dto.HallGetResponse;
import com.mrs.app.hall.entity.Hall;
import com.mrs.app.hall.mapper.HallMapper;
import com.mrs.app.hall.repository.HallDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class HallService {
    private final HallDAO hallDAO;
    private final HallMapper hallMapper;

    @Transactional
    public Hall create(HallCreateRequest createRequest) {
        return hallDAO.save(hallMapper.toEntity(createRequest));
    }

    public HallGetResponse findById(long id) {
        return hallDAO
                .findById(id)
                .map(hallMapper::toResponse)
                .orElseThrow();
    }
}
