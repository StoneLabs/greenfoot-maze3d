import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

/**
 * GUI item that is a box that captures click events on itself
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class Button extends WindowComponent
{
    private Point size;
    public String text;
    private boolean clicked;
    private boolean pressed;
    
    private Color pressColor = new Color(100, 100, 100, 180);
    private GreenfootImage image;

    public Button(String text, Point size)
    {
        this.size = size;
        image = new GreenfootImage((int)size.getX(), (int)size.getY());
        setImage(image);
        this.text = text;
    }
    
    @Override
    public void act() 
    {
        super.act();
        pressed = Greenfoot.mousePressed(this);
        clicked = Greenfoot.mouseClicked(this);
        draw();
    }
    
    protected void draw()
    {
        image.setColor(pressed ? pressColor : backColor);
        if (image.getColor().getAlpha() != 255)
            image.clear();
        image.fill();
        
        Graphics2D g = image.getAwtImage().createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(textColor);
        g.drawString(text, ((int)size.getX() - g.getFontMetrics().stringWidth(text)) / 2 + (int)size.getX() % 2, ((int)size.getY() + image.getFont().getSize()) / 2 - 1);
        g.dispose();
        
        image.setColor(borderColor);
        image.drawRect(0, 0, (int)size.getX() - 1, (int)size.getY() - 1);
        image.setColor(new Color((backColor.getRed() + borderColor.getRed()) / 2, (backColor.getGreen() + borderColor.getGreen()) / 2, (backColor.getBlue() + borderColor.getBlue()) / 2));
        image.drawRect(1, 1, (int)size.getX() - 3, (int)size.getY() - 3);
    }
    
    public boolean wasClicked()
    {
        boolean c = clicked;
        clicked = false;
        return c;
    }
}