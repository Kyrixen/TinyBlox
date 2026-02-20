package org.kyrixen;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;


public class Main implements Runnable {
    
    public static Canvas canvas;
    public static JFrame frame;

    public static boolean running = false;


    // Init window
    private void init(){

        canvas = new Canvas();

        if (canvas == null) throw new IllegalStateException("Unable to initialize Canvas");

        // Setup window
        canvas.setPreferredSize( new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        canvas.setFocusable(true); // Key Input accepting
        frame = new JFrame("TinyBlox " + Constants.VERSION);

        if (frame == null) throw new RuntimeException("Failed to create window");

        frame.add(canvas); // Add the canvas to the frame
        frame.pack(); // Setup frame size

        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true); // Make it visible
        SwingUtilities.invokeLater(() -> canvas.requestFocusInWindow()); // Requests Input 

        running = true; // Starts the app

        // Handle window closing
        frame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                running = false; // Stops the app 
            }

        });

        // Key listener
        canvas.addKeyListener(new KeyAdapter() {
           
            @Override
            public void keyPressed(KeyEvent e) {
                Input.keyPressed(e);
            }
           
            @Override
            public void keyReleased(KeyEvent e) {
                Input.keyReleased(e);
            }
        
        });

        // Mouse listener
        canvas.addMouseListener(new MouseAdapter() {
        
            @Override
            public void mousePressed(MouseEvent e) {
                Input.mousePressed(e);
            }
        
            @Override
            public void mouseReleased(MouseEvent e) {
                Input.mouseReleased(e);
            }
        
        });
    
    }

    // Close app
    private void cleanup(){

        // Destroy window
        if (frame != null) frame.dispose();

        frame = null;
        canvas = null;

        System.gc(); // Help GC

    }

    // Run app
    public void run() {

        init();

        new Engine().run();

        cleanup();
    
    }

    // Main entry-point
    public static void main(String[] args){
        new Main().run();
    }

}
