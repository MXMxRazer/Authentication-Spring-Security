package ca.sheridancollege.hoodsi.security;

import java.util.ArrayList;
import java.util.List;

import ca.sheridancollege.hoodsi.beans.CheckUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.hoodsi.database.DatabaseAccess;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private DatabaseAccess da;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Find the user based on the username (read email)
		ca.sheridancollege.hoodsi.beans.User user = da.findUserAccount(username);
		
		// If the user doesn't exist, throw an exception
		if (user == null) {
			System.out.println("User not found:" + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}
		
		//Get a list of roles for that user
		List<String> roleNameList = da.getRolesById(user.getUserId());

		// Change the list of the user's roles into a list of GrantedAuthority
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNameList != null) {
			for (String role : roleNameList) {
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		
		// Create a user based on the information above
		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), grantList);
		return userDetails;

	}

	public void insertion(CheckUser user) {
		da.insert(user.getEmail(), user.getPassword(), user.getRoleId());
	}

}