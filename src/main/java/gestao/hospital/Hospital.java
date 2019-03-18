package gestao.hospital;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="hospital")
public class Hospital {
  private String id;

  private String name;

  private String description;

  public Hospital( String name, String description){
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}