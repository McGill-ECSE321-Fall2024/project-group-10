/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/
// TODO
// Create a dictionary to keep track of returned items
package com.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 91 "../../../../../../model.ump"
// line 215 "../../../../../../model.ump"
public class SpecificGame
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ItemStatus { Confirmed, Returned }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, SpecificGame> specificgamesById = new HashMap<Integer, SpecificGame>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificGame Attributes
  private int id;
  private ItemStatus itemStatus;

  //SpecificGame Associations
  private Order order;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificGame(int aId, Order aOrder)
  {
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setOrder(aOrder))
    {
      throw new RuntimeException("Unable to create SpecificGame due to aOrder. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
      specificgamesById.remove(anOldId);
    }
    specificgamesById.put(aId, this);
    return wasSet;
  }

  public boolean setItemStatus(ItemStatus aItemStatus)
  {
    boolean wasSet = false;
    itemStatus = aItemStatus;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static SpecificGame getWithId(int aId)
  {
    return specificgamesById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public ItemStatus getItemStatus()
  {
    return itemStatus;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setOrder(Order aNewOrder)
  {
    boolean wasSet = false;
    if (aNewOrder != null)
    {
      order = aNewOrder;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    specificgamesById.remove(getId());
    order = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "itemStatus" + "=" + (getItemStatus() != null ? !getItemStatus().equals(this)  ? getItemStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null");
  }
}