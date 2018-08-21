package com.riverbed.ims.model;

import java.util.List;
//import java.util.Map;
import java.util.LinkedHashMap;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;

import com.riverbed.ims.model.Method;

@Data
public class Tier {
  private final String url;
  private final List<Method> method;
  private final List<LinkedHashMap> call;
}
