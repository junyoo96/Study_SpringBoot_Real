package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

//JUnit실행할 때 Spring과 통합해서 실행하기 위해 사용
@RunWith(SpringRunner.class)
//SpringBoot를 띄운 상태로 테스트하기 위해 사용(이게 없으면 @Autowired 다 실패)
@SpringBootTest
//반복가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랙잭션을 시작하고 테스트가 끝나면 트랜잭션을 강제로 롤백
    //but, 이 어노테이션이 테스트 케이스에서 사용될 때만 롤백
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("위에서 예외가 발생해야 한다.");
    }

}