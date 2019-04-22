package my.dnd.app.controllers;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Item;
import my.dnd.app.model.Monster;

@Controller
@RequestMapping("/dnd/create")
public class CreationController {

	@RequestMapping("/item")
	public String item() {
		return "dnd/createItem";
	}

	@RequestMapping("/monster")
	public String monster() {
		return "dnd/createMonster";
	}

	@RequestMapping(value = { "/createItem" }, method = RequestMethod.POST)
	public String createItem(@RequestParam("name") String name, @RequestParam("type") String type,
			@RequestParam("weight") double weight, @RequestParam("rarity") String rarity,
			@RequestParam("text") String text) {
		Item item = new Item();
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\'') {
				name = name.substring(Math.min(i - 1, 0), i) + "\\" + name.substring(i, name.length());
				i++;
			}
		}
		item.setName(name);
		item.setType(type);
		item.setWeight(weight);
		item.setRarity(rarity);
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\'') {
				text = text.substring(Math.min(i - 1, 0), i) + "\\" + text.substring(i, text.length());
				i++;
			}
		}
		item.setText(text);
		try {
			JDBCConnection.getInstance().getConnection().createStatement()
					.executeUpdate("INSERT INTO items (name, type, weight, rarity, text) VALUES ('" + item.getName()
							+ "', '" + item.getType() + "', " + item.getWeight() + ", '" + item.getRarity() + "', '"
							+ item.getText() + "')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "dnd/createItem";
	}

	@RequestMapping(value = { "/createMonster" }, method = RequestMethod.POST)
	public String createMonster(@RequestParam("name") String name, @RequestParam("size") String size,
			@RequestParam("type") String type, @RequestParam("alignment") String alignment, @RequestParam("ac") int ac,
			@RequestParam("averagehp") int averagehp, @RequestParam("numdice") int numdice,
			@RequestParam("dicesize") int dicesize, @RequestParam("bonushp") int bonushp, @RequestParam("cr") double cr,
			@RequestParam(value="islegendary", required=false) boolean islegendary, @RequestParam("text") String text) {
		if(name.isEmpty())
			return "dnd/createMonster";
		Monster m = new Monster();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\'') {
				text = text.substring(Math.min(i - 1, 0), i) + "\\" + text.substring(i, text.length());
				i++;
			}
		}
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '\'') {
				name = name.substring(Math.min(i - 1, 0), i) + "\\" + name.substring(i, name.length());
				i++;
			}
		}
		m.setSize(size);
		m.setType(type);
		String[] alignments = alignment.split(" ");
		m.setAlignment1(alignments[0]);
		m.setAlignment2(alignments[1]);
		m.setAc(ac);
		m.setAverageHp(averagehp);
		m.setNumDice(numdice);
		m.setDiceSize(dicesize);
		m.setBonusHp(bonushp);
		m.setCr(cr);
		m.setLegendary(islegendary);
		String sql = "INSERT INTO monsters (name, size, type, alignment1, alignment2, ac, averagehp, numdice, dicesize, bonushp, cr, islegendary, text) values ('"
				+ name + "', '" + m.getSize() + "', '" + m.getType() + "', '" + m.getAlignment1() + "', '"
				+ m.getAlignment2() + "', " + m.getAc() + ", " + m.getAverageHp() + ", " + m.getNumDice() + ", "
				+ m.getDiceSize() + ", " + m.getBonusHp() + ", " + m.getCr() + ", " + m.isLegendary() + ", '" + text
				+ "')";
		try {
			JDBCConnection.getInstance().getConnection().createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "dnd/createMonster";
	}

}
