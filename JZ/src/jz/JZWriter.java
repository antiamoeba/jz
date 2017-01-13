package jz;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
public class JZWriter implements ActionListener
{
	JFrame frame;
	JButton openB;
	JButton saveB;
	JButton run;
	JButton newB;
	JButton jdkB;
	JTextPane jz;
	JTextPane java;
	JEditorPane console;
	JScrollPane scrollJZ;
	JScrollPane scrollJ;
	JScrollPane scrollConsole;
	JZWriter jzw;
	JFileChooser fc;
	JFileChooser jd;
	String filename = null;
	String name;
	File choose;
	JPanel main;
	JZ jzC;
	String jdk = "C:\\Program Files\\Java\\jdk1.7.0_21";
	String consoleT;
	ArrayList<Data> whiteSpace;
	public JPanel createJZ()
	{
		main = new JPanel();
		openB = new JButton("Open");
		saveB = new JButton("Save");
		run = new JButton("Run");
		newB = new JButton("New");
		jdkB = new JButton("Set JDK Path");
		run.addActionListener(this);
		openB.addActionListener(this);
		saveB.addActionListener(this);
		newB.addActionListener(this);
		jdkB.addActionListener(this);
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(openB);
		buttons.add(saveB);
		buttons.add(run);
		buttons.add(newB);
		buttons.add(jdkB);
		JPanel jz = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		scrollJZ.setBorder(BorderFactory.createTitledBorder(" JZ "));
		scrollJ.setBorder(BorderFactory.createTitledBorder("Java"));
		scrollConsole.setBorder(BorderFactory.createTitledBorder("Console"));
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weighty = 1;
		c.weightx = 0.5;
		jz.add(scrollJZ, c);
		c.gridx = 2;
		jz.add(scrollJ, c);

		main.setLayout(new GridBagLayout());
		GridBagConstraints a = new GridBagConstraints();
		a.gridx = 0;
		a.gridy = 0;
		a.weightx = 0;
		a.weighty = 0;
		a.gridwidth = 1;
		a.anchor = GridBagConstraints.NORTH;
		a.fill = GridBagConstraints.HORIZONTAL;
		main.add(buttons, a);
		a.weightx = 1;
		a.weighty = 1;
		a.gridy = 1;
		a.fill = GridBagConstraints.BOTH;
		main.add(jz, a);
		a.gridy = 2;
		a.weighty = 0.2;
		main.add(scrollConsole, a);
		main.setOpaque(true);
		main.setBackground(Color.WHITE);
		return main;	
	}
	private static void createAndShowGUI()
	{
		JFrame lframe = new JFrame("JZWriter");
		JZWriter jz = new JZWriter();
		lframe.setContentPane(jz.createJZ());
		lframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		lframe.setVisible(true);
		jz.setFrame(lframe);
		jz.setJz(jz);
	}
	private void setFrame(JFrame lframe)
	{
		frame = lframe;
	}
	private void setJz(JZWriter jz)
	{
		jzw = jz;
	}
	public JZWriter()
	{
		jzC = new JZ();
		whiteSpace = new ArrayList<Data>();
		//AbstractDocument doc = (AbstractDocument) jz.getDocument();StyleContext sc = new StyleContext();
		StyleContext sc = new StyleContext();
		DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		DefaultStyledDocument doc1 = new DefaultStyledDocument(sc);

		jz = new JTextPane(doc);
		jz.setEditable(true);
		jz.setFocusable(true);
		doc.setDocumentFilter(new JZWriter.spaceFilter());
		Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
		final Style mainStyle = sc.addStyle("mainStyle", defaultStyle);
		StyleConstants.setFontFamily(mainStyle, "serif");
		StyleConstants.setFontSize(mainStyle, 14);
		doc.setLogicalStyle(0, mainStyle);
		java = new JTextPane(doc1);
		java.setEditable(false);
		doc1.setDocumentFilter(new JZWriter.spaceFilter());
		doc1.setLogicalStyle(0, mainStyle);
		console = new JTextPane();
		console.setEditable(false);
		scrollJZ = new JScrollPane(jz);
		scrollJ = new JScrollPane(java);
		scrollConsole = new JScrollPane(console);
		fc = new JFileChooser();
		JZFilter z = new JZFilter();
		fc.addChoosableFileFilter(z);
		fc.setFileFilter(z);
		fc.setAcceptAllFileFilterUsed(false);
		jd = new JFileChooser();
		jd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		consoleT = "";
	}
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				createAndShowGUI();
			}
		});
	}
	public void update()
	{
		String text=jz.getText();
		String[] texts = text.split("\n");
		for(int i=0;i<texts.length;i++)
		{
			String z = "";
			for(int a=0; a<texts[i].length();a++)
			{
				String s = texts[i].substring(a,a+1);
				if(s.equals("\t")||s.equals(" "))
				{
					z+=s;
				}
				else
				{
					break;
				}
			}
			whiteSpace.add(new Data(z,i));
		}
		ArrayList<String> f = jzC.interpret(text);
		String s = "";
		for(int i = 0; i<f.size();i++)
		{
			for(int a = 0; a<whiteSpace.size();a++)
			{
				if(whiteSpace.get(a).getLine()==i)
				{
					s+=whiteSpace.get(a).getS();
					whiteSpace.remove(a);
				}
			}
			s += f.get(i) + "\n";
		}
		java.setText(s);
	}
	public void console(String s)
	{
		consoleT+=s + "\n";
		console.setText(consoleT);
	}
	public void save() 
	{
		String text = jz.getText();
		if(filename !=null)
		{
			update();
			try
			{
				BufferedWriter write = new BufferedWriter(new FileWriter(filename));
				String[] jv = text.split("\n");
				for(int i = 0; i<jv.length; i++)
				{
					write.write(jv[i]+"\n");
				}
				write.close();
				frame.setTitle("JZWriter " + filename);
				console("Saving " + filename);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{	
			update();
			int returnVal = fc.showSaveDialog(main);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				choose = file;
				//filename = file.getName();
				filename = file.getAbsolutePath();
				name = file.getName();
				if(name.endsWith(".jz"))
				{
					name = name.substring(0,name.length()-3);
				}
				try
				{
					if(!filename.endsWith(".jz"))
					{
						filename += ".jz";
					}
					BufferedWriter write = new BufferedWriter(new FileWriter(filename));
					String[] jv = text.split("\n");
					for(int i = 0; i<jv.length; i++)
					{
						write.write(jv[i]+"\n");
					}
					write.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				frame.setTitle("JZWriter " + filename);
				console("Saving " + filename);
			} 
		}
		ArrayList<String> f = jzC.interpret(text);
		String s = "";
		for(int i = 0; i<f.size();i++)
		{
			s += f.get(i) + "\n";
		}
		try
		{
			String name = filename;
			if(name.endsWith(".jz"))
			{
				name = filename.substring(0, filename.length()-3);
			}
			BufferedWriter write = new BufferedWriter(new FileWriter(name + ".java"));
			String[] jv = s.split("\n");
			for(int i = 0; i<jv.length; i++)
			{
				write.write(jv[i]+"\n");
			}
			write.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == openB) 
		{
			int returnVal = fc.showOpenDialog(main);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();
				choose = file;
				filename = file.getAbsolutePath();
				name = file.getName();
				try
				{
					if(!filename.endsWith(".jz"))
					{
						filename += ".jz";
					}
					if(name.endsWith(".jz"))
					{
						name = name.substring(0,name.length()-3);
					}
					String s;
					String output = "";
					FileReader in = new FileReader(file);
					BufferedReader read = new BufferedReader(in);
					while((s=read.readLine())!=null)
					{
						if(s.length()>0)
						{
							output += s + "\n";
						}
					}
					read.close();
					jz.setText(output);
					frame.setTitle("JZWriter " + filename);
					console("Opening " + filename);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		else if (e.getSource() == saveB) 
		{
			save();
		}
		else if (e.getSource() == run)
		{
			compile();
		}
		else if(e.getSource() == newB)
		{
			newF();
		}
		else if(e.getSource() == jdkB)
		{
			int returnVal = jd.showOpenDialog(main);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File f = jd.getSelectedFile();
				try {
					jdk = f.getAbsolutePath();
				}
				catch(Exception g)
				{
					console(g.toString());
				}
			}
		}
	}
	public void compile()
	{
		try {
			System.setProperty("java.home", jdk);
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			console(System.getProperty("java.home"));
			String path = filename;
			if(filename.endsWith(".jz"))
			{
				path = filename.substring(0, filename.length()-3) + ".java";
			}
			console("Compiling " +  filename);
			OutputStream out = new ByteArrayOutputStream();
			if(compiler.run(null, null, out, path) != 0)
			{
				console(out.toString());
			}
			else
			{
				if(path.endsWith(".java"))
				{
					path = path.substring(0, path.length()-5);
				}
				try
				{
					Process pr = null;
					String[] list = {"java", name};
					ProcessBuilder pb = new ProcessBuilder(list);
					pb.directory(new File(path.substring(0,path.length()-name.length())));
					pr = pb.start();
					console("Running " + name + ":\n\n");
					BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
					String s = "";
					String line = "";
					while((line=input.readLine())!=null)
					{
						s += line + "\n";
					}
					while((line=error.readLine())!=null)
					{
						s += line + "\n";
					}
					input.close();
					console(s);
				}
				catch(Exception e)
				{
					console(e.toString());
				}
			}
		}
		catch (Exception e)
		{
			console(e.toString());
		}
	}
	public void newF()
	{
		save();
		filename = null;
		jz.setText("");
		java.setText("");
		frame.setTitle("JZWriter");
	}
	class spaceFilter extends DocumentFilter
	{
		public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
				throws BadLocationException
				{
			if ("\n".equals(str))
				str = addWhiteSpace(fb.getDocument(), offs);

			super.insertString(fb, offs, str, a);
				}

		public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a)
				throws BadLocationException
				{
			if ("\n".equals(str))
				str = addWhiteSpace(fb.getDocument(), offs);

			super.replace(fb, offs, length, str, a);
				}

		private String addWhiteSpace(Document doc, int offset)
				throws BadLocationException
				{
			StringBuilder wS = new StringBuilder("\n");
			Element rootElement = doc.getDefaultRootElement();
			int line = rootElement.getElementIndex( offset );
			int i = rootElement.getElement(line).getStartOffset();
			while (true)
			{
				String temp = doc.getText(i, 1);

				if (temp.equals(" ") || temp.equals("\t"))
				{
					wS.append(temp);
					i++;		
				}
				else
					break;
			}
			//whiteSpace.add(new Data(wS.toString(), line));
			return wS.toString();
				}

	}
}
class JZFilter extends FileFilter 
{
	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		return f.isFile()&&f.getName().endsWith(".jz");
	}

	@Override
	public String getDescription() {
		return "jz";
	}
}
class Data
{
	private String s;
	private int line;
	public Data(String s, int line)
	{
		this.s = s;
		this.line = line;
	}
	public String getS()
	{
		return s;
	}
	public int getLine()
	{
		return line;
	}
}


