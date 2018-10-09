package co.model;

import java.util.List;
import java.util.LinkedHashMap;

import lombok.Setter;
import lombok.Data;
import lombok.ToString;

import co.model.Method;

@Data
public class Tier {
  private final String url;
  private final List<Method> method;
  private final List<LinkedHashMap> call;
}
