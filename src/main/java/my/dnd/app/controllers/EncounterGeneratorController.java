package my.dnd.app.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.Monster;
import my.dnd.app.service.EncounterGenerator;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/encounterGenerator")
public class EncounterGeneratorController {
	
	@RequestMapping("/home")
	public String home(HttpSession session) {
		session.setAttribute("monsterTypes", EncounterGenerator.getInstance().getMonsterTypes());
		return "encounterGeneratorHome";
	}
	
	@RequestMapping("/givenThematicEncounter")
	public String givenThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number, @RequestParam("type") String type) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateGivenThematicEncounterByLevel(level, number, type);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomSoloEncounter")
	public String randomSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomEasySoloEncounter")
	public String randomEasySoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomEasySoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomMediumSoloEncounter")
	public String randomMediumSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomMediumSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomHardSoloEncounter")
	public String randomHardSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomHardSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomDeadlySoloEncounter")
	public String randomDeadlySoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomDeadlySoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomThematicEncounter")
	public String randomThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomEasyThematicEncounter")
	public String randomEasyThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomEasyThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomMediumThematicEncounter")
	public String randomMediumThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomMediumThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomHardThematicEncounter")
	public String randomHardThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomHardThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomDeadlyThematicEncounter")
	public String randomDeadlyThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomDeadlyThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomEncounter")
	public String randomEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomEasyEncounter")
	public String randomEasyEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomEasyEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomMediumEncounter")
	public String randomMediumEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomMediumEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomHardEncounter")
	public String randomHardEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomHardEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/randomDeadlyEncounter")
	public String randomDeadlyEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<Monster> list = EncounterGenerator.getInstance().generateRandomDeadlyEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/sortByCR")
	public String sortByCR(HttpSession session) {
		List<Monster> list = (List<Monster>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/encounterGenerator/home";
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster first, Monster second) {
				if(first.getCr() < second.getCr())
					return 1;
				else if(first.getCr() > second.getCr())
					return -1;
				return 0;
			}
		});
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/sortByName")
	public String sortByName(HttpSession session) {
		List<Monster> list = (List<Monster>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/encounterGenerator/home";
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster first, Monster second) {
				return first.getName().compareTo(second.getName());
			}
		});
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/sortByType")
	public String sortByType(HttpSession session) {
		List<Monster> list = (List<Monster>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/encounterGenerator/home";
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster first, Monster second) {
				return first.getType().compareTo(second.getType());
			}
		});
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/sortByAC")
	public String sortByAC(HttpSession session) {
		List<Monster> list = (List<Monster>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/encounterGenerator/home";
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster first, Monster second) {
				return second.getAc() - first.getAc();
			}
		});
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	@RequestMapping("/sortByHP")
	public String sortByHP(HttpSession session) {
		List<Monster> list = (List<Monster>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/encounterGenerator/home";
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster first, Monster second) {
				return second.getActualHp() - first.getActualHp();
			}
		});
		session.setAttribute("monsterList", list);
		return "encounterList";
	}
	
	private void setHpOfMonsters(List<Monster> list) {
		Random r = new Random();
		for(Monster m : list) {
			int temp = 0;
			for(int i = 0; i < m.getNumDice(); i++) {
				temp += r.nextInt(m.getDiceSize())+1;
			}
			m.setActualHp(temp + m.getBonusHp());
		}
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				if(o1.getCr() < o2.getCr())
					return -1;
				else if(o1.getCr() > o2.getCr())
					return 1;
				else
					return o2.getName().compareTo(o1.getName());
			}
		});
		Collections.reverse(list);
	}
}
