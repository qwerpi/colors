import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class colors {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java colors [filename]");
			System.exit(1);
		}
		try {
			doThatThing(args[0]);
		} catch (Exception ex) {
			System.out.println("Error!");
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void doThatThing(String filename) throws Exception {
		BufferedImage img = ImageIO.read(new File(filename));
		TreeMap<Integer, Integer> counts = new TreeMap<Integer, Integer>();
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int color = img.getRGB(i, j);
				if (!counts.containsKey(color))
					counts.put(color, 0);
				counts.put(color, counts.get(color) + 1);
			}
		}
		ArrayList<Entry<Integer, Integer>> entries = new ArrayList<Entry<Integer, Integer>>();
		entries.addAll(counts.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		//for (Entry<Integer, Integer> entry : entries) {
		//	System.out.printf("%20s - %-3d\n", colorToRGB(entry.getKey()), entry.getValue());
		//}

		class blah extends Canvas {

			int[] allColors;
			BufferedImage img;

			public blah(ArrayList<Entry<Integer, Integer>> entries) {
				int length = 0;
				for (Entry<Integer, Integer> e : entries) {
					length += e.getValue();
				}
				allColors = new int[length];
				int i = 0;
				for (Entry<Integer, Integer> e : entries) {
					for (int j = 0; j < e.getValue(); j++) {
						allColors[i++] = e.getKey();
					}
				}
				setPreferredSize(new Dimension(1000, 300));
			}

			public void paint(Graphics2D g) {
				g.scale(((double) this.getWidth()) / ((double) allColors.length), this.getHeight());
				for (int i = 0; i < allColors.length; i++) {
					g.setColor(new Color(allColors[i], true));
					g.fillRect(i, 0, 1, 1);
				}
			}

			public void paint(Graphics g) {
				paint((Graphics2D) g);
			}

			public void update(Graphics g) {
				Graphics offgc;
				Image offscreen = null;
				Dimension d = getSize();

				// create the offscreen buffer and associated Graphics
				offscreen = createImage(d.width, d.height);
				offgc = offscreen.getGraphics();
				// clear the exposed area
				offgc.setColor(getBackground());
				offgc.fillRect(0, 0, d.width, d.height);
				offgc.setColor(getForeground());
				// do normal redraw
				paint(offgc);
				// transfer offscreen to window
				g.drawImage(offscreen, 0, 0, this);
			}
		}

		JFrame frame = new JFrame("color!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new blah(entries));
		frame.pack();
		frame.setVisible(true);
	}

	public static String colorToRGB(int c) {
		int r = c & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c >> 16) & 0xFF;
		int a = (c >> 24) & 0xFF;
		return String.format("%3d (%3d, %3d, %3d)", a, r, g, b);
	}
}
