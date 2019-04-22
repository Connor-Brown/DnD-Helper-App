package my.dnd.app.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.model.Item;
import my.dnd.app.service.TreasureGenerator;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/dnd/itemList")
public class ItemListController {
	
	/**
	 * 0 = name
	 * 1 = type
	 * 2 = weight
	 * 3 = rarity
	 */
	int sort = 0;
	
	@RequestMapping("")
	public String homeRedirect() {
		return "redirect:/dnd/itemList/";
	}
	
	@RequestMapping(value= {"/","/home"})
	public String home(HttpSession session) {
		if(session.getAttribute("itemList") == null) {
			session.setAttribute("itemList", TreasureGenerator.getInstance().getAllItems());
		}
		return "dnd/itemList";
	}
	
	@RequestMapping("/filterItems")
	public String filterItems(HttpSession session, @RequestParam(value="type", required=false) List<String> type, @RequestParam(value="minWeight", required=false) Integer minWeight, @RequestParam(value="maxWeight", required=false) Integer maxWeight, @RequestParam(value="rarity", required=false) List<String> rarity) {
		session.setAttribute("itemList", TreasureGenerator.getInstance().filterItems(type, rarity, minWeight, maxWeight));
		return "dnd/itemList";
	}
	
	@RequestMapping("/reset")
	public String reset(HttpSession session) {
		session.setAttribute("itemList", TreasureGenerator.getInstance().getAllItems());
		return "dnd/itemList";
	}
	
	@RequestMapping("/sortByName")
	public String sortByName(HttpSession session) {
		if(sort == 0)
			Collections.reverse((List<Item>) session.getAttribute("itemList"));
		else
			Collections.sort((List<Item>) session.getAttribute("itemList"), new Comparator<Item>() {
				@Override
				public int compare(Item o1, Item o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		sort = 0;
		return "dnd/itemList";
	}
	
	@RequestMapping("/sortByType")
	public String sortByType(HttpSession session) {
		if(sort == 1)
			Collections.reverse((List<Item>) session.getAttribute("itemList"));
		else
			Collections.sort((List<Item>) session.getAttribute("itemList"), new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					return first.getType().compareTo(second.getType());
				}
			});
		sort = 1;
		return "dnd/itemList";
	}
	
	@RequestMapping("/sortByWeight")
	public String sortByWeight(HttpSession session) {
		if(sort == 2)
			Collections.reverse((List<Item>) session.getAttribute("itemList"));
		else
			Collections.sort((List<Item>) session.getAttribute("itemList"), new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					if (first.getWeight() > second.getWeight())
						return -1;
					else if (first.getWeight() < second.getWeight())
						return 1;
					else
						return first.getName().compareTo(second.getName());
				}
			});
		sort = 2;
		return "dnd/itemList";
	}
	
	@RequestMapping("/sortByRarity")
	public String sortByRarity(HttpSession session) {
		if(sort == 3)
			Collections.reverse((List<Item>) session.getAttribute("itemList"));
		else
			Collections.sort((List<Item>) session.getAttribute("itemList"), new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					int f = toRarityInt(first);
					int s = toRarityInt(second);
					if ((f - s) == 0)
						return first.getName().compareTo(second.getName());
					return f - s;
				}

				private int toRarityInt(Item item) {
					String i = item.getRarity();
					if (i.equals("GOLD"))
						return 0;
					else if (i.equals("ARTIFACT"))
						return 1;
					else if (i.equals("LEGENDARY"))
						return 2;
					else if (i.equals("VERY RARE"))
						return 3;
					else if (i.equals("RARE"))
						return 4;
					else if (i.equals("UNCOMMON"))
						return 5;
					else if (i.equals("COMMON"))
						return 6;
					else
						return 7;
				}
			});
		sort = 3;
		return "dnd/itemList";
	}
	
	@RequestMapping("/itemInfo{index}")
	public String itemInfo(HttpSession session, Model model, @PathVariable("index") int index) {
		model.addAttribute("item", ((List<Item>) session.getAttribute("itemList")).get(index));
		return "dnd/itemInfo";
	}
	
}
