package com.example.demo.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Query("select o from Order o " +
		       "where o.member.email = :email " +
		       "order by o.orderDate desc")
	List<Order> findOrders(@Param("email") String email, org.springframework.data.domain.Pageable pageable);
	
	
	@Query("select count(o) from Order o "
			+ "where o.member.email = :email")
	Long countOrder(@Param("email") String email);
	
	

}
