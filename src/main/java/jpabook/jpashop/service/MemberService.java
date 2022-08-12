package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//DB에서 롤백하지 않고 실제로 데이터 변경을 적용할 지 여부 설정할 수 있음
    //readOnly : 데이터 변경이 없는 읽기 전용 메소드에서 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상
@Transactional(readOnly = true)
//클래스의 멤버변수에 대해 생성자를 자동으로 만들어줌
@RequiredArgsConstructor
public class MemberService {

    //생성자 주입 방식 사용
    //생성자가 하나면, @Autowired 생략 가능
    //멤버변수 초기화하지 않았을 경우 컴파일 시점에서 실수 잡기 위해 final 사용
    private final MemberRepository memberRepository;

    /**
     * 회원가입
    **/
    //데이터 쓰기 역할을 하는 메소드만 따로 Transactional(read = false) 명시
        //read = false가 디폴트이기 때문에 Transactional만 적어도 됨
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복회원 확인
    **/
    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
    **/
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 개별 조회
    **/
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
