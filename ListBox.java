import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;

/**
 * Collection of Objects that is displayed in a list of rows. Acts very much like an ArrayList.<p>
 * The collection's contents are shown via the Object's toString() method.<p>
 * Scrollable when list is longer than height.<p>
 * Implements single and multi selecting.<p>
 * When has focus, can use "up" and "down" arrow keys to move selection index (when selection count equals 1) up and down through the list.<p>
 * <p>
 * Action listener: hasChanged()
 * 
 * @author Taylor Born
 * @version February 2011 - April 2014
 */
public class ListBox<E> extends WindowComponent
{
    // Size of this ListBox.
    private Point size;
    
    private GreenfootImage image;
    
    // Height of scrollBar.
    private int scrollBarHeight = 40;
    
    // Y position of where mouse pressed on scrollBar, used as reference for when dragged.
    private int mouseY;
    
    private int cellHeight = font.getSize() + 2;
    private ArrayList<E> objs;
    private ScrollingListener scroller = initializeScroller();
    private int scroll;
    private ArrayList<Integer> index = new ArrayList<Integer>();
    private boolean allowMultipleSelect;
    private boolean dragging;
    private Point lastMouse = new Point(-5, -5);
    private boolean changed;
    private Color scrollColor = Color.RED;

    /**
     * Create a new ListBox.
     * @param size The width and height of the ListBox.
     * @param objs The list of Objects to display in the ListBox.
     */
    public ListBox(Point size, ArrayList<E> objs)
    {
        this.size = size;
        this.objs = objs;
        image = new GreenfootImage((int)size.getX(), (int)size.getY());
        setImage(image);
        draw();
    }
    
    public void resize(Point size)
    {
        this.size.setLocation(size);
        image = new GreenfootImage((int)size.getX(), (int)size.getY());
        setImage(image);
        draw();
    }
    
    private boolean needToRedraw;
    private boolean mouseOverThisLastAct;
    
    public void setScrollColor(Color scroll)
    {
        scrollColor = scroll;
        needToRedraw = true;
    }
    
    /**
     * Update the ListBox's image.
     */
    private void draw()
    {
        needToRedraw = false;
        
        if (backColor.getAlpha() != 255)
            image.clear();
        image.setColor(backColor);
        image.fill();
        
        Graphics2D g = image.getAwtImage().createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        
        // Draw items.
        for (int i = scroll / cellHeight; i < objs.size() && i - scroll / cellHeight < size.getY() / cellHeight + 2; i++)
        {
            // If selected.
            if (index.contains(i))
            {
                g.setColor(selectColor);
                g.fillRect(0, cellHeight * i - scroll, (int)size.getX() - 10, cellHeight);
            }
            // If hovered over.
            else if (!dragging && mouseOverThis() && lastMouse.getX() > getX() - size.getX() / 2 && lastMouse.getX() < getX() + size.getX() / 2 - 10 && lastMouse.getY() >= getY() - size.getY() / 2 + cellHeight * i - scroll && lastMouse.getY() < getY() - size.getY() / 2 + cellHeight * i - scroll + cellHeight)
            {
                g.setColor(hoverColor);
                g.fillRect(0, cellHeight * i - scroll, (int)size.getX() - 10, cellHeight);
            }
            g.setColor(borderColor);
            g.drawRect(0, cellHeight * i - scroll, (int)size.getX() - 10, cellHeight);
            g.setColor(textColor);
            g.drawString(objs.get(i).toString(), 4, cellHeight * i - scroll + cellHeight - 2);
        }
        
        // For when text is long enough to reach into scrollbar track
        g.setColor(backColor);
        g.fillRect((int)size.getX() - 9, 0, 9, (int)size.getY());
        
        // Scroll bar.
        if (objs.size() > size.getY() / cellHeight) {
            g.setColor(scrollColor);
            g.fillRect((int)size.getX() - 9, (int)(scroll / (double)(objs.size() * cellHeight - (size.getY() - 2)) * (size.getY() - scrollBarHeight)), 9, scrollBarHeight);
        }
        
        g.setColor(new Color((backColor.getRed() + borderColor.getRed()) / 2, (backColor.getGreen() + borderColor.getGreen()) / 2, (backColor.getBlue() + borderColor.getBlue()) / 2));
        g.drawRect(1, 1, (int)size.getX() - 3, (int)size.getY() - 3);
        g.setColor(borderColor);
        g.drawRect(0, 0, (int)size.getX() - 1, (int)size.getY() - 1);
        
        g.dispose();
    }
    
    
    /**
     * Act.
     */
    @Override
    public void act()
    {
        super.act();
        
        if (index.size() == 1 && hasFocus()) {
            String s = Greenfoot.getKey();
            if (s != null) {
                int i = index.get(0);
                if (s.equals("up")) {
                    if (i != 0)
                        setIndex(i - 1);
                }
                else if (s.equals("down") && i != objs.size() - 1)
                    setIndex(i + 1);
            }
        }
        
        if (Greenfoot.mouseMoved(null) || Greenfoot.mouseDragged(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            lastMouse.setLocation(mouse.getX(), mouse.getY());
        }
        
        boolean mouseOverThis = mouseOverThis();
        
        if (objs.size() > size.getY() / cellHeight) {
            
            int topOfScrollBar = (int)(scroll / (double)(objs.size() * cellHeight - (size.getY() - 2)) * (size.getY() - scrollBarHeight));
        
            if (Greenfoot.mousePressed(this) && lastMouse.getX() - (getX() - size.getX() / 2) > size.getX() - 10 && lastMouse.getY() - (getY() - size.getY() / 2) > topOfScrollBar && lastMouse.getY() - (getY() - size.getY() / 2) < topOfScrollBar + scrollBarHeight)
            {
                dragging = true;
                mouseY = (int)(lastMouse.getY() - (getY() - size.getY() / 2)) - topOfScrollBar;
            }
            
            int n = scroller.getScroll();
            if (mouseOverThis)
            {
                scroll += n;
                if (scroll < 0)
                    scroll = 0;
                else if (scroll + (size.getY() - 2) > objs.size() * cellHeight)
                    scroll = objs.size() * cellHeight - (int)(size.getY() - 2);
            }
            
            if (dragging && (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null)))
                dragging = false;
            if (dragging && Greenfoot.mouseDragged(null)) {
                topOfScrollBar = (int)(lastMouse.getY() - (getY() - size.getY() / 2)) - mouseY;
                if (topOfScrollBar < 0)
                    topOfScrollBar = 0;
                else if (lastMouse.getY() - (getY() - (int)size.getY() / 2) + (scrollBarHeight - mouseY) > size.getY() - 2)
                    topOfScrollBar = (int)size.getY() - scrollBarHeight;
                scroll = (int)(topOfScrollBar / (double)(size.getY() - scrollBarHeight) * (objs.size() * cellHeight - (size.getY() - 2)));
            }
        }
        
        if (Greenfoot.mousePressed(this) && lastMouse.getX() - (getX() - size.getX() / 2) < size.getX() - 10) {
            int i = (int)(scroll + lastMouse.getY() - (getY() - size.getY() / 2)) / cellHeight;
            if (i < objs.size()) {
                if (allowMultipleSelect && Greenfoot.isKeyDown("shift")) {
                    if (index.contains(i))
                        index.remove(new Integer(i));
                    else
                        index.add(i);
                }
                else {
                    index.clear();
                    index.add(i);
                }
                setIndexInView(i);
                changed = true;
            }
        }
        
        if (needToRedraw || mouseOverThis || mouseOverThisLastAct)
            draw();
        mouseOverThisLastAct = mouseOverThis();
    }
    
    /**
     * Get the indexes of what is selected. Meant to be used when multiple selecting is enabled, otherwise getOneIndex() is more appropriate.
     * @return The indexes of what is selected.
     * @see getOneIndex()
     */
    public ArrayList<Integer> getIndexes()
    {
        return index;
    }
    
    /**
     * Get the index of what is selected. Meant to be used when multiple selecting is disabled, otherwise getIndexes() is more appropriate.
     * @return The index of what is selected. -1 if none selected.
     * @see getIndexes()
     */
    public int getOneIndex()
    {
        return index.isEmpty() ? -1 : index.get(0);
    }
    
    /**
     * Deselect selection.
     */
    public void deselect()
    {
        index.clear();
        needToRedraw = true;
    }
    
    /**
     * Action listener for when selection has been altered.
     * @return Whethor or not selection has been altered since last call.
     */
    public boolean hasChanged()
    {
        if (changed) {
            changed = false;
            return true;
        }
        return false;
    }
    
    /**
     * Set which item to be selected.
     * @param index The index of the item to be selected.
     * @see addIndex(int)
     */
    public void setIndex(int index)
    {
        if (this.index.size() != 1 || this.index.get(0) != index)
            changed = true;
        this.index.clear();
        if (index > -1 && index < objs.size())
        {
            if (!allowMultipleSelect)
                deselect();
            else
                needToRedraw = true;
            this.index.add(index);
            setIndexInView(index);
        }
    }
    
    /**
     * Cause the Object at the given index to be selected. Meant to be used when multiple selecting is enabled.
     * @param index The index of the Object to be selected.
     * @see setIndex(int)
     */
    public void addIndex(int index)
    {
        if (allowMultipleSelect && index > -1 && index < objs.size())
            if (!this.index.contains(index))
            {
                this.index.add(index);
                needToRedraw = true;
                setIndexInView(index);
            }
    }
    
    /**
     * Ajust scroll amount to set the Object determined by the given index, to be in view.
     * @param index Index for Object to be set to be in view.
     */
    private void setIndexInView(int index)
    {
        if (index > -1 && index < objs.size())
            if (index * cellHeight < scroll)
                scroll = index * cellHeight;
            else if ((index + 1) * cellHeight > scroll + (size.getY() - 2))
                scroll = (index + 1) * cellHeight - (int)(size.getY() - 2);
    }
    
    /**
     * Determine if there are any Objects selected.
     * @return Whether there are any Objects selected.
     */
    public boolean hasSelection()
    {
        return !index.isEmpty();
    }
    
    /**
     * Toggle the Object at the given index to be selected or not be selected.
     * @param index The index of Object to toggle selection.
     */
    public void toggleSelect(int index)
    {
        if (index < 0 || index > objs.size() - 1)
            return;
        if (this.index.contains(index))
            this.index.remove(new Integer(index));
        else
        {
            this.index.add(index);
            setIndexInView(index);
        }
        needToRedraw = true;
    }
    
    /**
     * Swap two Objects in position. If Objects are selected, they will remain selected after the swap.
     * @param indexA Index of Object to swap position.
     * @param indexB Index of Object to swap position.
     */
    public void swap(int indexA, int indexB)
    {
        if (indexA < 0 || indexA > objs.size() - 1 || indexB < 0 || indexB > objs.size() - 1)
            return;
        boolean a = index.contains(indexA);
        boolean b = index.contains(indexB);
        E s = objs.get(indexA);
        objs.set(indexA, objs.get(indexB));
        objs.set(indexB, s);
        index.remove(new Integer(indexA));
        index.remove(new Integer(indexB));
        if (a)
            index.add(indexB);
        if (b)
            index.add(indexA);
        needToRedraw = true;
    }
    
    /**
     * Get the selected Object. Meant to be used when multiple selecting is disabled, otherwise getSelection() is more appropriate.
     * @return The selected Object. Null if none selected.
     * @see getSelection()
     */
    public E getOneSelection()
    {
        if (index.size() == 0)
            return null;
        return objs.get(index.get(0));
    }
    
    /**
     * Get all selected Object. Meant to be used when multiple selecting is enabled, otherwise getOneSelection() is more appropriate.
     * @return The selected Objects. Null if none selected.
     * @see getOneSelection()
     */
    public ArrayList<E> getSelection()
    {
        ArrayList<E> s = new ArrayList<E>();
        for (int i = 0; i < objs.size(); i++)
            if (index.contains(i))
                s.add(objs.get(i));
        return s;
    }
    
    /**
     * Get the Object in the ListBox at the given index.
     * @param index The index for what Object in the ListBox to get.
     * @return The Object at the index of the ListBox. Null if invalid index.
     */
    public E get(int index)
    {
        // Check to see if valid index
        if (index > -1 && index < objs.size())
            return objs.get(index);
        return null;
    }
    
    /**
     * Get the contents of the ListBox.
     * @return The contents of the ListBox.
     */
    public ArrayList<E> getList()
    {
        return objs;
    }
    
    /**
     * Determine whethor or not the given Object is contained within the list.
     * @param The Object in question.
     * @return Whethor or not the given Object is contained within the list.
     */
    public boolean contains(E s)
    {
        return objs.contains(s);
    }
    
    /**
     * Determine the index for where the given Object lies within the list.
     * @param The Object in question.
     * @return The index for where the given Object lies within the list.
     */
    public int indexOf(E s)
    {
        return objs.indexOf(s);
    }
    
    /**
     * Set the contents to the given list.
     * @param list New list of Objects.
     */
    public void setList(ArrayList<E> list)
    {
        deselect();
        objs = list;
        scroll = 0;
    }
    
    /**
     * Add an Object to the ListBox, making it selected and in view.
     * @param s The Object to be added.
     */
    public void add(E s)
    {
        changed = true;
        objs.add(s);
        deselect();
        index.add(objs.size() - 1);
        setIndexInView(objs.size() - 1);
    }
    
    /**
     * Insert an Object to the ListBox at the given position, making it selected and in view.
     * @param s The Object to be inserted.
     * @param index The position to insert String.
     */
    public void add(E s, int index)
    {
        changed = true;
        objs.add(index, s);
        deselect();
        this.index.add(index);
        setIndexInView(index);
    }
    
    /**
     * Remove what is selected.
     */
    public void removeSelected()
    {
        ArrayList<E> objsD = new ArrayList<E>();
        for (Integer i : index)
            objsD.add(objs.get(i));
        for (E s : objsD)
            objs.remove(s);
        deselect();
        changed = true;
    }
    
    /**
     * Get the number of Objects in the ListBox's collection.
     * @return The number of Objects in the ListBox's collection.
     */
    public int size()
    {
        return objs.size();
    }
    
    /**
     * Remove everything from the ListBox.
     */
    public void clear()
    {
        objs.clear();
        deselect();
    }
    
    /**
     * Change the Object at a given index.
     * @param index The index in the ListBox to change.
     * @param obj The Object to set to at the given index.
     */
    public void set(int index, E obj)
    {
        if (index > -1 && index < objs.size()) {
            objs.set(index, obj);
            needToRedraw = true;
        }
    }
    
    /**
     * Determine if the ListBox is set to allow multiple selecting of Objects.
     * @return If the ListBox is set to allow multiple selecting of Objects.
     */
    public boolean multipleSelectingIsEnabled()
    {
        return allowMultipleSelect;
    }
    
    /**
     * Change if the ListBox is set to allow multiple selecting of Objects.
     * @param enable If the ListBox will be set to allow multiple selecting of Objects.
     */
    public void setMultipleSelecting(boolean enable)
    {
        allowMultipleSelect = enable;
    }
    
    public void sort()
    {
        if (objs.isEmpty())
            return;
        
        boolean useToString = !(objs.get(0) instanceof Comparable);
        
        // Selection sort
        for (int i = 0; i < objs.size(); i++) {
            int min = i;
            for (int k = i + 1; k < objs.size(); k++)
                if ((useToString ? ((Comparable)objs.get(k)).compareTo(objs.get(min)) : objs.get(k).toString().compareTo(objs.get(min).toString())) < 0)
                    min = k;
            if (min != i) {
                E temp = objs.get(i);
                objs.set(i, objs.get(min));
                objs.set(min, temp);
                for (int ii = 0; ii < index.size(); ii++)
                    if (index.get(ii).intValue() == min)
                        index.set(ii, i);
                    else if (index.get(ii).intValue() == i)
                        index.set(ii, min);
            }
        }
        
        needToRedraw = true;
    }
}