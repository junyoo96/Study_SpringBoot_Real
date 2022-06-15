package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// DB에서 롤백하지 않고 실제로 데이터 변경을 적용할 수 여부
    // readOnly : 데이터 변경 비허용
@Transactional(readOnly = true)
// 클래스의 멤버변수에 대해 생성자를 자동으로 만들어줌
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;

    /**
     * 회원가입
    **/
    // 데이터 쓰기 역할을 하는 메소드만 따로 Transactional(read = false) 명시
        // read = false가 디폴트이기 때문에 Transactional만 적어도 됨
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
