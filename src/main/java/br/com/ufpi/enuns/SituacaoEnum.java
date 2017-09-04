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
	},
	ATIVO{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Ativo";
		}
	},
	INATIVO{
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Inativo";
		}
	}
		
}
