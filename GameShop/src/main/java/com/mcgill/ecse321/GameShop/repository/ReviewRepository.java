package com.mcgill.ecse321.GameShop.repository;



import org.springframework.data.repository.CrudRepository;




import com.mcgill.ecse321.GameShop.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
   public Review findByID(int review_id);
}