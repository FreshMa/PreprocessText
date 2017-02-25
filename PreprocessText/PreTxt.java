package preprocessTxt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
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
	private JButton spaceButton,chapterButton,copyCss;
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
	private String filePath ;


	
	public PreTxtUI(){
		setLocation(710, 390);
		setSize(500,200);
		
		menuBar = new JMenuBar();
		openMenu = new JMenu("文件");
		aboutMenu = new JMenu("关于");
		open = new JMenuItem("打开");
		about = new JMenuItem("关于");
		labelPanel = new JPanel();
		buttonPanel = new JPanel();
		MessageLabel = new JLabel();
		spaceButton = new JButton("删除空格");
		chapterButton = new JButton("章节标记");
		copyCss = new JButton("复制css");
//按钮一开始不可点击
		spaceButton.setEnabled(false);
		chapterButton.setEnabled(false);
		
		
		openMenu.add(open);
		aboutMenu.add(about);
		menuBar.add(openMenu);
		menuBar.add(aboutMenu);
		
		labelPanel.setLayout(new GridLayout());
		buttonPanel.setLayout(new GridLayout());
		MessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelPanel.add(MessageLabel);
		buttonPanel.add(spaceButton);
		buttonPanel.add(chapterButton);
		buttonPanel.add(copyCss);
		
		setLabel("txt文档最好采用utf-8编码(记事本编辑一下即可)");
		
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
					filePath = file.getParent();
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
					String outFileDes = filePath+"/imFile.txt";
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
					setLabel("空格替换完成");
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

					String desRegexVol = "^[　]*第[0123456789零一二三四五六七八九十百千]+卷.*";
					String desRegexChap = "^[　]*第[0123456789零一二三四五六七八九十百千]+章.*";
					Pattern volPattern = Pattern.compile(desRegexVol);
					Pattern chaPattern = Pattern.compile(desRegexChap);
					
					
					String desFileName = filePath+"/new"+file.getName();
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
					setLabel("章节标记完成");
					
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("open /save destination file error");
					e2.printStackTrace();
				}
				
			}
		});
		//增加一键复制css到剪切板按钮
		copyCss.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String css = "body { margin: 0; }\r\n"+
				"p {margin-top: 0pt; margin-bottom: 0pt; padding: 0pt; text-indent: 15pt; text-align: justify;}\r\n"+
				"h1 { text-align: center; padding-top: 350px; page-break-before: always;}\r\n"+
				"h2 {text-align: center; font-weight: 700; font-size: xx-large; padding-top: 12pt; padding-bottom: 20pt; page-break-before: always;}";
				
				StringSelection cssString = new StringSelection(css);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(cssString, null);
				setLable("复制成功！");
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
