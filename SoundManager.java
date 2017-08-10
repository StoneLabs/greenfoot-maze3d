import greenfoot.*;

/**
 * Write a description of class SoundManager here.
 * 
 * @author Levy E. 
 * @version July 2017
 * 
 * 
 * --SOUND ATTRIBUTION--
 * 
 * MENU CLICK SOUND BY Soughtaftersounds (CC-BY)
 * MENU BACKGROUND MUSIC BY joshuaempyre (CC-BY)
 */
public class SoundManager
{
    public static final GreenfootSound music;
    public static final GreenfootSound shot;
    public static final GreenfootSound click;
    
    static
    {
        music = new GreenfootSound("menu_music.wav");
        shot = new GreenfootSound("gun_shot.mp3");
        click = new GreenfootSound("menu_click.mp3");
        
        music.setVolume(20);
        shot.setVolume(20);
    }  
}
