package com.example.demo.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.member.Member;
import com.example.demo.member.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberTest {
	
	@Autowired
	MemberRepository memberRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Test
	@DisplayName("Auditing 테스트")
	@WithMockUser(username = "gildong", roles ="USER")
	@Transactional
	public void auditinhTest() {
		Member member = new Member();
		em.persist(member);
		em.flush();
		em.clear();
		
		Member savedMember = memberRepository.findById(member.getId()).get();
		
	}

}
