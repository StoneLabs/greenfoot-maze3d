import greenfoot.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Main raycasting and game logic
 * 
 * For details see publication: [todo - find document]
 * 
 * @author Levy Ehrstein
 * @version July 2017
 */
public class world extends World
{
    public static final int VPw = 1080, VPh = 640; /** TRUE 16x9 */
    public static final int texAmnt = 8;
    public static final int texWidth = 64, texHeight = 64;
    
    private int[][] map;
    public int mapHeight;
    public int mapWidth;
    
    private double posX = 1.5, posY = 1.5;         /**X any X as the player position vector     */
    private double dirX = -1.0, dirY = 0.0;          /**Rx and Ry as the players rotation vector  */
    private double planeX = 0.0, planeY = 0.66;      /**2D camera plane vector Cp                 */
    
    private double moveSpeed = 1;
    private double rotSpeed = 1;
    
    private final String level;
    
    private long oldTime = 0;
    
    GreenfootImage weapon = new GreenfootImage("textures\\weapon.png");
    GreenfootImage weaponEase = new GreenfootImage("textures\\weapon.png");
    GreenfootImage weaponAtac = new GreenfootImage("textures\\weapon_shoot.png");
    
    public static final Map<Integer, Integer> wallCodes;
    private static final int[][][] textures = new int[texAmnt][texWidth][texHeight];
    static {
        Map<Integer, Integer> tmp = new HashMap<Integer, Integer>();
        tmp.put(0xFFFFFFFF, 0x00);
        tmp.put(0xFF000000, 0x01);
        tmp.put(0xFF808080, 0x02);
        tmp.put(0xFF0000FF, 0x03);
        tmp.put(0xFF000080, 0x04);
        tmp.put(0xFF8000FF, 0x05);
        tmp.put(0xFFFF00FF, 0x06);
        tmp.put(0xFF804000, 0x07);
        tmp.put(0xFF00FF00, 0x08);
        wallCodes = Collections.unmodifiableMap(tmp);
        
        //load textures
        for (int i = 0; i < texAmnt; i++)
        {
            BufferedImage image = ImageLoader.readImage("textures\\wall_" +  (i+1) + ".jpg");
            if (image.getHeight() != texHeight || image.getWidth() != texWidth) 
            {
                System.out.println("Invalid image dimension ("+(i+1)+")!");
                break;
            }
            for (int x = 0; x < texWidth; x++)
                for (int y = 0; y < texHeight; y++)
                    textures[i][x][y] = image.getRGB(x, y);
        }
    }
    
    public world(String name)
    {    
        // Create a new world with 600x100 cells with a cell size of 1x1 pixels.
        super(1080, 640, 1); 
        oldTime = System.nanoTime();              
        System.out.println("Loading level " + name);
        level = name;
              
        //load level
        BufferedImage level = ImageLoader.readImage("level\\" + name + ".bmp");
        map = new int[level.getWidth()][level.getHeight()];
        for (int x = 0; x < level.getWidth(); x++)
            for (int y = 0; y < level.getHeight(); y++)
            {
                if (level.getRGB(x, y) == 0xFFFFFF80) {posX = x + 0.5; posY = y + 0.5; map[x][y] = 0x00; continue;}
                Integer wallCode = wallCodes.get(level.getRGB(x, y));
                if (wallCode == null)
                    System.out.println("Invalid code: " + Integer.toHexString(level.getRGB(x, y)));
                map[x][y] = wallCode == null ? 0 : wallCode;
            }
                
        mapHeight = map[0].length;
        mapWidth  = map.length;
        
        Greenfoot.stop();
        Greenfoot.start();
    }
    
    boolean lastTickSpaceDown = false;
    public void act()
    {
        /**** UPDATE ****/
        long curTime = System.nanoTime();
        double dTime = (curTime - oldTime); //Calculating delta T ( time since last update )
        int fps = (int)(1/dTime * 1000000000); // fpns -> fps (1E+9)
        oldTime = curTime;
        
        double calMoveSpeed = moveSpeed * dTime * 0.0000000025;
        double calRotSpeed = rotSpeed * dTime * 0.0000000025;
        
        double newX = posX;
        double newY = posY;
        //move forward if no wall in front of you
        if (Greenfoot.isKeyDown("up"))
        {
          newX = (posX + dirX * calMoveSpeed);
          newY = (posY + dirY * calMoveSpeed);
        }
        //move backwards if no wall behind you
        if (Greenfoot.isKeyDown("down"))
        {
          newX = (posX - dirX * calMoveSpeed);
          newY = (posY - dirY * calMoveSpeed);
        }
        
        if(newX >= 0 && newX < mapWidth  && (map[(int)newX][(int)posY] == 0 || map[(int)newX][(int)posY] == 8)) posX = newX;
        if(newY >= 0 && newY < mapHeight && (map[(int)posX][(int)newY] == 0 || map[(int)posX][(int)newY] == 8)) posY = newY;
        //rotate to the right
        if (Greenfoot.isKeyDown("right"))
        {
          //both camera direction and camera plane must be rotated
          double oldDirX = dirX;
          dirX = dirX * Math.cos(-calRotSpeed) - dirY * Math.sin(-calRotSpeed);
          dirY = oldDirX * Math.sin(-calRotSpeed) + dirY * Math.cos(-calRotSpeed);
          double oldPlaneX = planeX;
          planeX = planeX * Math.cos(-calRotSpeed) - planeY * Math.sin(-calRotSpeed);
          planeY = oldPlaneX * Math.sin(-calRotSpeed) + planeY * Math.cos(-calRotSpeed);
        }
        //rotate to the left
        if (Greenfoot.isKeyDown("left"))
        {
          //both camera direction and camera plane must be rotated
          double oldDirX = dirX;
          dirX = dirX * Math.cos(calRotSpeed) - dirY * Math.sin(calRotSpeed);
          dirY = oldDirX * Math.sin(calRotSpeed) + dirY * Math.cos(calRotSpeed);
          double oldPlaneX = planeX;
          planeX = planeX * Math.cos(calRotSpeed) - planeY * Math.sin(calRotSpeed);
          planeY = oldPlaneX * Math.sin(calRotSpeed) + planeY * Math.cos(calRotSpeed);
        }
        
        if (!Greenfoot.isKeyDown("space")) weapon = weaponEase;
        if (Greenfoot.isKeyDown("space") && !lastTickSpaceDown)
        {
            SoundManager.shot.stop();
            SoundManager.shot.play();
            weapon = weaponAtac;
            RayHit hit = cast(dirX, dirY);
            if (hit.blockID == 7) //wood
                map[hit.mapX][hit.mapY] = 0;
        }
        lastTickSpaceDown = Greenfoot.isKeyDown("space");
        
        if (Greenfoot.isKeyDown("escape")) Greenfoot.setWorld(new menu_main());
        
        if (map[(int)posX][(int)posY] == 8) Greenfoot.setWorld(new menu_win());
        
        render();
        showText("FPS/UPS: "+fps, 75, 30);
        showText("Level: "+level, 75, 50);
    }
    
    public void render()
    {
        /**** RENDER ****/
        //get the greenfoot background image
        GreenfootImage img = this.getBackground();
        img.setColor(Color.GRAY.darker());
        img.fillRect(0,0,VPw, VPh/2);
        
        img.setColor(Color.GRAY.brighter());
        img.fillRect(0,VPh/2,VPw, VPh/2);
        
        for(int x = 0; x < VPw; x++)
        {
            /** calculate ray position and direction */
            double cameraX = 2 * x / (double)(VPw) - 1; /** x-coordinate in camera space (-1 ; 1) */
            double rayDirX = dirX + planeX * cameraX; /** Sum of dir Vector and a part of the plane vector */
            double rayDirY = dirY + planeY * cameraX;
            
            /** Send the ray **/
            RayHit hit = cast(rayDirX, rayDirY);
            
            /** Calculate height of line to draw on screen */
            int lineHeight = (int)(VPh / hit.distance);
            
            // calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + VPh / 2;
            if(drawStart < 0)drawStart = 0;
            int drawEnd = lineHeight / 2 + VPh / 2;
            if(drawEnd >= VPh)drawEnd = VPh - 1;
            
            
            /** x coordinate on the texture **/
            int texX = (int)(hit.blockX * (double)texWidth);
            //flip the x coordinate in some cases
            if(hit.side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
            if(hit.side == 1 && rayDirY < 0) texX = texWidth - texX - 1;
            
            /** draw stripe **/
            for(int y = drawStart; y<drawEnd; y++)
            {
                int d = y * 256 - VPh * 128 + lineHeight * 128;  //256 and 128 factors to avoid floats
                int texY = ((d * texHeight) / lineHeight) / 256;
                if (texX < 0) texX = 0; if (texX > texWidth)  texX = texWidth;
                if (texY < 0) texY = 0; if (texY > texHeight) texX = texHeight;
                int colorCode = textures[hit.blockID - 1][texX][texY];
                Color color = new Color(colorCode);
                if(hit.side == 1) color = color.darker();//(colorCode >> 1) & 8355711; //011111...
                img.setColorAt(x, y, color);
            }
            
            //draw the pixels of the stripe as a vertical line
            //img.drawLine(x, drawStart, x, drawEnd);
        }
        
        img.setColor(new Color(0xFFFF00FF));
        img.drawLine(VPw/2 - 10, VPh/2, VPw/2 - 03, VPh/2);
        img.drawLine(VPw/2 + 03, VPh/2, VPw/2 + 10, VPh/2);
        img.drawLine(VPw/2, VPh/2 - 10, VPw/2, VPh/2 - 03);
        img.drawLine(VPw/2, VPh/2 + 03, VPw/2, VPh/2 + 10);
        
        img.drawImage(weapon, VPw/2 - weapon.getWidth()/2, VPh - weapon.getHeight());
        
        this.setBackground(img);
    }
    
    private RayHit cast(double rayDirX, double rayDirY)
    {
        double rayPosX = posX;
        double rayPosY = posY;
        
        /** which box of the map we're in */
        int mapX = (int)rayPosX;
        int mapY = (int)rayPosY;
        
        /** length of ray from current position to next x or y-side */
        double sideDistX;
        double sideDistY;
        
        /** length of ray from one x or y-side to next x or y-side */
        double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
        double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
        double perpWallDist;
        
        /** what direction to step in x or y-direction (either +1 or -1) */
        int stepX;
        int stepY;
        
        int hit = 0; /** was there a wall hit?       */
        int side = -1;    /** was a NS or a EW wall hit?  */
        
        /** calculate step and initial sideDist */
        if (rayDirX < 0)
        {
            stepX = -1;
            sideDistX = (rayPosX - mapX) * deltaDistX;
        }
        else
        {
            stepX = 1;
            sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
        }
        if (rayDirY < 0)
        {
            stepY = -1;
            sideDistY = (rayPosY - mapY) * deltaDistY;
        }
        else
        {
            stepY = 1;
            sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
        }
        
        while (hit == 0)
        {
            /** jump to next map square, OR in x-direction, OR in y-direction */
            if (sideDistX < sideDistY)
            {
                sideDistX += deltaDistX;
                mapX += stepX;
                side = 0;
            }
            else
            {
                sideDistY += deltaDistY;
                mapY += stepY;
                side = 1;
            }
            //Check if ray has hit a wall
            if(mapX < 0 || mapX >= mapWidth || mapY < 0 || mapY >= mapHeight)
                return new RayHit(1, 0, Double.MAX_VALUE, 0, 0, 0); //Not hit anything
            if (map[mapX][mapY] > 0) hit = 1;
        } 
        
        if (side == -1) return null; /** ERROR */
        
        /** Calculate distance projected on camera direction (oblique distance will give fisheye effect!) */
        if (side == 0) perpWallDist = (mapX - rayPosX + (1 - stepX) / 2) / rayDirX;
        else           perpWallDist = (mapY - rayPosY + (1 - stepY) / 2) / rayDirY;
        
        //calculate value of wallX
        double wallX; //where exactly the wall was hit
        if (side == 0) wallX = rayPosY + perpWallDist * rayDirY;
        else           wallX = rayPosX + perpWallDist * rayDirX;
        wallX -= Math.floor((wallX));
        
        return new RayHit(map[mapX][mapY], side, perpWallDist, mapX, mapY, wallX);
    }
    
    public class RayHit
    {
        public final int blockID, side, mapX, mapY;
        public final double distance, blockX;
        
        public RayHit(int blockID, int side, double distance, int mapX, int mapY, double blockX)
        {
            this.blockX = blockX; this.side = side; this.distance = distance; this.blockID = blockID; this.mapX = mapX; this.mapY = mapY;
        }
    }
}
