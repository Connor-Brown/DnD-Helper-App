package my.dnd.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import my.dnd.app.model.Monster;

public class EncounterGenerator {

	private static List<Monster> monsterList;
	private static int[][] xpThreshold;
	private static Random r;

	private static EncounterGenerator instance;

	static AmazonS3 s3Client;
	
	public static EncounterGenerator getInstance() {
		if (instance == null) {
			readFileToList();
			initializeXPThresholds();
			r = new Random();
			instance = new EncounterGenerator();
		}
		return instance;
	}

	private EncounterGenerator() {
	}

	public List<Monster> getMonsterList() {
		return monsterList;
	}

	public List<Monster> generateGivenThematicEncounterByLevel(int level, int numPlayers, String type) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		int totalXP = 0;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			Monster m = monsterList.get(r.nextInt(monsterList.size()));
			if (m.getType().equals(type)) {
				returner.add(m);
				totalXP = crToXP(returner.get(0).getCr());
			}
		} while (totalXP == 0 || totalXP > xpRange[3]);
		return getNextThematicMonster(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	public ArrayList<Monster> generateRandomThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		int totalXP;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			returner.add(monsterList.get(r.nextInt(monsterList.size())));
			totalXP = crToXP(returner.get(0).getCr());
		} while (totalXP == 0 || totalXP > xpRange[3]);
		return getNextThematicMonster(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	private ArrayList<Monster> getNextThematicMonster(ArrayList<Monster> returner, int totalXP, double maxXP,
			double minXP, int numPlayers) {
		long before = System.currentTimeMillis();
		double modifiedTotalXP = totalXP;
		while (modifiedTotalXP < minXP) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				break;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (monsterList.get(nextInt).getType().equals(returner.get(0).getType())
					&& checkXP(returner, totalXP, tempXP, maxXP)) {
				totalXP += tempXP;
				modifiedTotalXP = modifyXP(returner, totalXP, numPlayers);
				returner.add(monsterList.get(nextInt));
			}
		}
		return returner;
	}

	public ArrayList<Monster> generateRandomEasyThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[1] - xpRange[0]) / 2;
		int totalXP;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			returner.add(monsterList.get(r.nextInt(monsterList.size())));
			totalXP = crToXP(returner.get(0).getCr());
		} while (totalXP == 0 || totalXP > xpRange[0] + buffer);
		return getNextThematicMonster(returner, totalXP, xpRange[0] + buffer, xpRange[0] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomMediumThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[2] - xpRange[1]) / 2;
		int totalXP;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			returner.add(monsterList.get(r.nextInt(monsterList.size())));
			totalXP = crToXP(returner.get(0).getCr());
		} while (totalXP == 0 || totalXP > xpRange[1] + buffer);
		return getNextThematicMonster(returner, totalXP, xpRange[1] + buffer, xpRange[1] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomHardThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			returner.add(monsterList.get(r.nextInt(monsterList.size())));
			totalXP = crToXP(returner.get(0).getCr());
		} while (totalXP == 0 || totalXP > xpRange[2] + buffer);
		return getNextThematicMonster(returner, totalXP, xpRange[2] + buffer, xpRange[2] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomDeadlyThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP;
		long before = System.currentTimeMillis();
		do {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			if (!returner.isEmpty())
				returner.clear();
			returner.add(monsterList.get(r.nextInt(monsterList.size())));
			totalXP = crToXP(returner.get(0).getCr());
		} while (totalXP == 0 || totalXP > xpRange[3] + buffer);
		return getNextThematicMonster(returner, totalXP, xpRange[3] + buffer, xpRange[3] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXPForSoloMonster(tempXP, numPlayers, xpRange[3], xpRange[0])) {
				returner.add(monsterList.get(nextInt));
				return returner;
			}
		}
	}

	private boolean checkXPForSoloMonster(int tempXP, int numPlayers, double maxXP, double minXP) {
		if (numPlayers > 5) {
			tempXP /= 2;
		} else if (numPlayers < 3) {
			tempXP *= 1.5;
		}
		return (tempXP < maxXP && tempXP > minXP);
	}

	public ArrayList<Monster> generateRandomEasySoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[1] - xpRange[0]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXPForSoloMonster(tempXP, numPlayers, xpRange[0] + buffer, xpRange[0] - buffer)) {
				returner.add(monsterList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<Monster> generateRandomMediumSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[2] - xpRange[1]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXPForSoloMonster(tempXP, numPlayers, xpRange[1] + buffer, xpRange[1] - buffer)) {
				returner.add(monsterList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<Monster> generateRandomHardSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXPForSoloMonster(tempXP, numPlayers, xpRange[2] + buffer, xpRange[2] - buffer)) {
				returner.add(monsterList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<Monster> generateRandomDeadlySoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXPForSoloMonster(tempXP, numPlayers, xpRange[3] + buffer, xpRange[3] - buffer)) {
				returner.add(monsterList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<Monster> generateRandomEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		int totalXP = 0;
		return getNextRandomMonster(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	private ArrayList<Monster> getNextRandomMonster(ArrayList<Monster> returner, int totalXP, double maxXP,
			double minXP, int numPlayers) {
		long before = System.currentTimeMillis();
		double modifiedTotalXP = totalXP;
		while (modifiedTotalXP < minXP) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				break;
			}
			int nextInt = r.nextInt(monsterList.size());
			int tempXP = crToXP(monsterList.get(nextInt).getCr());
			if (checkXP(returner, totalXP, tempXP, maxXP)) {
				totalXP += tempXP;
				modifiedTotalXP = modifyXP(returner, totalXP, numPlayers);
				returner.add(monsterList.get(nextInt));
			}
		}
		return returner;
	}

	private double modifyXP(ArrayList<Monster> returner, int totalXP, int numPlayers) {
		if (numPlayers > 5) {
			if (returner.size() > 14) {
				return totalXP * 3;
			} else if (returner.size() > 10) {
				return totalXP * 2.5;
			} else if (returner.size() > 6) {
				return totalXP * 2;
			} else if (returner.size() > 2) {
				return totalXP * 1.5;
			} else if (returner.size() == 2) {
				return totalXP * 1;
			} else {
				return totalXP * 0.5;
			}
		} else if (numPlayers < 3) {
			if (returner.size() > 14) {
				return totalXP * 5;
			} else if (returner.size() > 10) {
				return totalXP * 4;
			} else if (returner.size() > 6) {
				return totalXP * 3;
			} else if (returner.size() > 2) {
				return totalXP * 2.5;
			} else if (returner.size() == 2) {
				return totalXP * 2;
			} else {
				return totalXP * 1.5;
			}
		} else {
			if (returner.size() > 14) {
				return totalXP * 4;
			} else if (returner.size() > 10) {
				return totalXP * 3;
			} else if (returner.size() > 6) {
				return totalXP * 2.5;
			} else if (returner.size() > 2) {
				return totalXP * 2;
			} else if (returner.size() == 2) {
				return totalXP * 1.5;
			} else {
				return totalXP;
			}
		}
	}

	public ArrayList<Monster> generateRandomEasyEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[1] - xpRange[0]) / 2;
		int totalXP = 0;
		return getNextRandomMonster(returner, totalXP, xpRange[0] + buffer, xpRange[0] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomMediumEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[2] - xpRange[1]) / 2;
		int totalXP = 0;
		return getNextRandomMonster(returner, totalXP, xpRange[1] + buffer, xpRange[1] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomHardEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP = 0;
		return getNextRandomMonster(returner, totalXP, xpRange[2] + buffer, xpRange[2] - buffer, numPlayers);
	}

	public ArrayList<Monster> generateRandomDeadlyEncounterByLevel(int level, int numPlayers) {
		ArrayList<Monster> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP = 0;
		return getNextRandomMonster(returner, totalXP, xpRange[3] + buffer, xpRange[3] - buffer, numPlayers);
	}

	private static void readFileToList() {
		try {
			InputStream is = (InputStream) EncounterGenerator.class.getResourceAsStream("/monsters.xml");

			monsterList = new ArrayList<>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("monster");
			
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Monster m = new Monster();
					m.setName(element.getElementsByTagName("name").item(0).getTextContent());
					m.setSize(element.getElementsByTagName("size").item(0).getTextContent());
					Scanner scan = new Scanner(element.getElementsByTagName("type").item(0).getTextContent());
					scan.useDelimiter(" |,|\\(");
					m.setType(scan.next());
					scan.close();
					setAlignments(m, element.getElementsByTagName("alignment").item(0).getTextContent());
					scan = new Scanner(element.getElementsByTagName("ac").item(0).getTextContent());
					m.setAc(Integer.parseInt(scan.next()));
					scan.close();
					scan = new Scanner(element.getElementsByTagName("hp").item(0).getTextContent());
					m.setAverageHp(Integer.parseInt(scan.next()));

					if (scan.hasNext()) {
						String s = scan.next();
						s = s.substring(1, s.length() - 1);
						Scanner scanner = new Scanner(s);
						scanner.useDelimiter(" |d|\\+|\\-");
						int numDice = scanner.nextInt();
						int diceSize = scanner.nextInt();
						m.setNumDice(numDice);
						m.setDiceSize(diceSize);
						if (scanner.hasNext()) {
							int bonus = scanner.nextInt();
							m.setBonusHp(bonus);
						}
						scanner.close();
					}

					scan.close();
					m.setCr(crToDouble(element.getElementsByTagName("cr").item(0).getTextContent()));
					if (element.getElementsByTagName("legendary").item(0) != null) {
						m.setLegendary(true);
					} else {
						m.setLegendary(false);
					}
					monsterList.add(m);
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

	public List<String> getMonsterTypes() {
		List<String> returner = new ArrayList<>();
		for (Monster m : monsterList) {
			if (!returner.contains(m.getType()))
				returner.add(m.getType());
		}
		Collections.sort(returner);
		return returner;
	}

	private static void setAlignments(Monster m, String s) {
		Scanner scan = new Scanner(s);
		String first = scan.next();
		if (scan.hasNext()) {
			// has two alignments
			m.setAlignment1(first);
			m.setAlignment2(scan.next());
		} else {
			m.setAlignment1("neutral");
			m.setAlignment2("neutral");
		}
		scan.close();
	}

	private static double crToDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			if (s.equals("1/8"))
				return .125;
			else if (s.equals("1/4"))
				return .25;
			else
				return .5;
		}
	}

	private static int[] getXPs(int level, int numPlayers) {
		int[] returner = new int[4];
		returner[0] = xpThreshold[0][level - 1] * numPlayers;
		returner[1] = xpThreshold[1][level - 1] * numPlayers;
		returner[2] = xpThreshold[2][level - 1] * numPlayers;
		returner[3] = xpThreshold[3][level - 1] * numPlayers;
		return returner;
	}

	private boolean checkXP(List<Monster> returner, int totalXP, int tempXP, double maxXP) {
		if (returner.size() > 14) {
			return ((totalXP + tempXP) * 4 < maxXP);
		} else if (returner.size() > 10) {
			return ((totalXP + tempXP) * 3 < maxXP);
		} else if (returner.size() > 6) {
			return ((totalXP + tempXP) * 2.5 < maxXP);
		} else if (returner.size() > 2) {
			return ((totalXP + tempXP) * 2 < maxXP);
		} else if (returner.size() == 2) {
			return ((totalXP + tempXP) * 1.5 < maxXP);
		} else {
			return (totalXP + tempXP < maxXP);
		}
	}

	private static int crToXP(double cr) {
		if (cr == 30)
			return 155000;
		else if (cr == 29)
			return 135000;
		else if (cr == 28)
			return 120000;
		else if (cr == 27)
			return 105000;
		else if (cr == 26)
			return 90000;
		else if (cr == 25)
			return 75000;
		else if (cr == 24)
			return 62000;
		else if (cr == 23)
			return 50000;
		else if (cr == 22)
			return 41000;
		else if (cr == 21)
			return 33000;
		else if (cr == 20)
			return 25000;
		else if (cr == 19)
			return 22000;
		else if (cr == 18)
			return 20000;
		else if (cr == 17)
			return 18000;
		else if (cr == 16)
			return 15000;
		else if (cr == 15)
			return 13000;
		else if (cr == 14)
			return 11500;
		else if (cr == 13)
			return 10000;
		else if (cr == 12)
			return 8400;
		else if (cr == 11)
			return 7200;
		else if (cr == 10)
			return 5900;
		else if (cr == 9)
			return 5000;
		else if (cr == 8)
			return 3900;
		else if (cr == 7)
			return 2900;
		else if (cr == 6)
			return 2300;
		else if (cr == 5)
			return 1800;
		else if (cr == 4)
			return 1100;
		else if (cr == 3)
			return 700;
		else if (cr == 2)
			return 450;
		else if (cr == 1)
			return 200;
		else if (cr == .5)
			return 100;
		else if (cr == .25)
			return 50;
		else if (cr == .125)
			return 25;
		else
			return 10;
	}

	private static void initializeXPThresholds() {
		xpThreshold = new int[4][20];
		xpThreshold[0][0] = 25;
		xpThreshold[1][0] = 50;
		xpThreshold[2][0] = 75;
		xpThreshold[3][0] = 100;

		xpThreshold[0][1] = 50;
		xpThreshold[1][1] = 100;
		xpThreshold[2][1] = 150;
		xpThreshold[3][1] = 200;

		xpThreshold[0][2] = 75;
		xpThreshold[1][2] = 150;
		xpThreshold[2][2] = 225;
		xpThreshold[3][2] = 400;

		xpThreshold[0][3] = 125;
		xpThreshold[1][3] = 250;
		xpThreshold[2][3] = 375;
		xpThreshold[3][3] = 500;

		xpThreshold[0][4] = 250;
		xpThreshold[1][4] = 500;
		xpThreshold[2][4] = 750;
		xpThreshold[3][4] = 1100;

		xpThreshold[0][5] = 300;
		xpThreshold[1][5] = 600;
		xpThreshold[2][5] = 900;
		xpThreshold[3][5] = 1400;

		xpThreshold[0][6] = 350;
		xpThreshold[1][6] = 750;
		xpThreshold[2][6] = 1100;
		xpThreshold[3][6] = 1700;

		xpThreshold[0][7] = 450;
		xpThreshold[1][7] = 900;
		xpThreshold[2][7] = 1400;
		xpThreshold[3][7] = 2100;

		xpThreshold[0][8] = 550;
		xpThreshold[1][8] = 1100;
		xpThreshold[2][8] = 1600;
		xpThreshold[3][8] = 2400;

		xpThreshold[0][9] = 600;
		xpThreshold[1][9] = 1200;
		xpThreshold[2][9] = 1900;
		xpThreshold[3][9] = 2800;

		xpThreshold[0][10] = 800;
		xpThreshold[1][10] = 1600;
		xpThreshold[2][10] = 2400;
		xpThreshold[3][10] = 3600;

		xpThreshold[0][11] = 1000;
		xpThreshold[1][11] = 2000;
		xpThreshold[2][11] = 3000;
		xpThreshold[3][11] = 4500;

		xpThreshold[0][12] = 1100;
		xpThreshold[1][12] = 2200;
		xpThreshold[2][12] = 3400;
		xpThreshold[3][12] = 5100;

		xpThreshold[0][13] = 1250;
		xpThreshold[1][13] = 2500;
		xpThreshold[2][13] = 3800;
		xpThreshold[3][13] = 5700;

		xpThreshold[0][14] = 1400;
		xpThreshold[1][14] = 2800;
		xpThreshold[2][14] = 4300;
		xpThreshold[3][14] = 6400;

		xpThreshold[0][15] = 1600;
		xpThreshold[1][15] = 3200;
		xpThreshold[2][15] = 4800;
		xpThreshold[3][15] = 7200;

		xpThreshold[0][16] = 2000;
		xpThreshold[1][16] = 3900;
		xpThreshold[2][16] = 5900;
		xpThreshold[3][16] = 8800;

		xpThreshold[0][17] = 2100;
		xpThreshold[1][17] = 4200;
		xpThreshold[2][17] = 6300;
		xpThreshold[3][17] = 9500;

		xpThreshold[0][18] = 2400;
		xpThreshold[1][18] = 4900;
		xpThreshold[2][18] = 7300;
		xpThreshold[3][18] = 10900;

		xpThreshold[0][19] = 2800;
		xpThreshold[1][19] = 5700;
		xpThreshold[2][19] = 8500;
		xpThreshold[3][19] = 12700;
	}

	private void setTarrasques(ArrayList<Monster> returner) {
		if (returner.isEmpty()) {
			Monster tarrasque = null;
			for (Monster m : monsterList) {
				if (m.getName().equals("Tarrasque")) {
					tarrasque = m;
					break;
				}
			}
			returner.add(tarrasque);
			returner.add(tarrasque);
			returner.add(tarrasque);
			returner.add(tarrasque);
			returner.add(tarrasque);
		}
	}
}