import greenfoot.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO ;
import java.io.File;
import java.io.IOException;



public class ImageLoader
{
    public static BufferedImage readImage(String path) 
    {
        try {
            return ImageIO.read(new File(System.getProperty("user.dir") + "\\" + path));
        } catch (IOException e) {
            System.out.println("Couldn't read image (" +  System.getProperty("user.dir") + "\\" + path + ")");
            return null;
        }
    }    
}
