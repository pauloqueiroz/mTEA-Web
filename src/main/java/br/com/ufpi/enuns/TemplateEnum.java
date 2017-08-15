package br.com.ufpi.enuns;

/**
 * 
 * @author Paulo Sergio
 *
 */
public enum TemplateEnum {

	FORMAR_PALAVRA(0, 1, "Formar Palavra") {
		public String toString() {
			return "Formar Palavra";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	SOBREPOR_PALAVRA(0, 1, "Sobrepor Palavra") {
		public String toString() {
			return "Sobrepor Palavra";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	JOGO_MEMORIA(2, 6, "Jogo de Memória") {
		public String toString() {
			return "Jogo de Memória";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	NOMEAR_FIGURA(1, 1, "Nomear Figura") {
		public String toString() {
			return "Nomear Figura";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	GENIOS(2, 6, "Gênios") {
		public String toString() {
			return "Gênios";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return false;
		}
	},
	DITADO_MUDO(1, 1, "Ditado Mudo") {
		public String toString() {
			return "Ditado Mudo";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return true;
		}
	},
	COPIA_POR_COMPOSICAO(0, 0, "Cópia Por Composição") {
		public String toString() {
			return "Cópia Por Composição";
		}

		@Override
		public boolean isTemplateAudio() {
			// TODO Auto-generated method stub
			return true;
		}
	};

	private final int quantidadeMinimaArquivos;
	private final int quantidadeMaximaArquivos;
	private final String descricao;

	public abstract boolean isTemplateAudio();

	private TemplateEnum(int quantidadeMinimaArquivos, int quantidadeMaximaArquivos, String descricao) {
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
