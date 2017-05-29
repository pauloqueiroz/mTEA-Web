package br.com.ufpi.enuns;

/**
 * 
 * @author Paulo Sergio
 *
 *Enum que representa a situação de uma lista de atividades.
 */
public enum SituacaoEnum {
	
	CADASTRADO{
		public String toString(){
			return "Cadastrado(a)";
		}
	},
	ENVIADO{
		public String toString(){
			return "Enviado(a)";
		}
	},
	CONCLUIDO{
		public String toString(){
			return "Concluído(a)";
		}
	}

}
