package hajiton.chaebee.domain.member.service;



import hajiton.chaebee.domain.member.dto.MemberReq;
import hajiton.chaebee.domain.member.dto.MemberRes;

public interface MemberService {

    MemberRes.Login login(MemberReq.Login request);
}