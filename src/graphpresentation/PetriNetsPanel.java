/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import graphnet.GraphPetriNet;
import graphnet.GraphPetriPlace;
import graphnet.GraphPetriTransition;
import graphnet.GraphArcIn;
import graphnet.GraphArcOut;

/**
 * Creates new form PetriNetsPanel
 *
 * @author Ольга
 */
public class PetriNetsPanel extends javax.swing.JPanel {

    /**
     * Creates new form PetriNetsPanel
     */
    private static int id; // нумерація графічних елементів
    private GraphPetriNet graphNet;  //added 4.12.2012
    private List<GraphPetriNet> graphNetList = new ArrayList<>();  // для відображення кількох мереж  09.01.13
    private boolean isSettingArc;
    private GraphElement current;
    private GraphElement choosen;
    private GraphArc currentArc;
    private GraphArc choosenArc;
    private int savedId;
    public SetArc setArcFrame = new SetArc(this);
    public SetPosition setPositionFrame = new SetPosition(this);
    public SetTransition setTransitionFrame = new SetTransition(this);
   // private Point currentPlacementPoint; // поточна точка на панелі, вибрана користувачем  09.01.13
    private JTextField nameTextField;
  //  private final String COPY_NAME = "_copy";
    private final String DEFAULT_NAME = "Untitled";
  //  private AffineTransform at = new AffineTransform();
    private Point prevMouseLocation;
    private Point startDragMouseLocation = null;
    private Point currentDragMouseLocation = null;
    private List<GraphElement> choosenElements = new ArrayList<>();
	private double scale = 1.0;
	private boolean leftMouseButtonPressed = false;

	public List<GraphElement> getChoosenElements() {
		return choosenElements;
	}
	
    public PetriNetsPanel(JTextField textField) {

        initComponents();
        this.setBackground(Color.WHITE);
        
        
        nameTextField = textField;
        this.setNullPanel(); // починаємо заново створювати усі списки графічних елементів  //додано 3.12.2012
        setFocusable(true);
        
    

        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
        addMouseWheelListener(new MouseWheelHendler());
       

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               /* System.out.println("keyPressed:  e "+e.getKeyCode());
                if(choosen!=null)System.out.println("keyPressed: choosen "+choosen.getName());
                    else  System.out.println("keyPressed:  choosen null");
                if(choosenArc!=null)System.out.println("keyPressed: choosenArc"+choosenArc.getQuantity());
                    else  System.out.println("keyPressed:  choosenArc null");*/
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (choosenArc != null) {
                        removeArc(choosenArc);
                        choosenArc = null;
                        currentArc = null;
                    }
                    if (choosen != null) {
                        try {
                            remove(choosen);
                            choosen = null;
                            current = null;
                        } catch (ExceptionInvalidNetStructure ex) {
                            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(!choosenElements.isEmpty()){
                    	int result = JOptionPane.showConfirmDialog((Component) null, "Are you sure you want to delete selected elements?",
                    	        "Delete", JOptionPane.OK_CANCEL_OPTION);
                    	if(result == JOptionPane.OK_OPTION){
                    		try {
								for (GraphElement graphElement : choosenElements) {
									remove(graphElement);
								}
							} catch (ExceptionInvalidNetStructure ex) {
								Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
							} finally {
								choosenElements.clear();
							}
                    	}
                    }
                }
                
                //if Ctrl + A pressed
    			if (e.isControlDown() && e.getKeyCode() == 65) {
    		        selectAll();
    		        repaint();
    			}
            }
        });

    }

    private void removeArc(GraphArc s) {
        if (s == null) {
            return;
        }
        if (s == currentArc) {
            currentArc = null;
        }

        if (s.getClass().equals(GraphArcOut.class)) {
            graphNet.getGraphArcOutList().remove((GraphArcOut) s); //added by Inna 4.12.2012

        } else {
            graphNet.getGraphArcInList().remove((GraphArcIn) s); //added by Inna 4.12.2012
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        this.requestFocusInWindow(); //added 1.06.2013
        //додано 3.12.2012
        if (graphNet == null) {
            graphNet = new GraphPetriNet();
        }
        //тобто на початку роботи встановлюється графічна мережа з порожніми списками графічних елементів та порожньою мережею Петрі!!!
/*        if (currentPlacementPoint != null) {
            paintCurrentPlacementPoint(g2);
        }*/
        graphNet.paintGraphPetriNet(g2, g);
        // промальовуємо всі мережі
        
 
        for (GraphPetriNet pnet : graphNetList) {
            
            if (pnet != graphNet) {
                pnet.paintGraphPetriNet(g2, g);
            }
        }
        if (currentArc != null) {
            g2.setColor(Color.BLUE);
            g.setColor(Color.BLUE);
            currentArc.drawGraphElement(g2);
        }
        if (choosenArc != null) {
            g2.setColor(Color.BLUE);
            g.setColor(Color.BLUE);
            choosenArc.drawGraphElement(g2);
        }
        if (current != null) {
            g2.setColor(Color.BLUE);
            current.drawGraphElement(g2);
        }
        if (choosen != null) {
            g2.setColor(Color.BLUE);
            choosen.drawGraphElement(g2);
        } 
        for (GraphElement graphElement : choosenElements) {
        	g2.setColor(Color.BLUE);
        	graphElement.drawGraphElement(g2);
		}
        g2.setColor(Color.BLACK);
        //12.01.16
       
     //   this.transform(g2);
        
        if(currentDragMouseLocation != null && startDragMouseLocation != null && leftMouseButtonPressed){
			g2.setStroke((Stroke) new BasicStroke(1.0f,                      
                    BasicStroke.CAP_ROUND,    
                    BasicStroke.JOIN_BEVEL,    
                    20.0f,                     
                    new float[] {15.0f,15.0f}, 
                    0.0f));
        	g2.drawRect(startDragMouseLocation.x, startDragMouseLocation.y,
					currentDragMouseLocation.x-startDragMouseLocation.x, 
					currentDragMouseLocation.y-startDragMouseLocation.y);
        }
    }

    public GraphElement find(Point2D p) {
        for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {
            if (pp.isGraphElement(p)) {
                return pp;
            }
        }
        for (GraphPetriTransition pt : graphNet.getGraphPetriTransitionList()) {
            if (pt.isGraphElement(p)) {
                return pt;
            }
        }
        // 11.01.13
        // якщо є декілька мереж, то ведеться пошук по всім мережам і встановлюється Поточна мережа та, в якій буде знайдено елемент
        for (GraphPetriNet pnet : graphNetList) {
            for (GraphPetriPlace pp : pnet.getGraphPetriPlaceList()) {
                if (pp.isGraphElement(p)) {
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return pp;

                }
            }
            for (GraphPetriTransition pt : pnet.getGraphPetriTransitionList()) {
                if (pt.isGraphElement(p)) {
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return pt;
                }
            }
        }
        return null;
    }

    public GraphArc findArc(Point2D p) {
        for (GraphArcOut to : graphNet.getGraphArcOutList()) {
            if (to.isEnoughDistance(p)) {
                return to;
            }
        }
        for (GraphArcIn ti : graphNet.getGraphArcInList()) {
            if (ti.isEnoughDistance(p)) {
                return ti;
            }
        }
        for (GraphPetriNet pnet : graphNetList) {
            for (GraphArcOut to : pnet.getGraphArcOutList()) {
                if (to.isEnoughDistance(p)) {
                   // System.out.println("Current element is from  net = " + pnet.getPetriNet().getName());
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                      /*  if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return to;
                }
            }
            for (GraphArcIn ti : pnet.getGraphArcInList()) {
                if (ti.isEnoughDistance(p)) {
                   // System.out.println("Current element is from  net = " + pnet.getPetriNet().getName());
                    graphNet = pnet;
                    if (pnet.getPetriNet() != null) {
                        String pnetName = graphNet.getPetriNet().getName();
                    /*    if (pnetName.contains(COPY_NAME)) {
                            pnetName = pnetName.substring(0, pnet.getPetriNet().getName().length() - COPY_NAME.length());
                        }*/
                        nameTextField.setText(pnetName);
                    } else {
                        nameTextField.setText(DEFAULT_NAME);
                    }
                    return ti;
                }
            }
        }
        return null;
    }

    public void remove(GraphElement s) throws ExceptionInvalidNetStructure {
        if (s == null) {
            return;
        }
        if (s == current) {
            current = null;

        }
       /* if(current!=null)System.out.println("remove : "+current.getName()+"  "+s.getName());
        else System.out.println("remove : current null");*/
        graphNet.delGraphElement(s); //added by Inna 4.12.2012

        repaint();
    }

    public class MouseWheelHendler implements MouseWheelListener{

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getWheelRotation()==-1 && scale <=0.15)return;
			scale+=(double)e.getWheelRotation()/10;
			repaint();
		}
    	
    }
    
    public void selectAll(){
    	choosenElements.clear();
    	for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {
    		choosenElements.add(pp);
		}
		for (GraphPetriTransition pt : graphNet
				.getGraphPetriTransitionList()) {
			choosenElements.add(pt);
		}
    }
    
    public class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent ev) {
        	Point scaledCurrentMousePoint = new Point((int)(ev.getX()/scale),(int)(ev.getY()/scale));
        	if(SwingUtilities.isLeftMouseButton(ev))leftMouseButtonPressed = true;
        	if(startDragMouseLocation == null){
        		startDragMouseLocation = scaledCurrentMousePoint;
        	}
        	prevMouseLocation = scaledCurrentMousePoint;
            if (current != null) {
                current = null;
                repaint();
            } else {
                current = find(scaledCurrentMousePoint);
                if (current != null) {
                    current.setNewCoordinates(scaledCurrentMousePoint);
                    setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                    choosen = current;
                    
                    for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                    	ti.updateCoordinates();
                    }
                    
                    for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                    	to.updateCoordinates();
                    }
                    //Це більше не потрібно, код наведений вище працює краще
                    /*String currentType = current.getType(); // added by Katya 23.10.2016
                    int currentId = current.getId();
                    Point2D currentCenter = current.getGraphElementCenter();
                    
                    for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                        if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                            to.movingBeginElement(currentCenter);
                            to.changeBorder();
                        }
                        if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                            to.movingEndElement(currentCenter);
                            to.changeBorder();
                        }
                    }
                    for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                        if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                            ti.movingBeginElement(currentCenter);
                            ti.changeBorder();
                        }
                        if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                            ti.movingEndElement(currentCenter);
                            ti.changeBorder();
                        }
                    }*/
                    choosenArc = null;
                }
               // currentPlacementPoint = e.getPoint();
            }

            if (isSettingArc == true) {
                current = find(scaledCurrentMousePoint);
                if (current != null) {
                    if (current.getClass().equals(GraphPetriPlace.class)) {
                        currentArc = new GraphArcIn();
                        graphNet.getGraphArcInList().add((GraphArcIn) currentArc); //3.12.2012
                        currentArc.settingNewArc(current); //set begin element, point and setting LINe(0,0)
                    } else if (current.getClass().equals(GraphPetriTransition.class)) { //26.01.2013
                        currentArc = new GraphArcOut();
                        graphNet.getGraphArcOutList().add((GraphArcOut) currentArc); //3.12.2012
                        currentArc.settingNewArc(current);
                    }
                } else {    //26.01.2013
                    isSettingArc = false;
                }
                // System.out.println("after added tie we have such graph net:");
                // graphNet.print();
            }
            isSettingArc = false;//26.01.2013
            choosenArc = null;

            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent ev) {
        	Point scaledCurrentMousePoint = new Point((int)(ev.getX()/scale),(int)(ev.getY()/scale));
        	if (current == null && currentArc == null) {
        		choosenElements.clear();
        		choosen = null;
        	}
        	if (current != null) {           
                current.setNewCoordinates(scaledCurrentMousePoint);
            } else {
          
                current = find(scaledCurrentMousePoint);
                if(current != null)choosen = current;
                if (current != null && ev.getClickCount() >= 2) { //change 2->1??
                	choosen = current;
                	
                	for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                    	ti.updateCoordinates();
                    }
                    
                    for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                    	to.updateCoordinates();
                    }
                    //Це більше не потрібно, код наведений вище працює краще
                	/*String currentType = current.getType(); // added by Katya 23.10.2016
                    int currentId = current.getId();
                    Point2D currentCenter = current.getGraphElementCenter();
                    
                    for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                        if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                            ti.movingBeginElement(currentCenter);
                            ti.changeBorder();
                            break;
                        }
                        if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                            ti.movingEndElement(currentCenter);
                            ti.changeBorder();
                            break;
                        }
                    }
                    for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                        if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                            to.movingBeginElement(currentCenter);
                            to.changeBorder();
                            break;
                        }
                        if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                            to.movingEndElement(currentCenter);
                            to.changeBorder();
                            break;
                        }
                    }*/

                    if (choosen.getClass().equals(GraphPetriPlace.class)) {
                        setPositionFrame.setVisible(true);
                        setPositionFrame.setInfo(choosen);

                    } else {
                        setTransitionFrame.setVisible(true);
                        setTransitionFrame.setInfo(choosen);
                    }
                }

                currentArc = findArc(scaledCurrentMousePoint);
                if (currentArc != null && ev.getClickCount() >= 2) {
                    choosenArc = currentArc;
                    setArcFrame.setVisible(true);
                    setArcFrame.setInfo(choosenArc);
                }
                if (currentArc != null) {
                    choosenArc = currentArc;
                    choosen = null;
                    currentArc = null;
                }
            }
            current = null;

            setCursor(Cursor.getDefaultCursor());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent ev) {
        	Point scaledCurrentMousePoint = new Point((int)(ev.getX()/scale),(int)(ev.getY()/scale));
        	if (startDragMouseLocation!=null&&currentDragMouseLocation!=null && leftMouseButtonPressed) {
				for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {
					if (pp.getGraphElementCenter().getX() >= startDragMouseLocation.x
							&& pp.getGraphElementCenter().getX() <= currentDragMouseLocation
									.getX()
							&& pp.getGraphElementCenter().getY() >= startDragMouseLocation.y
							&& pp.getGraphElementCenter().getY() <= currentDragMouseLocation
									.getY()) {
						choosenElements.add(pp);
					}
				}
				for (GraphPetriTransition pt : graphNet
						.getGraphPetriTransitionList()) {
					if (pt.getGraphElementCenter().getX() >= startDragMouseLocation.x
							&& pt.getGraphElementCenter().getX() <= currentDragMouseLocation
									.getX()
							&& pt.getGraphElementCenter().getY() >= startDragMouseLocation.y
							&& pt.getGraphElementCenter().getY() <= currentDragMouseLocation
									.getY()) {
						choosenElements.add(pt);
					}
				}
			}
			startDragMouseLocation = null;
        	currentDragMouseLocation = null;
            current = null;
            setCursor(Cursor.getDefaultCursor());
            if (currentArc != null) {
                current = find(scaledCurrentMousePoint);
                if (current != null) {
                    if (currentArc.finishSettingNewArc(current)) {
                        currentArc.setPetriElements();
                        currentArc.changeBorder();
                        currentArc.updateCoordinates();
                        isSettingArc = false;
                        
                        int currBeginId, currEndId;
                        if (currentArc.getClass().equals(GraphArcIn.class)) {
                            currBeginId = ((GraphPetriPlace)currentArc.getBeginElement()).getId();
                            currEndId = ((GraphPetriTransition)currentArc.getEndElement()).getId();
                        } else {
                            currBeginId = ((GraphPetriTransition)currentArc.getBeginElement()).getId();
                            currEndId = ((GraphPetriPlace)currentArc.getEndElement()).getId();
                        }

                        for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                            if (((GraphPetriPlace)ti.getBeginElement()).getId() == currEndId && ((GraphPetriTransition)ti.getEndElement()).getId() == currBeginId) {
                                currentArc.twoArcs(ti);
                                currentArc.updateCoordinates();
                            }
                        }
                        for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                            if (((GraphPetriTransition)to.getBeginElement()).getId() == currEndId && ((GraphPetriPlace)to.getEndElement()).getId() == currBeginId) {
                                currentArc.twoArcs(to);
                                currentArc.updateCoordinates();
                            }
                        }
                        currentArc = null;
                    } else {                        //1.02.2013 цей фрагмент дозволяє відслідковувати намагання 
                        removeCurrentArc();// з"єднати позицію з позицією чи перехід з переходом
                        //та знищувати неправильно намальовану дугу
                    }

                    current = null;
                } else {
                    removeCurrentArc();//1.02.2013;
                }
            }
            currentArc = null;
            leftMouseButtonPressed = false;
            repaint();
        }
    }

    private void removeCurrentArc() { //1.02.2013 цей метод дозволяє знищувати намальовану дугу
        if (currentArc.getClass().equals(GraphArcIn.class)) // 
        {
            graphNet.getGraphArcInList().remove(currentArc);
        } else if (currentArc.getClass().equals(GraphArcOut.class)) {
            graphNet.getGraphArcOutList().remove(currentArc);
        } else ;
        currentArc = null;
        repaint();
    }

    private class MouseMotionHandler implements MouseMotionListener {
    	
        @Override
        public void mouseDragged(MouseEvent ev) {
        	Point scaledCurrentMousePoint = new Point((int)(ev.getX()/scale),(int)(ev.getY()/scale));
        	if(choosen==null && choosenElements.isEmpty())currentDragMouseLocation = scaledCurrentMousePoint;
        	if (current != null && currentArc == null) {  //пересування позиції чи переходу
                // currentPlacementPoint = null;
                current.setNewCoordinates(scaledCurrentMousePoint);
                
                for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                	ti.updateCoordinates();
                }
                
                for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                	to.updateCoordinates();
                }
                //Це більше не потрібно, код наведений вище працює краще
                /*String currentType = current.getType(); // added by Katya 23.10.2016
                int currentId = current.getId();
                Point ePoint = e.getPoint();
                
                for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                    if (((GraphPetriPlace)ti.getBeginElement()).getId() == currentId && ti.getBeginElement().getType().equals(currentType)) {
                        ti.movingBeginElement(ePoint);
                        ti.changeBorder();
                    }
                    if (((GraphPetriTransition)ti.getEndElement()).getId() == currentId && ti.getEndElement().getType().equals(currentType)) {
                        ti.movingEndElement(ePoint);
                        ti.changeBorder();
                    }
                }
                for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                    if (((GraphPetriTransition)to.getBeginElement()).getId() == currentId && to.getBeginElement().getType().equals(currentType)) {
                        to.movingBeginElement(ePoint);
                        to.changeBorder();
                    }
                    if (((GraphPetriPlace)to.getEndElement()).getId() == currentId && to.getEndElement().getType().equals(currentType)) {
                        to.movingEndElement(ePoint);
                        to.changeBorder();
                    }
                }*/
            }
            // коли малюємо дугу
            if (currentArc != null && current != null) {
                // System.out.println("Setting new coordinates for tie");
                currentArc.setNewCoordinates(scaledCurrentMousePoint);
            }
            // коли рухаємо виділені елементи
            if (!choosenElements.isEmpty() && leftMouseButtonPressed) {
            	setCursor(new Cursor(Cursor.MOVE_CURSOR));
            	for (GraphElement graphElement : choosenElements) {
					Point currentLocation = new Point((int) graphElement
							.getGraphElementCenter().getX(), (int) graphElement
							.getGraphElementCenter().getY());
					Point newLocation = new Point(currentLocation.x
							+ (int)scaledCurrentMousePoint.getX()
							- prevMouseLocation.x, currentLocation.y
							+ (int)scaledCurrentMousePoint.getY()
							- prevMouseLocation.y);
            		graphElement.setNewCoordinates(newLocation);
				}
            	for (GraphArcIn ti : graphNet.getGraphArcInList()) {
                	ti.updateCoordinates();
                }
                
                for (GraphArcOut to : graphNet.getGraphArcOutList()) {
                	to.updateCoordinates();
                }
            	prevMouseLocation = scaledCurrentMousePoint;
            }
            if (startDragMouseLocation != null && SwingUtilities.isRightMouseButton(ev)) {
                JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, PetriNetsPanel.this);
                if (viewPort != null) {
                    int deltaX = (int) ((startDragMouseLocation.x - scaledCurrentMousePoint.getX())*scale);
                    int deltaY = (int) ((startDragMouseLocation.y - scaledCurrentMousePoint.getY())*scale);

                    Rectangle view = viewPort.getViewRect();
                    view.x += deltaX;
                    view.y += deltaY;

                    PetriNetsPanel.this.scrollRectToVisible(view);
                }
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent ev) {
        	Point scaledCurrentMousePoint = new Point((int)(ev.getX()/scale),(int)(ev.getY()/scale));
            if (current != null && currentArc == null) {
               // currentPlacementPoint = null;
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                current.setNewCoordinates(scaledCurrentMousePoint);
                repaint();
            }
        }
    }

    public GraphElement getCurrent() {
        return current;
    }

    public void setCurrent(GraphElement e) {
        current = e;
    }

    public GraphElement getChoosen() {
        return choosen;
    }

    public void setCurrentGraphArc(GraphArc t) {
        currentArc = t;
    }

    public GraphArc getCurrentGraphArc() {
        return currentArc;
    }

    public GraphArc getChoosenArc() {
        return choosenArc;
    }

    public int getSavedId() {
        return savedId;
    }

    public void saveId() {
        this.savedId = id;
    }

    public static String getPetriTName() {
        return "T" + id;
    }

    public static String getPetriPName() {
        return "P" + id;
    }

    public void setIsSettingArc(boolean b) { //26.01.2013
        isSettingArc = b;
    }

    public final void setNullPanel() {
        current = null;
        currentArc = null;
        choosen = null;
        choosenArc = null;
        id = 0;
        PetriP.initNext(); //ось тут і обнуляється, а я шукаю...
        PetriT.initNext(); //навіть коли читаємо з файлу...
        ArcIn.initNext(); //додано Інна 20.11.2012
        ArcOut.initNext(); //додано Інна 20.11.2012
        GraphPetriPlace.setNullSimpleName();
        GraphPetriTransition.setNullSimpleName();
        graphNetList = new ArrayList<>(); // 15.01.13
        graphNet = new GraphPetriNet();
        repaint();
    }

    public void addGraphNet(GraphPetriNet net) {  //Тепер написаний цей метод... //можливо достатньо скористатись setNet???

        // graphNet = net; //4.12.2012  createCopy() НЕ працює...
        //02.02.2012

        graphNetList.add(graphNet);
        graphNetList.add(net);
        graphNet = net;

        int maxIdPetriNet = 0; //
        for (GraphPetriPlace pp : graphNet.getGraphPetriPlaceList()) {  //відшукуємо найбільший id для позицій
            if (maxIdPetriNet < pp.getId()) {
                maxIdPetriNet = pp.getId();
            }
        }
        for (GraphPetriTransition pt : graphNet.getGraphPetriTransitionList()) { //відшукуємо найбільший id для переходів і позицій 
            if (maxIdPetriNet < pt.getId()) {
                maxIdPetriNet = pt.getId();
            }
        }
        if (maxIdPetriNet > id) // встановлюємо новий id - найбільший
        {
            id = maxIdPetriNet;
        }
        id++;
        // graphNetList.add(graphNet); //11.01.13
        repaint();
    }

    public void deletePetriNet() {
        graphNet = null;
        repaint();
    }

    public GraphPetriNet getGraphNet() {
        return graphNet;
    }

    public void setGraphNet(GraphPetriNet net) { //коректно працює тільки якщо потім не змінювати граф
        //рекомендується використовувати addGraphNet
        graphNet = net;
        repaint();
    }

    public List<GraphPetriNet> getGraphNetList() {  //11.01.13
        return graphNetList;
    }

    public GraphPetriNet getLastGraphNetList() {  //11.01.13
        return graphNetList.get(graphNetList.size() - 1);
    }

    public static int getIdPosition() {  //назва методу не за стандартом Чоме немає id для зв"язків?
        return id++;
    }

    public static int getIdTransition() { //назва методу не за стандартом 
        return id++;
    }

 /*   public Point getCurrentPlacementPoint() { //09.01.13
        return currentPlacementPoint;
    }*/

    //11.01.13
  /*  private void paintCurrentPlacementPoint(Graphics2D g2) {
        Double x1 = currentPlacementPoint.getX();
        Double y1 = currentPlacementPoint.getY() - 5;
        Double y2 = y1 + 10;
        g2.drawLine(x1.intValue(), y1.intValue(), x1.intValue(), y2.intValue());
        x1 = x1 - 5;
        Double x2 = x1 + 10;
        y1 = y1 + 5;
        g2.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y1.intValue());
    }*/
    
    //12.01.16
  /*  public void transform(Graphics2D g2){
      //  AffineTransform at = g2.getTransform();
        AffineTransform toCenterAt = new AffineTransform();
        toCenterAt.concatenate(at);
     
        toCenterAt.scale(0.1, 0.1);
        g2.transform(toCenterAt);
        g2.setTransform(at);
        
        System.out.println("TRANSFORM PANEL");
        
        
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(229, 229, 229));
        setPreferredSize(new java.awt.Dimension(20000, 20000));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    public void animateIn(PetriT tr) {    //Саша 05.17
        
        ArrayList<GraphArcIn> list = new ArrayList<GraphArcIn>();
        for (GraphArcIn t : graphNet.getGraphArcInList()) {
            if (t.getArcIn().getNumT() == tr.getNumber()) {
                list.add(t);
            }
        }
        try {
            for (GraphArcIn t : list) {
                t.setColor(new Color(255, 77, 77));
                t.setLineWidth(3);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphArcIn t : list) {
                t.setLineWidth(5);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphArcIn t : list) {
                t.setLineWidth(7);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphArcIn t : list) {
                t.setLineWidth(5);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphArcIn t : list) {
                t.setLineWidth(3);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphArcIn t : list) {
                t.setLineWidth(1);
                t.setColor(Color.BLACK);
                this.repaint();
            }
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void animateT(PetriT tr) {   //Саша 05.17
        ArrayList<GraphPetriTransition> list = new ArrayList<GraphPetriTransition>();
        for (GraphPetriTransition t : graphNet.getGraphPetriTransitionList()) {
            if (t.getPetriTransition().getNumber() == tr.getNumber()) {
                list.add(t);
            }
        }
        try {
            for (GraphPetriTransition t : list) {
                t.setColor(new Color(255, 77, 77));
                t.setLineWidth(7);
                //t.setFont(11);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriTransition t : list) {
                t.setLineWidth(10);
                //t.setFont(12);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriTransition t : list) {
                t.setLineWidth(12);
                //t.setFont(14);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriTransition t : list) {
                t.setLineWidth(10);
                //t.setFont(12);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriTransition t : list) {
                t.setLineWidth(7);
                //t.setFont(11);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriTransition t : list) {
                t.setLineWidth(5);
                //t.setFont(10);
                t.setColor(Color.BLACK);
                this.repaint();
            }
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void animateP(ArrayList<Integer> inP) {  //Саша 05.17
        ArrayList<GraphPetriPlace> list = new ArrayList<GraphPetriPlace>();
        for (GraphPetriPlace p : graphNet.getGraphPetriPlaceList()) {
            for (Integer inp : inP) {
                if (p.getPetriPlace().getNumber() == inp) {
                    list.add(p);
                }
            }
        }

        try {
            for (GraphPetriPlace p : list) {
                p.setColor(new Color(255, 77, 77));
                //p.setActiveFontColor(new Color(255, 77, 77));
                p.setLineWidth(5);
                //p.setFont(11);
                this.repaint();
            }
            Thread.sleep(100);
            for (GraphPetriPlace p : list) {
                p.setLineWidth(7);
                //p.setFont(12);
                this.repaint();

            }
            Thread.sleep(100);
            for (GraphPetriPlace p : list) {
                p.setLineWidth(10);
                //p.setFont(14);
                this.repaint();
            }

            Thread.sleep(100);
            for (GraphPetriPlace p : list) {
                p.setLineWidth(7);
               // p.setFont(12);
                this.repaint();

            }
            Thread.sleep(100);
            for (GraphPetriPlace p : list) {
                p.setLineWidth(5);
                //p.setFont(11);
                this.repaint();

            }
            Thread.sleep(100);
            for (GraphPetriPlace p : list) {

                p.setLineWidth(2);
                //p.setFont(10);
                //p.setActiveFontColor(Color.BLACK);
                p.setColor(Color.BLACK);
                this.repaint();

            }
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

       
    public void animateOut(PetriT eventMin) {   //Саша 05.17
        ArrayList<GraphArcOut> list = new ArrayList<GraphArcOut>();
        for (GraphArcOut t : graphNet.getGraphArcOutList()) {
            if (t.getArcOut().getNumT() == eventMin.getNumber()) {
                list.add(t);
            }
        }
        try {
            for (GraphArcOut t : list) {
                t.setColor(new Color(255, 77, 77));
                t.setLineWidth(3);
                this.repaint();
            }
            Thread.sleep(50);
            for (GraphArcOut t : list) {
                t.setLineWidth(5);
                this.repaint();
            }
            Thread.sleep(50);
            for (GraphArcOut t : list) {
                t.setLineWidth(7);
                this.repaint();
            }
            Thread.sleep(50);
            for (GraphArcOut t : list) {
                t.setLineWidth(5);
                this.repaint();
            }
            Thread.sleep(10);
            for (GraphArcOut t : list) {
                t.setLineWidth(3);
                this.repaint();
            }
            Thread.sleep(50);
            for (GraphArcOut t : list) {
                t.setLineWidth(1);
                t.setColor(Color.BLACK);
                this.repaint();
            }
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(PetriNetsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
