package org.kyrixen;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;


public class Input {

    // Currently held keys
    private static final Set<Integer> heldKeys = new HashSet<>();
    private static final Set<Integer> heldMouse = new HashSet<>();

    // Key Handling //

    public static void keyPressed(KeyEvent e) {
        heldKeys.add(e.getKeyCode());
    }

    public static void keyReleased(KeyEvent e) {
        heldKeys.remove(e.getKeyCode());
    }

    public static boolean keyPressed(int keyCode) {
        return heldKeys.contains(keyCode);
    }

    public static boolean anyKeyPressed() {
        return !heldKeys.isEmpty();
    }

    public static boolean anyWASDPressed() {
        return keyPressed(KeyEvent.VK_W) ||
               keyPressed(KeyEvent.VK_A) ||
               keyPressed(KeyEvent.VK_S) ||
               keyPressed(KeyEvent.VK_D);
    }

    // Mouse Handling //
    
    public static void mousePressed(MouseEvent e) {
        heldMouse.add(e.getButton());
    }

    public static void mouseReleased(MouseEvent e) {
        heldMouse.remove(e.getButton());
    }

    public static boolean mousePressed(int button) {
        return heldMouse.contains(button);
    }

    public static boolean anyMousePressed() {
        return !heldMouse.isEmpty();
    }

    // Reset (clears all input - call at end of frame if needed)
    public static void reset() {
        heldKeys.clear();
        heldMouse.clear();
    }

}
