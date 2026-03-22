/*******************************************************************************
 * This file is part of the Bellerophon client side application.
 * 
 * Filename: InfoPanel.java
 * Author: Eric J. Lingerfelt
 * Author Contact: eric@pandiasoftware.com
 * Copyright (c) 2009 - 2022, Oak Ridge National Laboratory
 * All rights reserved.
 *******************************************************************************/
package org.bellerophon.gui.info;

import info.clearthought.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ListIterator;

import javax.swing.*;
import org.bellerophon.data.MainData;
import org.bellerophon.gui.format.Borders;
import org.bellerophon.gui.format.Colors;
import org.bellerophon.gui.format.Hyperlinks;
import org.bellerophon.gui.format.Icons;
import org.bellerophon.gui.info.worker.GetSystemsStatusWorker;
import org.bellerophon.gui.util.WordWrapLabel;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXHyperlink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The Class InfoPanel.
 *
 * @author Eric J. Lingerfelt
 */
public class InfoPanel extends JPanel implements ActionListener{

	private Timer timer = new Timer(0, this);
	private StatusPanel eosPanel, titanPanel, rheaPanel, hpssOLCFPanel,
							coriPanel, edisonPanel, hpssNERSCPanel;
	
	/**
	 * The Enum PlatformCenter.
	 *
	 * @author Eric J. Lingerfelt
	 */
	public enum PlatformCenter {OLCF, NERSC}
	
	/**
	 * Instantiates a new info panel.
	 *
	 * @param frame the frame
	 */
	public InfoPanel(Frame frame){
	
		String urlRepoOld = MainData.TRAC_URL + "/browser/trunk/RadHyd";
		String urlRepoNew = MainData.TRAC_URL + "/browser/trunk/Chimera";
		String urlTicket = MainData.TRAC_URL + "/newticket";
		String urlWikiPub = "http://eagle.phys.utk.edu/chimerasn/trac/";
		String urlWikiPriv = MainData.TRAC_URL;
		String urlOLCF = "http://www.olcf.ornl.gov/";
		String urlNERSC = "http://www.nersc.gov/";
		String urlArchives = "https://elist.ornl.gov/mailman/private/chimera-team";
		String email = "chimera-team@elist.ornl.gov";
		
		JXHyperlink wikiPrivHyperlink = Hyperlinks.getHyperlink("View Chimera's Trac Wiki"
												, Color.black
												, urlWikiPriv
												, frame);
		
		JXHyperlink wikiPubHyperlink = Hyperlinks.getHyperlink("View Chimera's Public Wiki"
												, Color.black
												, urlWikiPub
												, frame);
		
		JXHyperlink repoOldHyperlink = Hyperlinks.getHyperlink("Browse the Old Chimera 2D Repository with Trac"
												, Color.black
												, urlRepoOld
												, frame);
		
		JXHyperlink repoNewHyperlink = Hyperlinks.getHyperlink("Browse the New Chimera Repository with Trac"
												, Color.black
												, urlRepoNew
												, frame);
		
		JXHyperlink olcfHyperlink = Hyperlinks.getHyperlink("View the OLCF Homepage"
												, Color.black
												, urlOLCF
												, frame);
		
		JXHyperlink nerscHyperlink = Hyperlinks.getHyperlink("View the NERSC Homepage"
												, Color.black
												, urlNERSC
												, frame);
		
		JXHyperlink ticketHyperlink = Hyperlinks.getHyperlink("Create a New Chimera Ticket with Trac"
												, Color.black
												, urlTicket
												, frame);
		
		JXHyperlink mailHyperlink = Hyperlinks.getMailHyperlink("Post to Chimera's Mailing List"
												, Color.black
												, email
												, frame);
		
		JXHyperlink archivesHyperlink = Hyperlinks.getHyperlink("Browse Chimera's Mailing List Archives"
												, Color.black
												, urlArchives
												, frame);

		JPanel linksPanel = new JPanel();
		linksPanel.setBorder(Borders.getBorder("Important Links"));
		double[] colLinks = {10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.PREFERRED, 10};
		double[] rowLinks = {10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL, 10};
		linksPanel.setLayout(new TableLayout(colLinks, rowLinks));
		linksPanel.add(mailHyperlink,     "1, 1, l, f");
		linksPanel.add(archivesHyperlink, "1, 3, l, f");
		linksPanel.add(ticketHyperlink,   "1, 5, l, f");
		linksPanel.add(wikiPrivHyperlink, "1, 7, l, f");
		linksPanel.add(wikiPubHyperlink,  "1, 9, l, f");
		linksPanel.add(repoOldHyperlink,  "1, 11, l, f");
		linksPanel.add(repoNewHyperlink,  "1, 13, l, f");
		linksPanel.add(olcfHyperlink,     "1, 15, l, f");
		linksPanel.add(nerscHyperlink,    "1, 17, l, f");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/mail-message-new.png")),       "3, 1, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/folder-saved-search.png")),    "3, 3, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/document-properties.png")),    "3, 5, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/wiki.png")), 					"3, 7, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/wiki.png")), 					"3, 9, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/trac.png")), 					"3, 11, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/trac.png")), 					"3, 13, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/olcf.png")), 					"3, 15, c, c");
		linksPanel.add(new JLabel(Icons.createImageIcon("/resources/images/icons/nersc.png")), 					"3, 17, c, c");
		
		eosPanel = new StatusPanel("Eos", "https://www.olcf.ornl.gov/for-users/system-user-guides/eos/", frame);
		titanPanel = new StatusPanel("Titan", "https://www.olcf.ornl.gov/for-users/system-user-guides/titan/", frame);
		hpssOLCFPanel = new StatusPanel("HPSS", "https://www.olcf.ornl.gov/olcf-resources/data-visualization-resources/hpss/", frame);
		rheaPanel = new StatusPanel("Rhea", "https://www.olcf.ornl.gov/for-users/system-user-guides/rhea/", frame);
		
		JPanel statusPanelOLCF = new JPanel();
		statusPanelOLCF.setBorder(Borders.getBorder("OLCF Systems Status"));
		double[] colOLCF = {10, TableLayoutConstants.FILL, 10};
		double[] rowOLCF = {10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL, 10};
		statusPanelOLCF.setLayout(new TableLayout(colOLCF, rowOLCF));
		statusPanelOLCF.add(titanPanel,     "1, 1, f, f");
		statusPanelOLCF.add(eosPanel,       "1, 3, f, f");
		statusPanelOLCF.add(rheaPanel,      "1, 5, f, f");
		statusPanelOLCF.add(hpssOLCFPanel,  "1, 7, f, f");
		
		coriPanel = new StatusPanel("Hopper", "https://www.nersc.gov/systems/cori/", frame);
		edisonPanel = new StatusPanel("Edison", "https://www.nersc.gov/systems/edison-cray-xc30/", frame);
		hpssNERSCPanel = new StatusPanel("HPSS", "https://www.nersc.gov/systems/hpss-data-archive/", frame);
		
		JPanel statusPanelNERSC = new JPanel();
		statusPanelNERSC.setBorder(Borders.getBorder("NERSC Systems Status"));
		double[] colNERSC = {10, TableLayoutConstants.FILL, 10};
		double[] rowNERSC = {10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL, 10};
		statusPanelNERSC.setLayout(new TableLayout(colNERSC, rowNERSC));
		statusPanelNERSC.add(coriPanel,   	"1, 1, f, f");
		statusPanelNERSC.add(edisonPanel,   	"1, 3, f, f");
		statusPanelNERSC.add(hpssNERSCPanel, "1, 5, f, f");
		
		WordWrapLabel versionLabel = new WordWrapLabel(true);
		versionLabel.setText("Version " + MainData.VERSION);
		
		WordWrapLabel teamLabel = new WordWrapLabel(true);
		teamLabel.setText("Current Development Team");
		
		WordWrapLabel authorsLabel = new WordWrapLabel(false);
		authorsLabel.setText("Eric Lingerfelt<br>Bronson Messer");
		
		WordWrapLabel contribLabel = new WordWrapLabel(true);
		contribLabel.setText("Past Contributors");
		
		WordWrapLabel pastLabel = new WordWrapLabel(false);
		pastLabel.setText("Eric Lentz<br>Reuben Budiardja<br>Sharvari Desai<br>Austin Harris<br>Chastity Holt<br>Tony Mezzacappa<br>James Osborne");
		
		WordWrapLabel toolsLabel = new WordWrapLabel(true);
		toolsLabel.setText("Bellerophon uses the following third party software products.");
		
		String urlMatplotlib = "https://wci.llnl.gov/codes/matplotlib/";
		String urlGrace = "http://plasma-gate.weizmann.ac.il/Grace/";
		String urlStat = "http://www.statsvn.org/";
		String urlTango = "http://tango.freedesktop.org/Tango_Icon_Library";
		String urlSwing = "https://swingx.dev.java.net";
		String urlChooser = "http://www.jtechlabs.com/components/jdirectorychooser";
		String urlTable = "https://tablelayout.dev.java.net";
		String urlX264 = "http://www.videolan.org/developers/x264.html";
		String urlFFmpeg = "http://www.ffmpeg.org/";
		String urlWeblaf = "http://weblookandfeel.com/";
		
		JXHyperlink matplotlibHyperlink = Hyperlinks.getHyperlink("Matplotlib Visualization Library"
												, Color.black
												, urlMatplotlib
												, frame);
		
		JXHyperlink graceHyperlink = Hyperlinks.getHyperlink("Grace Plotting Tool"
												, Color.black
												, urlGrace
												, frame);
		
		JXHyperlink statHyperlink = Hyperlinks.getHyperlink("StatSVN Code Repository Statistics Tool"
												, Color.black
												, urlStat
												, frame);
		
		JXHyperlink tangoHyperlink = Hyperlinks.getHyperlink("Tango Icon Library"
												, Color.black
												, urlTango
												, frame);
		
		JXHyperlink swingHyperlink = Hyperlinks.getHyperlink("SwingX GUI Extensions"
												, Color.black
												, urlSwing
												, frame);
		
		JXHyperlink chooserHyperlink = Hyperlinks.getHyperlink("JTechLabs JDirectoryChooser Java Bean"
												, Color.black
												, urlChooser
												, frame);
		
		JXHyperlink tableHyperlink = Hyperlinks.getHyperlink("TableLayout GUI Layout Manager"
												, Color.black
												, urlTable
												, frame);
		
		JXHyperlink x264Hyperlink = Hyperlinks.getHyperlink("VideoLAN X264 Library"
												, Color.black
												, urlX264
												, frame);
		
		JXHyperlink ffmpegHyperlink = Hyperlinks.getHyperlink("FFMPEG Video Encoder"
												, Color.black
												, urlFFmpeg
												, frame);
		
		JXHyperlink weblafHyperlink = Hyperlinks.getHyperlink("Web Look and Feel"
												, Color.black
												, urlWeblaf
												, frame);

		int gap = 10;
		int smallGap = 6;
		JPanel aboutPanel = new JPanel();
		aboutPanel.setBorder(Borders.getBorder("About Bellerophon"));
		double[] colAbout = {10, TableLayoutConstants.FILL, 10};
		double[] rowAbout = {10, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, gap, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, gap, TableLayoutConstants.PREFERRED
							, 20, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED
							, smallGap, TableLayoutConstants.PREFERRED, 10};
		aboutPanel.setLayout(new TableLayout(colAbout, rowAbout));
		aboutPanel.add(versionLabel,       "1, 1, l, c");
		aboutPanel.add(teamLabel,          "1, 3, l, c");
		aboutPanel.add(authorsLabel,       "1, 5, f, c");
		aboutPanel.add(contribLabel,       "1, 7, l, c");
		aboutPanel.add(pastLabel,          "1, 9, f, c");
		aboutPanel.add(toolsLabel,         "1, 11, l, c");
		aboutPanel.add(matplotlibHyperlink,"1, 13, l, c");
		aboutPanel.add(graceHyperlink,     "1, 15, l, c");
		aboutPanel.add(statHyperlink,      "1, 17, l, c");
		aboutPanel.add(tangoHyperlink,     "1, 19, l, c");
		aboutPanel.add(swingHyperlink,     "1, 21, l, c");
		aboutPanel.add(chooserHyperlink,   "1, 23, l, c");
		aboutPanel.add(tableHyperlink,     "1, 25, l, c");
		aboutPanel.add(x264Hyperlink,      "1, 27, l, c");
		aboutPanel.add(ffmpegHyperlink,    "1, 29, l, c");
		aboutPanel.add(weblafHyperlink,    "1, 31, l, c");
		
		double[] col = {10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL
							, 10, TableLayoutConstants.FILL, 10};
		double[] row = {10, 0.57
							, 10, 0.43
							, 10};
		setLayout(new TableLayout(col, row));
		add(linksPanel,			"1, 1, 1, 3, f, f");
		add(statusPanelOLCF, 	"3, 1, f, f");
		add(statusPanelNERSC, 	"3, 3, f, f");
		add(aboutPanel, 			"5, 1, 5, 3, f, f");
	
	}
	
	/**
	 * Sets the current state.
	 */
	public void setCurrentState(){
		timer.setDelay(60000);
		timer.start();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==timer){
			eosPanel.setWaitingState();
			titanPanel.setWaitingState();
			hpssOLCFPanel.setWaitingState();
			rheaPanel.setWaitingState();
			
			edisonPanel.setWaitingState();
			coriPanel.setWaitingState();
			hpssNERSCPanel.setWaitingState();
			
			GetSystemsStatusWorker workerOLCF = new GetSystemsStatusWorker(this, PlatformCenter.OLCF);
			workerOLCF.execute();
			
			GetSystemsStatusWorker workerNERSC = new GetSystemsStatusWorker(this, PlatformCenter.NERSC);
			workerNERSC.execute();
		}
	}
	
	/**
	 * Process status.
	 *
	 * @param center the center
	 * @param contents the contents
	 */
	public void processStatus(PlatformCenter center, String contents){
		
		if(center==PlatformCenter.OLCF){
			
			boolean eosFlag = false;
			boolean titanFlag = false;
			boolean hpssOLCFFlag = false;
			boolean rheaFlag = false;
			
			Document doc = Jsoup.parse(contents);
			Elements statusElements = doc.getElementsByClass("system-status");
			ListIterator<Element> itr = statusElements.listIterator();
			while(itr.hasNext()){
				Element e = itr.next();
				String html = e.html();
				if(html.contains("Titan") && html.contains("status-up")){
					titanFlag = true;
				}
				if(html.contains("Eos") && html.contains("status-up")){
					eosFlag = true;
				}
				if(html.contains("Rhea") && html.contains("status-up")){
					rheaFlag = true;
				}
				if(html.contains("HPSS") && html.contains("status-up")){
					hpssOLCFFlag = true;
				}
			}
			
			eosPanel.setCurrentState(eosFlag);
			titanPanel.setCurrentState(titanFlag);
			hpssOLCFPanel.setCurrentState(hpssOLCFFlag);
			rheaPanel.setCurrentState(rheaFlag);
			
		}else if(center==PlatformCenter.NERSC){
			
			String[] array = contents.split("\n");
			
			String coriString = "<b>Cori</b>";
			String edisonString = "<b>Edison</b>";
			String hpssNERSCString = "<b>HPSS Regent (Backup)</b>";
			
			boolean edisonFlag = false;
			boolean coriFlag = false;
			boolean hpssNERSCFlag = false;
			
			for(int i=0; i<array.length; i++){
				String line = array[i];
				if(line.indexOf(coriString)!=-1 && line.indexOf("Up")!=-1){
					coriFlag = true;
				}
				if(line.indexOf(edisonString)!=-1 && line.indexOf("Up")!=-1){
					edisonFlag = true;
				}
				if(line.indexOf(hpssNERSCString)!=-1 && line.indexOf("Up")!=-1){
					hpssNERSCFlag = true;
				}
			}
			
			edisonPanel.setCurrentState(edisonFlag);
			coriPanel.setCurrentState(coriFlag);
			hpssNERSCPanel.setCurrentState(hpssNERSCFlag);
			
		}
		
	}
	
}

class StatusPanel extends JPanel{
	
	private JXHyperlink mainLink;
	private JLabel iconLabel;
	private JXBusyLabel waitLabel;
	
	public StatusPanel(String systemString, String url, Frame frame){
		
		mainLink = Hyperlinks.getHyperlink(systemString, Color.black, url, frame);
		iconLabel = new JLabel();
		
		waitLabel = new JXBusyLabel(new Dimension(50, 51));
		waitLabel.getBusyPainter().setHighlightColor(Colors.frontColor); 
		waitLabel.getBusyPainter().setBaseColor(Colors.backColor); 
		waitLabel.getBusyPainter().setPoints(12);
		waitLabel.getBusyPainter().setTrailLength(15);
		waitLabel.setDelay(65);
		waitLabel.setBusy(true); 
		
		double[] col = {TableLayoutConstants.FILL
						, 10, TableLayoutConstants.FILL};
		double[] row = {TableLayoutConstants.FILL};
		setLayout(new TableLayout(col, row));
		add(mainLink, "0, 0, f, f");
		add(iconLabel, "2, 0, f, f");
		
	}
	
	public void setWaitingState(){
		remove(iconLabel);
		add(waitLabel, "2, 0, f, f");
		repaint();
	}
	
	public void setCurrentState(boolean state){
		remove(waitLabel);
		if(state){
			iconLabel.setIcon(Icons.createImageIcon("/resources/images/icons/up.jpg"));
		}else{
			iconLabel.setIcon(Icons.createImageIcon("/resources/images/icons/down.jpg"));
		}
		add(iconLabel, "2, 0, f, f");
		repaint();
	}
}