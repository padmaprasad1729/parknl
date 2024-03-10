package com.infosys.parknl.service;

import com.infosys.parknl.model.RegisterDTO;
import com.infosys.parknl.model.UnRegisterDTO;

public interface ParkingService {

    public String register(RegisterDTO input);

    public String unregister(UnRegisterDTO input);
}
