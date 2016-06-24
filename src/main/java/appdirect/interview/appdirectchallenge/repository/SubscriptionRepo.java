package main.java.appdirect.interview.appdirectchallenge.repository;

import java.sql.PreparedStatement;
import java.util.List;

import main.java.appdirect.interview.appdirectchallenge.domain.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepo {

	protected final JdbcTemplate jdbcTemplate;

	@Autowired
	public SubscriptionRepo(JdbcTemplate jdbc) {
		this.jdbcTemplate = jdbc;
	}

	/**
	 * @param subsc
	 * @return uuid
	 */
	public Long createSubscription(Subscription subsc) {
		final KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate
				.update(c -> {
					PreparedStatement ps = c
							.prepareStatement(
									"INSERT INTO subscription(company_name, edition, status, market_place_base_url) VALUES (?, ?, ?, ?)",
									new String[] { "id" });
					ps.setString(1, subsc.companyName);
					ps.setString(2, subsc.edition);
					ps.setString(3, subsc.status);
					ps.setString(4, subsc.marketPlaceBaseUrl);
					return ps;
				}, key);
		return key.getKey().longValue();
	}
	
	/**
	 * @return the subscriptions
	 */
	public List<Subscription> listSubscriptions() {
		return jdbcTemplate
				.query("SELECT id, company_name, edition, status, market_place_base_url FROM subscription",
						mapper);
	}

	/**
	 * @param subscId
	 * @return the subscription object
	 */
	public Subscription readSubscription(Long subscId) {
		return jdbcTemplate
				.queryForObject(
						"SELECT id, company_name, edition, status, market_place_base_url FROM subscription WHERE id=?",
						mapper, subscId);
	}

	/**
	 * @param subscId
	 * @param edition
	 * @return true if updated, else false
	 */
	public boolean update(Long subscId, String edition) {
		return jdbcTemplate
				.update("UPDATE subscription SET edition = ? WHERE id = ?",
						edition, subscId) != 0;
	}

	/**
	 * @param subscId
	 * @param status
	 *            of the subscription
	 * @return true if updated, else false
	 */
	public boolean updateStatus(Long subscId, String status) {
		return jdbcTemplate.update(
				"UPDATE subscription SET status = ? WHERE id = ?", status, subscId) != 0;
	}

	/**
	 * @param subscId
	 *            of the subscription
	 * @return true if deleted, else falsee
	 */
	public boolean deleteSubscription(Long subscId) {
		return jdbcTemplate.update("DELETE FROM subscription WHERE id = ?", subscId) != 0;
	}

	private RowMapper<Subscription> mapper = (rs, rowNum) -> new Subscription.Builder()
			.id(rs.getLong("id")).companyName(rs.getString("company_name"))
			.edition(rs.getString("edition")).status(rs.getString("status"))
			.marketPlaceBaseUrl(rs.getString("market_place_base_url")).build();

}