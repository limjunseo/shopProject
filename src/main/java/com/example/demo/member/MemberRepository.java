package com.example.demo.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.protobuf.Option;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Optional<Member> findByEmail(String email);
	Optional<Member> findByName(String name);

}
