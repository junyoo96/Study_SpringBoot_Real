package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); //빈화면 이더라도 validation을 위해 비어있는 객체를 들고감
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    //@Valid : View에서 넘어오는 해당 객체에 대한 유효성 검사를 진행할 수 있음
    //BindingResult : 유효성 검사 후 문제가 생긴경우 에러처리하지 않고 코드를 그대로 진행하며 특정 처리(특정 페이지로 이동한다던지)를 진행할 수 있음
    public String create(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors()) {
            //회원등록 유효성 검사에 문제가 생겨서 다시 회원 등록폼으로 보내기
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
