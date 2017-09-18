package br.com.ufpi.enuns;

public enum TipoArquivoEnum {

	IMAGEM("image"){
		
	},
	AUDIO("audio"){
		
	},
	VIDEO("video"){
		
	};
	
	private String descricao;

	
	
	private TipoArquivoEnum(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
