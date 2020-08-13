package br.com.alura.gusto.microservice.loja.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.gusto.microservice.loja.client.FornecedorClient;
import br.com.alura.gusto.microservice.loja.client.TransportadorClient;
import br.com.alura.gusto.microservice.loja.controller.dto.CompraDto;
import br.com.alura.gusto.microservice.loja.controller.dto.InfoEntregaDto;
import br.com.alura.gusto.microservice.loja.controller.dto.InfoFornecedorDto;
import br.com.alura.gusto.microservice.loja.controller.dto.InfoPedidoDto;
import br.com.alura.gusto.microservice.loja.controller.dto.VoucherDto;
import br.com.alura.gusto.microservice.loja.model.Compra;
import br.com.alura.gusto.microservice.loja.model.CompraState;
import br.com.alura.gusto.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {

	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private TransportadorClient transportadorClient;

	@Autowired
	private CompraRepository compraRepository;

	public Compra reprocessaCompra(Long id) {
		return null;
	}
	
	public Compra cancelaCompra(Long id) {
		return null;
	}
	
	@HystrixCommand(threadPoolKey = "getbyIdThreadPool")
	public Compra getbyId(Long id) {
		return compraRepository.findById(id).orElse(new Compra());
	}
	
	@HystrixCommand(fallbackMethod = "realizaCompraFallback", threadPoolKey = "realizaCompraThreadPool")
	public Compra realizaCompra(CompraDto compra) {

		Compra compraSalva = new Compra();
		compraSalva.setState(CompraState.RECEBIDO);
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		compraRepository.save(compraSalva);
		compra.setCompraId(compraSalva.getId());

		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		InfoPedidoDto pedido = fornecedorClient.realizaPedido(compra.getItens());

		compraSalva.setState(CompraState.PEDIDO_REALIZADO);
		compraSalva.setPedidoId(pedido.getId());
		compraSalva.setTempoDePreparo(pedido.getTempoDePreparo());
		compraRepository.save(compraSalva);

		InfoEntregaDto entregaDto = new InfoEntregaDto();
		entregaDto.setPedidoId(pedido.getId());
		entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo()));
		entregaDto.setEnderecoOrigem(info.getEndereco());
		VoucherDto voucher = transportadorClient.reservaEntrega(entregaDto);
		compraSalva.setState(CompraState.RESERVA_ENTREGA_REALIZADA);
		compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
		compraSalva.setVoucher(voucher.getNumero());
		compraRepository.save(compraSalva);

		return compraSalva;
	}

	public Compra realizaCompraFallback(CompraDto compra) {
		if (compra.getCompraId() != null) {
			return compraRepository.findById(compra.getCompraId()).get();
		}
		
		Compra compraFallback = new Compra();
		compraFallback.setEnderecoDestino(compra.getEndereco().getEstado());
		return compraFallback;
	}

}
