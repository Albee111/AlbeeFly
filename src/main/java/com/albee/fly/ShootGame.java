

package com.albee.fly;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * Created by Albee on 2017/5/13.
 */
public class ShootGame extends JPanel {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;
	private int state;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	private int score = 0;
	private Timer timer;
	private int intervel = 10;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	private FlyingObject[] flyings = new FlyingObject[0];
	private Bullet[] bullets = new Bullet[0];
	private Hero hero = new Hero();
	int flyEnteredIndex = 0;
	int shootIndex = 0;

	static {
		try {
			background = ImageIO.read(ShootGame.class.getResource("image/backgroundAlbee.jpg"));
			start = ImageIO.read(ShootGame.class.getResource("image/start.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("image/airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("image/bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("image/bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("image/hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("image/hero1.png"));
			pause = ImageIO.read(ShootGame.class.getResource("image/pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("image/gameover.png"));
		} catch (Exception var1) {
			var1.printStackTrace();
		}

	}

	public ShootGame() {
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, (ImageObserver)null);
		this.paintHero(g);
		this.paintBullets(g);
		this.paintFlyingObjects(g);
		this.paintScore(g);
		this.paintState(g);
	}

	public void paintHero(Graphics g) {
		g.drawImage(this.hero.getImage(), this.hero.getX(), this.hero.getY(), (ImageObserver)null);
	}

	public void paintBullets(Graphics g) {
		for(int i = 0; i < this.bullets.length; ++i) {
			Bullet b = this.bullets[i];
			g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(), (ImageObserver)null);
		}

	}

	public void paintFlyingObjects(Graphics g) {
		for(int i = 0; i < this.flyings.length; ++i) {
			FlyingObject f = this.flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), (ImageObserver)null);
		}

	}

	public void paintScore(Graphics g) {
		int x = 10;
		int y = 25;
		Font font = new Font("SansSerif", 1, 22);
		g.setColor(new Color(16711680));
		g.setFont(font);
		g.drawString("SCORE:" + this.score, x, y);
		y = y + 20;
		g.drawString("LIFE:" + this.hero.getLife(), x, y);
	}

	public void paintState(Graphics g) {
		switch(this.state) {
			case 0:
				g.drawImage(start, 0, 0, (ImageObserver)null);
			case 1:
			default:
				break;
			case 2:
				g.drawImage(pause, 0, 0, (ImageObserver)null);
				break;
			case 3:
				g.drawImage(gameover, 0, 0, (ImageObserver)null);
		}

	}

	public static void main(String[] args) {
		System.out.println("交流学习，谢谢关注 知乎专栏：小仙女Albee  微信 K2Romm");

		JFrame frame = new JFrame("Albee");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(3);
		frame.setIconImage((new ImageIcon("images/icon.jpg")).getImage());
		frame.setLocationRelativeTo((Component)null);
		frame.setVisible(true);
		game.action();
	}

	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(ShootGame.this.state == 1) {
					int x = e.getX();
					int y = e.getY();
					ShootGame.this.hero.moveTo(x, y);
				}

			}

			public void mouseEntered(MouseEvent e) {
				if(ShootGame.this.state == 2) {
					ShootGame.this.state = 1;
				}

			}

			public void mouseExited(MouseEvent e) {
				if(ShootGame.this.state == 1) {
					ShootGame.this.state = 2;
				}

			}

			public void mouseClicked(MouseEvent e) {
				switch(ShootGame.this.state) {
					case 0:
						ShootGame.this.state = 1;
					case 1:
					case 2:
					default:
						break;
					case 3:
						ShootGame.this.flyings = new FlyingObject[0];
						ShootGame.this.bullets = new Bullet[0];
						ShootGame.this.hero = new Hero();
						ShootGame.this.score = 0;
						ShootGame.this.state = 0;
				}

			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			public void run() {
				if(ShootGame.this.state == 1) {
					ShootGame.this.enterAction();
					ShootGame.this.stepAction();
					ShootGame.this.shootAction();
					ShootGame.this.bangAction();
					ShootGame.this.outOfBoundsAction();
					ShootGame.this.checkGameOverAction();
				}

				ShootGame.this.repaint();
			}
		}, (long)this.intervel, (long)this.intervel);
	}

	public void enterAction() {
		++this.flyEnteredIndex;
		if(this.flyEnteredIndex % 40 == 0) {
			FlyingObject obj = nextOne();
			this.flyings = (FlyingObject[])Arrays.copyOf(this.flyings, this.flyings.length + 1);
			this.flyings[this.flyings.length - 1] = obj;
		}

	}

	public void stepAction() {
		int i;
		for(i = 0; i < this.flyings.length; ++i) {
			FlyingObject f = this.flyings[i];
			f.step();
		}

		for(i = 0; i < this.bullets.length; ++i) {
			Bullet b = this.bullets[i];
			b.step();
		}

		this.hero.step();
	}

	public void flyingStepAction() {
		for(int i = 0; i < this.flyings.length; ++i) {
			FlyingObject f = this.flyings[i];
			f.step();
		}

	}

	public void shootAction() {
		++this.shootIndex;
		if(this.shootIndex % 30 == 0) {
			Bullet[] bs = this.hero.shoot();
			this.bullets = (Bullet[])Arrays.copyOf(this.bullets, this.bullets.length + bs.length);
			System.arraycopy(bs, 0, this.bullets, this.bullets.length - bs.length, bs.length);
		}

	}

	public void bangAction() {
		for(int i = 0; i < this.bullets.length; ++i) {
			Bullet b = this.bullets[i];
			this.bang(b);
		}

	}

	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[this.flyings.length];

		for(int i = 0; i < this.flyings.length; ++i) {
			FlyingObject f = this.flyings[i];
			if(!f.outOfBounds()) {
				flyingLives[index++] = f;
			}
		}

		this.flyings = (FlyingObject[])Arrays.copyOf(flyingLives, index);
		index = 0;
		Bullet[] bulletLives = new Bullet[this.bullets.length];

		for(int i = 0; i < this.bullets.length; ++i) {
			Bullet b = this.bullets[i];
			if(!b.outOfBounds()) {
				bulletLives[index++] = b;
			}
		}

		this.bullets = (Bullet[])Arrays.copyOf(bulletLives, index);
	}

	public void checkGameOverAction() {
		if(this.isGameOver()) {
			this.state = 3;
		}

	}

	public boolean isGameOver() {
		for(int i = 0; i < this.flyings.length; ++i) {
			int index = -1;
			FlyingObject obj = this.flyings[i];
			if(this.hero.hit(obj)) {
				this.hero.subtractLife();
				this.hero.setDoubleFire(0);
				index = i;
			}

			if(index != -1) {
				FlyingObject t = this.flyings[index];
				this.flyings[index] = this.flyings[this.flyings.length - 1];
				this.flyings[this.flyings.length - 1] = t;
				this.flyings = (FlyingObject[])Arrays.copyOf(this.flyings, this.flyings.length - 1);
			}
		}

		if(this.hero.getLife() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void bang(Bullet bullet) {
		int index = -1;

		FlyingObject temp;
		for(int i = 0; i < this.flyings.length; ++i) {
			temp = this.flyings[i];
			if(temp.shootBy(bullet)) {
				index = i;
				break;
			}
		}

		if(index != -1) {
			FlyingObject one = this.flyings[index];
			temp = this.flyings[index];
			this.flyings[index] = this.flyings[this.flyings.length - 1];
			this.flyings[this.flyings.length - 1] = temp;
			this.flyings = (FlyingObject[])Arrays.copyOf(this.flyings, this.flyings.length - 1);
			if(one instanceof Enemy) {
				Enemy e = (Enemy)one;
				this.score += e.getScore();
			} else if(one instanceof Award) {
				Award a = (Award)one;
				int type = a.getType();
				switch(type) {
					case 0:
						this.hero.addDoubleFire();
						break;
					case 1:
						this.hero.addLife();
				}
			}
		}

	}

	public static FlyingObject nextOne() {
		Random random = new Random();
		int type = random.nextInt(20);
		return (FlyingObject)(type < 4?new Bee():new Airplane());
	}
}
