package my.dnd.app.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.Monster;
import my.dnd.app.model.MonsterHolder;
import my.dnd.app.service.EncounterGenerator;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/dnd/encounterMaker")
public class EncounterMakerController {
	
	/**
	 * 0 = count
	 * 1 = name
	 * 2 = size
	 * 3 = type
	 * 4 = alignment
	 * 5 = ac
	 * 6 = hp
	 * 7 = cr
	 * 8 = legendary
	 */
	int selectedSort = -1;
	int currentSort = -1;
	
	@RequestMapping("")
	public String homeRedirect() {
		return "redirect:/dnd/encounterMaker/";
	}
	
	@RequestMapping(value= {"/","/home"})
	public String home(HttpSession session) {
		if(session.getAttribute("monsterList") == null) {
			List<MonsterHolder> list = new ArrayList<>();
			for(MonsterHolder m : EncounterGenerator.getInstance().getMonsterHolderListSortedByName(EncounterGenerator.getInstance().getMonsterHolderList())) {
				list.add(m);
			}
			session.setAttribute("monsterList", list);
		}
		if(session.getAttribute("currentList") == null)
			session.setAttribute("currentList", EncounterGenerator.getInstance().monsterHolderListToMonsterList((List<MonsterHolder>) session.getAttribute("monsterList")));
		if(session.getAttribute("selectedList") == null)
			session.setAttribute("selectedList", new ArrayList<MonsterHolder>());
		if(session.getAttribute("expTotal") == null)
			session.setAttribute("expTotal", 0);
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/selectedMonsterInfo{index}")
	public String selectedMonsterInfo(HttpSession session, Model model, @PathVariable("index") int index) {
		model.addAttribute("monster", ((List<MonsterHolder>) session.getAttribute("selectedList")).get(index).getMonster());
		return "dnd/monsterInfo";
	}
	
	@RequestMapping("/currentMonsterInfo{index}")
	public String currentMonsterInfo(HttpSession session, Model model, @PathVariable("index") int index) {
		model.addAttribute("monster", ((List<Monster>) session.getAttribute("currentList")).get(index));
		return "dnd/monsterInfo";
	}
	
	@RequestMapping("/filterMonsters")
	public String filterMonsters(HttpSession session, @RequestParam(value="minSize", required=false) String minSize, @RequestParam(value="maxSize", required=false) String maxSize, @RequestParam(value="minAC", required=false) Integer minAC, @RequestParam(value="maxAC", required=false) Integer maxAC, @RequestParam(value="minHP", required=false) Integer minHP, @RequestParam(value="maxHP", required=false) Integer maxHP, @RequestParam(value="minCR", required=false) Double minCR, @RequestParam(value="maxCR", required=false) Double maxCR, @RequestParam(value="isLegendary", required=false) String isLegendary) {
		List<Monster> list = EncounterGenerator.getInstance().getFilteredMonsterHolderList(minSize, maxSize, minAC, maxAC, minHP, maxHP, minCR, maxCR, isLegendary);
		session.setAttribute("currentList", list);
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/reset")
	public String reset(HttpSession session) {
		session.setAttribute("currentList", EncounterGenerator.getInstance().monsterHolderListToMonsterList((List<MonsterHolder>) session.getAttribute("monsterList")));
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/addCurrentList{index}")
	public String addMonsterToSelectedList(HttpSession session, @PathVariable("index") int index) {
		Monster m = ((List<Monster>) session.getAttribute("currentList")).get(index);
		boolean check = false;
		for(int i = 0; i < ((List<MonsterHolder>) session.getAttribute("selectedList")).size(); i++) {
			if(((List<MonsterHolder>) session.getAttribute("selectedList")).get(i).getMonster().equals(m)) {
				check = true;
				((List<MonsterHolder>) session.getAttribute("selectedList")).get(i).increaseCount();
				break;
			}
		}
		if(!check) {
			((List<MonsterHolder>) session.getAttribute("selectedList")).add(new MonsterHolder(m, 1));
		}
		session.setAttribute("expTotal", totalCR((List<MonsterHolder>) session.getAttribute("selectedList")));
		return "dnd/encounterMaker";
	}

	@RequestMapping("/removeSelectedList{index}")
	public String removeMonsterFromSelectedList(HttpSession session, @PathVariable("index") int index) {
		((List<MonsterHolder>) session.getAttribute("selectedList")).remove(index);
		session.setAttribute("expTotal", totalCR((List<MonsterHolder>) session.getAttribute("selectedList")));
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/clearSelectedList")
	public String clearSelectedList(HttpSession session) {
		session.setAttribute("selectedList", new ArrayList<MonsterHolder>());
		session.setAttribute("expTotal", 0);
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByCount")
	public String sortSelectedByCount(HttpSession session) {
		if(selectedSort == 0)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByCount((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 0;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByName")
	public String sortSelectedByName(HttpSession session) {
		if(selectedSort == 1)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByName((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 1;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedBySize")
	public String sortSelectedBySize(HttpSession session) {
		if(selectedSort == 2)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedBySize((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 2;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByType")
	public String sortSelectedByType(HttpSession session) {
		if(selectedSort == 3)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByType((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 3;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByAlignment")
	public String sortSelectedByAlignment(HttpSession session) {
		if(selectedSort == 4)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByAlignment((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 4;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByAC")
	public String sortSelectedByAC(HttpSession session) {
		if(selectedSort == 5)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByAC((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 5;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByHP")
	public String sortSelectedByHP(HttpSession session) {
		if(selectedSort == 6)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByHp((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 6;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByCR")
	public String sortSelectedByCR(HttpSession session) {
		if(selectedSort == 7)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByCr((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 7;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortSelectedByLegendary")
	public String sortSelectedByLegendary(HttpSession session) {
		if(selectedSort == 8)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("selectedList"));
		else
			EncounterGenerator.getInstance().getMonsterHolderListSortedByLegendaryStatus((List<MonsterHolder>) session.getAttribute("selectedList"));
		selectedSort = 8;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByName")
	public String sortCurrentByName(HttpSession session) {
		if(currentSort == 0)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByName((List<Monster>) session.getAttribute("currentList"));
		currentSort = 0;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentBySize")
	public String sortCurrentBySize(HttpSession session) {
		if(currentSort == 1)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedBySize((List<Monster>) session.getAttribute("currentList"));
		currentSort = 1;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByType")
	public String sortCurrentByType(HttpSession session) {
		if(currentSort == 2)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByType((List<Monster>) session.getAttribute("currentList"));
		currentSort = 2;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByAlignment")
	public String sortCurrentByAlignment(HttpSession session) {
		if(currentSort == 3)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByAlignment((List<Monster>) session.getAttribute("currentList"));
		currentSort = 3;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByAC")
	public String sortCurrentByAC(HttpSession session) {
		if(currentSort == 4)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByAC((List<Monster>) session.getAttribute("currentList"));
		currentSort = 4;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByHP")
	public String sortCrrentByHP(HttpSession session) {
		if(currentSort == 5)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByHp((List<Monster>) session.getAttribute("currentList"));
		currentSort = 5;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByCR")
	public String sortCurrentByCR(HttpSession session) {
		if(currentSort == 6)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByCr((List<Monster>) session.getAttribute("currentList"));
		currentSort = 6;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/sortCurrentByLegendary")
	public String sortCurrentByLegendary(HttpSession session) {
		if(currentSort == 7)
			Collections.reverse((List<MonsterHolder>) session.getAttribute("currentList"));
		else
			EncounterGenerator.getInstance().getMonsterListSortedByLegendaryStatus((List<Monster>) session.getAttribute("currentList"));
		currentSort = 7;
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/increaseCount")
	public String increaseCount(HttpSession session, @RequestParam("index") int index) {
		((List<MonsterHolder>) session.getAttribute("selectedList")).get(index).increaseCount();
		session.setAttribute("expTotal", totalCR((List<MonsterHolder>) session.getAttribute("selectedList")));
		return "dnd/encounterMaker";
	}
	
	@RequestMapping("/decreaseCount")
	public String decreaseCount(HttpSession session, @RequestParam("index") int index) {
		((List<MonsterHolder>) session.getAttribute("selectedList")).get(index).decreaseCount();
		if(((List<MonsterHolder>) session.getAttribute("selectedList")).get(index).getCount() == 0)
			((List<MonsterHolder>) session.getAttribute("selectedList")).remove(index);
		session.setAttribute("expTotal", totalCR((List<MonsterHolder>) session.getAttribute("selectedList")));
		return "dnd/encounterMaker";
	}

	private double totalCR(List<MonsterHolder> list) {
		int expSum = 0;
		for(MonsterHolder m : list) {
			expSum += EncounterGenerator.crToXP(m.getMonster().getCr()) * m.getCount();
		}
		return expSum;
	}
	
}
