package my.dnd.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Item;

public class TreasureGenerator {

	private static List<Item> magicItemList;
	private static List<Item> mundaneItemList;
	private static List<Item> valuableItemList;
	private static Random r;

	private static TreasureGenerator instance;

	public static TreasureGenerator getInstance() {
		if (instance == null) {
			 readMagicItemsFileToList();
			 readMundaneItemsFileToList();
			 readValuableItemsFileToList();
			r = new Random();
			instance = new TreasureGenerator();
		}
		return instance;
	}

	private TreasureGenerator() {
	}

	public List<Item> getXRandomValuableItems(int amount) {
		List<Item> returner = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			Item item = getValuableItem();
			int index = searchItemListByItemName(returner, item.getName());
			if(index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount()+1);
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
			if(index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount()+1);
			else
				returner.add(item);
		}
		return returner;
	}

	public Item getMundaneItem() {
		try {
			ResultSet rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM items where rarity='COMMON' ORDER BY RAND() LIMIT 1;").executeQuery();
			if(rs.next()) {
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
			ResultSet rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM items where rarity <> 'COMMON' ORDER BY RAND() LIMIT 1;").executeQuery();
			if(rs.next()) {
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
			if(index >= 0)
				returner.get(index).setAmount(returner.get(index).getAmount()+1);
			else 
				returner.add(item);
		}
		return returner;
	}

	public List<Item> getXRandomMagicItemsBySpecificRarity(int amount, String rarity) {
		List<Item> returner = new ArrayList<>();
		try {
			for(int j = 0; j < amount; j++) {
				ResultSet rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM items WHERE rarity='"+rarity+"' ORDER BY RAND() LIMIT 1").executeQuery();
				while(rs.next()) {
					Item i = new Item();
					i.setAmount(1);
					i.setMagic(true);
					i.setRarity(rs.getString("rarity"));
					i.setName(rs.getString("name"));
					i.setText(rs.getString("text"));
					i.setType(rs.getString("type"));
					i.setWeight(rs.getDouble("weight"));
					int index = searchItemListByItemName(returner, i.getName());
					if(index >= 0)
						returner.get(index).setAmount(returner.get(index).getAmount()+1);
					else 
						returner.add(i);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	private static void readValuableItemsFileToList() {
		try {
			InputStream is = (InputStream) EncounterGenerator.class.getResourceAsStream("/valuableitems.xml");

			valuableItemList = new ArrayList<>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("item");
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Item m = new Item();
					m.setName(element.getElementsByTagName("name").item(0).getTextContent());
					m.setType(element.getElementsByTagName("type").item(0).getTextContent().toUpperCase());
					m.setMagic(false);
					if (element.getElementsByTagName("weight").item(0) != null)
						if (element.getElementsByTagName("weight").item(0).getTextContent() != "")
							m.setWeight(Double
									.parseDouble(element.getElementsByTagName("weight").item(0).getTextContent()));
						else
							m.setWeight(0);
					else
						m.setWeight(0);
					m.setRarity("COMMON");
					StringBuilder sb = new StringBuilder();
					NodeList nlist = element.getElementsByTagName("text");
					for (int j = 0; j < nlist.getLength(); j++) {
						sb.append(nlist.item(j).getTextContent() + " ");
					}
					m.setText(sb.toString());
					valuableItemList.add(m);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readMundaneItemsFileToList() {
		try {
			InputStream is = (InputStream) EncounterGenerator.class.getResourceAsStream("/mundaneitems.xml");

			mundaneItemList = new ArrayList<>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("item");
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Item m = new Item();
					m.setName(element.getElementsByTagName("name").item(0).getTextContent());
					m.setType(element.getElementsByTagName("type").item(0).getTextContent().toUpperCase());
					m.setMagic(false);
					if (element.getElementsByTagName("weight").item(0) != null)
						if (element.getElementsByTagName("weight").item(0).getTextContent() != "")
							m.setWeight(Double
									.parseDouble(element.getElementsByTagName("weight").item(0).getTextContent()));
						else
							m.setWeight(0);
					else
						m.setWeight(0);
					m.setRarity("COMMON");
					mundaneItemList.add(m);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readMagicItemsFileToList() {
		try {
			InputStream is = (InputStream) EncounterGenerator.class.getResourceAsStream("/magicitems.xml");

			magicItemList = new ArrayList<>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("item");
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Item m = new Item();
					m.setName(element.getElementsByTagName("name").item(0).getTextContent());
					m.setType(element.getElementsByTagName("type").item(0).getTextContent().toUpperCase());
					m.setMagic(true);
					if (element.getElementsByTagName("weight").item(0) != null)
						if (element.getElementsByTagName("weight").item(0).getTextContent() != "")
							m.setWeight(Double
									.parseDouble(element.getElementsByTagName("weight").item(0).getTextContent()));
						else
							m.setWeight(0);
					else
						m.setWeight(0);
					if (element.getElementsByTagName("rarity").item(0) != null)
						m.setRarity(element.getElementsByTagName("rarity").item(0).getTextContent().toUpperCase());
					else
						m.setRarity("UNCOMMON");
					StringBuilder sb = new StringBuilder();
					NodeList nlist = element.getElementsByTagName("text");
					for (int j = 0; j < nlist.getLength(); j++) {
						sb.append(nlist.item(j).getTextContent() + " ");
					}
					m.setText(sb.toString());
					magicItemList.add(m);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
