package com.concord.trivio.service;

import com.concord.trivio.dto.RequirementRequest;
import com.concord.trivio.entity.Requirement;

public interface RequirementService {

    Requirement cadastrar(RequirementRequest request);
}
