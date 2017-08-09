import greenfoot.World;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;

/**
 * Write a description of class menu_levelSelection here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class menu_levelSelection extends World
{
    Button myButton;
    ListBox myList;
    
    static {
        System.out.println("\fWorking Directory = " +
              System.getProperty("user.dir"));
            }  
    
    public menu_levelSelection()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(world.VPw, world.VPh, 1); 
        
        setPaintOrder(GUI_Component.class);
        showText("Select a level to play!", world.VPw / 2, 20);
        
        ArrayList<String> strs2 = new ArrayList<String>(){{for (int i = 0; i < 100; i++) add(""+i);}};
        // Initialize ListBox with size and ArrayList of Strings
        myList = new ListBox(new Point(world.VPw / 6, world.VPh / 8 * 2), strs2);
        addObject(myList, world.VPw / 2, world.VPh / 8 * 2);
        
        myButton = new Button("Play", new Point(world.VPw / 6, 30));
        addObject(myButton, world.VPw / 2, world.VPh / 16 * 7);
        
        Greenfoot.start();
    }
    
    @Override
    public void act()
    {
        if (myButton.wasClicked())
        {
            System.out.println("Selected: " + myList.getSelection());
            Greenfoot.setWorld(new world("01"));
        }
    }
}