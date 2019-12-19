import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
public class main
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Simon");
        frame.setSize(400, 450);
        JPanel pane = new JPanel();
        frame.setContentPane(pane);
        pane.setLayout(null);
        
        //Canvas
        MyCanvas canvas = new MyCanvas();
        pane.add(canvas);
        pane.addMouseListener(canvas);
        canvas.setBounds(0, 0, 400, 500);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
class MyCanvas extends JPanel implements MouseListener, ActionListener
{
    Timer timer = new Timer(45, this);
    int speed = 0;
    
    boolean weh = true;
    int score = 0;
    int turn = 0;//turn 0 show answers turn 1 player picks turn 2 lose
    
    int idk = 0;
    int idkagain = 0;
    
    int selected = 0;
    int[] answer = new int[0];
    
    
    int select = 0;
    //int[] answerCheck;
    public void playAudio(int color)
    {
        try
        {
            AudioInputStream audio;
            switch (color)
            {
                case 0://c
                    audio = AudioSystem.getAudioInputStream(this.getClass().getResource("C.wav"));
                    break;
                case 1://e
                    audio = AudioSystem.getAudioInputStream(this.getClass().getResource("E.wav"));
                    break;
                case 2://g
                    audio = AudioSystem.getAudioInputStream(this.getClass().getResource("G.wav"));
                    break;
                case 3://c
                    audio = AudioSystem.getAudioInputStream(this.getClass().getResource("C2.wav"));
                    break;
                default:
                    audio = null;//AudioSystem.getAudioInputStream(this.getClass().getResource("C.wav"));
                    break;
            }
            //AudioInputStream audio = AudioSystem.getAudioInputStream(this.getClass().getResource("/Users/stephenchi/Documents/Programming/Java/Zombie boom boom/hilary duff - wake up.mp3"));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }
        catch (Exception e){}
    }
    public int random(int min, int max)
    {
        return (int)(Math.random() * (max - min + 1) + min);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        timer.start();
        
        if (weh)
        {
            answer = Arrays.copyOf(answer, answer.length + 1);
            answer[answer.length - 1] = random(0, 3);
            weh = false;
        }
        
        //simon board
        g.setColor(Color.red);//topleft
        g.fillRect(0, 0, 200, 200);
        g.setColor(Color.green);//topright
        g.fillRect(200, 0, 200, 200);
        g.setColor(Color.blue);//bottomleft
        g.fillRect(0, 200, 200, 200);
        g.setColor(Color.yellow);//bottomright
        g.fillRect(200, 200, 200, 200);
        g.setColor(Color.black);
        for (int i = 0; i < 4; i++)
        {
            g.drawRect((i % 2) * 200, (i / 2) * 200, 200, 200);
        }
        
        if (turn == 0 && idk < 19 - speed * 8 && idkagain > 30 - speed * 8)//selected tile
        {
            g.setColor(Color.white);
            g.fillRect((answer[selected] % 2) * 102 + 99, (answer[selected] / 2) * 102 + 99, 100, 100);
        }
        
        //indicates turn
        Color color;
        if (turn == 0 && idkagain > 30 - speed * 8)
        {
            color = new Color(255, 200, 200);
        }
        else
        {
            color = new Color(200, 255, 200);
        }
        g.setColor(color);
        g.fillOval(150, 150, 100, 100);//score
        g.setColor(Color.black);
        g.drawOval(150, 150, 100, 100);
        g.drawString(Integer.toString(score), 195, 205);
        
        
        
        //restart/speed options
        g.setColor(Color.gray);//selected speed
        g.fillRect(100 * speed + 100, 400, 100, 30);
        g.setColor(Color.black);
        for (int i = 0; i < 4; i++)
        {
            g.drawRect(100 * i, 400, 100, 30);
        }
        g.drawString("Restart", 30, 420);
        g.drawString("Slow", 135, 420);
        g.drawString("Medium", 225, 420);
        g.drawString("Fast", 340, 420);
        
        //lose
        if (turn == 2)
        {
            g.setColor(Color.black);
            g.fillOval(150, 150, 100, 100);
            g.setColor(Color.red);
            g.drawString("YOU LOSE", 170, 195);
            g.drawString("Score: " + Integer.toString(score), 170, 215);
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        //show answer
        idkagain++;//so sound from turn 1 dont overlap
        if (turn == 0 && idkagain > 30 - speed * 8)
        {
            //sound
            if (idk == 0)
            {
                playAudio(answer[selected]);
            }
            //flash
            idk++;
            if (idk == 20 - speed * 8)//3 frames of no white
            {
                idk = 0;
                selected++;
            }
            //if all answers shown
            if (selected == answer.length)
            {
                selected = 0;
                turn = 1;
            }
        }
        repaint();
    }
    public void mousePressed(MouseEvent e)
    {
        if (turn == 2)//restart only if lose
        {
            int x = e.getX();
            int y = e.getY();
            if (x > 0 && x < 100 && y > 400 && y < 450)
            {
                turn = 1;
                select = 0;
                selected = 0;
                int speed = 0;
                weh = true;
                score = 0;
                turn = 0;
                idk = 0;
                idkagain = 0;
                answer = new int[0];
                int select = 0;
            }
        }
        if (turn == 1)
        {
            int x = e.getX();
            int y = e.getY();
            
            
            //speeds
            for (int i = 0; i < 3; i++)
            {
                if (x > 100 + 100 * i && x < 200 + 100 * i && y > 400 && y < 450)
                {
                    speed = i;
                    break;
                }
            }
            
            
            for (int i = 0; i < 4; i++)
            {
                if (x > (i % 2) * 200 && x < (i % 2) * 200 + 200 && y > (i / 2) * 200 && y < (i / 2) * 200 + 200)
                {
                    //if correct
                    if (answer[select] == i)
                    {
                        playAudio(answer[select]);
                        select++;
                    }
                    else
                    {
                        turn = 2;
                    }
                    //if all correct
                    if (select == answer.length)
                    {
                        score++;
                        turn = 0;
                        select = 0;
                        weh = true;
                        idkagain = 0;
                    }
                }
            }
        }
        repaint();
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}