package br.com.alura.gusto.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.gusto.microservice.loja.client.FornecedorClient;
import br.com.alura.gusto.microservice.loja.controller.dto.CompraDto;
import br.com.alura.gusto.microservice.loja.controller.dto.InfoFornecedorDto;
import br.com.alura.gusto.microservice.loja.controller.dto.InfoPedidoDto;
import br.com.alura.gusto.microservice.loja.model.Compra;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallback")
	public Compra realizaCompra(CompraDto compra) {
		final String estado = compra.getEndereco().getEstado();
		
		LOG.info("buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		
		LOG.info("realizando um pedido");
		InfoPedidoDto infoPedido = fornecedorClient.realizaPedido(compra.getItens());
		
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(infoPedido.getId());
		compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());
		compraSalva.setEnderecoDestino(info.getEndereco().toString());
		
		return compraSalva;
	}

	public Compra realizaCompraFallback(CompraDto compra) {
		Compra compraFallback = new Compra();
		compraFallback.setEnderecoDestino(compra.getEndereco().getEstado());
		return compraFallback;
	}
	
}
