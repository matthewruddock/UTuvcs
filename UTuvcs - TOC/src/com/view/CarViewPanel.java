package com.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CarViewPanel extends JPanel {

	private static final long serialVersionUID = 5778800614976590100L;

	private Image backgroundImage;
	/**
	 * Create the panel.
	 */
	public CarViewPanel() {
		try {
			BufferedImage background = ImageIO.read(Dashboard.class.getResource("/img/INTERIOR.jpg"));
	        //backgroundImage = background.getScaledInstance(-1, background.getHeight() / 4, Image.SCALE_SMOOTH);
	        backgroundImage = background;
	      
		} catch (IOException ex) {
	    	  ex.printStackTrace();
	    }
		
	    setLayout(null);
	}

    @Override
    public Dimension getPreferredSize() {
    	return new Dimension(744, 391);
    }

    @Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - backgroundImage.getWidth(this)) / 2;
		int y = (getHeight() - backgroundImage.getHeight(this)) / 2;
		g.drawImage(backgroundImage, x, y, this);
    }
}
