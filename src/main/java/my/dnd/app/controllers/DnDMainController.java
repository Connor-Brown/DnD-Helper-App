package my.dnd.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dnd")
public class DnDMainController {//implements ErrorController 
	
	@RequestMapping(value= {"/","","/home"})
	public String home() {
		return "dnd/home";
	}
	
//	@RequestMapping("/error")
//	public String errorRedirect() {
//		return "error";
//	}
//	
//	@Override
//	public String getErrorPath() {
//		return "/error";
//	}

}
