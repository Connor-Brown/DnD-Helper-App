package my.dnd.app.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.MonsterHolder;
import my.dnd.app.service.EncounterGenerator;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/dnd/encounterGenerator")
public class EncounterGeneratorController {
	
	int sorter = -1;
	
	@RequestMapping("/home")
	public String home(HttpSession session) {
		session.setAttribute("monsterTypes", EncounterGenerator.getInstance().getMonsterTypes());
		return "dnd/encounterGeneratorHome";
	}
	
	@RequestMapping("monsterInfo{index}")
	public String monsterInfo(HttpSession session, Model model, @PathVariable("index") int index) {
		model.addAttribute("monster", ((List<MonsterHolder>) session.getAttribute("monsterList")).get(index).getMonster());
		return "dnd/monsterInfo";
	}
	
	@RequestMapping("/givenThematicEncounter")
	public String givenThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number, @RequestParam("type") String type) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateGivenThematicEncounterByLevel(level, number, type);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}

	@RequestMapping("/randomSoloEncounter")
	public String randomSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomEasySoloEncounter")
	public String randomEasySoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomEasySoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomMediumSoloEncounter")
	public String randomMediumSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomMediumSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomHardSoloEncounter")
	public String randomHardSoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomHardSoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomDeadlySoloEncounter")
	public String randomDeadlySoloEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomDeadlySoloEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomThematicEncounter")
	public String randomThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomEasyThematicEncounter")
	public String randomEasyThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomEasyThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomMediumThematicEncounter")
	public String randomMediumThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomMediumThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomHardThematicEncounter")
	public String randomHardThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomHardThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomDeadlyThematicEncounter")
	public String randomDeadlyThematicEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomDeadlyThematicEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomEncounter")
	public String randomEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomEasyEncounter")
	public String randomEasyEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomEasyEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomMediumEncounter")
	public String randomMediumEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomMediumEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomHardEncounter")
	public String randomHardEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomHardEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/randomDeadlyEncounter")
	public String randomDeadlyEncounter(HttpSession session, @RequestParam("level") int level, @RequestParam("number") int number) {
		session.setAttribute("level", level);
		session.setAttribute("number", number);
		List<MonsterHolder> list = EncounterGenerator.getInstance().generateRandomDeadlyEncounterByLevel(level, number);
		setHpOfMonsters(list);
		session.setAttribute("monsterList", list);
		session.setAttribute("xp", monsterListToCr(list));
		return "dnd/encounterList";
	}
	
	@RequestMapping("/sortByCR")
	public String sortByCR(HttpSession session) {
		List<MonsterHolder> list = (List<MonsterHolder>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/dnd/encounterGenerator/home";
		}
		if(sorter == 0)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<MonsterHolder>() {
				@Override
				public int compare(MonsterHolder first, MonsterHolder second) {
					if(first.getMonster().getCr() < second.getMonster().getCr())
						return 1;
					else if(first.getMonster().getCr() > second.getMonster().getCr())
						return -1;
					return 0;
				}
			});
		}
		sorter = 0;
		session.setAttribute("monsterList", list);
		return "dnd/encounterList";
	}
	
	@RequestMapping("/sortByName")
	public String sortByName(HttpSession session) {
		List<MonsterHolder> list = (List<MonsterHolder>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/dnd/encounterGenerator/home";
		}
		if(sorter == 1)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<MonsterHolder>() {
				@Override
				public int compare(MonsterHolder first, MonsterHolder second) {
					return first.getMonster().getName().compareTo(second.getMonster().getName());
				}
			});
		}
		sorter = 1;
		session.setAttribute("monsterList", list);
		return "dnd/encounterList";
	}
	
	@RequestMapping("/sortByType")
	public String sortByType(HttpSession session) {
		List<MonsterHolder> list = (List<MonsterHolder>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/dnd/encounterGenerator/home";
		}
		if(sorter == 2)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<MonsterHolder>() {
				@Override
				public int compare(MonsterHolder first, MonsterHolder second) {
					return first.getMonster().getType().compareTo(second.getMonster().getType());
				}
			});
		}
		sorter = 2;
		session.setAttribute("monsterList", list);
		return "dnd/encounterList";
	}
	
	@RequestMapping("/sortByAC")
	public String sortByAC(HttpSession session) {
		List<MonsterHolder> list = (List<MonsterHolder>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/dnd/encounterGenerator/home";
		}
		if(sorter == 3)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<MonsterHolder>() {
				@Override
				public int compare(MonsterHolder first, MonsterHolder second) {
					return second.getMonster().getAc() - first.getMonster().getAc();
				}
			});
		}
		sorter = 3;
		session.setAttribute("monsterList", list);
		return "dnd/encounterList";
	}
	
	@RequestMapping("/sortByHP")
	public String sortByHP(HttpSession session) {
		List<MonsterHolder> list = (List<MonsterHolder>) session.getAttribute("monsterList");
		if(list == null) {
			return "redirect:/dnd/encounterGenerator/home";
		}
		if(sorter == 4)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<MonsterHolder>() {
				@Override
				public int compare(MonsterHolder first, MonsterHolder second) {
					return second.getMonster().getActualHp() - first.getMonster().getActualHp();
				}
			});
		}
		sorter = 4;
		session.setAttribute("monsterList", list);
		return "dnd/encounterList";
	}
	
	private void setHpOfMonsters(List<MonsterHolder> list) {
		Random r = new Random();
		for(MonsterHolder m : list) {
			int temp = 0;
			for(int i = 0; i < m.getMonster().getNumDice(); i++) {
				temp += r.nextInt(m.getMonster().getDiceSize())+1;
			}
			m.getMonster().setActualHp(temp + m.getMonster().getBonusHp());
		}
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				if(o1.getMonster().getCr() < o2.getMonster().getCr())
					return -1;
				else if(o1.getMonster().getCr() > o2.getMonster().getCr())
					return 1;
				else
					return o2.getMonster().getName().compareTo(o1.getMonster().getName());
			}
		});
		sorter = 0;
		Collections.reverse(list);
	}
	
	private int monsterListToCr(List<MonsterHolder> list) {
		int sum = 0;
		for(MonsterHolder m : list) {
			sum += EncounterGenerator.crToXP(m.getMonster().getCr());
		}
		return sum;
	}
}
