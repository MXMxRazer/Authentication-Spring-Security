package ca.sheridancollege.hoodsi.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.hoodsi.beans.User;

@Repository
public class DatabaseAccess {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private NamedParameterJdbcTemplate jdbc; 

	public User findUserAccount(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM sec_user where email = :email";
		namedParameters.addValue("email", email);

		try {
			return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<User>(User.class));
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}
	
	public List<String> getRolesById(Long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		String query = "SELECT sec_role.roleName "
			+ "FROM user_role, sec_role "
			+ "WHERE user_role.roleId = sec_role.roleId "
			+ "AND userId = :userId";
		namedParameters.addValue("userId", userId);
		
		return jdbc.queryForList(query, namedParameters, String.class);
	}

	public void insert(String email, String password, int roleId) {
		MapSqlParameterSource namedParamerters = new MapSqlParameterSource();

		String query = "INSERT INTO sec_user ( email, encryptedPassword, enabled ) VALUES (:email, :encoded, :enabled)";

		namedParamerters.addValue("email", email);
		namedParamerters.addValue("encoded", encoder.encode(password));
		namedParamerters.addValue("enabled", 1);

		jdbc.update(query, namedParamerters);

		User found = findUserAccount(email);

		long reqId = found.getUserId();

		String query1 = "INSERT INTO user_role (userId, roleId) VALUES (:id, :role)";
		namedParamerters.addValue("id", reqId);
		namedParamerters.addValue("role", roleId);

		jdbc.update(query1, namedParamerters);
	}

	
	
}
