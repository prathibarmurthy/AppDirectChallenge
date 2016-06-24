package main.java.appdirect.interview.appdirectchallenge.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import main.java.appdirect.interview.appdirectchallenge.domain.UserAccountInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserAccountRepo {

	protected final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserAccountRepo(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @param uuid
	 * @return list of all the user accounts
	 */
	public List<UserAccountInfo> list(Long subscriptionId) {
		return jdbcTemplate
				.query("SELECT id, openid, firstname, lastname, email, subscription_id FROM user_account WHERE subscription_id = ?",
						mapper, subscriptionId);
	}

	/**
	 * @param useraccount
	 * @return uuid
	 */
	public Long createUserAccount(UserAccountInfo useraccount) {
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate
				.update(c -> {
					PreparedStatement preparedStatement = c
							.prepareStatement(
									"INSERT INTO user_account(openid, firstname, lastname, email, subscription_id) VALUES (?, ?, ?, ?, ?)",
									new String[] { "id" });
					preparedStatement.setString(1, useraccount.openId);
					preparedStatement.setString(2, useraccount.firstname);
					preparedStatement.setString(3, useraccount.lastname);
					preparedStatement.setString(4, useraccount.email);
					preparedStatement.setLong(5, useraccount.subscriptionId);
					return preparedStatement;
				}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	/**
	 * @param openId
	 *            to be deleted
	 * @return true if deleted, else false
	 */
	public boolean deleteUsingOpenId(String openId) {
		return jdbcTemplate.update("DELETE FROM user_account WHERE openid = ?",
				openId) != 0;
	}

	/**
	 * @param uuid
	 *            to delete
	 * @return true if deleted, else false
	 */
	public boolean deleteUsingSubscriptionId(Long subscriptionId) {
		return jdbcTemplate.update(
				"DELETE FROM user_account WHERE subscription_id = ?",
				subscriptionId) != 0;
	}
	
	/**
	 * @param openId
	 *            of User
	 * @return
	 */
	public Optional<UserAccountInfo> readUsingOpenId(String openId) {
		try {
			return Optional
					.of(jdbcTemplate
							.queryForObject(
									"SELECT id, openid, firstname, lastname, email, subscription_id FROM user_account WHERE openid=?",
									mapper, openId));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private RowMapper<UserAccountInfo> mapper = (rs, rowNum) -> new UserAccountInfo.Builder()
			.id(rs.getLong("id")).openId(rs.getString("openid"))
			.name(rs.getString("firstname"), rs.getString("lastname"))
			.email(rs.getString("email"))
			.subscriptionId(rs.getLong("subscription_id")).build();

}