package Ojt.Board.service;

import Ojt.Board.domain.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    public Member saveMember(Member member);

    public void validDuplicateMember(Member member);


}
