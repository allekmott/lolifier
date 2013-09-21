import javax.swing.JFrame;

/**
 * Separate GUI stuff to promote modularity
 * = LolifierFrame being JFrame instead of
 * Lolifier.
 **/
public class LolifierFrame extends JFrame {
	public LolifierFrame() {
		super("Lolifier Disk Troller");
		initGUI();
	}
	public void initGUI() {
		setSize(500, 500); // 500px x 500px
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}