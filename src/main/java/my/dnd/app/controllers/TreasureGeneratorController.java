package my.dnd.app.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Item;
import my.dnd.app.service.TreasureGenerator;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/dnd/treasureGenerator")
public class TreasureGeneratorController {

	Random r = new Random();
	int sorter = -1;

	@RequestMapping("/home")
	public String home() {
		return "dnd/treasureGeneratorHome";
	}
	
	@RequestMapping("/legendary")
	public String all(HttpSession session) {
		List<Item> itemList = new ArrayList<>();
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM dnddb.items WHERE rarity=\'ARTIFACT\' OR rarity=\'LEGENDARY\'")
					.executeQuery();
			while(rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(true);
				i.setName(rs.getString("name"));
				i.setRarity(rs.getString("rarity"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				i.setWeight(rs.getDouble("weight"));
				itemList.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("treasure", itemList);
		return sortByRarity(session);
	}

	@RequestMapping("/sortByName")
	public String sortByName(HttpSession session) {
		List<Item> list = (List<Item>) session.getAttribute("treasure");
		if (list == null) {
			return "redirect:/dnd/treasureGenerator/home";
		}
		if(sorter == 0)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					return first.getName().compareTo(second.getName());
				}
			});
		}
		sorter = 0;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/sortByAmount")
	public String sortByAmount(HttpSession session) {
		List<Item> list = (List<Item>) session.getAttribute("treasure");
		if (list == null) {
			return "redirect:/dnd/treasureGenerator/home";
		}
		if(sorter == 1)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					if ((first.getAmount() - second.getAmount()) == 0)
						return first.getName().compareTo(second.getName());
					return second.getAmount() - first.getAmount();
				}
			});
		}
		sorter = 1;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/sortByType")
	public String sortByType(HttpSession session) {
		List<Item> list = (List<Item>) session.getAttribute("treasure");
		if (list == null) {
			return "redirect:/dnd/treasureGenerator/home";
		}
		if(sorter == 2)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Item>() {
				@Override
				public int compare(Item first, Item second) {
					return first.getType().compareTo(second.getType());
				}
			});
		}
		sorter = 2;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/sortByWeight")
	public String sortByWeight(HttpSession session) {
		List<Item> list = (List<Item>) session.getAttribute("treasure");
		if (list == null) {
			return "redirect:/dnd/treasureGenerator/home";
		}
		if(sorter == 3)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Item>() {
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
		}
		sorter = 3;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/sortByRarity")
	public String sortByRarity(HttpSession session) {
		List<Item> list = (List<Item>) session.getAttribute("treasure");
		if (list == null) {
			return "redirect:/dnd/treasureGenerator/home";
		}
		if(sorter == 4)
			Collections.reverse(list);
		else {
			Collections.sort(list, new Comparator<Item>() {
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
		}
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomEncounterTreasure")
	public String randomMonsterTreasure(HttpSession session, @RequestParam("level") String inputedLevel) {
		int level = changeInputedLevelToLevel(inputedLevel);
		List<Item> treasureList = new ArrayList<>();
		int g = getRandomIndividualGold(level);
		if (g > 0) {
			Item gold = new Item();
			gold.setName("Gold");
			gold.setMagic(false);
			gold.setRarity("GOLD");
			gold.setWeight(0);
			gold.setType("$");
			gold.setText("The standard unit of currency");
			gold.setAmount(g);
			treasureList.add(gold);
		}
		session.setAttribute("treasure", treasureList);
		return "dnd/treasureList";
	}

	private Integer getRandomIndividualGold(int level) {
		int roll = r.nextInt(100) + 1;
		if (level > 16) {
			if (roll >= 56)
				return (r.nextInt(11) + 2) * 1000 + (r.nextInt(6) + 1) * 1000;
			else if (roll >= 16)
				return (r.nextInt(6) + 1) * 1000 + (r.nextInt(6) + 1) * 1000;
			else
				return (r.nextInt(41) + 8) * 100 + (r.nextInt(11) + 2) * 500;
		} else if (level > 10) {
			if (roll >= 76)
				return (r.nextInt(11) + 2) * 100 + (r.nextInt(11) + 2) * 100;
			else if (roll >= 36)
				return (r.nextInt(6) + 1) * 100 + (r.nextInt(11) + 2) * 100;
			else if (roll >= 21)
				return (r.nextInt(6) + 1) * 100 + (r.nextInt(6) + 1) * 50;
			else
				return (r.nextInt(6) + 1) * 100 + (r.nextInt(21) + 4) * 10;
		} else if (level > 4) {
			if (roll >= 96)
				return (r.nextInt(16) + 3) * 10 + (r.nextInt(11) + 2) * 10;
			else if (roll >= 71)
				return (r.nextInt(21) + 4) * 10;
			else if (roll >= 61)
				return (r.nextInt(11) + 2) * 10 + (r.nextInt(16) + 3) * 5;
			else if (roll >= 31)
				return (r.nextInt(11) + 2) * 10 + (r.nextInt(31) + 6);
			else
				return (r.nextInt(6) + 1) * 5 + (r.nextInt(21) + 4);
		} else {
			if (roll >= 96)
				return (r.nextInt(6) + 1) * 10;
			else if (roll >= 71)
				return r.nextInt(16) + 3;
			else if (roll >= 61)
				return r.nextInt(8) + 1;
			else if (roll > 30)
				return r.nextInt(3);
			else
				return 0;
		}
	}

	@RequestMapping("/randomTreasureHoard")
	public String randomTreasureHoard(HttpSession session, @RequestParam("level") String inputedLevel) {
		int level = changeInputedLevelToLevel(inputedLevel);
		List<Item> treasureList = TreasureGenerator.getInstance().generateTreasureHoard(level);
		sorter = 4;
		session.setAttribute("treasure", treasureList);
		return "dnd/treasureList";
	}

	private int changeInputedLevelToLevel(String inputedLevel) {
		switch (inputedLevel) {
		case "1 - 4":
			return 1;
		case "5 - 10":
			return 5;
		case "11 - 16":
			return 11;
		case "17 - 20":
			return 20;
		}
		return 1;
	}

	@RequestMapping("/randomMagicItems")
	public String randomMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItems(a);
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomCommonMagicItems")
	public String randomCommonMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "COMMON");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomUncommonMagicItems")
	public String randomUnCommonMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "UNCOMMON");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomRareMagicItems")
	public String randomRareMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			Random r = new Random();
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "RARE");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomVeryRareMagicItems")
	public String randomVeryRareMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "VERY RARE");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomLegendaryMagicItems")
	public String randomLegendaryMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "LEGENDARY");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomArtifactMagicItems")
	public String randomArtifactMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMagicItemsBySpecificRarity(a, "ARTIFACT");
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomMundaneItems")
	public String randomMundaneMagicItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomMundaneItems(a);
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

	@RequestMapping("/randomValuableItems")
	public String randomValuableItems(HttpSession session, @RequestParam("number") String amount) {
		int a;
		if (amount.equals("Surprise me")) {
			a = r.nextInt(10) + 1;
		} else {
			a = Integer.parseInt(amount);
		}
		List<Item> list = TreasureGenerator.getInstance().getXRandomValuableItems(a);
		Collections.sort(list, new Comparator<Item>() {
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
		sorter = 4;
		session.setAttribute("treasure", list);
		return "dnd/treasureList";
	}

}
