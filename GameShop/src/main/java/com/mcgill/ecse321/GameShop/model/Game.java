/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package com.mcgill.ecse321.GameShop.model;
import java.util.*;
import java.sql.Date;

// line 69 "../../../../../../model.ump"
// line 234 "../../../../../../model.ump"
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GameStatus { InStock, OutOfStock, Archived }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Game> gamesByGame_id = new HashMap<Integer, Game>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private int game_id;
  private String description;
  private int price;
  private GameStatus gameStatus;
  private int stockQuantity;
  private String photoUrl;

  //Game Associations
  private List<Review> review;
  private List<Category> categories;
  private List<Platform> platforms;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(int aGame_id, String aDescription, int aPrice, GameStatus aGameStatus, int aStockQuantity, String aPhotoUrl)
  {
    description = aDescription;
    price = aPrice;
    gameStatus = aGameStatus;
    stockQuantity = aStockQuantity;
    photoUrl = aPhotoUrl;
    if (!setGame_id(aGame_id))
    {
      throw new RuntimeException("Cannot create due to duplicate game_id. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    review = new ArrayList<Review>();
    categories = new ArrayList<Category>();
    platforms = new ArrayList<Platform>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGame_id(int aGame_id)
  {
    boolean wasSet = false;
    Integer anOldGame_id = getGame_id();
    if (anOldGame_id != null && anOldGame_id.equals(aGame_id)) {
      return true;
    }
    if (hasWithGame_id(aGame_id)) {
      return wasSet;
    }
    game_id = aGame_id;
    wasSet = true;
    if (anOldGame_id != null) {
      gamesByGame_id.remove(anOldGame_id);
    }
    gamesByGame_id.put(aGame_id, this);
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

  public int getGame_id()
  {
    return game_id;
  }
  /* Code from template attribute_GetUnique */
  public static Game getWithGame_id(int aGame_id)
  {
    return gamesByGame_id.get(aGame_id);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithGame_id(int aGame_id)
  {
    return getWithGame_id(aGame_id) != null;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfReview()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Review addReview(int aReview_id, Date aReviewDate, String aDescription, int aRating, Review.GameRating aGameRating, Customer aCustomer)
  {
    return new Review(aReview_id, aReviewDate, aDescription, aRating, aGameRating, this, aCustomer);
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

  public void delete()
  {
    gamesByGame_id.remove(getGame_id());
    while (review.size() > 0)
    {
      Review aReview = review.get(review.size() - 1);
      aReview.delete();
      review.remove(aReview);
    }
    
    categories.clear();
    platforms.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "game_id" + ":" + getGame_id()+ "," +
            "description" + ":" + getDescription()+ "," +
            "price" + ":" + getPrice()+ "," +
            "stockQuantity" + ":" + getStockQuantity()+ "," +
            "photoUrl" + ":" + getPhotoUrl()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameStatus" + "=" + (getGameStatus() != null ? !getGameStatus().equals(this)  ? getGameStatus().toString().replaceAll("  ","    ") : "this" : "null");
  }
}