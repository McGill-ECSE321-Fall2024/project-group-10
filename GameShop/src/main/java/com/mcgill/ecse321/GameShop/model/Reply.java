/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 60 "../../../../../../model.ump"
// line 201 "../../../../../../model.ump"
public class Reply
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ReviewRating { Like, Dislike }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Reply> replysById = new HashMap<Integer, Reply>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reply Attributes
  private int id;
  private Date replyDate;
  private String description;
  private ReviewRating reviewRating;

  //Reply Associations
  private Manager manager;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reply(int aId, Date aReplyDate, String aDescription, Manager aManager)
  {
    replyDate = aReplyDate;
    description = aDescription;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setManager(aManager))
    {
      throw new RuntimeException("Unable to create Reply due to aManager. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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
      replysById.remove(anOldId);
    }
    replysById.put(aId, this);
    return wasSet;
  }

  public boolean setReplyDate(Date aReplyDate)
  {
    boolean wasSet = false;
    replyDate = aReplyDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setReviewRating(ReviewRating aReviewRating)
  {
    boolean wasSet = false;
    reviewRating = aReviewRating;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Reply getWithId(int aId)
  {
    return replysById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public Date getReplyDate()
  {
    return replyDate;
  }

  public String getDescription()
  {
    return description;
  }

  public ReviewRating getReviewRating()
  {
    return reviewRating;
  }
  /* Code from template association_GetOne */
  public Manager getManager()
  {
    return manager;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setManager(Manager aNewManager)
  {
    boolean wasSet = false;
    if (aNewManager != null)
    {
      manager = aNewManager;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    replysById.remove(getId());
    manager = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "replyDate" + "=" + (getReplyDate() != null ? !getReplyDate().equals(this)  ? getReplyDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "reviewRating" + "=" + (getReviewRating() != null ? !getReviewRating().equals(this)  ? getReviewRating().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "manager = "+(getManager()!=null?Integer.toHexString(System.identityHashCode(getManager())):"null");
  }
}