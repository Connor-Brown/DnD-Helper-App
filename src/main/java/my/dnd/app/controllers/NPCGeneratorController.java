package my.dnd.app.controllers;

import java.util.Random;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.NPC;
import my.dnd.app.service.NPCGenerator;

@Controller
@RequestMapping("/npcGenerator")
public class NPCGeneratorController {
	
	Random r = new Random();
	
	@RequestMapping("/home")
	public String home() {
		return "npcGeneratorHome";
	}
	
	@RequestMapping("/generateNpc")
	public String generateNpc(Model model, @RequestParam("classname") String classname, @RequestParam("race") String race, @RequestParam("level") String level) {
		int a;
		if (level.equals("Surprise me")) {
			a = r.nextInt(20) + 1;
		} else {
			a = Integer.parseInt(level);
		}
		if(classname.equals("Surprise me")) {
			String[] s = {"BARBARIAN", "BARD", "CLERIC", "DRUID", "FIGHTER", "MONK", "PALADIN", "RANGER", "ROGUE", "SORCERER", "WARLOCK", "WIZARD"};
			classname = s[r.nextInt(s.length)];
		}
		if(race.equals("Surprise me")) {
			String[] s = {"AARAKOCRA", "AASIMAR", "CHANGELING", "DRAGONBORN", "DWARF", "ELF", "FIRBOLG", "GENASI", "GNOME", "GOLIATH", "HALF-ELF",
					"HALF-ORC", "HUMAN", "LIZARDFOLK", "LOXODON", "ORC", "SHIFTER", "TABAXI", "TIEFLING", "TORTLE", "TRITON", "WARFORGED"};
			race = s[r.nextInt(s.length)];
		}
		NPC npc = NPCGenerator.getInstance().generateNPC(classname, race, a);
		npc.setClassname(formatString(npc.getClassname()));
		npc.setRace(formatString(npc.getRace()));
		for(int i = 0; i < npc.getFeats().size(); i++) {
			npc.getFeats().set(i, formatString(npc.getFeats().get(i)));
		}
		model.addAttribute("npc", npc);
		return "npcDetails";
	}
	
	private String formatString(String string) {
		Scanner scan = new Scanner(string);
		String returner = "";
		while(scan.hasNext()) {
			String s = scan.next();
			s = s.toLowerCase();
			s = Character.toUpperCase(s.charAt(0)) + s.substring(1, s.length());
			returner += s + " ";
		}
		return returner;
	}
	
}
