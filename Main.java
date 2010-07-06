package ecv;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {
	private JPanel result = new JPanel();
	private JComboBox choice = new JComboBox(new String[]{"Evaluation", "Infix Print", "Postfix print", "Graph"});
	private JTextArea expr = new JTextArea("", 3, 20);

	public static void main(String args[]) {
		new Main();
	}

	private Main() {
		super("ECV");
		setLayout(new FlowLayout());
		add(expr);
		JButton go = new JButton("GO"); 
		go.addActionListener(this);
		add(choice);
		add(go);
		add(result);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent arg) {
		try {
			result.removeAll();
			PropertiesContext context = new PropertiesContext ();
			JLabel jlabel = null;
			Expression expression = parse(expr.getText());
			switch(choice.getSelectedIndex()) {
			case 0:	jlabel = new JLabel("" + new EvaluatingVisitor (context).valueOf (expression));
				break;
			case 1:	StringWriter iwriter = new StringWriter();
				new InfixPrinterVisitor(iwriter).print (expression);
				jlabel = new JLabel(iwriter.toString ());
				break;
			case 2:	StringWriter pwriter = new StringWriter();
				new PostfixPrinterVisitor (pwriter).print (expression);
				jlabel = new JLabel(pwriter.toString ());
				break;
			case 3:	jlabel = new JLabel(new ImageIcon (new PaintVisitor ().createImage (expression)));
			}
			result.add(jlabel);
			pack();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Expression parse(String expression) throws IOException
	{
		ExpressionBuilder builder = new ExpressionBuilder();
		new Parser(new StreamTokenizerAdapter(new StringReader(expression)), builder).parse();
		return builder.getResult();
	}
}
