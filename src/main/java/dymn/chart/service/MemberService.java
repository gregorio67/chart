package dymn.chart.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sun.istack.internal.NotNull;

import dymn.chart.vo.UserVo;


public class MemberService implements UserDetailsService{

	@Resource(name="userService")
	private UserService userService;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVo userVo = userService.getUserById(username);
		
		if (userVo == null) {
			throw new UsernameNotFoundException("Not Found user " + username);
		}
		
		Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		UserDetails user = new User(username, userVo.getPassword(), roles);
		
		return user;
	}

}
