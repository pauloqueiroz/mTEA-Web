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
			return "Cadastrada";
		}
	},
	ENVIADO{
		public String toString(){
			return "Enviada";
		}
	},
	CONCLUIDO{
		public String toString(){
			return "Concluída";
		}
	}

}
