package com.riverbed.ims.model;

import lombok.Setter;
import lombok.Data;
import lombok.ToString;

@Data
public class Result {
  private String type;
  private long actualRuntime;
  private Integer iterations;
  private Integer duration;
  private double load;
}
