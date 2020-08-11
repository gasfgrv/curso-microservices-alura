package br.com.alura.gusto.microservice.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.gusto.microservice.loja.controller.dto.CompraDto;
import br.com.alura.gusto.microservice.loja.model.Compra;
import br.com.alura.gusto.microservice.loja.service.CompraService;

@RestController
@RequestMapping("/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@PostMapping
	public Compra realizaCompra(@RequestBody CompraDto compra) {
		return compraService.realizaCompra(compra);
	}
}
