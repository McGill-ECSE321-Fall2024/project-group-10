/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;

// line 20 "../../../../../../model.ump"
// line 185 "../../../../../../model.ump"
public class Customer extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  private List<WishList> wishList;
  private Cart cart;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(int aId, String aEmail, String aUsername, String aPassword, int aPhoneNumber, String aAddress, Cart aCart)
  {
    super(aId, aEmail, aUsername, aPassword, aPhoneNumber, aAddress);
    wishList = new ArrayList<WishList>();
    if (!setCart(aCart))
    {
      throw new RuntimeException("Unable to create Customer due to aCart. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public WishList getWishList(int index)
  {
    WishList aWishList = wishList.get(index);
    return aWishList;
  }

  public List<WishList> getWishList()
  {
    List<WishList> newWishList = Collections.unmodifiableList(wishList);
    return newWishList;
  }

  public int numberOfWishList()
  {
    int number = wishList.size();
    return number;
  }

  public boolean hasWishList()
  {
    boolean has = wishList.size() > 0;
    return has;
  }

  public int indexOfWishList(WishList aWishList)
  {
    int index = wishList.indexOf(aWishList);
    return index;
  }
  /* Code from template association_GetOne */
  public Cart getCart()
  {
    return cart;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWishList()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public WishList addWishList(int aId, String aTitle)
  {
    return new WishList(aId, aTitle, this);
  }

  public boolean addWishList(WishList aWishList)
  {
    boolean wasAdded = false;
    if (wishList.contains(aWishList)) { return false; }
    Customer existingCustomer = aWishList.getCustomer();
    boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
    if (isNewCustomer)
    {
      aWishList.setCustomer(this);
    }
    else
    {
      wishList.add(aWishList);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWishList(WishList aWishList)
  {
    boolean wasRemoved = false;
    //Unable to remove aWishList, as it must always have a customer
    if (!this.equals(aWishList.getCustomer()))
    {
      wishList.remove(aWishList);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWishListAt(WishList aWishList, int index)
  {  
    boolean wasAdded = false;
    if(addWishList(aWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishList()) { index = numberOfWishList() - 1; }
      wishList.remove(aWishList);
      wishList.add(index, aWishList);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWishListAt(WishList aWishList, int index)
  {
    boolean wasAdded = false;
    if(wishList.contains(aWishList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWishList()) { index = numberOfWishList() - 1; }
      wishList.remove(aWishList);
      wishList.add(index, aWishList);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWishListAt(aWishList, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCart(Cart aNewCart)
  {
    boolean wasSet = false;
    if (aNewCart != null)
    {
      cart = aNewCart;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    while (wishList.size() > 0)
    {
      WishList aWishList = wishList.get(wishList.size() - 1);
      aWishList.delete();
      wishList.remove(aWishList);
    }
    
    cart = null;
    super.delete();
  }

}