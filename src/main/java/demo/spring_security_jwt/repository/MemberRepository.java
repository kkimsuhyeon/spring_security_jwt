package demo.spring_security_jwt.repository;

import demo.spring_security_jwt.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    public final JdbcTemplate jdbcTemplate;

    @Autowired
    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByMemberId(Long memberId) {
        String query = "SELECT * FROM member WHERE member_id = ?";
        List<Member> result = jdbcTemplate.query(query, memberRowMapper(), memberId);

        return result.stream().findFirst();
    }

    public Optional<Member> findById(String id) {

        String query = "SELECT * FROM member WHERE id = ?";
        List<Member> result = jdbcTemplate.query(query, memberRowMapper(), id);

        return result.stream().findFirst();
    }

    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {

            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {

                Member member = new Member();
                member.setMemberId(rs.getLong("member_id"));
                member.setId(rs.getString("id"));
                member.setName(rs.getString("name"));
                member.setPassword(rs.getString("password"));
                member.setRoles(List.of(rs.getString("role")));

                return member;
            }
        };
    }
}
