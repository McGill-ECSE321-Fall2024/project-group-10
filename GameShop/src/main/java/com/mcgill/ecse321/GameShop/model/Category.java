/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 83 "../../../../../../model.ump"
// line 212 "../../../../../../model.ump"
public class Category
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Category> categorysById = new HashMap<Integer, Category>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Category Attributes
  private int id;
  private String categoryName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Category(int aId, String aCategoryName)
  {
    categoryName = aCategoryName;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
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
      categorysById.remove(anOldId);
    }
    categorysById.put(aId, this);
    return wasSet;
  }

  public boolean setCategoryName(String aCategoryName)
  {
    boolean wasSet = false;
    categoryName = aCategoryName;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Category getWithId(int aId)
  {
    return categorysById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void delete()
  {
    categorysById.remove(getId());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "categoryName" + ":" + getCategoryName()+ "]";
  }
}