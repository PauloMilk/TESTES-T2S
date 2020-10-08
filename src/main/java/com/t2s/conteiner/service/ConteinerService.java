package com.t2s.conteiner.service;

import com.t2s.conteiner.model.entity.Conteiner;
import org.springframework.stereotype.Service;

@Service
public interface ConteinerService {

    Conteiner salvar(Conteiner conteiner);

}
