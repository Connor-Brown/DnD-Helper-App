package my.dnd.app.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.Spell;
import my.dnd.app.service.SpellService;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/dnd/spellService")
public class SpellController {
	
	int sorter = -1;
	
	@RequestMapping("/home")
	public String home(HttpSession session) {
		session.setAttribute("schools", SpellService.getInstance().getSchools());
		session.setAttribute("classes", SpellService.getInstance().getClasses());
		session.setAttribute("spellList", SpellService.getInstance().getSpellList());
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/filterSearch")
	public String filterSearch(HttpSession session, @RequestParam(value="level", required=false) List<String> levels, @RequestParam(value="school", required=false) List<String> schools, @RequestParam(value="c", required=false) List<String> classes) {
		List<Integer> newLevels = levelsToIntList(levels);
		List<Spell> list = SpellService.getInstance().getFilteredSpellList(newLevels, schools, classes);
		session.setAttribute("spellList", list);
		session.setAttribute("schools", SpellService.getInstance().getSchools());
		session.setAttribute("classes", SpellService.getInstance().getClasses());
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/clearFilter")
	public String clearFilter(HttpSession session) {
		session.setAttribute("spellList", SpellService.getInstance().getSpellList());
		session.setAttribute("schools", SpellService.getInstance().getSchools());
		session.setAttribute("classes", SpellService.getInstance().getClasses());
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/sortByName")
	public String sortByName(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 0)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					return first.getName().compareTo(second.getName());
				}
			});
		}
		sorter = 0;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/sortByLevel")
	public String sortByLevel(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 1)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					if((first.getLevel() - second.getLevel()) == 0)
						return first.getName().compareTo(second.getName());
					return first.getLevel() - second.getLevel();
				}
			});
		}
		sorter = 1;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/sortBySchool")
	public String sortBySchool(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 2)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					return first.getSchool().compareTo(second.getSchool());
				}
			});
		}
		sorter = 2;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}
	@RequestMapping("/sortByDuration")
	public String sortByDuration(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 3)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					int f = toDurationInt(first);
					int s = toDurationInt(second);
					if((f - s) == 0)
						return first.getName().compareTo(second.getName());
					return f - s;
				}
				private int toDurationInt(Spell spell) {
					String d = spell.getDuration();
					if(d.contains("Instantaneous"))
						return 0;
					else if(d.contains("1 round"))
						return 1;
					else if(d.contains("1 minute") || d.contains("6 round"))
						return 2;
					else if(d.contains("10 minute"))
						return 3;
					else if(d.contains("1 hour"))
						return 4;
					else if(d.contains("2 hour"))
						return 5;
					else if(d.contains("8 hour"))
						return 6;
					else if(d.contains("24 hour") || d.contains("1 day"))
						return 7;
					else if(d.contains("7 day"))
						return 8;
					else if(d.contains("10 day"))
						return 9;
					else if(d.contains("30 day"))
						return 10;
					else if(d.contains("Until dispelled"))
						return 11;
					else
						return 12;
				}
			});
		}
		sorter = 3;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/sortByRange")
	public String sortByRange(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 4)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					int f = toRangeInt(first);
					int s = toRangeInt(second);
					if((f - s) == 0)
						return first.getName().compareTo(second.getName());
					return f - s;
				}
				private int toRangeInt(Spell spell) {
					String r = spell.getRange();
					if(r.equals("Self"))
						return 0;
					else if(r.contains("Touch"))
						return 1;
					else if(r.contains("5 feet") || r.contains("5-foot"))
						return 2;
					else if(r.contains("10 feet") || r.contains("10-foot"))
						return 3;
					else if(r.contains("15 feet") || r.contains("15-foot"))
						return 4;
					else if(r.contains("30 feet") || r.contains("30-foot"))
						return 5;
					else if(r.contains("60 feet") || r.contains("60-foot") || r.contains("60 foot"))
						return 6;
					else if(r.contains("90"))
						return 7;
					else if(r.contains("100"))
						return 8;
					else if(r.contains("120"))
						return 9;
					else if(r.contains("150"))
						return 10;
					else if(r.contains("300"))
						return 11;
					else if(r.contains("500 feet"))
						return 12;
					else if(r.contains("1 mile"))
						return 13;
					else if(r.contains("5-mile"))
						return 14;
					else if(r.contains("500"))
						return 15;
					else if(r.contains("Sight"))
						return 16;
					else if(r.contains("Unlimited"))
						return 17;
					else if(r.contains("Special"))
						return 18;
					else
						return 19;
				}
			});
		}
		sorter = 4;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}

	@RequestMapping("/sortByCastingTime")
	public String sortByCastingTime(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 5)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					int f = toCastingTimeInt(first);
					int s = toCastingTimeInt(second);
					if((f - s) == 0)
						return first.getName().compareTo(second.getName());
					return f - s;
				}
				private int toCastingTimeInt(Spell spell) {
					String c = spell.getCastingTime();
					if(c.contains("reaction"))
						return 0;
					else if(c.contains("bonus"))
						return 1;
					else if(c.contains("action"))
						return 2;
					else if(c.contains("1 minute"))
						return 3;
					else if(c.contains("10"))
						return 4;
					else if(c.contains("1 hour"))
						return 5;
					else if(c.contains("8 hour"))
						return 6;
					else if(c.contains("12 hour"))
						return 7;
					else if(c.contains("24"))
						return 8;
					else
						return 9;
				}
			});
		}
		sorter = 5;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}
	
	@RequestMapping("/sortByClasses")
	public String sortByClasses(HttpSession session) {
		List<Spell> list = (List<Spell>) session.getAttribute("spellList");
		if(list == null) {
			list = SpellService.getInstance().getSpellList();
			session.setAttribute("schools", SpellService.getInstance().getSchools());
			session.setAttribute("classes", SpellService.getInstance().getClasses());
		}
		if(sorter == 6)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Spell>() {
				@Override
				public int compare(Spell first, Spell second) {
					int f = toClassInt(first);
					int s = toClassInt(second);
					if((f - s) == 0)
						return first.getName().compareTo(second.getName());
					return f - s;
				}
				private int toClassInt(Spell first) {
					List<String> list = first.getClasses();
					if(list.contains("Artificer"))
						return 0;
					else if(list.contains("Bard"))
						return 1;
					else if(list.contains("Cleric"))
						return 2;
					else if(list.contains("Druid"))
						return 3;
					else if(list.contains("Fighter"))
						return 4;
					else if(list.contains("Paladin"))
						return 5;
					else if(list.contains("Ranger"))
						return 6;
					else if(list.contains("Rogue"))
						return 7;
					else if(list.contains("Sorcerer"))
						return 8;
					else if(list.contains("Warlock"))
						return 9;
					else if(list.contains("Wizard"))
						return 10;
					else
						return 11;
				}
			});
		}
		sorter = 6;
		session.setAttribute("spellList", list);
		return "dnd/spellServiceHome";
	}

	private List<Integer> levelsToIntList(List<String> levels) {
		if(levels == null)
			return null;
		List<Integer> list = new ArrayList<>();
		for(String s : levels) {
			if(s.equals("Cantrip"))
				list.add(0);
			else
				list.add(Integer.parseInt(s));
		}
		return list;
	}

}
