/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 42 "../../../../../../model.ump"
// line 191 "../../../../../../model.ump"
public class Order
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Order> ordersById = new HashMap<Integer, Order>();
  private static Map<String, Order> ordersByTrackingNumber = new HashMap<String, Order>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int id;
  private String trackingNumber;
  private Date orderDate;
  private String note;
  private int paymentCard;

  //Order Associations
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aId, String aTrackingNumber, Date aOrderDate, String aNote, int aPaymentCard, Customer aCustomer)
  {
    orderDate = aOrderDate;
    note = aNote;
    paymentCard = aPaymentCard;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setTrackingNumber(aTrackingNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate trackingNumber. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create Order due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setId(int aId)
  {
    boolean wasSet = false;
    Integer anOldId = getId();
    if (anOldId != null && anOldId.equals(aId)) {
      return true;
    }
    if (hasWithId(aId)) {
      return wasSet;
    }
    id = aId;
    wasSet = true;
    if (anOldId != null) {
      ordersById.remove(anOldId);
    }
    ordersById.put(aId, this);
    return wasSet;
  }

  public boolean setTrackingNumber(String aTrackingNumber)
  {
    boolean wasSet = false;
    String anOldTrackingNumber = getTrackingNumber();
    if (anOldTrackingNumber != null && anOldTrackingNumber.equals(aTrackingNumber)) {
      return true;
    }
    if (hasWithTrackingNumber(aTrackingNumber)) {
      return wasSet;
    }
    trackingNumber = aTrackingNumber;
    wasSet = true;
    if (anOldTrackingNumber != null) {
      ordersByTrackingNumber.remove(anOldTrackingNumber);
    }
    ordersByTrackingNumber.put(aTrackingNumber, this);
    return wasSet;
  }

  public boolean setOrderDate(Date aOrderDate)
  {
    boolean wasSet = false;
    orderDate = aOrderDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setNote(String aNote)
  {
    boolean wasSet = false;
    note = aNote;
    wasSet = true;
    return wasSet;
  }

  public boolean setPaymentCard(int aPaymentCard)
  {
    boolean wasSet = false;
    paymentCard = aPaymentCard;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Order getWithId(int aId)
  {
    return ordersById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getTrackingNumber()
  {
    return trackingNumber;
  }
  /* Code from template attribute_GetUnique */
  public static Order getWithTrackingNumber(String aTrackingNumber)
  {
    return ordersByTrackingNumber.get(aTrackingNumber);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithTrackingNumber(String aTrackingNumber)
  {
    return getWithTrackingNumber(aTrackingNumber) != null;
  }

  public Date getOrderDate()
  {
    return orderDate;
  }

  public String getNote()
  {
    return note;
  }

  public int getPaymentCard()
  {
    return paymentCard;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCustomer(Customer aNewCustomer)
  {
    boolean wasSet = false;
    if (aNewCustomer != null)
    {
      customer = aNewCustomer;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    ordersById.remove(getId());
    ordersByTrackingNumber.remove(getTrackingNumber());
    customer = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "trackingNumber" + ":" + getTrackingNumber()+ "," +
            "note" + ":" + getNote()+ "," +
            "paymentCard" + ":" + getPaymentCard()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}