package com.riverbed.ims.model;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.Data;
import lombok.ToString;

@Data
public class Method {
  private final String name;
  private final int mode;
  private final int min;
  private final int max;
}
