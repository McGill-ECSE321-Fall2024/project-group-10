/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;

import jakarta.persistence.Entity;

// line 26 "../../../../../../model.ump"
// line 190 "../../../../../../model.ump"
@Entity
public class Employee extends Staff {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Employee(String aEmail, String aUsername, String aPassword, String aPhoneNumber, String aAddress) {
    super(aEmail, aUsername, aPassword, aPhoneNumber, aAddress);
  }

  protected Employee() {
  }
  // ------------------------
  // INTERFACE
  // ------------------------

  public void delete() {
    super.delete();
  }
}