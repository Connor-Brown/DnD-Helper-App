package my.dnd.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.service.NPCGenerator;

@Controller
@RequestMapping("/dnd/npcGenerator")
public class NPCGeneratorController {
		
	@RequestMapping("/home")
	public String home() {
		return "dnd/npcGeneratorHome";
	}
	
	@RequestMapping("/generateNpc")
	public String generateNpc(Model model, @RequestParam("classname") String classname, @RequestParam("race") String race, @RequestParam("level") String level) {
		model.addAttribute("npc", NPCGenerator.getInstance().generateNPC(classname, race, level));
		return "dnd/npcDetails";
	}
	
}
