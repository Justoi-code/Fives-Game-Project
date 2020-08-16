import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Game extends JFrame implements KeyListener
{
    private int score;
    static Card[][] cards;
    static int moves;
    static Random randy = new Random();
    static Card next;
    static boolean endState;
    static ArrayList<Thread> tQueue = new ArrayList<Thread>();
    
    /*
     * poopy Colors
     * dOrange = new Color(250, 162, 117);
     * lOrange = new Color(237, 106, 90);
     * bGreen = new Color(127, 182, 133);
     */
    
    //Color City
    Color dark = new Color(128,222, 217);
    Color light = new Color(174, 236, 239);
    Color value = new Color(109, 157, 197);
    Color value2 = new Color(83, 89, 154);  
    Color value3 = new Color(6, 141, 157);
    
    //Color of two and three values
    Color twoCol = new Color(6, 141, 157);
    Color threeCol = new Color(83,89, 154);
    
    public Game()
    {
        // title of applet
        super("Fives");
        
        cards = new Card[4][4];
        // Generate the first cards; CAN MAKE RANDOM IF WANT
        cards[0][0] = new Card(3, 95, 220);
        cards[0][2] = new Card(10, 261, 220);
        cards[1][1] = new Card(5, 178, 340);
        
        // boolean that determines if score is displayed and calculated
        endState = false;
       
        // instantiates an instance of ColorPanel class
        ColorPanel canvas = new ColorPanel(Color.WHITE);
        Container win = getContentPane();
        win.add(canvas);
        
        // keeps track of moves made; NOT IMPLEMENTED IN CODE YET
        moves = 0;
        
        // generates the first "next" card
        nextGen();
        
        // sets parameters of board (size, visilbity, keylistener)
        setSize(500, 800);
        setVisible(true);
        addKeyListener(this);
    }
    
    // Allows for merging and navigation through cards array
    public static void arrayMove(int x, int y, int a, int b)
    {
        // grid size: (95, 220) to (344, 580)
        
        // if next spot is not empty, then set the card in the spot to have a 2nd value that matches the 1st value of the initial card
        if(cards[x+a][y+b] != null)
        {
            cards[x+a][y+b].setValue2(cards[x][y].value);
            cards[x][y] = null;
            System.out.println("HEY");
        }
        else // if next spot is empty,
        {
            cards[x+a][y+b] = cards[x][y];
            cards[x][y] = null;
            
            if(cards[x][y] != null && cards[x][y].getValue2() > 0)
            {
                cards[x+a][y+b].setValue2(0);
                cards[x][y].setValue(cards[x][y].value);
                cards[x][y].setValue2(0);
            }
            
            cards[x+a][y+b].setMoved(true);
        }
    }
    
    // printing the background cards array for debugging
    public static void printArray()
    {
        for(int x = 0; x < 4; x++)
        {
            for(int y = 0; y < 4; y++)
            {
                System.out.print("[");
                
                if(cards[x][y] != null)
                    System.out.print(cards[x][y].getValue() + ", " + cards[x][y].getValue2());
                
                System.out.print("]\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /*
    public static boolean checkDone()
    {
        for(int x = 0; x < cards.length; x++)
            for(int y = 0; y < cards[x].length; y++)
                if(cards[x][y] != null)
                    if(cards[x][y].getXVol() != 0 || cards[x][y].getYVol() != 0)
                        return false;
                        
        return true;
    }
    */
   
    // static method to merge all cards (1st and 2nd values) and reset move boolean
    public static void mergeSequence()
    {
        while(tQueue.size() > 0) { }
        
        for(int x = 0; x < cards.length; x++)
        {
            for(int y = 0; y < cards[x].length; y++)
            {
                if(cards[x][y] != null)
                {
                    if(cards[x][y].getValue2() > 0 && cards[x][y].merge().getValue() != 0)
                        cards[x][y] = cards[x][y].merge();
                        
                    cards[x][y].setMoved(false);
                }
            }
        }
    }
    
    // method to count amount of a certain value within the cards array
    public int countValues(int c)
    {
        int a = 0;
        
        for(int x = 0; x < cards.length; x++)
            for(int y = 0; y< cards[x].length; y++)
                if(cards[x][y] != null)
                    if(cards[x][y].getValue() == c)
                        a++;
                        
        return a;
    }
    
    // method to generate next card value
    public int nextGen()
    {
        int v = 0;
        int x = 226;
        int y = 15;
        
        if(Math.abs(countValues(2) - countValues(3)) > 1)
        {
            if(countValues(2) > countValues(3))
            {
                next = new Card(3, x, y);
                return 3;
            }
            else if(countValues(3) > countValues(2))
            {
                next = new Card(2, x, y);
                return 2;
            }
        }
        
        int r = randy.nextInt(101);
        if (r < 45)
        {
            next = new Card(2, x, y);
            v = 2;
        }
        else if (r >= 50 && r < 90)
        {
            next = new Card(3, x, y);
            v = 3;
        }
        else if (r >= 90)
        {
            r = randy.nextInt(9);
            
            if(r < 3)
            {
                next = new Card(5, x, y);
                v = 5;
            }
            else if(r >= 3 && r < 6)
            {
                next = new Card(10, x, y);
                v = 10;
            }
            else if(r >= 6)
            {
                next = new Card(20, x, y);
                v = 20;
            }
        }
        
        return v;
    }
    
    // adds card to side opposite of key input
    public void addCard(int d, int v)
    {
        // grid size: (95, 220) to (344, 580)
        
        int x;
        int y;
        
        if(d == 1)
        {
            x = 0;
            
            while(true)
            {
                // gives value 0, 1, 2, or 3
                y = randy.nextInt(4);
                
                    if(cards[y][0] == null)
                    {
                        cards[y][0] = new Card(v, 95, 220 + (120*y));
                        break;
                    }
            }
        }
        else if(d == 2)
        {
            y = 0;
            
            while(true)
            {
                // gives value 0, 1, 2, or 3
                x = randy.nextInt(4);
                
                    if(cards[3][x] == null)
                    {
                        cards[3][x] = new Card(v, 95 + (83 * x), 580);
                        break;
                    }
            }
        }
        else if(d == 3)
        {
            x = 0;
            
            while(true)
            {
                // gives value 0, 1, 2, or 3
                y = randy.nextInt(4);
                
                    if(cards[y][3] == null)
                    {
                        cards[y][3] = new Card(v, 344, 220 + (120 * y));
                        break;
                    }
            }
        }
        else if(d == 4)
        {
            y = 0;
            
            while(true)
            {
                // gives value 0, 1, 2, or 3
                x = randy.nextInt(4);
                
                    if(cards[0][x] == null)
                    {
                        cards[0][x] = new Card(v, 95 + (83 * x), 220);
                        break;
                    }
            }
        }
    }
    
    /*
    public void checkPos()
    {
        for(int x = 0; x < cards.length; x++)
            for(int y = 0; y < cards[x].length; y++)
            {
                if(cards[x][y] != null)
                    if((cards[x][y].getXPos() != (95 + (83 * x))) || (cards[x][y].getYPos() != (220 + (120 * y))))
                    {
                        cards[x][y].setXPos(95 + (83 * x));
                        cards[x][y].setYPos(220 + (120 * y));
                    }
            }
    }
    */
    
    // code that runs when end boolean is true in keyReleased method
    public void endGame()
    {
        endState = true;
        System.out.println("END");
        
    }
    
    /* ADD SCORE ADJUSTMENT CODE HERE */
    
    //
    
    public void keyPressed(KeyEvent e)
    {
        
    }
    
    public void keyReleased(KeyEvent e)
    {
        int c = e.getKeyCode();
        
        // if keys other than directional keys are released, method will return
        if(c != 38 && c != 37 && c != 39 && c!= 40)
            return; 
            
        // direction to instigate addCard method
        int direct = 0;
        
        // checks if able to move
        int end = 0;
        
        try
        {
            Thread.sleep(50);
        } catch(Exception ex) { }
        
        // grid size: (95, 220) to (344, 580)
        
        // Up Movement
            for(int x = 0; x < cards.length; x++)
            {
                for(int y = 0; y < cards[x].length; y++)
                {
                    // Checks if other threads are running and skips past empty slots in the cards array
                    if(cards[x][y] != null && cards[x][y].getXVol() == 0 && cards[x][y].getYVol() == 0)
                    {
                        // Checks boundary and whether the card has moved already
                        if(cards[x][y].getYPos() != 220 && cards[x][y].getMoved() == false)
                        {
                            // Prevents cards from ghosting through cards that don't merge with it
                            if(cards[x-1][y] == null || cards[x][y].checkMerge(cards[x-1][y]) == true) 
                            {
                                if(c == 38)
                                {
                                    cards[x][y].setYVol(-1);
                                    Thread t1 = new Thread(cards[x][y]);
                                    t1.start();
                                    //tQueue.add(t1);
                                    
                                    
                                    // Allows for merging and navigation through cards array
                                    arrayMove(x, y, -1, 0);
                                    direct = 2;
                                    end++;
                                }
                                else // adds value to end integer to show game has not yet ended; ONE FOR EACH DIRECTION
                                {
                                    end++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                // same as above comment; short circuits
                if(end > 0 && c != 38)
                    break;
            }
        
        // Left Movement
            for(int x = 0; x < cards.length; x++)
            {
                for(int y = 0; y < cards[x].length; y++)
                {
                    if(cards[x][y] != null && cards[x][y].getXVol() == 0 && cards[x][y].getYVol() == 0)
                    {
                        if(cards[x][y].getXPos() != 95 && cards[x][y].getMoved() == false)
                        {
                            if(cards[x][y-1] == null || cards[x][y].checkMerge(cards[x][y-1]) == true) 
                            {
                                if(c == 37)
                                {
                                    cards[x][y].setXVol(-1);
                                    Thread t2 = new Thread(cards[x][y]);
                                    t2.start();
                                    //tQueue.add(t2);
                                    
                                    arrayMove(x, y, 0, -1);
                                    direct = 3;
                                    end++;
                                }
                                else
                                {
                                    end++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                if(end > 0 && c != 37)
                    break;
            }
        
        // Right Movement
            for(int x = 0; x < cards.length; x++)
            {
                for(int y = cards[x].length - 1; y >= 0; y--)
                {
                    if(cards[x][y] != null && cards[x][y].getXVol() == 0 && cards[x][y].getYVol() == 0)
                    {
                        if(cards[x][y].getXPos() != 344 && cards[x][y].getMoved() == false)
                        {
                            if(cards[x][y+1] == null || cards[x][y].checkMerge(cards[x][y+1]) == true) 
                            {
                                if(c == 39)
                                {
                                    cards[x][y].setXVol(1);
                                    Thread t3 = new Thread(cards[x][y]);
                                    t3.start();
                                    //tQueue.add(t3);
                                    
                                    arrayMove(x, y, 0, 1);
                                    direct = 1;
                                    end++;
                                }
                                else
                                {
                                    end++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                if(end > 0 && c != 39)
                    break;
            }
        
        // Down Movement
            for(int x = cards.length - 1; x >= 0; x--)
            {
                for(int y = 0; y < cards[x].length; y++)
                {
                    if(cards[x][y] != null && cards[x][y].getXVol() == 0 && cards[x][y].getYVol() == 0)
                    {
                        if(cards[x][y].getYPos() != 580 && cards[x][y].getMoved() == false)
                        {
                            if(cards[x+1][y] == null || cards[x][y].checkMerge(cards[x+1][y]) == true) 
                            {
                                if(c == 40)
                                {
                                    cards[x][y].setYVol(1);
                                    Thread t4 = new Thread(cards[x][y]);
                                    t4.start();
                                    //tQueue.add(t4);
                                    
                                    arrayMove(x, y, 1, 0);
                                    direct = 4;
                                }
                                else
                                {
                                    end++;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                if(end > 0 && c != 40)
                    break;
            }
        
        try
        {
            Thread.sleep(50);
        } catch(Exception ex) { }
       
        if (end == 0)
            endGame();
       
        mergeSequence();
        
        if(direct > 0)
        {
            addCard(direct, next.getValue());
            
            // generates the next card and draws it   
            nextGen();
        }
        
        printArray();    
        
        repaint();
    }
    
    public void keyTyped(KeyEvent e)
    {
        
    }
    
    public class Card implements Runnable
    {
        private int value, value2, xPos, yPos, xVol, yVol;
        private boolean moved;
        
        public Card(int v, int x, int y)
        {
            // display value of card
            value = v;
            
            // temporary storage for value when overlap
            value2 = 0;
            
            // x and y position of card on board
            xPos = x;
            yPos = y;
            
            // velocities of cards for when using animation
            xVol = 0;
            yVol = 0;
            
            // set movement state
            moved = false;
        }
    
        public void run()
        {
            // int x = 0;
            // int y = 0;
            int startX = xPos;
            int startY = yPos;
            
            while(true)
            {
                // causes movement of card
                xPos += xVol;
                yPos += yVol;
                // x += xVol;
                // y += yVol;
                
                // delay for animation thread
                try
                {
                    Thread.sleep(1);
                } catch(InterruptedException e) {   }
                
                repaint();
                
                // end conditions for animation
                if(Math.abs(xPos - startX) >= 83 || Math.abs(yPos - startY) >= 120)
                {
                    // selects appropriate arrayMove based on direct variable
                    
                    xVol = 0;
                    yVol = 0;
                    break;
                }
            }
        }
        
        // combines first and second value of a card for merging
        public Card merge()
        {
            Card c = new Card(0, 0, 0);
            
            if(value + value2 == 5){
                c = new Card(5, xPos, yPos);
            }
            else if(value % 5 == 0 && value2 % 5 == 0 && value == value2){
                c = new Card((value + value2), xPos, yPos);
            }
            
            return c;
        }
        
        // checks if cards can merge (prevents "ghosting" between cards)
        public boolean checkMerge(Card c)
        {
            if(value + c.value == 5){
                return true;
            }
            else if(value % 5 == 0 && c.value % 5 == 0 && value == c.value){
                return true;
            }
            
            return false;
        }
        
        // modifier methods
        public void setValue(int v)
        {
            value = v;
        }
        
        public void setMoved(boolean m)
        {
            moved = m;
        }
        
        public void setXPos(int x)
        {
            xPos = x;
        }
        
        public void setYPos(int y)
        {
            yPos = y;
        }
        
        public void setXVol(int x)
        {
            xVol = x;
        }
        
        public void setYVol(int y)
        {
            yVol = y;
        }
        
        public void setValue2(int v)
        {
            value2 = v;
        }
        
        // accessor methods
        public int getValue()
        {
            return value;
        }
        
        public int getXPos()
        {
            return xPos;
        }
        
        public int getYPos()
        {
            return yPos;
        }
        
        public int getXVol()
        {
            return xVol;
        }
        
        public int getYVol()
        {
            return yVol;
        }
        
        public boolean getMoved()
        {
            return moved;
        }
        
        public int getValue2()
        {
            return value2;
        }
    }
    
    public class ColorPanel extends JPanel
    {
        public ColorPanel(Color backColor)
        {
            setBackground(backColor);
        }
        
        public void paintComponent(Graphics g)
        {
            // PARAMETERES FOR ROUND RECTANGLE:
            // graphics.fillRoundRect(xPos, yPos, height, width, arc_width, arc_height);
            
            // draws background for grid to reside in
            g.setColor(light);
            g.fillRoundRect(75, 195, 350, 500, 50, 50);
            g.setColor(dark);
            g.fillRoundRect(75, 200, 350, 500, 50, 50);
            
            // draws background grid for cards
            g.setColor(light);
            for(int x = 0; x < 4; x++)
                for(int y = 0; y < 4; y++)
                    g.fillRoundRect(95 + (83*x), 220 + (120*y), 63, 100, 20, 20);
                    //grid is from (95, 220) to (344, 580)
                
            // draws rectange for next card
            g.setColor(dark);
            g.fillRoundRect( 215, -10, 70, 125, 30, 30);
            
            // draws string for "next"
            g.setColor(value);
            g.setFont(new Font("Times New Roman", Font.BOLD, 25));
            g.drawString("next", 225, 135);
            
            // draws the cards in the grid
            for(int x = 0; x < cards.length; x++)
            {
                for(int y = 0; y < cards[x].length; y++)
                {
                    if(cards[x][y] != null)
                    {
                        if(cards[x][y].getValue() == 2)
                        {
                            cardDraw(g, cards[x][y], light, twoCol, Color.WHITE);
                        }
                        else if(cards[x][y].getValue() == 3)
                        {
                            cardDraw(g, cards[x][y], light, threeCol, Color.WHITE);
                        }
                        else
                        {
                            cardDraw(g, cards[x][y], light, Color.WHITE, value);
                        }
                    }
                }
            }
            
            // draws the "next" card
            if(next != null)
            {
                if(next.getValue() == 2)
                        {
                            nextDraw(g, next, light, twoCol, Color.WHITE);
                        }
                        else if(next.getValue() == 3)
                        {
                            nextDraw(g, next, light, threeCol, Color.WHITE);
                        }
                        else
                        {
                            nextDraw(g, next, light, Color.WHITE, value);
                        }
            }
            
            // displays score once static end boolean is set to true (WHEN THE GAME ENDS, THE SCORE WILL SHOW)
            if(endState)
            {
                g.setColor(value);
                g.setFont(new Font("Times New Roman", Font.BOLD, 50));
                g.drawString("Score:", 100, 750);
            }
        }
    }
    
    public void cardDraw(Graphics g, Card c, Color c1, Color c2, Color c3)
    {
        int w = 63;
        int h = 100;
        int m = 1;
        int count = -1;
        
        g.setColor(c1);
        g.fillRoundRect(c.getXPos() - 5, c.getYPos() + 10, w + 10, h - 5, 20, 20);
        g.setColor(c2);
        g.fillRoundRect(c.getXPos(), c.getYPos(), w, h, 20, 20);
        g.setColor(c3);
        
        while(c.getValue() / m > 0)
        {
            m *= 10;
            count++;
        }
        
        switch(count)
        {
            case 1  :   g.setFont(new Font("Times New Roman", Font.BOLD, 55));
                        break;
            case 2  :   g.setFont(new Font("Times New Roman", Font.BOLD, 35));
                        break;
            case 3  :   g.setFont(new Font("Times New Roman", Font.BOLD, 25));
                        break;
            case 4  :   g.setFont(new Font("Times New Roman", Font.BOLD, 22));
                        break;
            default :   g.setFont(new Font("Times New Roman", Font.BOLD, 75));
                        break;
        }
        
        g.drawString(c.getValue() + "", c.getXPos() + (12 / (count + 1)), c.getYPos() + 75 - (8*count));
    }
    
    public void nextDraw(Graphics g, Card c, Color c1, Color c2, Color c3)
    {
        int w = 48;
        int h = 75;
        int m = 1;
        int count = -1;
        
        g.setColor(c1);
        g.fillRoundRect(c.getXPos() - 3, c.getYPos() + 7, w + 7, h - 3, 20, 20);
        g.setColor(c2);
        g.fillRoundRect(c.getXPos(), c.getYPos(), w, h, 20, 20);
        g.setColor(c3);
        
        while(c.getValue() % m == 0)
        {
            m *= 10;
            count++;
        }
            
        g.setFont(new Font("Times New Roman", Font.BOLD, 56 - (10*count)));
        g.drawString(c.getValue() + "", c.getXPos() + (9 / (count + 1)), c.getYPos() + 56 - (5*count));
    }
    
    
    public static void main(String [] args)
    {
        Game app = new Game();
        /*
         * 
        while(true)
            if(tQueue.size() > 0)
            {
                tQueue.get(0).start();
                
                try
                {
                    Thread.sleep(1000);
                } catch(Exception ex) { }
                
                tQueue.remove(0);
            }
            */
    }
}
