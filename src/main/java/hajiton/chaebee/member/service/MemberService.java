package hajiton.chaebee.member.service;



import hajiton.chaebee.member.dto.MemberReq;
import hajiton.chaebee.member.dto.MemberRes;

public interface MemberService {

    MemberRes.Login login(MemberReq.Login request);
}