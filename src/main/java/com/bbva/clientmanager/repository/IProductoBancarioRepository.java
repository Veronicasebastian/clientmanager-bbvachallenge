package com.bbva.clientmanager.repository;

import com.bbva.clientmanager.entity.ProductoBancario;
import com.bbva.clientmanager.entity.TipoProductoBancario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductoBancarioRepository  extends JpaRepository<ProductoBancario, Long> {
    boolean existsByTipoProductoBancario(TipoProductoBancario tipoProductoBancario);
    List<ProductoBancario> findByTipoProductoBancarioIn(List<TipoProductoBancario> tipos);
}
