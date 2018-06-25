/**
 * Java Library for Geometric Algorithms subject
 * 
 * @author Lidia Ortega, Alejandro Graciano
 * @version 1.0
 */

package algGeom.lib3D;

import Util.Draw;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import com.sun.opengl.util.Animator;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GLEventListener;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.GLAutoDrawable;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.media.opengl.GL;
import Parser.ParserDTM;

public class Test extends Frame implements GLEventListener,
        MouseListener,
        MouseMotionListener,
        MouseWheelListener {

    public static void main(String[] args) {
        Draw.HEIGH = HEIGHT;
        Draw.WIDTH = WIDTH;
        Draw.DEPTH = 200;

        Frame frame = new Frame("Terrain DTM");
        canvas = new GLCanvas();

        canvas.addGLEventListener(new Test());
        frame.add(canvas);
        frame.setSize(HEIGHT, WIDTH);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

        frame.addKeyListener(new KeyListener() {
            long clock = 0;

            /**
             * Handle the key typed event from the text field.
             */
            @Override
            public void keyTyped(KeyEvent e) {
            }

            /**
             * Handle the key pressed event from the text field.
             */
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'E':
                    case 'e':
                        visualizeAxis = !visualizeAxis;
                        canvas.repaint();
                        clock = e.getWhen();
                        break;
                    case 'W':
                    case 'w':
                        visualizeWireframe = !visualizeWireframe;
                        canvas.repaint();
                        clock = e.getWhen();
                        break;
                    case '+':
                        cameraPosition.setX(cameraPosition.getX()+500);
                        cameraLookAt.setX(cameraLookAt.getX()+500);
                        canvas.repaint();
                        clock = e.getWhen();
                        break;
                    case '-':
                        cameraPosition.setX(cameraPosition.getX()-500);
                        cameraLookAt.setX(cameraLookAt.getX()-500);
                        canvas.repaint();
                        clock = e.getWhen();
                        break;
                    case 27: // esc
                        System.exit(0);
                }

            }

            /**
             * Handle the key released event from the text field.
             */
            @Override
            public void keyReleased(KeyEvent e) {
                clock = e.getWhen();
            }
        });

        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }
    
// ------------------------- Terrain file path to load -------------------------
    public static String TERRAINPATH = "Terrain/MDT10Terrain.sec";
//    public static String TERRAINPATH = "Terrain/MDT25Almuñecar.asc";
//    public static String TERRAINPATH = "Terrain/MDT25Ubeda.asc";
// -----------------------------------------------------------------------------
    static GLCanvas canvas;
    // So we can zoom in and out 
    private final float zoom = 0.0f;
    // rotating the scene
    private float view_rotx = 20.0f; //20
    private float view_roty = 30.0f; //30
    // remember last mouse position
    private int oldMouseX;
    private int oldMouseY;
    static int HEIGHT = 800, WIDTH = 800;

    static Vect3d cameraPosition, cameraLookAt, cameraUp;
    
    TIN dtm;
    DrawTIN dtin;
    
    // Assigment
    static boolean visualizeAxis = true;
    static boolean visualizeWireframe = false;
    
    public void initLight(GL gl) {
        gl.glPushMatrix();
        gl.glShadeModel(GL.GL_SMOOTH);

        float ambient[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float diffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float specular[] = {0.2f, 0.0f, 0.0f, 1.0f};
        float position[] = {0.0f, 80.0f, 0.0f, 0.0f};
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specular, 0);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);

        gl.glEnable(GL.GL_NORMALIZE);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glPopMatrix();

    }

    public void displayExercise(GL gl) {     
        dtin.drawObject(gl);
    }

    public void initExercise() throws Exception {
        ParserDTM.getInstance();
        dtm = new TIN(200);
        dtin = new DrawTIN(dtm);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        // Set backgroundcolor and shading mode
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnable(GL.GL_POINT_SMOOTH);
        // Set backgroundcolor and shading mode
        gl.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
        
        System.out.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println(drawable.getChosenGLCapabilities());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        cameraPosition = new Vect3d(100, 100, 100);
        cameraLookAt = new Vect3d(0, 0, 0);
        cameraUp = new Vect3d(0, 1, 0);
        
        initLight(gl);
         
        try {
            initExercise();
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        drawable.addMouseListener(this);
        drawable.addMouseMotionListener(this);
        drawable.addMouseWheelListener(this);
    }

    @Override
    public void reshape(GLAutoDrawable drawable,
            int x, int y, int width, int height) {
        WIDTH = width; // new width and height saved
        HEIGHT = height;

        GL gl = drawable.getGL();
        GLU glu = new GLU();
        if (height <= 0) {
            height = 1;
        }
        // keep ratio
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 0.1, 50000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        GLU glu = new GLU(); // needed for lookat
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        glu.gluLookAt(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(), // eye pos
                      cameraLookAt.getX(), cameraLookAt.getY(), cameraLookAt.getZ(), // look at
                      cameraUp.getX(), cameraUp.getY(), cameraUp.getZ());  // up

        gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);

        // Fill this method with the proposed exercises
        if (visualizeAxis) {
            DrawAxis ejes = new DrawAxis();
            ejes.drawObject(gl);
        }

        displayExercise(gl);
        gl.glFlush();
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable,
            boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldMouseX = e.getX();
        oldMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Dimension size = e.getComponent().getSize();
        float thetaY = 360.0f * ((float) (x - oldMouseX) / (float) size.width);
        float thetaX = 360.0f * ((float) (oldMouseY - y) / (float) size.height);
        oldMouseX = x;
        oldMouseY = y;
        view_rotx += thetaX;
        view_roty += thetaY;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getUnitsToScroll() < 0) {
            Segment3d seg = new Segment3d(cameraPosition, cameraLookAt);
            cameraPosition = seg.getPoint(zoom - ((double) e.getUnitsToScroll() / 100.0f));
        } else {
            Segment3d seg = new Segment3d(cameraPosition, cameraLookAt);
            cameraPosition = seg.getPoint(zoom - ((double) e.getUnitsToScroll() / 100.0f));
        }
    }
}
