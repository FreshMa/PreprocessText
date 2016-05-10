package preprocessTxt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class PreTxtUI extends JFrame {
	private JFileChooser fileChooser;
	private JLabel MessageLabel;
	private JButton spaceButton,chapterButton;
	private JMenuBar menuBar;
	private JMenu openMenu;
	private JMenu aboutMenu;
	private JMenuItem open;
	private JMenuItem about;
	private JPanel labelPanel;
	private JPanel buttonPanel;
	private File file;
	private File imFile;
	private File desFile;
	private String encoding = "utf-8";
	private String dirName = "D:/KPP";

	
	public PreTxtUI(){
		setLocation(710, 390);
		setSize(500,200);
		
		menuBar = new JMenuBar();
		openMenu = new JMenu("file");
		aboutMenu = new JMenu("about");
		open = new JMenuItem("open");
		about = new JMenuItem("about");
		labelPanel = new JPanel();
		buttonPanel = new JPanel();
		MessageLabel = new JLabel();
		spaceButton = new JButton("del space");
		chapterButton = new JButton("detect chapter");
//按钮一开始不可点击
		spaceButton.setEnabled(false);
		chapterButton.setEnabled(false);
		
		openMenu.add(open);
		aboutMenu.add(about);
		menuBar.add(openMenu);
		menuBar.add(aboutMenu);
		labelPanel.setLayout(new GridLayout());
		buttonPanel.setLayout(new GridLayout());
		labelPanel.add(MessageLabel);
		buttonPanel.add(spaceButton);
		buttonPanel.add(chapterButton);
		
		setLabel("CAUTION:.txt file should be saved as utf-8");
		
		this.setJMenuBar(menuBar);
		this.add(labelPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		//打开文件
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return ".txt";
					}
					
					@Override
					public boolean accept(File f) {
						// TODO Auto-generated method stub
						if(f.isDirectory()) 
							//可能文件夹里边也包含着txt文件
							return true;
						else
							return f.getName().endsWith(getDescription());
					}
				});
				fileChooser.showDialog(new JLabel(),"选择");
				file = fileChooser.getSelectedFile(); 
				//System.out.println(fileChooser.getSelectedFile().getName());
				if(file.isFile()&&file.exists()){
					setLabel(file.getPath() +" is selected!");
					//选中文件之后可以点击去除空格
					spaceButton.setEnabled(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "No file is selected!");
				}
				
				
			}
		});
		//统一空格格式，每段开头两个全角空格
		spaceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					FileInputStream in = new FileInputStream(file);
					BufferedReader bfReader = new BufferedReader(new UnicodeReader(in, Charset.defaultCharset().name()));
					
					String lineTxt = null;
					String outFileDes = "D:/imFile.txt";
					String spaceRegex = "(^[\\s|　]*)(.*)";
					Pattern spacePattern = Pattern.compile(spaceRegex);
					imFile = new File(outFileDes);
					imFile.createNewFile();
					//true，代表append方式
					BufferedWriter out= new BufferedWriter(new FileWriter(imFile,true));
					//按行读取并按行写入新文件中，fileWriter有newLine()方法，每次在新的一行上添加
					while((lineTxt = bfReader.readLine())!=null){
						Matcher spaceMatcher = spacePattern.matcher(lineTxt);
						if(spaceMatcher.find()){
							lineTxt = "　　"+spaceMatcher.group(2);
						}
						else{
							System.out.println("Not found!");
							continue;
						}
						
						out.write(lineTxt);
						out.newLine();
						out.flush();			
					}
					out.close();
					bfReader.close();
					setLabel("space replace Success");
					//去除空格之后可以加markDown标记
					chapterButton.setEnabled(true);
					
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("open/save error！");
					e2.printStackTrace();
				}
			}
		});
		//给每一卷名和章名添加markDown标记，卷用"#"标记(h1)，章用“##”标记(h2)
		chapterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					FileInputStream in2 = new FileInputStream(imFile);
					BufferedReader desBfReader = new BufferedReader(new UnicodeReader(in2, Charset.defaultCharset().name()));
					String desLine = null;

					String desRegexVol = "^[　]*第[0123456789一二三四五六七八九十百千]+卷.*";
					String desRegexChap = "^[　]*第[0123456789一二三四五六七八九十百千]+章.*";
					Pattern volPattern = Pattern.compile(desRegexVol);
					Pattern chaPattern = Pattern.compile(desRegexChap);
					
					
					String desFileName = "D:/desFile.txt";
					desFile = new File(desFileName);
					desFile.createNewFile();
					
					BufferedWriter desWriter = new BufferedWriter(new FileWriter(desFile,true));
					
					
					while((desLine = desBfReader.readLine())!=null){
						
						Matcher volMatcher = volPattern.matcher(desLine);
						Matcher chaMatcher = chaPattern.matcher(desLine);
						
						if(volMatcher.find()){
							desLine = "#"+volMatcher.group(0);
						}
						else if(chaMatcher.find()){
							desLine = "##"+chaMatcher.group(0);
						}
						desWriter.write(desLine);
						desWriter.newLine();
						desWriter.flush();
					}
					
					desWriter.close();
					desBfReader.close();
					//删除中间文件
					if(imFile.isFile()&&imFile.exists()){
						imFile.delete();
					}
					setLabel("chapter MarkDown done");
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("open /save destination file error");
					e2.printStackTrace();
				}
				
			}
		});
		
		//关于的事件
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(rootPane, "powered by kid");
			}
		});
		
	}
	
	public void setLabel(String s){
		MessageLabel.setText(s);
	}
}
