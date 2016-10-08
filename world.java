import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class world here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class world extends World
{
    public final int VPw = 1620, VPh = 860; /** TRUE 16x9 */
    
    public int[][] map = new int[][]
    {
        {4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,7,7,7,7,7,7,7,7},
        {4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
        {4,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
        {4,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7},
        {4,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,7},
        {4,0,4,0,0,0,0,5,5,5,5,5,5,5,5,5,7,7,0,7,7,7,7,7},
        {4,0,5,0,0,0,0,5,0,5,0,5,0,5,0,5,7,0,0,0,7,7,7,1},
        {4,0,6,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
        {4,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,1},
        {4,0,8,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,0,0,0,8},
        {4,0,0,0,0,0,0,5,0,0,0,0,0,0,0,5,7,0,0,0,7,7,7,1},
        {4,0,0,0,0,0,0,5,5,5,5,0,5,5,5,5,7,7,7,7,7,7,7,1},
        {6,6,6,6,6,6,6,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
        {8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
        {6,6,6,6,6,6,0,6,6,6,6,0,6,6,6,6,6,6,6,6,6,6,6,6},
        {4,4,4,4,4,4,0,4,4,4,6,0,6,2,2,2,2,2,2,2,3,3,3,3},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,0,0,0,6,2,0,0,5,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
        {4,0,6,0,6,0,0,0,0,4,6,0,0,0,0,0,5,0,0,0,0,0,0,2},
        {4,0,0,5,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,2,0,2,2},
        {4,0,6,0,6,0,0,0,0,4,6,0,6,2,0,0,5,0,0,2,0,0,0,2},
        {4,0,0,0,0,0,0,0,0,4,6,0,6,2,0,0,0,0,0,2,0,0,0,2},
        {4,4,4,4,4,4,4,4,4,4,1,1,1,2,2,2,2,2,2,3,3,3,3,3}
    };
    public int mapHeight = map.length;
    public int mapWidth  = map[0].length;
    
    public double posX = 22.0, posY = 15.5;         /**X any X as the player position vector     */
    public double dirX = -1.0, dirY = 0.0;          /**Rx and Ry as the players rotation vector  */
    public double planeX = 0.0, planeY = 0.66;      /**2D camera plane vector Cp                 */
    
    public double moveSpeed = 1;
    public double rotSpeed = 1;
    
    long oldTime = 0;
    
    public world()
    {    
        // Create a new world with 600x100 cells with a cell size of 1x1 pixels.
        super(1620, 860, 1); 
        oldTime = System.nanoTime();
    }
    
    public void act()
    {
        /**** UPDATE ****/
        long curTime = System.nanoTime();
        double dTime = (curTime - oldTime); //Calculating delta T ( time since last update )
        int fps = (int)(1/dTime * 1000000000); // fpns -> fps (1E+9)
        oldTime = curTime;
        
        double calMoveSpeed = moveSpeed * dTime * 0.0000000025;
        double calRotSpeed = rotSpeed * dTime * 0.0000000025;
        
        //move forward if no wall in front of you
        if (Greenfoot.isKeyDown("up"))
        {
          if(map[(int)(posX + dirX * calMoveSpeed)][(int)posY] == 0) posX += dirX * calMoveSpeed;
          if(map[(int)posX][(int)(posY + dirY * calMoveSpeed)] == 0) posY += dirY * calMoveSpeed;
        }
        //move backwards if no wall behind you
        if (Greenfoot.isKeyDown("down"))
        {
          if(map[(int)(posX - dirX * calMoveSpeed)][(int)posY] == 0) posX -= dirX * calMoveSpeed;
          if(map[(int)posX][(int)(posY - dirY * calMoveSpeed)] == 0) posY -= dirY * calMoveSpeed;
        }
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
        
        render();
        showText("FPS/UPS: "+fps, 75, 30);
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
            double cameraX = 2 * x / (double)(VPw) - 1; /** x-coordinate in camera space */
            double rayPosX = posX;
            double rayPosY = posY;
            double rayDirX = dirX + planeX * cameraX; /** Sum of dir Vector and a part of the plane vector */
            double rayDirY = dirY + planeY * cameraX;
            
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
                if (map[mapX][mapY] > 0) hit = 1;
            } 
            
            if (side == -1) return; /** ERROR */
            
            /** Calculate distance projected on camera direction (oblique distance will give fisheye effect!) */
            if (side == 0) perpWallDist = (mapX - rayPosX + (1 - stepX) / 2) / rayDirX;
            else           perpWallDist = (mapY - rayPosY + (1 - stepY) / 2) / rayDirY;
            
            /** Calculate height of line to draw on screen */
            int lineHeight = (int)(VPh / perpWallDist);
            
            /** calculate lowest and highest pixel to fill in current stripe */
            int drawStart = -lineHeight / 2 + VPh / 2;
            if(drawStart < 0)drawStart = 0;
            int drawEnd = lineHeight / 2 + VPh / 2;
            if(drawEnd >= VPh)drawEnd = VPh - 1;
            
            Color color;
            switch(map[mapX][mapY])
            {
                case 1:  color = Color.RED;     break; //red
                case 2:  color = Color.GREEN;   break; //green
                case 3:  color = Color.BLUE;    break; //green
                case 4:  color = Color.MAGENTA; break; //green
                default: color = Color.YELLOW;  break; //yellow
            }
            //give x and y sides different brightness
            if (side == 1) {color = color.darker();}
            
            
            //draw the pixels of the stripe as a vertical line
            img.setColor(color);
            img.drawLine(x, drawStart, x, drawEnd);
        }
        
        this.setBackground(img);
    }
}
