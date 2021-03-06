import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.World;
import greenfoot.core.WorldHandler;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.List;

/**
 * A component to be used in GUI system.<p>
 * Handles listening for mouse hover, and statuses for being hidden, will show, and focus.
 * 
 * Simplified minimalistic version.
 * 
 * @author Taylor Born
 * @editor Levy Ehrstein
 * @version July 2017
 */
public abstract class GUI_Component extends Actor
{
    public static Font font = new Font("Helvetica", Font.PLAIN, 12);
    public static Color backColor = Color.WHITE;
    public static Color textColor = Color.BLACK;
    public static Color borderColor = Color.BLACK;
    public static Color hoverColor = new Color(192, 192, 192);
    public static Color disableColor = Color.GRAY;
    public static Color selectColor = Color.YELLOW;
        
    private boolean overThis;
    private boolean focus;

    protected ScrollingListener initializeScroller()
    {
        ScrollingListener sl = new ScrollingListener();
        WorldHandler.getInstance().getWorldCanvas().addMouseWheelListener(sl);
        return sl;
    }
    
    @Override
    public void act()
    {
        if (overThis) {
            if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this))
                overThis = false;
        }
        else if (Greenfoot.mouseMoved(this))
            overThis = true;
        
        if (Greenfoot.mousePressed(this)) {
            if (!focus) {
                focus = true;
                gainedFocus();
            }
        }
        else if (Greenfoot.mousePressed(null))
            focus = false;
    }
    
    public void giveFocus()
    {
        if (getWorld() == null)
            return;
        for (GUI_Component component : (List<GUI_Component>)getWorld().getObjects(GUI_Component.class))
            component.removeFocus();
        if (!focus) {
            focus = true;
            gainedFocus();
        }
    }
    
    protected boolean mouseOverThis() { return overThis; }
    protected void gainedFocus() {}
    public boolean hasFocus() { return focus; }
    public void removeFocus() {  focus = false; }
    
    public int getGUIWidth() { return getImage() == null ? 0 : getImage().getWidth(); }
    public int getGUIHeight() { return getImage() == null ? 0 : getImage().getHeight(); }
    
    protected class ScrollingListener implements MouseWheelListener
    {
        int amount = 0;
        
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            amount += e.getWheelRotation();
            e.consume();
        }
        
        public int getScroll()
        {
            int s = amount;
            amount = 0;
            return s;
        }
    }
}