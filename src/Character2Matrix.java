import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Simple Character to matrix tool
 * @author Aladi
 * @date 2017-06-12 18:11:04
 */
public class Character2Matrix {
	
	private BufferedImage bImage;
	private Graphics2D graphics;
	private final int SIZE;
	private final int BUFFER_LEN;

	public Character2Matrix(int size, Font font) {
		this.SIZE = size;
		BUFFER_LEN = (int) (size * 3.8 + 2 - 1);
		bImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		graphics = bImage.createGraphics();
		graphics.setColor(new Color(255, 255, 255));
		if (font == null) {
			font = new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, size+1);
		}
		graphics.setBackground(Color.BLACK);
		graphics.setFont(font);
	}
	
	public Character2Matrix(int size){
		this(size,null);
	}

	public Character2Matrix() {
		this(8, null);
	}

	public void setFont(Font font) {
		if (font == null) {
			throw new NullPointerException("font");
		}
		graphics.setFont(font);
	}

	/**
	 * convent first Character to integer array
	 * @param s
	 * @return
	 */
	public int[] convent(String s) {
		graphics.clearRect(0, 0, SIZE, SIZE);
		int[] text = new int[SIZE];
		if (s == null) {
			return text;
		}
		graphics.drawString(s, 0, SIZE - (SIZE/8));
		for (int x = 0; x < SIZE; x++) {
			int data = 0x0;
			int currentBit = 0x1;
			for (int y = 7; y >= 0; y--) {
				if (bImage.getRGB(y, x) > -16777216) {
					data += currentBit;
				}
				currentBit = currentBit << 1;
			}
			text[x] = data;
		}
		return text;
	}

	/**
	 * convent first Character to LUA table
	 * @param s
	 * @return
	 */
	public String convent2LuaTable(String s) {
		int[] text = convent(s);
		StringBuilder sbd = new StringBuilder(BUFFER_LEN);
		sbd.append("{");
		for (int i = 0; i < text.length; i++) {
			sbd.append(text[i]);
			if (i < text.length - 1) {
				sbd.append(',');
			}
		}
		sbd.append("}");
		return sbd.toString();
	}

	public void saveToDisk(File file) throws FileNotFoundException, IOException {
		if (file == null) {
			throw new NullPointerException("file");
		}
		
		if (file.exists()){
			file.createNewFile();
		}
		
		ImageIO.write(bImage, "PNG", new FileOutputStream(file));
	}

	public static void main(String[] args) {
		Character2Matrix f2m = new Character2Matrix(8);
		System.out.println(f2m.convent2LuaTable("Éú"));
		try {
			f2m.saveToDisk(new File("g:/dd.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(f2m.convent2LuaTable("ÎÒ"));

		System.out.println(f2m.convent2LuaTable("Ëû"));
	}

}
