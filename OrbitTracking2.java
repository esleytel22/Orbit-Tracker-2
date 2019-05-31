//
// Java animation: resonant orbits animation and tracking
//
// Sateesh R. Mane, copyright 2018
//

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class OrbitTracking2 extends JFrame {

    public OrbitTracking2() {
    	add(new Screen());
        setResizable(false);
        pack();
        
        setTitle("Orbit Tracking");    
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        
    }
    
    public static void main(String[] args) {        
        EventQueue.invokeLater(() -> {
		JFrame ex = new OrbitTracking();
		ex.setVisible(true);
	    });
    }
}


final class Screen extends JPanel 
implements Runnable {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private Thread animator;
    
    private static final int COUNT = 10000;
    //private static final int ORBITS = 200;
    
    private final double[] x = new double[COUNT];
    private final double[] y = new double[COUNT];
    //private final Color co[] = new Color[ORBITS];

    private static final Font font = new Font("TimesRoman", Font.BOLD, 18);
    //private static final Font font = new Font("Helvetica", Font.BOLD, 18);

    //private static final double xmax = 1000;
    private int num = 0;
    private double k = -3.0;
    private double r = 1.0/k;
    private double p = 3+2*Math.random();
    private double q = 1+Math.random();


    private void update_xp() {
    	if ((num < 2) || (num > COUNT)) return;
	int i = num - 1;
	double R = r *(k-1);
	double theta = 2* Math.PI * i/1000.0;
	x[i]= R* Math.cos(theta)+r*Math.cos((k-1)*theta);
	y[i]= R*Math.sin(theta)-r*Math.sin((k-1)*theta);
    }
    
    public Screen() {
    	init();
    }
    
    private void init() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);	
	setFocusable(true);
	
	double R = r*(k-1);
	num =0;
	x[0]= R +r;
	y[0]=0;
	
	for(int i=1; i<COUNT; ++i) {
		x[i]=x[i-1];
		y[i]=y[i-1];
	}

    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        updateDisplay(g);
    }

    private void updateDisplay(Graphics g) {
    	if (num < COUNT) {
	    ++num;
	    update_xp();
	}
    	// display text
    	g.setFont(font);
    	g.setColor(Color.LIGHT_GRAY);
    	
		g.drawString("p = " + p,20,30);

		g.drawString("q = " + q,20,30);

	g.setColor(Color.YELLOW);
	
    	// draw the points

	//g.setColor(display_color);
	    for (int i = 0; i< num; ++i) {
		double xx = x[i]*HEIGHT*0.3;
		double yy = y[i]*HEIGHT*0.3;
		int ix = (int)(xx+HEIGHT*0.5);
		int iy = (int)(yy+HEIGHT*0.5);
		g.fillRect(ix, iy, 2, 2);
	    }
	    Toolkit.getDefaultToolkit().sync();
    	}		
     
    
    @Override
    public void run() {
        while (animator.interrupted() == false) {
            repaint();
        }
    }
}