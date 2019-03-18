package gestao.hospital;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="hospital")
public class Hospital {
  private String id;

  private String nome;

  private String descricao;

  public Hospital( String nome, String descricao){
    this.nome = nome;
    this.descricao = descricao;
  }

  public String getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getDescricao() {
    return descricao;
  }
}