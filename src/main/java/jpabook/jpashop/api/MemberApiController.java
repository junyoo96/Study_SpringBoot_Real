package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller와 @ResponseBody 모두 포함하고 있는 어노테이션
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 파라미터로 엔티티를 받기
     * 문제점 : 엔티티가 변경되면 API 스펙이 변경됨
    **/
//    @PostMapping("/api/v1/members")
//    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
//        Long id = memberService.join(member);
//        return new CreateMemberResponse(id);
//    }

    /**
     * 회원 조회 메소드
    **/
    @GetMapping("api/v1/members")
    public Result membersV2()  {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    /**
     * Collections를 반환하면 나중에 json 스펙을 확장하기 어려워지므로 Result라는 객체를 만들어 반환
    **/
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    /**
     * 회원 조회 메소드 DTO
    **/
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    /**
     * 회원 저장 메소드
     * 파라미터로 DTO 받기
     * 실무에서는 엔티티를 API 스펙에 노출하면 안되기 때문
    **/
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 수정 메소드
     * 파라미터로 DTO 받기
    **/
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        //command 메소드와 쿼리 메소드 분리
        //command 메소드
        memberService.update(id, request.getName());
        //query 메소드(조회)
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /**
     * 회원 수정 DTO
    **/
    @Data static class UpdateMemberRequest {
        private String name;
    }

    /**
     * 회원 수정 DTO
     **/
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    /**
     * 회원 등록을 위한 DTO
     * 현재 Controller안에서만 사용하는 DTO의 경우 inner class로 만들고,
     * 그렇지 않으면 따로 Class 파일 만들어서 사용
    **/
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    /**
     * 회원 등록 DTO
    **/
    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}