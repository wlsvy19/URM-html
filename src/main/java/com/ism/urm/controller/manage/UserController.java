package com.ism.urm.controller.manage;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.manage.UserService;
import com.ism.urm.vo.manage.User;

@RestController
public class UserController {

	UserService service = new UserService();
   
	@GetMapping("/user")
	public List<User> search(@RequestParam Map<String, String> params) {
		System.out.println("************************UserController : search()************************");
		List<User> rtn = null;
		try {
			rtn = service.search(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}



}
