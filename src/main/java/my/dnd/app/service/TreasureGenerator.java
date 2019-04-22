package my.dnd.app.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Item;

public class TreasureGenerator {

	private static List<Item> magicItemList;
	private static List<Item> mundaneItemList;
	private static List<Item> valuableItemList;
	private static List<Item> allItems;
	private static Random r;

	private static TreasureGenerator instance;

	public static TreasureGenerator getInstance() {
		if (instance == null) {
			r = new Random();
			instance = new TreasureGenerator();
		}
		return instance;
	}

	private TreasureGenerator() {
	}
	
	public Item findItemByName(String name) {
		for(Item i : getAllItems()) {
			if(i.getName().equalsIgnoreCase(name))
				return i;
		}
		return null;
	}

	public List<Item> getAllItems() {
		allItems = getAllMundaneItems();
		allItems.addAll(getAllMagicItems());
		allItems.addAll(getAllValuableItems());
		allItems.sort(new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return allItems;
	}

	public List<Item> filterItems(List<String> type, List<String> rarity, Integer minWeight, Integer maxWeight) {
		if(minWeight == null)
			minWeight = 0;
		if(maxWeight == null)
			maxWeight = Integer.MAX_VALUE;
		List<Item> returner = new ArrayList<>();
		for(Item i : allItems) {
			if((type == null || type.contains(i.getType())) && (rarity == null || rarity.contains(i.getRarity()))
				&& i.getWeight() >= minWeight && i.getWeight() <= maxWeight)
				returner.add(i);
		}
		return returner;
	}

	public List<Item> getAllMagicItems() {
		List<Item> returner = new ArrayList<>();
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items where rarity!='COMMON';").executeQuery();
			while (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(true);
				i.setRarity(rs.getString("rarity"));
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				i.setWeight(rs.getDouble("weight"));
				returner.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returner;
	}

	public List<Item> getAllMundaneItems() {
		List<Item> returner = new ArrayList<>();
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items where rarity='COMMON';").executeQuery();
			while (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(false);
				i.setRarity(rs.getString("rarity"));
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				i.setWeight(rs.getDouble("weight"));
				returner.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returner;
	}

	public List<Item> getAllValuableItems() {
		List<Item> returner = new ArrayList<>();
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM artobjects;")
					.executeQuery();
			while (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(false);
				i.setRarity("COMMON");
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				returner.add(i);
			}
			rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM gems;").executeQuery();
			while (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(false);
				i.setRarity("COMMON");
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				returner.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returner;
	}

	public List<Item> getXRandomValuableItems(int amount) {
		List<Item> returner = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			Item item = getValuableItem();
			int index = searchItemListByItemName(returner, item.getName());
			if (index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount() + 1);
			else
				returner.add(item);
		}
		return returner;
	}

	private int searchItemListByItemName(List<Item> treasureList, String name) {
		for (Item i : treasureList) {
			if (i.getName().equals(name))
				return treasureList.indexOf(i);
		}
		return -1;
	}

	public Item getValuableItem() {
		ResultSet rs = null;
		try {
			if (r.nextBoolean()) {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM gems ORDER BY RAND() LIMIT 1;").executeQuery();
			} else {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM artobjects ORDER BY RAND() LIMIT 1;").executeQuery();
			}
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(false);
				i.setName(rs.getString("name"));
				i.setRarity("COMMON");
				i.setType("$");
				i.setText(rs.getString("text"));
				i.setWeight(0);
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Item> getXRandomMundaneItems(int amount) {
		List<Item> returner = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			Item item = getMundaneItem();
			int index = searchItemListByItemName(returner, item.getName());
			if (index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount() + 1);
			else
				returner.add(item);
		}
		return returner;
	}

	public Item getMundaneItem() {
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items where rarity='COMMON' ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(false);
				i.setRarity("COMMON");
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				i.setWeight(rs.getDouble("weight"));
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Item getMagicItem() {
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items where rarity <> 'COMMON' ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				i.setMagic(true);
				i.setRarity(rs.getString("rarity"));
				i.setName(rs.getString("name"));
				i.setText(rs.getString("text"));
				i.setType(rs.getString("type"));
				i.setWeight(rs.getDouble("weight"));
				return i;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Item> getXRandomMagicItems(int amount) {
		List<Item> returner = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			Item item = getMagicItem();
			int index = searchItemListByItemName(returner, item.getName());
			if (index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount() + 1);
			else
				returner.add(item);
		}
		return returner;
	}

	public List<Item> getXRandomMagicItemsBySpecificRarity(int amount, String rarity) {
		List<Item> returner = new ArrayList<>();
		try {
			for (int j = 0; j < amount; j++) {
				ResultSet rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM items WHERE rarity='" + rarity + "' ORDER BY RAND() LIMIT 1")
						.executeQuery();
				while (rs.next()) {
					Item i = new Item();
					i.setAmount(1);
					i.setMagic(true);
					i.setRarity(rs.getString("rarity"));
					i.setName(rs.getString("name"));
					i.setText(rs.getString("text"));
					i.setType(rs.getString("type"));
					i.setWeight(rs.getDouble("weight"));
					int index = searchItemListByItemName(returner, i.getName());
					if (index >= 0)
						returner.get(index).setAmount(returner.get(index).getAmount() + 1);
					else
						returner.add(i);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returner;
	}
	
	public List<Item> getXRandomItems(int amount) {
		List<Item> returner = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			Item item = null;
			if (r.nextBoolean())
				item = getMundaneItem();
			else
				item = getMagicItem();
			int index = searchItemListByItemName(returner, item.getName());
			if (index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount() + 1);
			else
				returner.add(item);
		}
		return returner;
	}

	public List<Item> getMagicItemList() {
		return magicItemList;
	}

	public List<Item> getMundaneItemList() {
		return mundaneItemList;
	}

	public List<Item> getValuableItemList() {
		return valuableItemList;
	}
	
	public List<Item> generateTreasureHoard(int level) {
		List<Item> treasureList = new ArrayList<>();

		// giving gold
		Item gold = new Item();
		gold.setName("Gold");
		gold.setMagic(false);
		gold.setRarity("GOLD");
		gold.setWeight(0);
		gold.setType("$");
		gold.setText("The standard unit of currency");
		if (level > 16) {
			gold.setAmount((r.nextInt(61) + 12) * 1000 + (r.nextInt(41) + 8) * 10000);
		} else if (level > 10) {
			gold.setAmount((r.nextInt(21) + 4) * 1000 + (r.nextInt(26) + 5) * 1000);
		} else if (level > 4) {
			gold.setAmount((r.nextInt(11) + 2) + (r.nextInt(11) + 2) * 10 + (r.nextInt(31) + 6) * 100
					+ (r.nextInt(16) + 3) * 100);
		} else {
			gold.setAmount((r.nextInt(31) + 6) + (r.nextInt(16) + 3) * 10 + (r.nextInt(11) + 2) * 10);
		}
		treasureList.add(gold);

		int roll = r.nextInt(100) + 1;
		ResultSet rs = null;
		try {
			if (level > 16) {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM hoard1720 where d100 like '%" + roll + " %' LIMIT 1")
						.executeQuery();
			} else if (level > 10) {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM hoard1116 where d100 like '%" + roll + " %' LIMIT 1")
						.executeQuery();
			} else if (level > 4) {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM hoard510 where d100 like '%" + roll + " %' LIMIT 1")
						.executeQuery();
			} else {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM hoard04 where d100 like '%" + roll + " %' LIMIT 1")
						.executeQuery();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rs != null) {
			rollForItemsBasedOnQuery(treasureList, rs);
		}
		Collections.sort(treasureList, new Comparator<Item>() {
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

		return treasureList;
	}
	
	private void rollForItemsBasedOnQuery(List<Item> treasureList, ResultSet rs) {
		try {
			if (rs.next()) {
				String valuables = rs.getString("valuables");
				String magicitems = rs.getString("magicitems");
				if (!valuables.equals("")) {
					Scanner scan = new Scanner(valuables);
					String first = scan.next();
					int value = scan.nextInt();
					String last = scan.next();
					scan.close();
					scan = new Scanner(first);
					scan.useDelimiter("d");
					int amount = roll(scan.nextInt(), scan.nextInt());

					if (last.contains("art")) {
						// art piece
						getValuableItems(treasureList, "art", amount, value);
					} else {
						// gem
						getValuableItems(treasureList, "gems", amount, value);
					}
					scan.close();
				}
				if (!magicitems.equals("")) {
					Scanner scan = new Scanner(magicitems);
					while (scan.hasNext()) {
						// in while loop in case there is rolls on multiple tables
						Scanner s = new Scanner(scan.next());
						s.useDelimiter("d");
						int amount = roll(s.nextInt(), s.nextInt());
						getMagicItems(treasureList, scan.next().toLowerCase(), amount);
						s.close();
					}
					scan.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void getValuableItems(List<Item> treasureList, String type, int amount, int value) throws SQLException {
		for (int j = 0; j < amount; j++) {
			ResultSet rs = null;
			if (type.equals("art")) {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement(
								"SELECT * FROM artobjects WHERE value='" + value + "gp' ORDER BY RAND() LIMIT 1;")
						.executeQuery();
			} else {
				rs = JDBCConnection.getInstance().getConnection()
						.prepareStatement("SELECT * FROM gems WHERE value='" + value + "gp' ORDER BY RAND() LIMIT 1;")
						.executeQuery();
			}
			if (rs.next()) {
				int index = searchTreasureListByItemName(treasureList, rs.getString("name"));
				if (index >= 0) {
					treasureList.get(index).setAmount(treasureList.get(index).getAmount() + 1);
				} else {
					Item i = new Item();
					i.setName(rs.getString("name"));
					i.setMagic(false);
					i.setRarity("COMMON");
					i.setType(rs.getString("type"));
					i.setText(rs.getString("text"));
					i.setWeight(0);
					i.setAmount(1);
					treasureList.add(i);
				}

			}
		}
	}

	private void getMagicItems(List<Item> treasureList, String table, int amount) throws SQLException {
		for (int i = 0; i < amount; i++) {
			int roll = roll(1, 100);
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM magic" + table + " where d100 like '%" + roll + "%' LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				ResultSet result = JDBCConnection.getInstance().getConnection()
						.prepareStatement(
								"SELECT * FROM items where name like '%" + rs.getString("magicitem") + "%' LIMIT 1;")
						.executeQuery();
				if (result.next()) {
					int index = searchTreasureListByItemName(treasureList, result.getString("name"));
					if (index >= 0) {
						treasureList.get(index).setAmount(treasureList.get(index).getAmount() + 1);
					} else {
						Item item = new Item();
						item.setAmount(1);
						item.setMagic(true);
						item.setName(result.getString("name"));
						item.setRarity(result.getString("rarity"));
						item.setText(result.getString("text"));
						item.setType(result.getString("type"));
						item.setWeight(result.getDouble("weight"));
						treasureList.add(item);
					}
				}
			}
		}
	}
	
	private int roll(int numDice, int diceSize) {
		int sum = 0;
		for (int i = 0; i < numDice; i++) {
			sum += r.nextInt(diceSize) + 1;
		}
		return sum;
	}
	
	private int searchTreasureListByItemName(List<Item> treasureList, String name) {
		for (Item i : treasureList) {
			if (i.getName().equals(name))
				return treasureList.indexOf(i);
		}
		return -1;
	}

}
