package org.kyrixen;


import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class UI {

    // Button
    public class Button {



    }

    // Cursor
    public class Cursor {



    }

    // Box (Dialogue window)
    public static class Box {

        // Position
        private int x, y;

        // Size
        private int w, h;

        // Textures
        private BufferedImage corner;
        private BufferedImage side;
        private BufferedImage center;

        private int tile; // Size of one tile

        // Init instance
        public void init(int x, int y, int w, int h) {

            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        
        }

        // Init textures
        public void initTexture(BufferedImage corner, BufferedImage side, BufferedImage center) {
        
            this.corner = corner;
            this.side = side;
            this.center = center;
            this.tile = corner.getWidth();
        
        }

        // Render dialog box
        public void render(Graphics2D g) {

            if (corner == null) return;

            // Corners //
            drawCorner(g, x, y, 0);                         // Top-left
            drawCorner(g, x + w - tile, y, 90);             // Top-right
            drawCorner(g, x + w - tile, y + h - tile, 180); // Bottom-right
            drawCorner(g, x, y + h - tile, 270);            // Bottom-left

            // Top and Bottom //
            for (int i = tile; i < w - tile; i += tile) {
                
                g.drawImage(side, x + i, y, tile, tile, null);            // Top
                drawRotated(g, side, x + i, y + h - tile, 180); // Bottom
            
            }

            // Left and Right //
            for (int i = tile; i < h - tile; i += tile) {
                
                drawRotated(g, side, x, y + i, 270);           // Left
                drawRotated(g, side, x + w - tile, y + i, 90); // Right
            
            }

            // Center //
            for (int ix = tile; ix < w - tile; ix += tile) {
                for (int iy = tile; iy < h - tile; iy += tile) {
                    
                    g.drawImage(center, x + ix, y + iy, tile, tile, null);
            
                }

            }

        }

        // Draw rotated corner
        private void drawCorner(Graphics2D g, int x, int y, int angle) {
            drawRotated(g, corner, x, y, angle);
        }

        // Rotate around top-left
        private void drawRotated(Graphics2D g, BufferedImage img, int x, int y, int angleDeg) {

            AffineTransform old = g.getTransform();

            g.translate(x + tile / 2.0, y + tile / 2.0);
            g.rotate(Math.toRadians(angleDeg));
            g.translate(-tile / 2.0, -tile / 2.0);

            g.drawImage(img, 0, 0, tile, tile, null);

            g.setTransform(old);
        
        }
   
    }

    // Switch (Toggle)
    public class Switch {



    }

    // Slider
    public class Slider {



    }

}
