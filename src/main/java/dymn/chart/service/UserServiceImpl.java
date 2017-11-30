package dymn.chart.service;

import org.springframework.stereotype.Service;

import com.sun.istack.internal.NotNull;

import dymn.chart.vo.UserVo;

@Service("userService")
public class UserServiceImpl implements UserService {

	public UserVo getUserById(@NotNull String username) {
		
		UserVo userVo = new UserVo();
		userVo.setPassword("kyobo11!");
		userVo.setUsername("kkimody");
		return userVo;
	}

}
