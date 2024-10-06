/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 68 "../../../../../../model.ump"
// line 206 "../../../../../../model.ump"
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GameStatus { InStock, OutOfStock, Archived }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Game> gamesById = new HashMap<Integer, Game>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int id;
  private String description;
  private int price;
  private GameStatus gameStatus;
  private int stockQuantity;
  private String photoUrl;

  //Game Associations
  private List<Review> review;
  private List<Category> categories;
  private List<Platform> platforms;
  private List<Promotion> promotions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aId, String aDescription, int aPrice, GameStatus aGameStatus, int aStockQuantity, String aPhotoUrl)
  {
    description = aDescription;
    price = aPrice;
    gameStatus = aGameStatus;
    stockQuantity = aStockQuantity;
    photoUrl = aPhotoUrl;
    if (!setId(aId))
    {
      throw new RuntimeException("Cannot create due to duplicate id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    review = new ArrayList<Review>();
    categories = new ArrayList<Category>();
    platforms = new ArrayList<Platform>();
    promotions = new ArrayList<Promotion>();
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
      gamesById.remove(anOldId);
    }
    gamesById.put(aId, this);
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setPrice(int aPrice)
  {
    boolean wasSet = false;
    price = aPrice;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameStatus(GameStatus aGameStatus)
  {
    boolean wasSet = false;
    gameStatus = aGameStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setStockQuantity(int aStockQuantity)
  {
    boolean wasSet = false;
    stockQuantity = aStockQuantity;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhotoUrl(String aPhotoUrl)
  {
    boolean wasSet = false;
    photoUrl = aPhotoUrl;
    wasSet = true;
    return wasSet;
  }

  public int getId()
  {
    return id;
  }
  /* Code from template attribute_GetUnique */
  public static Game getWithId(int aId)
  {
    return gamesById.get(aId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithId(int aId)
  {
    return getWithId(aId) != null;
  }

  public String getDescription()
  {
    return description;
  }

  public int getPrice()
  {
    return price;
  }

  public GameStatus getGameStatus()
  {
    return gameStatus;
  }

  public int getStockQuantity()
  {
    return stockQuantity;
  }

  public String getPhotoUrl()
  {
    return photoUrl;
  }
  /* Code from template association_GetMany */
  public Review getReview(int index)
  {
    Review aReview = review.get(index);
    return aReview;
  }

  public List<Review> getReview()
  {
    List<Review> newReview = Collections.unmodifiableList(review);
    return newReview;
  }

  public int numberOfReview()
  {
    int number = review.size();
    return number;
  }

  public boolean hasReview()
  {
    boolean has = review.size() > 0;
    return has;
  }

  public int indexOfReview(Review aReview)
  {
    int index = review.indexOf(aReview);
    return index;
  }
  /* Code from template association_GetMany */
  public Category getCategory(int index)
  {
    Category aCategory = categories.get(index);
    return aCategory;
  }

  public List<Category> getCategories()
  {
    List<Category> newCategories = Collections.unmodifiableList(categories);
    return newCategories;
  }

  public int numberOfCategories()
  {
    int number = categories.size();
    return number;
  }

  public boolean hasCategories()
  {
    boolean has = categories.size() > 0;
    return has;
  }

  public int indexOfCategory(Category aCategory)
  {
    int index = categories.indexOf(aCategory);
    return index;
  }
  /* Code from template association_GetMany */
  public Platform getPlatform(int index)
  {
    Platform aPlatform = platforms.get(index);
    return aPlatform;
  }

  public List<Platform> getPlatforms()
  {
    List<Platform> newPlatforms = Collections.unmodifiableList(platforms);
    return newPlatforms;
  }

  public int numberOfPlatforms()
  {
    int number = platforms.size();
    return number;
  }

  public boolean hasPlatforms()
  {
    boolean has = platforms.size() > 0;
    return has;
  }

  public int indexOfPlatform(Platform aPlatform)
  {
    int index = platforms.indexOf(aPlatform);
    return index;
  }
  /* Code from template association_GetMany */
  public Promotion getPromotion(int index)
  {
    Promotion aPromotion = promotions.get(index);
    return aPromotion;
  }

  public List<Promotion> getPromotions()
  {
    List<Promotion> newPromotions = Collections.unmodifiableList(promotions);
    return newPromotions;
  }

  public int numberOfPromotions()
  {
    int number = promotions.size();
    return number;
  }

  public boolean hasPromotions()
  {
    boolean has = promotions.size() > 0;
    return has;
  }

  public int indexOfPromotion(Promotion aPromotion)
  {
    int index = promotions.indexOf(aPromotion);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReview()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Review addReview(int aId, Date aReviewDate, String aDescription, int aRating, Review.GameRating aGameRating, Customer aCustomer)
  {
    return new Review(aId, aReviewDate, aDescription, aRating, aGameRating, this, aCustomer);
  }

  public boolean addReview(Review aReview)
  {
    boolean wasAdded = false;
    if (review.contains(aReview)) { return false; }
    Game existingGame = aReview.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aReview.setGame(this);
    }
    else
    {
      review.add(aReview);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReview(Review aReview)
  {
    boolean wasRemoved = false;
    //Unable to remove aReview, as it must always have a game
    if (!this.equals(aReview.getGame()))
    {
      review.remove(aReview);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addReviewAt(Review aReview, int index)
  {  
    boolean wasAdded = false;
    if(addReview(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReview()) { index = numberOfReview() - 1; }
      review.remove(aReview);
      review.add(index, aReview);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReviewAt(Review aReview, int index)
  {
    boolean wasAdded = false;
    if(review.contains(aReview))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReview()) { index = numberOfReview() - 1; }
      review.remove(aReview);
      review.add(index, aReview);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReviewAt(aReview, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCategories()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCategory(Category aCategory)
  {
    boolean wasAdded = false;
    if (categories.contains(aCategory)) { return false; }
    categories.add(aCategory);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCategory(Category aCategory)
  {
    boolean wasRemoved = false;
    if (categories.contains(aCategory))
    {
      categories.remove(aCategory);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCategoryAt(Category aCategory, int index)
  {  
    boolean wasAdded = false;
    if(addCategory(aCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCategories()) { index = numberOfCategories() - 1; }
      categories.remove(aCategory);
      categories.add(index, aCategory);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCategoryAt(Category aCategory, int index)
  {
    boolean wasAdded = false;
    if(categories.contains(aCategory))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCategories()) { index = numberOfCategories() - 1; }
      categories.remove(aCategory);
      categories.add(index, aCategory);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCategoryAt(aCategory, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlatforms()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPlatform(Platform aPlatform)
  {
    boolean wasAdded = false;
    if (platforms.contains(aPlatform)) { return false; }
    platforms.add(aPlatform);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlatform(Platform aPlatform)
  {
    boolean wasRemoved = false;
    if (platforms.contains(aPlatform))
    {
      platforms.remove(aPlatform);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlatformAt(Platform aPlatform, int index)
  {  
    boolean wasAdded = false;
    if(addPlatform(aPlatform))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlatforms()) { index = numberOfPlatforms() - 1; }
      platforms.remove(aPlatform);
      platforms.add(index, aPlatform);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlatformAt(Platform aPlatform, int index)
  {
    boolean wasAdded = false;
    if(platforms.contains(aPlatform))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlatforms()) { index = numberOfPlatforms() - 1; }
      platforms.remove(aPlatform);
      platforms.add(index, aPlatform);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlatformAt(aPlatform, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPromotions()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addPromotion(Promotion aPromotion)
  {
    boolean wasAdded = false;
    if (promotions.contains(aPromotion)) { return false; }
    promotions.add(aPromotion);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePromotion(Promotion aPromotion)
  {
    boolean wasRemoved = false;
    if (promotions.contains(aPromotion))
    {
      promotions.remove(aPromotion);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPromotionAt(Promotion aPromotion, int index)
  {  
    boolean wasAdded = false;
    if(addPromotion(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePromotionAt(Promotion aPromotion, int index)
  {
    boolean wasAdded = false;
    if(promotions.contains(aPromotion))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPromotions()) { index = numberOfPromotions() - 1; }
      promotions.remove(aPromotion);
      promotions.add(index, aPromotion);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPromotionAt(aPromotion, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    gamesById.remove(getId());
    while (review.size() > 0)
    {
      Review aReview = review.get(review.size() - 1);
      aReview.delete();
      review.remove(aReview);
    }
    
    categories.clear();
    platforms.clear();
    promotions.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "description" + ":" + getDescription()+ "," +
            "price" + ":" + getPrice()+ "," +
            "stockQuantity" + ":" + getStockQuantity()+ "," +
            "photoUrl" + ":" + getPhotoUrl()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameStatus" + "=" + (getGameStatus() != null ? !getGameStatus().equals(this)  ? getGameStatus().toString().replaceAll("  ","    ") : "this" : "null");
  }
}