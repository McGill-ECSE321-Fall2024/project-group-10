/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 88 "../../../../../../model.ump"
// line 227 "../../../../../../model.ump"
public class Platform
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Platform> platformsById = new HashMap<Integer, Platform>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Platform Attributes
  private int id;
  private String platformName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Platform(int aId, String aPlatformName)
  {
    platformName = aPlatformName;
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
      platformsById.remove(anOldId);
    }
    platformsById.put(aId, this);
    return wasSet;
  }

  public boolean setPlatformName(String aPlatformName)
  {
    boolean wasSet = false;
    platformName = aPlatformName;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Platform getWithId(int aId)
  {
    return platformsById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getPlatformName()
  {
    return platformName;
  }

  public void delete()
  {
    platformsById.remove(getId());
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "platformName" + ":" + getPlatformName()+ "]";
  }
}