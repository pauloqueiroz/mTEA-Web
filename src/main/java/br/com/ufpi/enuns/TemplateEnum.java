package br.com.ufpi.enuns;

/**
 * 
 * @author Paulo Sergio
 *
 */
public enum TemplateEnum {

	FORMAR_PALAVRA(0, 1, "Formar Palavra"){
		public String toString(){
			return "Formar Palavra";
		}
	},
	SOBREPOR_PALAVRA(0, 1, "Sobrepor Palavra"){
		public String toString(){
			return "Sobrepor Palavra";
		}
	},
	JOGO_MEMORIA(2, 6, "Jogo de Memória"){
		public String toString(){
			return "Jogo de Memória";
		}
	},
	NOMEAR_FIGURA(1, 1, "Nomear Figura"){
		public String toString(){
			return "Nomear Figura";
		}
	},
	GENIOS(2, 6, "Gênios"){
		public String toString(){
			return "Gênios";
		}
	};
	
	private final int quantidadeMinimaArquivos;
	private final int quantidadeMaximaArquivos;
	private final String descricao;
	
	private TemplateEnum(int quantidadeMinimaArquivos, int quantidadeMaximaArquivos, String descricao){
		this.quantidadeMinimaArquivos = quantidadeMinimaArquivos;
		this.quantidadeMaximaArquivos = quantidadeMaximaArquivos;
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public int getQuantidadeMinimaArquivos() {
		return quantidadeMinimaArquivos;
	}

	public int getQuantidadeMaximaArquivos() {
		return quantidadeMaximaArquivos;
	}
}
