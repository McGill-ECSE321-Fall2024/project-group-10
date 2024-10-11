package com.mcgill.ecse321.GameShop.repository;



import org.springframework.data.repository.CrudRepository;



import com.mcgill.ecse321.GameShop.model.Reply;

public interface ReplyRepository extends CrudRepository<Reply, Integer> {
   public Reply findByID(int reply_id);
}