package com.riverbed.ims.model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import lombok.Setter;
import lombok.Data;
import lombok.ToString;

import com.riverbed.ims.model.MethodResult;

@Data
public class CallResult {
  private List<MethodResult> methods = new ArrayList<MethodResult>();
  private Object call;

  public void addResult(MethodResult method) {
    this.methods.add(method);
  }
}
