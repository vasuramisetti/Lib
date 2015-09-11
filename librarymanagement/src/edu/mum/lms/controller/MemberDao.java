package edu.mum.lms.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.mum.lms.commonUtil.DbClient;
import edu.mum.lms.commonUtil.DbClient.FilterCondition;
import edu.mum.lms.commonUtil.JDBCUtil;
import edu.mum.lms.entity.Member;

public class MemberDao {

    private JDBCUtil db = new JDBCUtil();
    private static final String TABLE_NAME = "Member";

    public Member getMember(int memberId) {
        Member member = null;
        FilterCondition condition = new DbClient.FilterCondition();
        condition.addCondition("member_id", DbClient.EQUALS, memberId);

        Map<String, String> joinOn = new HashMap<String, String>();
        joinOn.put("person_id", "person_id");

        List<Map<String, Object>> membersMap = db.join(TABLE_NAME, "Person", null, null, null, "", joinOn);

        if (membersMap.size() > 0) {
            for (Map<String, Object> memberMap : membersMap) {
                member = new Member();
                member.setMemberId(memberId);
                member.setFirstName((String) memberMap.get("firstName"));
                member.setLastName((String) memberMap.get("lastName"));
                member.setPhoneNo((String) memberMap.get("phoneNo"));
                member.setStreet((String) memberMap.get("street"));
                member.setCity((String) memberMap.get("city"));
                member.setState((String) memberMap.get("state"));
                member.setZip((int) memberMap.get("zip"));
            }
        }
        return member;
    }

    public int addMember(int personId) {

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("person_id", personId);

        return db.insertRow(TABLE_NAME, map, true);
    }
}
