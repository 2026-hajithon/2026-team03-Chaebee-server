package hajiton.chaebee.domain.member;

import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;

public interface MemberService {

    MemberRes.Login login(MemberReq.Login request);
}
