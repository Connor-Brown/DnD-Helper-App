package my.dnd.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import my.dnd.app.model.Spell;

public class SpellService {
	
	private static List<Spell> spellList;
	private static List<String> schoolList;
	private static List<String> classList;
	private static SpellService instance;
	
	public static SpellService getInstance() {
		if(instance == null) {
			readFileToList();
			instance = new SpellService();
		}
		return instance;
	}
	private SpellService() {}
	
	public List<Spell> getSpellList() {
		return spellList;
	}
	public List<String> getSchools() {
		return schoolList;
	}
	public List<String> getClasses() {
		return classList;
	}
	
	private static void readFileToList() {
		try {			
			InputStream is = (InputStream) EncounterGenerator.class.getResourceAsStream("/spells.xml");
						
			spellList = new ArrayList<>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("spell");
			
			for(int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Spell m = new Spell();
					m.setName(element.getElementsByTagName("name").item(0).getTextContent());
					m.setLevel(Integer.parseInt(element.getElementsByTagName("level").item(0).getTextContent()));
					m.setSchool(element.getElementsByTagName("school").item(0).getTextContent());
					m.setCastingTime(element.getElementsByTagName("time").item(0).getTextContent());
					m.setRange(element.getElementsByTagName("range").item(0).getTextContent());
					m.setDuration(element.getElementsByTagName("duration").item(0).getTextContent());
					Scanner scan = new Scanner(element.getElementsByTagName("classes").item(0).getTextContent());
					List<String> classes = new ArrayList<>();
					while(scan.hasNext()) {
						String s = scan.next();
						if(!(s.startsWith("(") || s.endsWith(")") || s.endsWith("),")) && !s.equals("Old")) {
							if(s.endsWith(","))
								s = s.substring(0, s.length()-1);
							classes.add(s);
						}
					}
					m.setClasses(classes);
					scan.close();
					StringBuilder sb = new StringBuilder();
					NodeList nlist = element.getElementsByTagName("text");
					for (int j = 0; j < nlist.getLength(); j++) {
						sb.append(nlist.item(j).getTextContent() + " ");
					}
					m.setText(sb.toString());
					spellList.add(m);
				}
				schoolList = new ArrayList<>();
				for(Spell s : spellList) {
					if(!schoolList.contains(s.getSchool()))
						schoolList.add(s.getSchool());
				}
				classList = new ArrayList<>();
				for(Spell s : spellList) {
					for(String string : s.getClasses()) {
						if(!classList.contains(string))
							classList.add(string);
					}
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
	public List<Spell> getFilteredSpellList(List<Integer> levels, List<String> schools, List<String> classes) {
		List<Spell> list = new ArrayList<>();
		for(Spell s : spellList) {
			boolean check1 = false;
			boolean check2 = false;
			boolean check3 = false;
			if(levels != null && levels.contains(s.getLevel()))
				check1 = true;
			if(schools != null && schools.contains(s.getSchool()))
				check2 = true;
			if(classes != null) {
				for(String string : s.getClasses()) {
					if(classes.contains(string)) {
						check3 = true;
						break;
					}
				}
			}
			if(check1 && check2 && check3)
				list.add(s);
		}
		return list;
	}
	
}
