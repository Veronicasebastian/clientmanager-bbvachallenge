package com.bbva.clientmanager.repository;

import com.bbva.clientmanager.entity.Client;
import com.bbva.clientmanager.entity.TipoProductoBancario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByProductoBancarioList_TipoProductoBancario(TipoProductoBancario tipoProductoBancario);
}
