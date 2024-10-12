/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Table;

import java.sql.Date;

// line 41 "../../../../../../model.ump"
// line 216 "../../../../../../model.ump"
@Entity
@Table(name = "\"order\"")
public class Order
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Order> ordersByTrackingNumber = new HashMap<String, Order>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String trackingNumber;
  private Date orderDate;
  private String note;
  private int paymentCard;

  //Order Associations
  @ManyToOne
  @JoinColumn(name = "customer_id", nullable=false)
  private Customer customer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aOrderDate, String aNote, int aPaymentCard, Customer aCustomer)
  {
    orderDate = aOrderDate;
    note = aNote;
    paymentCard = aPaymentCard;
  

    if (!setCustomer(aCustomer))
    {
      throw new RuntimeException("Unable to create Order due to aCustomer. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  protected Order(){}

  //------------------------
  // INTERFACE
  //------------------------

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
    ordersByTrackingNumber.remove(getTrackingNumber());
    customer = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "trackingNumber" + ":" + getTrackingNumber()+ "," +
            "note" + ":" + getNote()+ "," +
            "paymentCard" + ":" + getPaymentCard()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orderDate" + "=" + (getOrderDate() != null ? !getOrderDate().equals(this)  ? getOrderDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null");
  }
}