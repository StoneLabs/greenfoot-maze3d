import greenfoot.World;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.io.File;

/**
 * Main menu of the game
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class menu_main extends World
{
    static 
    {
        System.out.println("\fWorking Directory = " +
              System.getProperty("user.dir"));
    }  
            
    Button mySelect;
    Button myEdit;
    Button myNew;
    
    public menu_main()
    {    
        super(world.VPw, world.VPh, 1); 
        
        showText("MAIN MENU!", world.VPw / 2, 20);
        
        mySelect = new Button("Play", new Point(world.VPw / 6, 30));
        myEdit = new Button("Edit", new Point(world.VPw / 6, 30));
        myNew = new Button("Create", new Point(world.VPw / 6, 30));
        addObject(mySelect, world.VPw / 2, 60);
        addObject(myEdit, world.VPw / 2, 120);
        addObject(myNew, world.VPw / 2, 160);
        
        Greenfoot.start();
    }
    
    @Override
    public void act()
    {
        if (mySelect.wasClicked())
            Greenfoot.setWorld(new menu_levelSelection());
            
        if (myEdit.wasClicked())
            Greenfoot.setWorld(new menu_levelEditor());
            
        if (myNew.wasClicked())
            try
            { new ProcessBuilder("tools\\pf.exe").start(); }
            catch (Exception ex) {}
    }
}
