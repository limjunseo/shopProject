package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.cart.Cart;
import com.example.demo.cart.CartRepository;
import com.example.demo.member.Member;
import com.example.demo.member.MemberFormDto;
import com.example.demo.member.MemberRepository;
import com.mysql.cj.log.Log;

import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
@Slf4j
public class CartTest {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@Transactional
	@DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
	public void findCartAndMemberTest() {
		Member member = this.createMember();
		Cart cart= new Cart();

		em.persist(member);
		em.persist(cart);
		cart.setMember(member);
		
		em.flush();
		em.clear();
		
		Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
		assertEquals(savedCart.getMember().getId(), member.getId());

	}
	
	
	
	private Member createMember() {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail("test@email.com");
		memberFormDto.setAddress("서울");
		memberFormDto.setPassword("1234");
		memberFormDto.setName("홍길동");
		
		return Member.createMember(memberFormDto, passwordEncoder);
	}

}
