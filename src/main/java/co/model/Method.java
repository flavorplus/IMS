package co.model;

import lombok.Setter;
import lombok.Data;
import lombok.ToString;

@Data
public class Method {
  private final String name;
  private final int mode;
  private final int min;
  private final int max;
  private final String message;
}
