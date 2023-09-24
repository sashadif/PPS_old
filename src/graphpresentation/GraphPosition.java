/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphpresentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Ольга
 */
public class GraphPosition extends GraphElement {
    
    private static  int diameter = 40;       // діаметр кола
    private int lineWidth;
    private Color color;

    /**
     * @return the diameter
     */
    public static int getDiameter() {
        return diameter;
    }

    /**
     * @param aDiameter the diameter to set
     */
    public static void setDiameter(int aDiameter) {
        diameter = aDiameter;
    }
 
    private Ellipse2D graphElement=new Ellipse2D.Double(0,0, getDiameter(), getDiameter());  // координати розташування кола

    public GraphPosition() {
        lineWidth = 3;
        color = Color.BLACK;
    }

    @Override
    public void drawGraphElement(Graphics2D g2) {
        g2.setStroke(new BasicStroke(getLineWidth()));
        g2.setColor(getColor());
        g2.draw(graphElement);
        g2.setColor(Color.WHITE);
        g2.fill(graphElement);
        g2.setStroke(new BasicStroke(getLineWidth()));
        g2.setColor(getColor());
     
    }

    
    public static int getDIAMETER() {
        return getDiameter();
    }

    
    @Override
    public void setNewCoordinates(Point2D p) {
            graphElement.setFrame(p.getX() - getDiameter() / 2, p.getY() - getDiameter() / 2, getDiameter(), getDiameter());
            
    }

        
    @Override
    public boolean isGraphElement(Point2D p) {
        if (graphElement.contains(p)) {
            return true;
        }
        return false;
    }

    @Override
    public Point2D getGraphElementCenter() {
        return new Point2D.Double(graphElement.getX() + getDiameter() / 2, graphElement.getY() + getDiameter() / 2);
    }

  
    @Override
    public String getType() {
        return graphElement.getClass().toString();
    }

   
    @Override
    public  int getBorder() {
        return getDiameter() / 2;
    }
    

    public Ellipse2D getGraphElement() {
        return graphElement;
    }

    public void setGraphElement(Ellipse2D graphElement) {
        this.graphElement = graphElement;
    }

    /**
     * @return the lineWidth
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @param lineWidth the lineWidth to set
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    
    
}
