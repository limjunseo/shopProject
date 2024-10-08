package com.example.demo.cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findByMemberId(Long memberId);

}
