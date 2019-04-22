package my.dnd.app.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import my.dnd.app.model.MonsterHolder;
import my.dnd.app.jdbc.JDBCConnection;
import my.dnd.app.model.Monster;
import my.dnd.app.model.Monster.InnerText;

public class EncounterGenerator {

	private static List<MonsterHolder> MonsterHolderList;
	private static int[][] xpThreshold;
	private static Random r;

	private static EncounterGenerator instance;

	public static EncounterGenerator getInstance() {
		if (instance == null) {
			// readFileToList();
			initMonsterHolderList();
			initializeXPThresholds();
			r = new Random();
			instance = new EncounterGenerator();
		}
		return instance;
	}

	private EncounterGenerator() {
	}

	private static void initMonsterHolderList() {
		try {
			MonsterHolderList = new ArrayList<>();
			ResultSet rs = JDBCConnection.getInstance().getConnection().prepareStatement("SELECT * FROM monsters")
					.executeQuery();
			while (rs.next()) {
				Monster m = new Monster();
				m.setAc(rs.getInt("ac"));
				m.setName(rs.getString("name"));
				m.setSize(rs.getString("size"));
				m.setType(rs.getString("type"));
				m.setAlignment1(rs.getString("alignment1"));
				m.setAlignment2(rs.getString("alignment2"));
				m.setAverageHp(rs.getInt("averagehp"));
				m.setNumDice(rs.getInt("numdice"));
				m.setDiceSize(rs.getInt("dicesize"));
				m.setBonusHp(rs.getInt("bonushp"));
				m.setCr(rs.getShort("cr"));
				m.setLegendary(rs.getBoolean("islegendary"));
				String text = rs.getString("text");
				List<InnerText> list = new ArrayList<>();
				list.add(m.new InnerText(text, null));
				m.setInnerTextList(list);
				MonsterHolderList.add(new MonsterHolder(m, 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Monster findMonsterByName(String name) {
		for (MonsterHolder m : MonsterHolderList) {
			if (m.getMonster().getName().equalsIgnoreCase(name))
				return m.getMonster();
		}
		return null;
	}

	public List<MonsterHolder> getMonsterHolderList() {
		return MonsterHolderList;
	}

	public List<Monster> getMonsterListSortedByName(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByName(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return o1.getMonster().getName().compareTo(o2.getMonster().getName());
			}
		});
		return list;
	}

	public List<Monster> getMonsterListSortedBySize(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return new Integer(sizeToInt(o1.getSize())).compareTo(new Integer(sizeToInt(o2.getSize())));
			}
		});
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedBySize(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Integer(sizeToInt(o1.getMonster().getSize()))
						.compareTo(new Integer(sizeToInt(o2.getMonster().getSize())));
			}
		});
		return list;
	}

	private int sizeToInt(String size) {
		switch (size) {
		case "T":
			return 0;
		case "S":
			return 1;
		case "M":
			return 2;
		case "L":
			return 3;
		case "H":
			return 4;
		case "G":
			return 5;
		default:
			return -1;
		}
	}

	public List<Monster> getMonsterListSortedByType(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return o1.getType().compareTo(o2.getType());
			}
		});
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByType(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return o1.getMonster().getType().compareTo(o2.getMonster().getType());
			}
		});
		return list;
	}

	public List<Monster> getMonsterListSortedByAlignment(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return (o1.getAlignment1() + " " + o1.getAlignment2())
						.compareTo(o2.getAlignment1() + " " + o2.getAlignment2());
			}
		});
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByAlignment(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return (o1.getMonster().getAlignment1() + " " + o1.getMonster().getAlignment2())
						.compareTo(o2.getMonster().getAlignment1() + " " + o2.getMonster().getAlignment2());
			}
		});
		return list;
	}

	public List<Monster> getMonsterListSortedByAC(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return new Integer(o1.getAc()).compareTo(new Integer(o2.getAc()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByAC(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Integer(o1.getMonster().getAc()).compareTo(new Integer(o2.getMonster().getAc()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<Monster> getMonsterListSortedByHp(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return new Integer(o1.getAverageHp()).compareTo(new Integer(o2.getAverageHp()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByHp(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Integer(o1.getMonster().getAverageHp())
						.compareTo(new Integer(o2.getMonster().getAverageHp()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<Monster> getMonsterListSortedByCr(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return new Double(o1.getCr()).compareTo(new Double(o2.getCr()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByCr(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Double(o1.getMonster().getCr()).compareTo(new Double(o2.getMonster().getCr()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<Monster> getMonsterListSortedByLegendaryStatus(List<Monster> list) {
		Collections.sort(list, new Comparator<Monster>() {
			@Override
			public int compare(Monster o1, Monster o2) {
				return new Boolean(o1.isLegendary()).compareTo(new Boolean(o2.isLegendary()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<MonsterHolder> getMonsterHolderListSortedByLegendaryStatus(List<MonsterHolder> list) {
		Collections.sort(list, new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Boolean(o1.getMonster().isLegendary()).compareTo(new Boolean(o2.getMonster().isLegendary()));
			}
		});
		Collections.reverse(list);
		return list;
	}

	public List<MonsterHolder> generateGivenThematicEncounterByLevel(int level, int numPlayers, String type) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			MonsterHolder m = MonsterHolderList.get(r.nextInt(MonsterHolderList.size()));
			if (m.getMonster().getType().equals(type)) {
				returner.add(m);
				totalXP = crToXP(returner.get(0).getMonster().getCr());
			}
		} while (totalXP == 0 || totalXP > xpRange[3]);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			returner.add(MonsterHolderList.get(r.nextInt(MonsterHolderList.size())));
			totalXP = crToXP(returner.get(0).getMonster().getCr());
		} while (totalXP == 0 || totalXP > xpRange[3]);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	private ArrayList<MonsterHolder> getNextThematicMonsterHolder(ArrayList<MonsterHolder> returner, int totalXP,
			double maxXP, double minXP, int numPlayers) {
		long before = System.currentTimeMillis();
		double modifiedTotalXP = totalXP;
		while (modifiedTotalXP < minXP) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				break;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (MonsterHolderList.get(nextInt).getMonster().getType().equals(returner.get(0).getMonster().getType())
					&& checkXP(returner, totalXP, tempXP, maxXP)) {
				totalXP += tempXP;
				modifiedTotalXP = modifyXP(returner, totalXP, numPlayers);
				returner.add(MonsterHolderList.get(nextInt));
			}
		}
		return returner;
	}

	public ArrayList<MonsterHolder> generateRandomEasyThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			returner.add(MonsterHolderList.get(r.nextInt(MonsterHolderList.size())));
			totalXP = crToXP(returner.get(0).getMonster().getCr());
		} while (totalXP == 0 || totalXP > xpRange[0] + buffer);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[0] + buffer, xpRange[0] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomMediumThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			returner.add(MonsterHolderList.get(r.nextInt(MonsterHolderList.size())));
			totalXP = crToXP(returner.get(0).getMonster().getCr());
		} while (totalXP == 0 || totalXP > xpRange[1] + buffer);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[1] + buffer, xpRange[1] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomHardThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			returner.add(MonsterHolderList.get(r.nextInt(MonsterHolderList.size())));
			totalXP = crToXP(returner.get(0).getMonster().getCr());
		} while (totalXP == 0 || totalXP > xpRange[2] + buffer);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[2] + buffer, xpRange[2] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomDeadlyThematicEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
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
			returner.add(MonsterHolderList.get(r.nextInt(MonsterHolderList.size())));
			totalXP = crToXP(returner.get(0).getMonster().getCr());
		} while (totalXP == 0 || totalXP > xpRange[3] + buffer);
		return getNextThematicMonsterHolder(returner, totalXP, xpRange[3] + buffer, xpRange[3] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXPForSoloMonsterHolder(tempXP, numPlayers, xpRange[3], xpRange[0])) {
				returner.add(MonsterHolderList.get(nextInt));
				return returner;
			}
		}
	}

	private boolean checkXPForSoloMonsterHolder(int tempXP, int numPlayers, double maxXP, double minXP) {
		if (numPlayers > 5) {
			tempXP /= 2;
		} else if (numPlayers < 3) {
			tempXP *= 1.5;
		}
		return (tempXP < maxXP && tempXP > minXP);
	}

	public ArrayList<MonsterHolder> generateRandomEasySoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[1] - xpRange[0]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXPForSoloMonsterHolder(tempXP, numPlayers, xpRange[0] + buffer, xpRange[0] - buffer)) {
				returner.add(MonsterHolderList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<MonsterHolder> generateRandomMediumSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[2] - xpRange[1]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXPForSoloMonsterHolder(tempXP, numPlayers, xpRange[1] + buffer, xpRange[1] - buffer)) {
				returner.add(MonsterHolderList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<MonsterHolder> generateRandomHardSoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXPForSoloMonsterHolder(tempXP, numPlayers, xpRange[2] + buffer, xpRange[2] - buffer)) {
				returner.add(MonsterHolderList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<MonsterHolder> generateRandomDeadlySoloEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		long before = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				return returner;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXPForSoloMonsterHolder(tempXP, numPlayers, xpRange[3] + buffer, xpRange[3] - buffer)) {
				returner.add(MonsterHolderList.get(nextInt));
				return returner;
			}
		}
	}

	public ArrayList<MonsterHolder> generateRandomEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		int totalXP = 0;
		return getNextRandomMonsterHolder(returner, totalXP, xpRange[3], xpRange[0], numPlayers);
	}

	private ArrayList<MonsterHolder> getNextRandomMonsterHolder(ArrayList<MonsterHolder> returner, int totalXP,
			double maxXP, double minXP, int numPlayers) {
		long before = System.currentTimeMillis();
		double modifiedTotalXP = totalXP;
		while (modifiedTotalXP < minXP) {
			if (System.currentTimeMillis() - before > 5000) {
				setTarrasques(returner);
				break;
			}
			int nextInt = r.nextInt(MonsterHolderList.size());
			int tempXP = crToXP(MonsterHolderList.get(nextInt).getMonster().getCr());
			if (checkXP(returner, totalXP, tempXP, maxXP)) {
				totalXP += tempXP;
				modifiedTotalXP = modifyXP(returner, totalXP, numPlayers);
				returner.add(MonsterHolderList.get(nextInt));
			}
		}
		return returner;
	}

	private double modifyXP(ArrayList<MonsterHolder> returner, int totalXP, int numPlayers) {
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

	public ArrayList<MonsterHolder> generateRandomEasyEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[1] - xpRange[0]) / 2;
		int totalXP = 0;
		return getNextRandomMonsterHolder(returner, totalXP, xpRange[0] + buffer, xpRange[0] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomMediumEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[2] - xpRange[1]) / 2;
		int totalXP = 0;
		return getNextRandomMonsterHolder(returner, totalXP, xpRange[1] + buffer, xpRange[1] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomHardEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP = 0;
		return getNextRandomMonsterHolder(returner, totalXP, xpRange[2] + buffer, xpRange[2] - buffer, numPlayers);
	}

	public ArrayList<MonsterHolder> generateRandomDeadlyEncounterByLevel(int level, int numPlayers) {
		ArrayList<MonsterHolder> returner = new ArrayList<>();
		int[] xpRange = getXPs(level, numPlayers);
		double buffer = (xpRange[3] - xpRange[2]) / 2;
		int totalXP = 0;
		return getNextRandomMonsterHolder(returner, totalXP, xpRange[3] + buffer, xpRange[3] - buffer, numPlayers);
	}

	// private static void readFileToList() {
	// try {
	// InputStream is = (InputStream)
	// EncounterGenerator.class.getResourceAsStream("/tomeOfFoes.xml");
	//
	// MonsterHolderList = new ArrayList<>();
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder db = factory.newDocumentBuilder();
	// Document doc = db.parse(is);
	// doc.getDocumentElement().normalize();
	// NodeList list = doc.getElementsByTagName("monster");
	//
	// for (int i = 0; i < list.getLength(); i++) {
	// Node node = list.item(i);
	// if (node.getNodeType() == Node.ELEMENT_NODE) {
	// Element element = (Element) node;
	// Monster m = new Monster();
	// m.setName(element.getElementsByTagName("name").item(0).getTextContent());
	// m.setSize(element.getElementsByTagName("size").item(0).getTextContent());
	// Scanner scan = new
	// Scanner(element.getElementsByTagName("type").item(0).getTextContent());
	// scan.useDelimiter(" |,|\\(");
	// m.setType(scan.next());
	// scan.close();
	// setAlignments(m,
	// element.getElementsByTagName("alignment").item(0).getTextContent());
	// scan = new
	// Scanner(element.getElementsByTagName("ac").item(0).getTextContent());
	// m.setAc(Integer.parseInt(scan.next()));
	// scan.close();
	// scan = new
	// Scanner(element.getElementsByTagName("hp").item(0).getTextContent());
	// m.setAverageHp(Integer.parseInt(scan.next()));
	//
	// if (scan.hasNext()) {
	// String s = scan.next();
	// s = s.substring(1, s.length() - 1);
	// Scanner scanner = new Scanner(s);
	// scanner.useDelimiter(" |d|\\+|\\-");
	// int numDice = Integer.parseInt(scanner.next());
	// int diceSize = Integer.parseInt(scanner.next());
	// m.setNumDice(numDice);
	// m.setDiceSize(diceSize);
	// if (scanner.hasNext()) {
	// int bonus = scanner.nextInt();
	// m.setBonusHp(bonus);
	// }
	// scanner.close();
	// }
	//
	// scan.close();
	// m.setCr(crToDouble(element.getElementsByTagName("cr").item(0).getTextContent()));
	// if (element.getElementsByTagName("legendary").item(0) != null) {
	// m.setLegendary(true);
	// } else {
	// m.setLegendary(false);
	// }
	//
	// List<InnerText> innerTextList = new ArrayList<>();
	// // traits
	// int count = 0;
	// while (element.getElementsByTagName("trait").item(count) != null) {
	// Element innerElement = (Element)
	// element.getElementsByTagName("trait").item(count);
	// InnerText innerText = m.new InnerText();
	// innerText.setName(
	// "Trait - " +
	// innerElement.getElementsByTagName("name").item(0).getTextContent());
	// int innerCount = 0;
	// while (innerElement.getElementsByTagName("text").item(innerCount) != null) {
	// if (innerText.getText() == null)
	// innerText.setText(
	// innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// else
	// innerText.setText(innerText.getText()
	// + innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// innerCount++;
	// }
	// count++;
	// innerTextList.add(innerText);
	// }
	//
	// // actions
	// count = 0;
	// while (element.getElementsByTagName("action").item(count) != null) {
	// Element innerElement = (Element)
	// element.getElementsByTagName("action").item(count);
	// InnerText innerText = m.new InnerText();
	// innerText.setName(
	// "Action - " +
	// innerElement.getElementsByTagName("name").item(0).getTextContent());
	// int innerCount = 0;
	// while (innerElement.getElementsByTagName("text").item(innerCount) != null) {
	// if (innerText.getText() == null)
	// innerText.setText(
	// innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// else
	// innerText.setText(innerText.getText()
	// + innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// innerCount++;
	// }
	// count++;
	// innerTextList.add(innerText);
	// }
	//
	// // legendary
	// count = 0;
	// while (element.getElementsByTagName("legendary").item(count) != null) {
	// Element innerElement = (Element)
	// element.getElementsByTagName("legendary").item(count);
	// InnerText innerText = m.new InnerText();
	// innerText.setName("Legendary Action - "
	// + innerElement.getElementsByTagName("name").item(0).getTextContent());
	// int innerCount = 0;
	// while (innerElement.getElementsByTagName("text").item(innerCount) != null) {
	// if (innerText.getText() == null)
	// innerText.setText(
	// innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// else
	// innerText.setText(innerText.getText()
	// + innerElement.getElementsByTagName("text").item(innerCount).getTextContent()
	// + "\n");
	// innerCount++;
	// }
	// count++;
	// innerTextList.add(innerText);
	// }
	// m.setInnerTextList(innerTextList);
	//
	// writeMonsterToDatabase(m);
	//
	// MonsterHolderList.add(new MonsterHolder(m, 1));
	// }
	// }
	// } catch (IOException | ParserConfigurationException | SAXException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private static void writeMonsterToDatabase(Monster m) {
	// String sql = "";
	// try {
	// StringBuilder sb = new StringBuilder();
	// for (InnerText i : m.getInnerTextList()) {
	// sb.append(i.getName() + "\n" + i.getText() + "\n");
	// }
	// String text = sb.toString();
	// for (int i = 0; i < text.length(); i++) {
	// if (text.charAt(i) == '\'') {
	// text = text.substring(Math.min(i - 1, 0), i) + "\\" + text.substring(i,
	// text.length());
	// i++;
	// }
	// }
	// String name = m.getName();
	// for (int i = 0; i < name.length(); i++) {
	// if (name.charAt(i) == '\'') {
	// name = name.substring(Math.min(i - 1, 0), i) + "\\" + name.substring(i,
	// name.length());
	// i++;
	// }
	// }
	// sql = "INSERT INTO monsters (name, size, type, alignment1, alignment2, ac,
	// averagehp, numdice, dicesize, bonushp, cr, islegendary, text) values ('"
	// + name + "', '" + m.getSize() + "', '" + m.getType() + "', '" +
	// m.getAlignment1() + "', '"
	// + m.getAlignment2() + "', " + m.getAc() + ", " + m.getAverageHp() + ", " +
	// m.getNumDice() + ", "
	// + m.getDiceSize() + ", " + m.getBonusHp() + ", " + m.getCr() + ", " +
	// m.isLegendary() + ", '" + text
	// + "')";
	// JDBCConnection.getInstance().getConnection().createStatement().executeUpdate(sql);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private static double crToDouble(String cr) {
	// switch (cr) {
	// case "30":
	// return 30;
	// case "29":
	// return 29;
	// case "28":
	// return 28;
	// case "27":
	// return 27;
	// case "26":
	// return 26;
	// case "25":
	// return 25;
	// case "24":
	// return 24;
	// case "23":
	// return 23;
	// case "22":
	// return 22;
	// case "21":
	// return 21;
	// case "20":
	// return 20;
	// case "19":
	// return 19;
	// case "18":
	// return 18;
	// case "17":
	// return 17;
	// case "16":
	// return 16;
	// case "15":
	// return 15;
	// case "14":
	// return 14;
	// case "13":
	// return 13;
	// case "12":
	// return 12;
	// case "11":
	// return 11;
	// case "10":
	// return 10;
	// case "9":
	// return 9;
	// case "8":
	// return 8;
	// case "7":
	// return 7;
	// case "6":
	// return 6;
	// case "5":
	// return 5;
	// case "4":
	// return 4;
	// case "3":
	// return 3;
	// case "2":
	// return 2;
	// case "1":
	// return 1;
	// case ".5":
	// case "1/2":
	// return .5;
	// case ".25":
	// case "1/4":
	// return .25;
	// case ".125":
	// case "1/8":
	// return .125;
	// default:
	// return 0;
	// }
	// }
	//
	// private static void setAlignments(Monster m, String s) {
	// Scanner scan = new Scanner(s);
	// String first = scan.next();
	// if (scan.hasNext()) {
	// // has two alignments
	// m.setAlignment1(first);
	// m.setAlignment2(scan.next());
	// } else {
	// m.setAlignment1("neutral");
	// m.setAlignment2("neutral");
	// }
	// scan.close();
	// }

	public List<String> getMonsterTypes() {
		return MonsterHolderList.stream().map(x -> x.getMonster().getType()).distinct().sorted().collect(Collectors.toList());
	}

	private static int[] getXPs(int level, int numPlayers) {
		int[] returner = new int[4];
		returner[0] = xpThreshold[0][level - 1] * numPlayers;
		returner[1] = xpThreshold[1][level - 1] * numPlayers;
		returner[2] = xpThreshold[2][level - 1] * numPlayers;
		returner[3] = xpThreshold[3][level - 1] * numPlayers;
		return returner;
	}

	private boolean checkXP(List<MonsterHolder> returner, int totalXP, int tempXP, double maxXP) {
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

	public static int crToXP(double cr) {
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

	private void setTarrasques(ArrayList<MonsterHolder> returner) {
		if (returner.isEmpty()) {
			MonsterHolder tarrasque = null;
			for (MonsterHolder m : MonsterHolderList) {
				if (m.getMonster().getName().equals("Tarrasque")) {
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

	public <T> List<T> clone(List<T> list) {
		List<T> returner = new ArrayList<>();
		for (T t : list)
			returner.add(t);
		return returner;
	}

	public List<Monster> getFilteredMonsterHolderList(String minSize, String maxSize, Integer minAC, Integer maxAC,
			Integer minHP, Integer maxHP, Double minCR, Double maxCR, String isLegendary) {
		return monsterHolderListToMonsterList(MonsterHolderList).stream().filter((x) -> {
			return ((minSize == null || sizeToInt(x.getSize()) >= sizeToInt(minSize))
					&& (maxSize == null || sizeToInt(x.getSize()) <= sizeToInt(maxSize))
					&& (minAC == null || x.getAc() >= minAC) && (maxAC == null || x.getAc() <= maxAC)
					&& (minHP == null || x.getAverageHp() >= minHP) && (maxHP == null || x.getAverageHp() <= maxHP)
					&& (minCR == null || x.getCr() >= minCR) && (maxCR == null || x.getCr() <= maxCR)
					&& (isLegendary == null || isLegendary.equals("Either")
							|| x.isLegendary() == Boolean.parseBoolean(isLegendary)));
		}).collect(Collectors.toList());
	}

	public List<Monster> monsterHolderListToMonsterList(List<MonsterHolder> monsterHolderList) {
		return monsterHolderList.stream().map(x -> x.getMonster()).collect(Collectors.toList());
	}

	public List<MonsterHolder> getMonsterHolderListSortedByCount(List<MonsterHolder> MonsterHolderList) {
		return MonsterHolderList.stream().sorted(new Comparator<MonsterHolder>() {
			@Override
			public int compare(MonsterHolder o1, MonsterHolder o2) {
				return new Integer(o1.getCount()).compareTo(o2.getCount());
			}
		}).collect(Collectors.toList());
	}

}