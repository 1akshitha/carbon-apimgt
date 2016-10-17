package org.wso2.carbon.apimgt.model;

import org.wso2.carbon.apimgt.model.Tier;
import java.util.*;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JaxRSServerCodegen", date = "2016-10-14T14:19:24.818+05:30")
public class TierList  {
  
  private Integer count = null;
  private String next = null;
  private String previous = null;
  private List<Tier> list = new ArrayList<Tier>();

  /**
   * Number of Tiers returned.

   **/
  public Integer getCount() {
    return count;
  }
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   * Link to the next subset of resources qualified.
Empty if no more resources are to be returned.

   **/
  public String getNext() {
    return next;
  }
  public void setNext(String next) {
    this.next = next;
  }

  /**
   * Link to the previous subset of resources qualified.
Empty if current subset is the first subset returned.

   **/
  public String getPrevious() {
    return previous;
  }
  public void setPrevious(String previous) {
    this.previous = previous;
  }

  /**
   **/
  public List<Tier> getList() {
    return list;
  }
  public void setList(List<Tier> list) {
    this.list = list;
  }


  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TierList {\n");
    
    sb.append("  count: ").append(count).append("\n");
    sb.append("  next: ").append(next).append("\n");
    sb.append("  previous: ").append(previous).append("\n");
    sb.append("  list: ").append(list).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
