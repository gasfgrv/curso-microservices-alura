package br.com.alura.gusto.microservice.loja.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.alura.gusto.microservice.loja.controller.dto.InfoEntregaDto;
import br.com.alura.gusto.microservice.loja.controller.dto.VoucherDto;

@FeignClient("transportador")
public interface TransportadorClient {

	@PostMapping("/entrega")
	public VoucherDto reservaEntrega(InfoEntregaDto pedidoDto);

}
