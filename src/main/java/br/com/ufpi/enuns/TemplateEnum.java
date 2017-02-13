package br.com.ufpi.enuns;

/**
 * 
 * @author Paulo Sergio
 *
 */
public enum TemplateEnum {

	FORMAR_PALAVRA(1, "Formar Palavra"){
		public String toString(){
			return "Formar Palavra";
		}
	},
	SOBREPOR_PALAVRA(1, "Sobrepor Palavra"){
		public String toString(){
			return "Sobrepor Palavra";
		}
	},
	JOGO_MEMORIA(6, "Jogo de Memória"){
		public String toString(){
			return "Jogo de Memória";
		}
	},
	NOMEAR_FIGURA(1, "Nomear Figura"){
		public String toString(){
			return "Nomear Figura";
		}
	},
	GENIOS(6, "Gênios"){
		public String toString(){
			return "Gênios";
		}
	};
	private final int quantidadeArquivos;
	private final String descricao;
	
	private TemplateEnum(int quantidadeArquivos, String descricao){
		this.quantidadeArquivos = quantidadeArquivos;
		this.descricao = descricao;
	}
	
	public int getQuantidadeArquivos() {
		return quantidadeArquivos;
	}

	public String getDescricao() {
		return descricao;
	}
}
