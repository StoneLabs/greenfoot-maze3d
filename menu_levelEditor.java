import greenfoot.World;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Point;
import java.io.File;

/**
 * Simple Menu allowing level editing using pixelformer
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class menu_levelEditor extends World
{    
    Button myButton;
    Button myButtonBack;
    ListBox myList;
    
    public menu_levelEditor()
    {
        super(world.VPw, world.VPh, 1); 
        
        setPaintOrder(GUI_Component.class);
        showText("Select a level to edit!", world.VPw / 2, 20);
        
        final File[] files = new File("level").listFiles();
        ArrayList<String> strs2 = new ArrayList<String>(){{for (File f : files) if (f.getName().contains(".pxf")) add(f.getName());}};
        // Initialize ListBox with size and ArrayList of Strings
        myList = new ListBox(new Point(world.VPw / 6, world.VPh / 8 * 2), strs2);
        addObject(myList, world.VPw / 2, world.VPh / 8 * 2);
        
        myButtonBack = new Button("Back", new Point(world.VPw / 6, 30));
        addObject(myButtonBack, world.VPw / 2, world.VPh / 32 * 3);
        
        myButton = new Button("Edit", new Point(world.VPw / 6, 30));
        addObject(myButton, world.VPw / 2, world.VPh / 16 * 7);
        
        addObject(new Instructions(), world.VPw / 2, world.VPh / 8 * 7);
        addObject(new Codes(), world.VPw / 8 * 6, world.VPh / 2);
        
        Greenfoot.start();
    }
    
    @Override
    public void act()
    {
        if (myButton.wasClicked())
        {
            SoundManager.click.play();
            System.out.println("Selected: " + myList.getSelection());
            if (myList.getSelection().size() == 1)
                try
                { new ProcessBuilder("tools\\pf.exe","level\\" + myList.getSelection().get(0).toString()).start(); }
                catch (Exception ex) {}
        }
        if (myButtonBack.wasClicked())
        {
            SoundManager.click.play();
            Greenfoot.setWorld(new menu_main());
        }
    }
}
