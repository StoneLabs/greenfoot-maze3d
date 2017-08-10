import greenfoot.World;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.io.File;

/**
 * Simple menu allowing level selection
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class menu_levelSelection extends World
{
    Button myButton;
    Button myButtonBack;
    ListBox myList;
            
    static String stripExtension (String str) {
        if (str == null) return null;
        
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos);
    }
    
    public menu_levelSelection()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(world.VPw, world.VPh, 1); 
        
        setPaintOrder(GUI_Component.class);
        showText("Select a level to play!", world.VPw / 2, 20);
        
        final File[] files = new File("level").listFiles();
        ArrayList<String> strs2 = new ArrayList<String>(){{for (File f : files) if (f.getName().contains(".bmp")) add(stripExtension(f.getName()));}};
        // Initialize ListBox with size and ArrayList of Strings
        myList = new ListBox(new Point(world.VPw / 6, world.VPh / 8 * 2), strs2);
        addObject(myList, world.VPw / 2, world.VPh / 8 * 2);
        
        myButtonBack = new Button("Back", new Point(world.VPw / 6, 30));
        addObject(myButtonBack, world.VPw / 2, world.VPh / 32 * 3);
        
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
            if (myList.getSelection().size() == 1)
                Greenfoot.setWorld(new world(myList.getSelection().get(0).toString()));
        }
        if (myButtonBack.wasClicked())
        {
            Greenfoot.setWorld(new menu_main());
        }
    }
}
