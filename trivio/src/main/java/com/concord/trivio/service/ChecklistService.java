package com.concord.trivio.service;

import com.concord.trivio.dto.ChecklistRequest;
import com.concord.trivio.entity.Checklist;

public interface ChecklistService {

    Checklist cadastrar(ChecklistRequest request);

}
