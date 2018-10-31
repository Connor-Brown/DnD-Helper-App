package my.dnd.app.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Item;
import my.dnd.app.model.NPC;

public class NPCGenerator {

	private static Random r;
	private static List<String> feats;

	private static NPCGenerator instance;

	private NPCGenerator() {
	}

	public static NPCGenerator getInstance() {
		if (instance == null) {
			r = new Random();
			initializeFeats();
			instance = new NPCGenerator();
		}
		return instance;
	}

	public NPC generateNPC(String classname, String race, int level) {
		NPC npc = new NPC();
		npc.setLevel(1);
		assignRaceFeatures(npc, race, level);
		assignClassFeatures(npc, classname, level);
		setBaseStats(npc);
		assignCommonFeatures(npc, level);
		npc.setLevel(level);
		setHp(npc);
		setArmor(npc);
//		assignInventory(npc);
		return npc;
	}

	private void assignCommonFeatures(NPC npc, int level) {
		if (level >= 4)
			abilityScoreImprovement(npc);
		if (level >= 8)
			abilityScoreImprovement(npc);
		if (level >= 12)
			abilityScoreImprovement(npc);
		if (level >= 16)
			abilityScoreImprovement(npc);
		if (level >= 19)
			abilityScoreImprovement(npc);
	}

	private void abilityScoreImprovement(NPC npc) {
		int sum = npc.getStrength() + npc.getDexterity() + npc.getConstitution() + npc.getIntelligence()
				+ npc.getWisdom() + npc.getCharisma();
		if (sum >= 120)
			chooseFeat(npc);
		else if (sum == 119) {
			if (r.nextBoolean())
				chooseFeat(npc);
			else {
				int i = r.nextInt(6);
				while (getStat(npc, i) > 19) {
					i = r.nextInt(6);
				}
				if (i == 0)
					npc.setStrength(npc.getStrength() + 1);
				else if (i == 1)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (i == 2)
					npc.setConstitution(npc.getConstitution() + 1);
				else if (i == 3)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (i == 4)
					npc.setWisdom(npc.getWisdom() + 1);
				else
					npc.setCharisma(npc.getCharisma() + 1);
			}
		} else {
			int rand = r.nextInt(3);
			if (rand == 0) {
				chooseFeat(npc);
			} else if (rand == 1) {
				int i = r.nextInt(6);
				while (getStat(npc, i) > 19) {
					i = r.nextInt(6);
				}
				if (i == 0)
					npc.setStrength(npc.getStrength() + 1);
				else if (i == 1)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (i == 2)
					npc.setConstitution(npc.getConstitution() + 1);
				else if (i == 3)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (i == 4)
					npc.setWisdom(npc.getWisdom() + 1);
				else
					npc.setCharisma(npc.getCharisma() + 1);
				i = r.nextInt(6);
				while (getStat(npc, i) > 19) {
					i = r.nextInt(6);
				}
				if (i == 0)
					npc.setStrength(npc.getStrength() + 1);
				else if (i == 1)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (i == 2)
					npc.setConstitution(npc.getConstitution() + 1);
				else if (i == 3)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (i == 4)
					npc.setWisdom(npc.getWisdom() + 1);
				else
					npc.setCharisma(npc.getCharisma() + 1);
			} else {
				int i = r.nextInt(6);
				while (getStat(npc, i) > 18) {
					i = r.nextInt(6);
				}
				if (i == 0)
					npc.setStrength(npc.getStrength() + 2);
				else if (i == 1)
					npc.setDexterity(npc.getDexterity() + 2);
				else if (i == 2)
					npc.setConstitution(npc.getConstitution() + 2);
				else if (i == 3)
					npc.setIntelligence(npc.getIntelligence() + 2);
				else if (i == 4)
					npc.setWisdom(npc.getWisdom() + 2);
				else
					npc.setCharisma(npc.getCharisma() + 2);
			}
		}
	}

	private void assignInventory(NPC npc) {
		givePrimaryWeapon(npc);
		giveOtherWeapons(npc);
		int check = r.nextInt(Math.max(1, npc.getLevel())) - 8;
		if (check > 0)
			npc.getInventory().add(giveMagicItem());
		Collections.sort(npc.getInventory(), new Comparator<Item>() {
			@Override
			public int compare(Item first, Item second) {
				return first.getName().compareTo(second.getName());
			}
		});
		npc.getInventory().addAll(getRandomMundaneItems());
		for (int i = 0; i < npc.getInventory().size() - 1; i++) {
			for (int j = i + 1; j < npc.getInventory().size(); j++) {
				if (npc.getInventory().get(i).getName().equals(npc.getInventory().get(j).getName())) {
					npc.getInventory().get(i)
							.setAmount(npc.getInventory().get(i).getAmount() + npc.getInventory().get(j).getAmount());
					npc.getInventory().remove(j);
					j--;
				}
			}
		}
		Item item = giveGold(npc);
		if (item != null)
			npc.getInventory().add(0, item);
	}

	private Item giveGold(NPC npc) {
		int roll = r.nextInt(100) + 1;
		int amount = 0;
		if (npc.getLevel() > 16) {
			if (roll >= 56)
				amount = (r.nextInt(11) + 2) * 1000 + (r.nextInt(6) + 1) * 1000;
			else if (roll >= 16)
				amount = (r.nextInt(6) + 1) * 1000 + (r.nextInt(6) + 1) * 1000;
			else
				amount = (r.nextInt(41) + 8) * 100 + (r.nextInt(11) + 2) * 500;
		} else if (npc.getLevel() > 10) {
			if (roll >= 76)
				amount = (r.nextInt(11) + 2) * 100 + (r.nextInt(11) + 2) * 100;
			else if (roll >= 36)
				amount = (r.nextInt(6) + 1) * 100 + (r.nextInt(11) + 2) * 100;
			else if (roll >= 21)
				amount = (r.nextInt(6) + 1) * 100 + (r.nextInt(6) + 1) * 50;
			else
				amount = (r.nextInt(6) + 1) * 100 + (r.nextInt(21) + 4) * 10;
		} else if (npc.getLevel() > 4) {
			if (roll >= 96)
				amount = (r.nextInt(16) + 3) * 10 + (r.nextInt(11) + 2) * 10;
			else if (roll >= 71)
				amount = (r.nextInt(21) + 4) * 10;
			else if (roll >= 61)
				amount = (r.nextInt(11) + 2) * 10 + (r.nextInt(16) + 3) * 5;
			else if (roll >= 31)
				amount = (r.nextInt(11) + 2) * 10 + (r.nextInt(31) + 6);
			else
				amount = (r.nextInt(6) + 1) * 5 + (r.nextInt(21) + 4);
		} else {
			if (roll >= 96)
				amount = (r.nextInt(6) + 1) * 10;
			else if (roll >= 71)
				amount = r.nextInt(16) + 3;
			else if (roll >= 61)
				amount = r.nextInt(8) + 1;
			else if (roll > 30)
				amount = r.nextInt(3);
			else
				amount = 0;
		}
		if (amount > 0) {
			Item i = new Item();
			i.setAmount(amount);
			i.setName("Gold");
			i.setMagic(false);
			i.setRarity("GOLD");
			i.setText("The standard unit of currency");
			i.setType("$");
			i.setWeight(0);
			return i;
		}
		return null;
	}

	private Item giveMagicItem() {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items WHERE rarity<>'ARTIFACT' ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private void giveOtherWeapons(NPC npc) {
		int amount = r.nextInt(4);
		for (int i = 0; i < amount; i++) {
			npc.getInventory().add(giveRandomWeapon());
		}
	}

	private Item giveRandomWeapon() {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection().prepareStatement(
					"SELECT * FROM items WHERE rarity='COMMON' AND (type='M' OR type='R') ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private void givePrimaryWeapon(NPC npc) {
		if (npc.getAttackStat() == 0) {
			int index = searchItemListByItemName(npc.getInventory(), "Shield");
			if (index >= 0) {
				npc.getInventory().add(giveOnehandedWeapon(npc));
			} else {
				npc.getInventory().add(giveRandomMeleeWeapon(npc));
			}
		} else if (npc.getAttackStat() == 1) {
			if (r.nextBoolean()) {
				Item i = giveFinesseWeapon(npc);
				npc.getInventory().add(i);
				if (r.nextBoolean())
					npc.getInventory().add(i);
			} else
				npc.getInventory().add(giveRangedWeapon());
		} else {
			if (r.nextBoolean())
				npc.getInventory().add(getItemFromDatabaseByName("Staff"));
			else
				npc.getInventory().add(getItemFromDatabaseByName("Wand"));
		}
	}

	private Item giveRangedWeapon() {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items WHERE rarity='COMMON' AND type='R' ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private Item giveFinesseWeapon(NPC npc) {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection().prepareStatement(
					"SELECT * FROM items WHERE rarity='COMMON' AND type='M' AND weight<3 ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private Item giveRandomMeleeWeapon(NPC npc) {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items WHERE rarity='COMMON' AND type='M' ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private Item giveOnehandedWeapon(NPC npc) {
		ResultSet rs;
		try {
			rs = JDBCConnection.getInstance().getConnection().prepareStatement(
					"SELECT * FROM items WHERE rarity='COMMON' AND type='M' AND weight<5 ORDER BY RAND() LIMIT 1;")
					.executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private int searchItemListByItemName(List<Item> treasureList, String name) {
		for (Item i : treasureList) {
			if (i.getName().equals(name))
				return treasureList.indexOf(i);
		}
		return -1;
	}

	private List<Item> getRandomMundaneItems() {
		List<Item> returner = new ArrayList<>();
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items WHERE rarity='COMMON' AND type='G' ORDER BY RAND() LIMIT "
							+ (r.nextInt(20) + 1))
					.executeQuery();
			while (rs.next()) {
				Item i = new Item();
				i.setAmount(r.nextInt(4) + 1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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
		Collections.sort(returner, new Comparator<Item>() {
			@Override
			public int compare(Item first, Item second) {
				return first.getName().compareTo(second.getName());
			}
		});
		return returner;
	}

	private void setHp(NPC npc) {
		int sum = npc.getHitDice() + getModifier(npc.getConstitution()) * npc.getLevel();
		for (int i = 1; i < npc.getLevel(); i++)
			sum += r.nextInt(npc.getHitDice()) + 1;
		if (npc.getFeats().contains("TOUGH"))
			sum += npc.getLevel() * 2;
		npc.setHp(npc.getHp() + sum);
	}

	private void setArmor(NPC npc) {
		if (npc.getRace().equals("TORTLE"))
			npc.setAc(17);
		else if (npc.getRace().equals("WARFORGED")) {
			int rand = r.nextInt(3);
			if (rand == 0)
				npc.setAc(11 + getModifier(npc.getDexterity()) + getProficiencyBonus(npc.getLevel()));
			else if (rand == 1) {
				if (npc.getDexterity() < 2)
					npc.setAc(13 + getModifier(npc.getDexterity()) + getProficiencyBonus(npc.getLevel()));
				else
					npc.setAc(15 + getProficiencyBonus(npc.getLevel()));
			} else
				npc.setAc(16 + getProficiencyBonus(npc.getLevel()));
		} else {
			if ((npc.getRace().equals("LOXODON") || npc.getRace().equals("LIZARDFOLK"))
					&& npc.getAc() < (13 + getModifier(npc.getDexterity())))
				npc.setAc(13 + getModifier(npc.getDexterity()));
			switch (npc.getClassname()) {
			case "BARBARIAN":
				if (r.nextInt(4) == 0) {
					giveShield(npc);
				}
				break;
			case "BARD":
			case "ROGUE":
			case "WARLOCK":
				giveLightArmor(npc);
				break;
			case "CLERIC":
			case "FIGHTER":
			case "PALADIN":
				int rand = r.nextInt(3);
				if (rand == 0) {
					giveHeavyArmor(npc);
				} else if (rand == 1) {
					giveMediumArmor(npc);
				} else {
					giveLightArmor(npc);
				}
				if (r.nextBoolean())
					giveShield(npc);
				break;
			case "DRUID":
				int ra = r.nextInt(3);
				if (ra == 0) {
					Item i = getItemFromDatabaseByName("Leather Armor");
					if (i != null)
						npc.getInventory().add(i);
					if (npc.getAc() < getModifier(npc.getDexterity()) + 11)
						npc.setAc(getModifier(npc.getDexterity()) + 11);
				} else if (ra == 1) {
					Item i = getItemFromDatabaseByName("Hide Armor");
					if (i != null)
						npc.getInventory().add(i);
					if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 12)
						npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 12);
				} else {
					Item i = getItemFromDatabaseByName("Scale Mail");
					if (i != null)
						npc.getInventory().add(i);
					if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 14)
						npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 14);
				}
				if (r.nextBoolean()) {
					giveShield(npc);
				}
				break;
			case "RANGER":
				if (r.nextBoolean())
					giveLightArmor(npc);
				else
					giveMediumArmor(npc);
				if (r.nextInt(8) == 0)
					giveShield(npc);
				break;
			default:
				if (npc.getAc() == 0)
					npc.setAc(10 + getModifier(npc.getDexterity()));
				break;
			}
		}
	}

	private void giveShield(NPC npc) {
//		Item i = getItemFromDatabaseByName("Shield");
//		if (i != null)
//			npc.getInventory().add(i);
		npc.setAc(npc.getAc() + 2);
	}

	private void giveHeavyArmor(NPC npc) {
		if (npc.getStrength() >= 15) {
			int temp = r.nextInt(4);
			if (temp == 0) {
//				Item i = getItemFromDatabaseByName("Plate Armor");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 18)
					npc.setAc(18);
			} else if (temp == 1) {
//				Item i = getItemFromDatabaseByName("Splint Armor");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 17)
					npc.setAc(17);
			} else if (temp == 2) {
//				Item i = getItemFromDatabaseByName("Chain Mail");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 16)
					npc.setAc(16);
			} else {
//				Item i = getItemFromDatabaseByName("Ring Mail");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 14)
					npc.setAc(14);
			}
		} else if (npc.getStrength() >= 13) {
			if (r.nextBoolean()) {
//				Item i = getItemFromDatabaseByName("Ring Mail");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 14)
					npc.setAc(14);
			} else {
//				Item i = getItemFromDatabaseByName("Chain Mail");
//				if (i != null)
//					npc.getInventory().add(i);
				if (npc.getAc() < 16)
					npc.setAc(16);
			}
		} else {
//			Item i = getItemFromDatabaseByName("Ring Mail");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < 14)
				npc.setAc(14);
		}
	}

	private void giveMediumArmor(NPC npc) {
		int temp = r.nextInt(5);
		if (temp == 0) {
//			Item i = getItemFromDatabaseByName("Hide Armor");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 12)
				npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 12);
		} else if (temp == 1) {
//			Item i = getItemFromDatabaseByName("Chain Shirt");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 13)
				npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 13);
		} else if (temp == 2) {
//			Item i = getItemFromDatabaseByName("Scale Mail");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 14)
				npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 14);
		} else if (temp == 3) {
//			Item i = getItemFromDatabaseByName("Breastplate");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 14)
				npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 14);
		} else {
//			Item i = getItemFromDatabaseByName("Half Plate");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < max2Dex(getModifier(npc.getDexterity())) + 15)
				npc.setAc(max2Dex(getModifier(npc.getDexterity())) + 15);
		}
	}

	private void giveLightArmor(NPC npc) {
		if (r.nextBoolean()) {
//			Item i = getItemFromDatabaseByName("Leather Armor");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < getModifier(npc.getDexterity()) + 11)
				npc.setAc(getModifier(npc.getDexterity()) + 11);
		} else {
//			Item i = getItemFromDatabaseByName("Studded Leather Armor");
//			if (i != null)
//				npc.getInventory().add(i);
			if (npc.getAc() < getModifier(npc.getDexterity()) + 12)
				npc.setAc(getModifier(npc.getDexterity()) + 12);
		}
	}

	private Item getItemFromDatabaseByName(String name) {
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection()
					.prepareStatement("SELECT * FROM items where name='" + name + "';").executeQuery();
			if (rs.next()) {
				Item i = new Item();
				i.setAmount(1);
				if (rs.getString("rarity").equals("COMMON"))
					i.setMagic(false);
				else
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

	private int getProficiencyBonus(int level) {
		if (level > 16)
			return 6;
		else if (level > 12)
			return 5;
		else if (level > 8)
			return 4;
		else if (level > 4)
			return 3;
		return 2;
	}

	private int getModifier(int stat) {
		if (stat % 2 == 1)
			return (stat - 11) / 2;
		return (stat - 10) / 2;
	}

	private void setBaseStats(NPC npc) {
		List<Integer> stats = new ArrayList<>();
		stats.add(rollForStat());
		stats.add(rollForStat());
		stats.add(rollForStat());
		stats.add(rollForStat());
		stats.add(rollForStat());
		stats.add(rollForStat());
		Collections.sort(stats);
		switch (npc.getAttackStat()) {
		case 0:
			npc.setStrength(npc.getStrength() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setDexterity(npc.getDexterity() + stats.remove(0));
			npc.setConstitution(npc.getConstitution() + stats.remove(0));
			npc.setIntelligence(npc.getIntelligence() + stats.remove(0));
			npc.setWisdom(npc.getWisdom() + stats.remove(0));
			npc.setCharisma(npc.getCharisma() + stats.remove(0));
			break;
		case 1:
			npc.setDexterity(npc.getDexterity() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setStrength(npc.getStrength() + stats.remove(0));
			npc.setConstitution(npc.getConstitution() + stats.remove(0));
			npc.setIntelligence(npc.getIntelligence() + stats.remove(0));
			npc.setWisdom(npc.getWisdom() + stats.remove(0));
			npc.setCharisma(npc.getCharisma() + stats.remove(0));
			break;
		case 2:
			npc.setConstitution(npc.getConstitution() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setDexterity(npc.getDexterity() + stats.remove(0));
			npc.setStrength(npc.getStrength() + stats.remove(0));
			npc.setIntelligence(npc.getIntelligence() + stats.remove(0));
			npc.setWisdom(npc.getWisdom() + stats.remove(0));
			npc.setCharisma(npc.getCharisma() + stats.remove(0));
			break;
		case 3:
			npc.setIntelligence(npc.getIntelligence() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setDexterity(npc.getDexterity() + stats.remove(0));
			npc.setConstitution(npc.getConstitution() + stats.remove(0));
			npc.setStrength(npc.getStrength() + stats.remove(0));
			npc.setWisdom(npc.getWisdom() + stats.remove(0));
			npc.setCharisma(npc.getCharisma() + stats.remove(0));
			break;
		case 4:
			npc.setWisdom(npc.getWisdom() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setDexterity(npc.getDexterity() + stats.remove(0));
			npc.setConstitution(npc.getConstitution() + stats.remove(0));
			npc.setIntelligence(npc.getIntelligence() + stats.remove(0));
			npc.setStrength(npc.getStrength() + stats.remove(0));
			npc.setCharisma(npc.getCharisma() + stats.remove(0));
			break;
		case 5:
			npc.setCharisma(npc.getCharisma() + stats.remove(stats.size() - 1));
			Collections.shuffle(stats);
			npc.setDexterity(npc.getDexterity() + stats.remove(0));
			npc.setConstitution(npc.getConstitution() + stats.remove(0));
			npc.setIntelligence(npc.getIntelligence() + stats.remove(0));
			npc.setWisdom(npc.getWisdom() + stats.remove(0));
			npc.setStrength(npc.getStrength() + stats.remove(0));
			break;
		}
	}

	private int rollForStat() {
		int first = r.nextInt(6) + 1;
		int second = r.nextInt(6) + 1;
		int third = r.nextInt(6) + 1;
		int fourth = r.nextInt(6) + 1;
		if (first <= second && first <= third && first <= fourth)
			return second + third + fourth;
		else if (second <= third && second <= fourth)
			return first + third + fourth;
		else if (third <= fourth)
			return first + second + fourth;
		else
			return first + second + third;
	}

	private void assignRaceFeatures(NPC npc, String race, int level) {
		switch (race) {
		case "AARAKOCRA":
			makeAarakocra(npc);
			break;
		case "AASIMAR":
			makeAasimar(npc);
			break;
		case "CHANGELING":
			makeChangeling(npc);
			break;
		case "DRAGONBORN":
			makeDragonborn(npc);
			break;
		case "DWARF":
			makeDwarf(npc, level);
			break;
		case "ELF":
			makeElf(npc);
			break;
		case "FIRBOLG":
			makeFirbolg(npc);
			break;
		case "GENASI":
			makeGenasi(npc);
			break;
		case "GNOME":
			makeGnome(npc);
			break;
		case "GOLIATH":
			makeGoliath(npc);
			break;
		case "HALF-ELF":
			makeHalfElf(npc);
			break;
		case "HALF-ORC":
			makeHalfOrc(npc);
			break;
		case "HUMAN":
			makeHuman(npc);
			break;
		case "LIZARDFOLK":
			makeLizardfold(npc);
			break;
		case "LOXODON":
			makeLoxodon(npc);
			break;
		case "ORC":
			makeOrc(npc);
			break;
		case "SHIFTER":
			makeShifter(npc);
			break;
		case "TABAXI":
			makeTabaxi(npc);
			break;
		case "TIEFLING":
			makeTiefling(npc);
			break;
		case "TORTLE":
			makeTortle(npc);
			break;
		case "TRITON":
			makeTriton(npc);
			break;
		case "WARFORGED":
			makeWarforged(npc);
			break;
		}
		if (r.nextBoolean())
			npc.setGender("Male");
		else
			npc.setGender("Female");
	}

	private void makeOrc(NPC npc) {
		npc.setRace("ORC");
		npc.setStrength(2);
		npc.setSpeed(30);
		npc.setAge(randomAge(14, 75));
		int temp = r.nextInt(4);
		if (temp == 0 || temp == 1)
			npc.setConstitution(1);
		else if (temp == 2)
			npc.setWisdom(1);
		else
			npc.setDexterity(1);
	}

	private void makeWarforged(NPC npc) {
		npc.setRace("WARFORGED");
		npc.setConstitution(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(2, 30));
	}

	private void makeTriton(NPC npc) {
		npc.setRace("TRITON");
		npc.setStrength(1);
		npc.setConstitution(1);
		npc.setCharisma(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(15, 200));
	}

	private void makeTortle(NPC npc) {
		npc.setRace("TORTLE");
		npc.setStrength(2);
		npc.setWisdom(1);
		npc.setAge(randomAge(15, 50));
		npc.setSpeed(30);
	}

	private void makeTiefling(NPC npc) {
		npc.setRace("TIEFLING");
		npc.setIntelligence(1);
		npc.setCharisma(2);
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 120));
	}

	private void makeTabaxi(NPC npc) {
		npc.setRace("TABAXI");
		npc.setDexterity(2);
		npc.setCharisma(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 100));
	}

	private void makeShifter(NPC npc) {
		npc.setRace("SHIFTER");
		npc.setDexterity(1);
		npc.setAge(randomAge(10, 70));
		npc.setSpeed(30);
		if (r.nextBoolean()) {
			if (r.nextBoolean())
				npc.setConstitution(2);
			else
				npc.setStrength(2);
		} else {
			if (r.nextBoolean()) {
				npc.setDexterity(1);
				npc.setCharisma(1);
				npc.setSpeed(35);
			} else
				npc.setWisdom(2);
		}
	}

	private void makeLoxodon(NPC npc) {
		npc.setRace("LOXODON");
		npc.setConstitution(2);
		npc.setWisdom(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 450));
	}

	private void makeLizardfold(NPC npc) {
		npc.setRace("LIZARDFOLK");
		npc.setConstitution(2);
		npc.setWisdom(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(14, 60));
	}

	private void makeHuman(NPC npc) {
		npc.setRace("HUMAN");
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 100));
		if (r.nextBoolean()) {
			npc.setStrength(1);
			npc.setDexterity(1);
			npc.setConstitution(1);
			npc.setIntelligence(1);
			npc.setWisdom(1);
			npc.setCharisma(1);
		} else {
			int f = r.nextInt(5);
			int s = r.nextInt(5);
			while (s == f) {
				s = r.nextInt(5);
			}
			if (f == 0 || s == 0)
				npc.setStrength(1);
			if (f == 1 || s == 1)
				npc.setDexterity(1);
			if (f == 2 || s == 2)
				npc.setConstitution(1);
			if (f == 3 || s == 3)
				npc.setIntelligence(1);
			if (f == 4 || s == 4)
				npc.setWisdom(1);
			chooseFeat(npc);
		}
	}

	private void makeHalfOrc(NPC npc) {
		npc.setRace("HALF-ORC");
		npc.setStrength(2);
		npc.setConstitution(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(14, 75));
	}

	private void makeHalfElf(NPC npc) {
		npc.setRace("HALF-ELF");
		npc.setCharisma(2);
		int f = r.nextInt(5);
		int s = r.nextInt(5);
		while (s == f) {
			s = r.nextInt(5);
		}
		if (f == 0 || s == 0)
			npc.setStrength(1);
		if (f == 1 || s == 1)
			npc.setDexterity(1);
		if (f == 2 || s == 2)
			npc.setConstitution(1);
		if (f == 3 || s == 3)
			npc.setIntelligence(1);
		if (f == 4 || s == 4)
			npc.setWisdom(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 180));
	}

	private void makeGoliath(NPC npc) {
		npc.setRace("GOLIATH");
		npc.setStrength(2);
		npc.setConstitution(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 100));
	}

	private void makeGnome(NPC npc) {
		npc.setRace("GNOME");
		npc.setIntelligence(2);
		if (r.nextBoolean())
			npc.setDexterity(1);
		else
			npc.setConstitution(1);
		npc.setSpeed(25);
		npc.setAge(randomAge(18, 5000));
	}

	private void makeGenasi(NPC npc) {
		npc.setRace("GENASI");
		npc.setConstitution(2);
		if (r.nextBoolean()) {
			if (r.nextBoolean())
				npc.setDexterity(1);
			else
				npc.setStrength(1);
		} else {
			if (r.nextBoolean())
				npc.setIntelligence(1);
			else
				npc.setWisdom(1);
		}
		npc.setSpeed(30);
		npc.setAge(randomAge(18, 120));
	}

	private void makeFirbolg(NPC npc) {
		npc.setRace("FIRBOLG");
		npc.setSpeed(30);
		npc.setWisdom(2);
		npc.setStrength(1);
		npc.setAge(randomAge(30, 500));
	}

	private void makeElf(NPC npc) {
		npc.setRace("ELF");
		npc.setSpeed(30);
		npc.setDexterity(2);
		int rand = r.nextInt(3);
		if (rand == 0)
			npc.setIntelligence(1);
		else if (rand == 1) {
			npc.setWisdom(1);
			npc.setSpeed(35);
		} else
			npc.setCharisma(1);
		npc.setAge(randomAge(18, 750));
	}

	private void makeDwarf(NPC npc, int level) {
		npc.setRace("DWARF");
		npc.setConstitution(2);
		if (r.nextBoolean()) {
			npc.setWisdom(1);
			npc.setHp(level);
		} else
			npc.setStrength(2);
		npc.setSpeed(25);
		npc.setAge(randomAge(25, 350));
	}

	private void makeDragonborn(NPC npc) {
		npc.setRace("DRAGONBORN");
		npc.setStrength(2);
		npc.setCharisma(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(15, 80));
	}

	private void makeChangeling(NPC npc) {
		npc.setRace("CHANGELING");
		npc.setCharisma(2);
		if (r.nextBoolean())
			npc.setDexterity(1);
		else
			npc.setIntelligence(1);
		npc.setSpeed(30);
		npc.setAge(randomAge(12, 100));
	}

	private void makeAasimar(NPC npc) {
		npc.setRace("AASIMAR");
		npc.setCharisma(2);
		int rand = r.nextInt(3);
		if (rand == 0)
			npc.setWisdom(1);
		else if (rand == 1)
			npc.setConstitution(1);
		else
			npc.setStrength(1);
		npc.setAge(randomAge(18, 160));
		npc.setSpeed(30);
	}

	private void makeAarakocra(NPC npc) {
		npc.setRace("AARAKOCRA");
		npc.setDexterity(2);
		npc.setWisdom(1);
		npc.setAge(randomAge(3, 30));
		npc.setSpeed(25);
	}

	private int randomAge(int min, int max) {
		return r.nextInt(max - min + 1) + min;
	}

	private int max2Dex(int modifier) {
		if (modifier > 2)
			return 2;
		return modifier;
	}

	private void assignClassFeatures(NPC npc, String classname, int level) {
		switch (classname) {
		case "BARBARIAN":
			makeBarbarian(npc, level);
			break;
		case "BARD":
			makeBard(npc, level);
			break;
		case "CLERIC":
			makeCleric(npc, level);
			break;
		case "DRUID":
			makeDruid(npc, level);
			break;
		case "FIGHTER":
			makeFighter(npc, level);
			break;
		case "MONK":
			makeMonk(npc, level);
			break;
		case "PALADIN":
			makePaladin(npc, level);
			break;
		case "RANGER":
			makeRanger(npc, level);
			break;
		case "ROGUE":
			makeRogue(npc, level);
			break;
		case "SORCERER":
			makeSorcerer(npc, level);
			break;
		case "WARLOCK":
			makeWarlock(npc, level);
			break;
		case "WIZARD":
			makeWizard(npc, level);
			break;
		}
	}

	private void makeWizard(NPC npc, int level) {
		npc.setClassname("WIZARD");
		npc.setAttackStat(3);
		npc.setHitDice(6);
		if (level >= 2) {
			String[] list = { "School of Abjuration", "School of Divination", "School of Enchantment",
					"School of Evocation", "School of Illusion", "School of Necromancy", "School of Transmutation" };
			setSubClass(npc, list);
		}
	}

	private void makeWarlock(NPC npc, int level) {
		npc.setClassname("WARLOCK");
		npc.setAttackStat(5);
		npc.setHitDice(8);
		String[] list = { "Archfey", "Fiend", "Great Old One" };
		setSubClass(npc, list);
	}

	private void makeSorcerer(NPC npc, int level) {
		npc.setClassname("SORCERER");
		npc.setAttackStat(5);
		npc.setHitDice(6);
		String[] list = { "Draconic Bloodline", "Wild Magic" };
		setSubClass(npc, list);
	}

	private void makeRogue(NPC npc, int level) {
		npc.setClassname("ROGUE");
		npc.setAttackStat(1);
		npc.setHitDice(8);
		if (level >= 3) {
			String[] list = { "Thief", "Assassin", "Arcane Trickster" };
			setSubClass(npc, list);
		}
	}

	private void makeRanger(NPC npc, int level) {
		npc.setClassname("RANGER");
		npc.setAttackStat(1);
		npc.setHitDice(10);
		if (!npc.getRace().equals("TORTLE") && !npc.getRace().equals("LOXODON") && r.nextInt(4) == 0)
			npc.setAc(npc.getAc() + 1);
		if (level >= 3) {
			String[] list = { "Hunter", "Beast Master" };
			setSubClass(npc, list);
		}
	}

	private void makePaladin(NPC npc, int level) {
		npc.setClassname("PALADIN");
		npc.setAttackStat(0);
		npc.setHitDice(10);
		if (!npc.getRace().equals("TORTLE") && !npc.getRace().equals("LOXODON") && r.nextInt(4) == 0)
			npc.setAc(npc.getAc() + 1);
		if (level >= 3) {
			String[] list = { "Oath of Devotion", "Oath of the Ancients", "Oath of Vengeance" };
			setSubClass(npc, list);
		}
	}

	private void makeMonk(NPC npc, int level) {
		npc.setClassname("MONK");
		npc.setAttackStat(1);
		npc.setHitDice(8);
		if (getModifier(npc.getDexterity()) + getModifier(npc.getWisdom()) + 10 > npc.getAc())
			npc.setAc(getModifier(npc.getDexterity()) + getModifier(npc.getWisdom()) + 10);
		if (level >= 2)
			npc.setSpeed(npc.getSpeed() + 10);
		if (level >= 3) {
			String[] list = { "Way of the Open Hand", "Way of the Shadow", "Way of the Four Elements" };
			setSubClass(npc, list);
		}
	}

	private void makeFighter(NPC npc, int level) {
		npc.setClassname("FIGHTER");
		if (r.nextBoolean())
			npc.setAttackStat(0);
		else
			npc.setAttackStat(1);
		npc.setHitDice(10);
		if (level >= 6)
			abilityScoreImprovement(npc);
		if (level >= 14)
			abilityScoreImprovement(npc);
		if (!npc.getRace().equals("TORTLE") && !npc.getRace().equals("LOXODON") && r.nextInt(4) == 0)
			npc.setAc(npc.getAc() + 1);
		if (level >= 3) {
			String[] list = { "Champion", "Battlemaster", "Eldritch Knight" };
			setSubClass(npc, list);
		}
	}

	private void makeDruid(NPC npc, int level) {
		npc.setClassname("DRUID");
		npc.setAttackStat(4);
		npc.setHitDice(8);
		if (level >= 2) {
			String[] list = { "Circle of the Land", "Circle of the Moon" };
			setSubClass(npc, list);
		}
	}

	private void makeCleric(NPC npc, int level) {
		npc.setClassname("CLERIC");
		npc.setAttackStat(4);
		npc.setHitDice(8);
		String[] list = { "Knowledge Domain", "Life Domain", "Light Domain", "Light Domain", "Nature Domain",
				"Tempest Domain", "Trickery Domain", "War Domain" };
		setSubClass(npc, list);
	}

	private void makeBard(NPC npc, int level) {
		npc.setClassname("BARD");
		npc.setAttackStat(5);
		npc.setHitDice(8);
		if (level >= 3) {
			String[] list = { "College of Lore", "College of Valor" };
			setSubClass(npc, list);
		}
	}

	private void makeBarbarian(NPC npc, int level) {
		npc.setClassname("BARBARIAN");
		npc.setAttackStat(0);
		npc.setHitDice(12);
		if (getModifier(npc.getDexterity()) + getModifier(npc.getConstitution()) + 10 > npc.getAc())
			npc.setAc(getModifier(npc.getDexterity()) + getModifier(npc.getConstitution()) + 10);
		if (level >= 5)
			npc.setSpeed(npc.getSpeed() + 10);
		if (level == 20) {
			npc.setStrength(npc.getStrength() + 4);
			if (npc.getStrength() > 24)
				npc.setStrength(24);
			npc.setConstitution(npc.getConstitution() + 4);
			if (npc.getConstitution() > 24)
				npc.setConstitution(24);
		}
		if (level >= 3) {
			String[] list = { "Path of the Berserker", "Path of the Totem Warrior" };
			setSubClass(npc, list);
		}
	}

	private void setSubClass(NPC npc, String[] list) {
		npc.setSubclass(list[r.nextInt(list.length)]);
	}

	private static void initializeFeats() {
		feats = new ArrayList<>();
		feats.add("ACTOR");
		feats.add("ATHLETE");
		feats.add("ALERT");
		feats.add("CHARGER");
		feats.add("DEFENSIVE DUELIST");
		feats.add("DUAL WIELDER");
		feats.add("DUNGEON DELVER");
		feats.add("DURABLE");
		feats.add("ELEMENTAL ADEPT");
		feats.add("GRAPPLER");
		feats.add("GREAT WEAPON MASTER");
		feats.add("HEALER");
		feats.add("INSPIRING LEADER");
		feats.add("KEEN MIND");
		feats.add("LINGUIST");
		feats.add("LUCKY");
		feats.add("MAGE SLAYER");
		feats.add("MAGIC INITIATE");
		feats.add("MARTIAL ADEPT");
		feats.add("MOBILE");
		feats.add("MOUNTED COMBATANT");
		feats.add("OBSERVANT");
		feats.add("RESILIENT");
		feats.add("RITUAL CASTER");
		feats.add("SAVAGE ATTACKER");
		feats.add("SENTINEL");
		feats.add("SHARPSHOOTER");
		feats.add("SKILLED");
		feats.add("SKULKER");
		feats.add("SPELL SNIPER");
		feats.add("TOUGH");
		feats.add("WAR CASTER");
		feats.add("BLADE MASTERY");
		feats.add("CROSSBOW EXPERT");
		feats.add("FELL HANDED");
		feats.add("FLAIL MASTERY");
		feats.add("POLEARM MASTER");
		feats.add("SHIELD MASTER");
		feats.add("SPEAR MASTER");
		feats.add("TAVERN BRAWLER");
		feats.add("WEAPON MASTER");
		feats.add("LIGHTLY ARMORED");
		feats.add("MODERATELY ARMORED");
		feats.add("MEDIUM ARMOR MASTER");
		feats.add("HEAVILY ARMORED");
		feats.add("HEAVY ARMOR MASTER");
		feats.add("ACROBAT");
		feats.add("ANIMAL HANDLER");
		feats.add("ARCANIST");
		feats.add("BRAWNY");
		feats.add("DIPLOMAT");
		feats.add("EMPATHIC");
		feats.add("HISTORIAN");
		feats.add("INVESTIGATOR");
		feats.add("MEDIC");
		feats.add("MENACING");
		feats.add("NATURALIST");
		feats.add("PERCEPTIVE");
		feats.add("PERFORMER");
		feats.add("QUICK-FINGERED");
		feats.add("SILVER-TONGUED");
		feats.add("STEALTHY");
		feats.add("SURVIVALIST");
		feats.add("THEOLOGIAN");
		feats.add("ALCHEMIST");
		feats.add("BURGLAR");
		feats.add("GOURMAND");
		feats.add("MASTER OF DISGUISE");
		feats.add("BARBED HIDE");
		feats.add("CRITTER FRIEND");
		feats.add("DRAGON FEAR");
		feats.add("DRAGON HIDE");
		feats.add("DRAGON WINGS");
		feats.add("DROW HIGH MAGIC");
		feats.add("DWARVEN FORTITUDE");
		feats.add("ELVEN ACCURACY");
		feats.add("EVERYBODY'S FRIEND");
		feats.add("FADE AWAY");
		feats.add("FEY TELEPORTATION");
		feats.add("FLAMES OF PHLEGETHOS");
		feats.add("GRUDGE-BEARER");
		feats.add("HUMAN DETERMINATION");
		feats.add("INFERNAL CONSTITUTION");
		feats.add("ORCISH AGGRESSION");
		feats.add("ORCISH FURY");
		feats.add("PRODIGY");
		feats.add("SQUAT NIMBLENESS");
		feats.add("SVIRFNEBLIN MAGIC");
		feats.add("WONDER MAKER");
		feats.add("WOOD ELF MAGIC");
	}

	private void chooseFeat(NPC npc) {
		int choice = r.nextInt(feats.size());
		while (!isLegalFeat(npc, choice)) {
			choice = r.nextInt(feats.size());
		}
		if (npc.getFeats() == null)
			npc.setFeats(new ArrayList<>());
		npc.getFeats().add(feats.get(choice));
		switch (choice) {
		case 0:
		case 50:
		case 55:
		case 58:
		case 60:
		case 67:
		case 76:
			if (npc.getCharisma() < 20)
				npc.setCharisma(npc.getCharisma() + 1);
			break;
		case 1:
		case 40:
		case 41:
		case 42:
			if (r.nextBoolean()) {
				if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
				else if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
			} else {
				if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
			}
			break;
		case 7:
		case 66:
		case 74:
		case 82:
			if (npc.getConstitution() < 20)
				npc.setConstitution(npc.getConstitution() + 1);
			break;
		case 13:
		case 14:
		case 48:
		case 52:
		case 53:
		case 56:
		case 63:
		case 64:
			if (npc.getIntelligence() < 20)
				npc.setIntelligence(npc.getIntelligence() + 1);
			break;
		case 19:
			npc.setSpeed(npc.getSpeed() + 10);
			break;
		case 21:
			if (r.nextBoolean()) {
				if (npc.getWisdom() < 20)
					npc.setWisdom(npc.getWisdom() + 1);
				else if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
			} else {
				if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (npc.getWisdom() < 20)
					npc.setWisdom(npc.getWisdom() + 1);
			}
			break;
		case 22:
		case 81:
			int rand = r.nextInt(6);
			while (getStat(npc, rand) >= 20) {
				rand = r.nextInt(6);
			}
			if (rand == 0)
				npc.setStrength(npc.getStrength() + 1);
			else if (rand == 1)
				npc.setDexterity(npc.getDexterity() + 1);
			else if (rand == 2)
				npc.setConstitution(npc.getConstitution() + 1);
			else if (rand == 3)
				npc.setIntelligence(npc.getIntelligence() + 1);
			else if (rand == 4)
				npc.setWisdom(npc.getWisdom() + 1);
			else if (rand == 5)
				npc.setCharisma(npc.getCharisma() + 1);
			break;
		case 39:
		case 84:
			if (r.nextBoolean()) {
				if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
				else if (npc.getConstitution() < 20)
					npc.setConstitution(npc.getConstitution() + 1);
			} else {
				if (npc.getConstitution() < 20)
					npc.setConstitution(npc.getConstitution() + 1);
				else if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
			}
			break;
		case 44:
		case 45:
		case 49:
			if (npc.getStrength() < 20)
				npc.setStrength(npc.getStrength() + 1);
			break;
		case 46:
		case 59:
		case 61:
		case 65:
			if (npc.getDexterity() < 20)
				npc.setDexterity(npc.getDexterity() + 1);
			break;
		case 47:
		case 51:
		case 54:
		case 57:
		case 62:
			if (npc.getWisdom() < 20)
				npc.setWisdom(npc.getWisdom() + 1);
			break;
		case 68:
			if (r.nextBoolean()) {
				if (npc.getCharisma() < 20)
					npc.setCharisma(npc.getCharisma() + 1);
				else if (npc.getConstitution() < 20)
					npc.setConstitution(npc.getConstitution() + 1);
			} else {
				if (npc.getConstitution() < 20)
					npc.setConstitution(npc.getConstitution() + 1);
				else if (npc.getCharisma() < 20)
					npc.setCharisma(npc.getCharisma() + 1);
			}
			break;
		case 70:
		case 71:
			int ra = r.nextInt(3);
			if (ra == 0) {
				if (r.nextBoolean()) {
					if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
				} else {
					if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				}

			} else if (ra == 1) {
				if (r.nextBoolean()) {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
				} else {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
				}
			} else {
				if (r.nextBoolean()) {
					if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				} else {
					if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
				}
			}
			break;
		case 77:
		case 88:
			if (r.nextBoolean()) {
				if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
			} else {
				if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
			}
			break;
		case 78:
		case 79:
			if (r.nextBoolean()) {
				if (npc.getCharisma() < 20)
					npc.setCharisma(npc.getCharisma() + 1);
				else if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
			} else {
				if (npc.getIntelligence() < 20)
					npc.setIntelligence(npc.getIntelligence() + 1);
				else if (npc.getCharisma() < 20)
					npc.setCharisma(npc.getCharisma() + 1);
			}
			break;
		case 80:
			int ran = r.nextInt(3);
			if (ran == 0) {
				if (r.nextBoolean()) {
					if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
				} else {
					if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				}

			} else if (ran == 1) {
				if (r.nextBoolean()) {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
				} else {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
				}
			} else {
				if (r.nextBoolean()) {
					if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				} else {
					if (npc.getWisdom() < 20)
						npc.setWisdom(npc.getWisdom() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getStrength() < 20)
						npc.setStrength(npc.getStrength() + 1);
				}
			}
			break;
		case 85:
			int rando = r.nextInt(3);
			if (rando == 0) {
				if (r.nextBoolean()) {
					if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
				} else {
					if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				}

			} else if (rando == 1) {
				if (r.nextBoolean()) {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
				} else {
					if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
				}
			} else {
				if (r.nextBoolean()) {
					if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
				} else {
					if (npc.getCharisma() < 20)
						npc.setCharisma(npc.getCharisma() + 1);
					else if (npc.getConstitution() < 20)
						npc.setConstitution(npc.getConstitution() + 1);
					else if (npc.getDexterity() < 20)
						npc.setDexterity(npc.getDexterity() + 1);
				}
			}
			break;
		case 86:
			if (r.nextBoolean()) {
				if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
				else if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
			} else {
				if (npc.getDexterity() < 20)
					npc.setDexterity(npc.getDexterity() + 1);
				else if (npc.getStrength() < 20)
					npc.setStrength(npc.getStrength() + 1);
			}
			npc.setSpeed(npc.getSpeed() + 5);
			break;
		case 75:
			int randomer = r.nextInt(6);
			while (randomer == 0 || randomer == 2 || getStat(npc, randomer) >= 20) {
				randomer = r.nextInt(6);
			}
			if (randomer == 1)
				npc.setDexterity(npc.getDexterity() + 1);
			else if (randomer == 3)
				npc.setIntelligence(npc.getIntelligence() + 1);
			else if (randomer == 4)
				npc.setWisdom(npc.getWisdom() + 1);
			else if (randomer == 5)
				npc.setCharisma(npc.getCharisma() + 1);
			break;
		}
	}

	private int getStat(NPC npc, int rand) {
		if (rand == 0)
			return npc.getStrength();
		if (rand == 1)
			return npc.getDexterity();
		if (rand == 2)
			return npc.getConstitution();
		if (rand == 3)
			return npc.getIntelligence();
		if (rand == 4)
			return npc.getWisdom();
		if (rand == 5)
			return npc.getCharisma();
		return -1;
	}

	private boolean isLegalFeat(NPC npc, int choice) {
		if (npc.getFeats().contains(feats.get(choice)))
			return false;
		switch (choice) {
		case 4:
		case 28:
			if (npc.getDexterity() < 13)
				return false;
			break;
		case 9:
			if (npc.getStrength() < 13)
				return false;
			break;
		case 12:
			if (npc.getCharisma() < 13)
				return false;
			break;
		case 23:
			if (npc.getIntelligence() < 13 && npc.getWisdom() < 13)
				return false;
			break;
		case 68:
		case 79:
		case 82:
			if (!npc.getRace().equals("TIEFLING"))
				return false;
			break;
		case 69:
		case 77:
		case 87:
		case 88:
			if (!npc.getRace().equals("GNOME"))
				return false;
			break;
		case 70:
		case 71:
		case 72:
			if (!npc.getRace().equals("DRAGONBORN"))
				return false;
			break;
		case 73:
		case 78:
		case 89:
			if (!npc.getRace().equals("ELF"))
				return false;
			break;
		case 74:
		case 80:
		case 86:
			if (!npc.getRace().equals("DWARF"))
				return false;
			break;
		case 75:
			if (!(npc.getRace().equals("ELF") || npc.getRace().equals("HALF-ELF")))
				return false;
			break;
		case 76:
			if (!npc.getRace().equals("HALF-ELF"))
				return false;
			break;
		case 81:
			if (!npc.getRace().equals("HUMAN"))
				return false;
			break;
		case 83:
		case 84:
			if (!npc.getRace().equals("HALF-ORC"))
				return false;
			break;
		case 85:
			if (!(npc.getRace().equals("HALF-ELF") || npc.getRace().equals("HALF-ORC")
					|| npc.getRace().equals("HUMAN")))
				return false;
			break;
		}
		return true;
	}
}
