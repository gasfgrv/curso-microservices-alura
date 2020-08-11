package br.com.alura.gusto.microservice.loja.controller.dto;

public class EnderecoDto {

	private String rua;
	private int numero;
	private String estado;

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	@Override
	public String toString() {
		return "EnderecoDto [rua=" + rua + ", numero=" + numero + ", estado=" + estado + "]";
	}

}
