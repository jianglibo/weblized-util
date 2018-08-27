package com.go2wheel.weblizedutil.repository;

import static com.go2wheel.weblizedutil.jooqschema.tables.UserAccount.USER_ACCOUNT;
import static com.go2wheel.weblizedutil.jooqschema.tables.UserAndRole.USER_AND_ROLE;
import static com.go2wheel.weblizedutil.jooqschema.tables.UserRole.USER_ROLE;

import java.util.HashSet;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go2wheel.weblizedutil.jooqschema.tables.records.UserAccountRecord;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.model.UserRole;


@Repository
public class JOOQUserAccountRepository extends RepositoryBaseImpl<UserAccountRecord, UserAccount>
		implements UserAccountRepository {
	@Autowired
	protected JOOQUserAccountRepository(DSLContext jooq) {
		super(USER_ACCOUNT, UserAccount.class, jooq);
	}

	@Override
	public UserAccount findByEmail(String email) {
		return jooq.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.EMAIL.eq(email)).fetchOneInto(UserAccount.class);
	}

	@Override
	public UserAccount findByMobile(String mobile) {
		return jooq.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.MOBILE.eq(mobile)).fetchOneInto(UserAccount.class);
	}

	@Override
	public UserAccount findByName(String name) {
		return jooq.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.USERNAME.eq(name)).fetchOneInto(UserAccount.class);
	}

	@Override
	public List<UserAccount> findLikeName(String partOfName) {
		String likeStr = partOfName.indexOf('%') == -1 ? '%' + partOfName + '%' : partOfName;
		return jooq.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.USERNAME.like(likeStr)).fetchInto(UserAccount.class);
	}

	@Override
	public UserAccount findByUsernameOrEmailOrMobile(String usernameOrEmailOrMobile) {
		UserAccount ua = jooq.selectFrom(USER_ACCOUNT).where(USER_ACCOUNT.USERNAME.eq(usernameOrEmailOrMobile)
				.or(USER_ACCOUNT.EMAIL.eq(usernameOrEmailOrMobile))
				.or(USER_ACCOUNT.MOBILE.eq(usernameOrEmailOrMobile))).fetchOneInto(UserAccount.class);
		if (ua != null) {
			List<UserRole> urs = jooq.selectDistinct()
				.from(USER_ROLE)
				.join(USER_AND_ROLE)
				.on(USER_ROLE.ID.eq(USER_AND_ROLE.ROLE_ID))
				.where(USER_AND_ROLE.USER_ID.eq(ua.getId()))
				.fetchInto(UserRole.class);
			ua.setRoles(new HashSet<>(urs));
		}
		return ua;
	}

	@Override
	public UserAccount saveRolesAndReturn(UserAccount ua, List<UserRole> userroles) {
		userroles.stream().forEach(r -> {
			jooq.insertInto(USER_AND_ROLE).set(USER_AND_ROLE.ROLE_ID, r.getId())
			.set(USER_AND_ROLE.USER_ID, ua.getId()).execute();
		});
		ua.setRoles(new HashSet<>(userroles));
		return ua;
	}
}
