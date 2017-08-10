import greenfoot.*;

/**
 * Simple win page being shown for 100 GF ticks
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class menu_win extends World
{
    public menu_win()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(world.VPw, world.VPh, 1);
        
        Greenfoot.start();
    }
    
    short updates = 0;
    
    @Override
    public void act()
    {
        if (++updates > 100) Greenfoot.setWorld(new menu_levelSelection());
    }
}
