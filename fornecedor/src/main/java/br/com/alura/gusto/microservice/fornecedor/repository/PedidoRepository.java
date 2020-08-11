package br.com.alura.gusto.microservice.fornecedor.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.gusto.microservice.fornecedor.model.Pedido;

@Repository
public interface PedidoRepository extends CrudRepository<Pedido, Long> {

}
