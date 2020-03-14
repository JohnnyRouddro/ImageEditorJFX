/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageeditorjfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Luc1FeR
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    ImageView imageview;
    Image image_original;
    WritableImage image;
    
    private double red = 1;
    private double green = 1;
    private double blue = 1;
    
    private double rLow = 1;
    private double rHigh = 0;
    private double gLow = 1;
    private double gHigh = 0;
    private double bLow = 1;
    private double bHigh = 0;
    
    private double gamma = 1;
    private double brightness = 1;
    private double contrast = 0;
    private double contrastFactor = 0.5;
    private int blur = 0;
    private boolean autoContrast = false;
    
    double sum = 0;

//    private double[][] blurFilter = {
//                                    {.1,.1,.1,.1,.1,.1,.1},
//                                    {.1,.3,.3,.3,.3,.3,.1},
//                                    {.1,.3,.5,.5,.5,.3,.1},
//                                    {.1,.3,.5,1,.5,.3,.1},
//                                    {.1,.3,.5,.5,.5,.3,.1},
//                                    {.1,.3,.3,.3,.3,.3,.1},
//                                    {.1,.1,.1,.1,.1,.1,.1}
//                                    };
//    private double[][] blurFilter = {
//                                    {0, 1, 2, 3, 2, 1, 0},
//                                    {1, 2, 3, 4, 3, 2, 1},
//                                    {2, 3, 4, 5, 4, 3, 2},
//                                    {3, 4, 5, 9, 5, 4, 3},
//                                    {2, 3, 4, 5, 4, 3, 2},
//                                    {1, 2, 3, 4, 3, 2, 1},
//                                    {0, 1, 2, 3, 2, 1, 0}
//                                    };
    private double[][] blurFilter = {
                                    {0, 1, 2, 1, 0},
                                    {1, 2, 5, 2, 1},
                                    {2, 5, 9, 5, 2},
                                    {1, 2, 5, 2, 1},
                                    {0, 1, 2, 1, 0}
                                    };
//    private double[][] blurFilter = {
//                                    {0, 10, 200, 10, 0},
//                                    {10, 20, 500, 20, 10},
//                                    {200, 500, 1, 500, 200},
//                                    {10, 20, 500, 20, 10},
//                                    {0, 10, 200, 10, 0}
//                                    };
    
    @FXML
    public void loadImage(){
            red = 1;
            green = 1;
            blue = 1;

            rLow = 1;
            rHigh = 0;
            gLow = 1;
            gHigh = 0;
            bLow = 1;
            bHigh = 0;

            gamma = 1;
            brightness = 1;
            contrast = 0;
            contrastFactor = 0.5;
            blur = 0;
            autoContrast = false;
    
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Image");
            
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
              
            File file = fileChooser.showOpenDialog(null);
                       
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image_original = SwingFXUtils.toFXImage(bufferedImage, null);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageview.setImage(image);
                
                PixelReader pixelReader = image_original.getPixelReader();
        
                for(int i = 0; i < image.getWidth()-1; i++){
                    for(int j = 0; j < image.getHeight()-1; j++){
                
                    Color color = pixelReader.getColor(i, j);

                    double r = color.getRed();
                    double g = color.getGreen();
                    double b = color.getBlue();
                            
                    // CALCULATING HIGH AND LOW R, G, B VALUES
                    rLow = Math.min(rLow, r);
                    rHigh = Math.max(rHigh, r);
                    gLow = Math.min(gLow, g);
                    gHigh = Math.max(gHigh, g);
                    bLow = Math.min(bLow, b);
                    bHigh = Math.max(bHigh, b);
            }
        }
                                
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @FXML
    public void saveImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        
        File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", file);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
    }
    
    // TRANSFORMATIONS
    @FXML
    public void brighter(){
        brightness += 0.1;
        refreshImage();
    }
    @FXML
    public void darker(){
        brightness -= 0.1;
        refreshImage();
    }
    
    @FXML
    public void contrastHigher(){
        contrast += 0.1;
        autoContrast = false;
        refreshImage();
    }
    @FXML
    public void contrastLower(){
        contrast -= 0.1;
        autoContrast = false;
        refreshImage();
    }
    
    @FXML
    public void autoContrast(){
        autoContrast = true;
        refreshImage();
    }
    
    @FXML
    public void redHigher(){
        red += 0.1;
        refreshImage();
    }
    @FXML
    public void redLower(){
        red -= 0.1;
        refreshImage();
    }
    @FXML
    public void greenHigher(){
        green += 0.1;
        refreshImage();
    }
    @FXML
    public void greenLower(){
        green -= 0.1;
        refreshImage();
    }
    @FXML
    public void blueHigher(){
        blue += 0.1;
        refreshImage();
    }
    @FXML
    public void blueLower(){
        blue -= 0.1;
        refreshImage();
    }
    @FXML
    public void gammaHigher(){
        gamma += 0.2;
        refreshImage();
    }
    @FXML
    public void gammaLower(){
        gamma -= 0.2;
        refreshImage();
    }
    
    @FXML
    public void blurMore(){
        blur += 1;
        blur = Math.max(0, blur);
        refreshImage();
    }
    @FXML
    public void blurLess(){
        blur -= 1;
        blur = Math.max(0, blur);
        refreshImage();
    }
    
    // TRANSFORMATIONS END HERE
    
    
    double truncate(double value){
        if(value < 0)
            return 0;
        if (value > 1)
            return 1;
        return value;
    }
    
    private double mirror(double min, double max, double value){
        if(value < min)
            return Math.abs(value);
        if(value > max)
            return max - (value-max);
        return value;
    }
    
    public void refreshImage(){
        PixelReader pixelReader = image_original.getPixelReader();
        
        PixelWriter pixelWriter = image.getPixelWriter();
        

        for(int i = 0; i < image.getWidth()-1; i++){
            for(int j = 0; j < image.getHeight()-1; j++){
                
                Color color = pixelReader.getColor(i, j);
                
                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();
                
                // BBBBBLURRRRRRR
                if(blur > 0){
                    double sumR = 0;
                    double sumG = 0;
                    double sumB = 0;

                    for(int row = -blur; row <= blur; row++){
                        for(int col = -blur; col <= blur; col++){
                            sumR += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getRed();
                            sumG += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getGreen();
                            sumB += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getBlue();

//                            sumR += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getRed() * blurFilter[row+2][col+2] * blur;
//                            sumG += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getGreen() * blurFilter[row+2][col+2] * blur;
//                            sumB += pixelReader.getColor((int)mirror(0, image.getWidth()-1, i+col), (int)mirror(0, image.getHeight()-1, j+row)).getBlue() * blurFilter[row+2][col+2] * blur;
                        }
                    }

                    r = truncate(sumR / ((2*blur+1) * (2*blur+1)));
                    g = truncate(sumG / ((2*blur+1) * (2*blur+1)));
                    b = truncate(sumB / ((2*blur+1) * (2*blur+1)));
//                    r = truncate(sumR / sum);
//                    g = truncate(sumG / sum);
//                    b = truncate(sumB / sum);
                }
                
                if(autoContrast){
                    contrast = 0;

                    // AUTO CONTRAST
                    r = truncate(rLow + (r - rLow) / (rHigh - rLow));
                    g = truncate(rLow + (g - gLow) / (gHigh - gLow));
                    b = truncate(rLow + (b - bLow) / (bHigh - bLow));
                }
                else{
                    // CONTRAST
                    contrastFactor = (1.015 * (contrast + 1)) / (1.015 - contrast);
                    r = truncate(contrastFactor * (r - 0.1) + 0.1);
                    g = truncate(contrastFactor * (g - 0.1) + 0.1);
                    b = truncate(contrastFactor * (b - 0.1) + 0.1);
                }
                
                // BRIGHTNESS
                r = Math.min(1, r*brightness);
                g = Math.min(1, g*brightness);
                b = Math.min(1, b*brightness);
                
                // HUE CORRECTION
                r = truncate(r*red);
                g = truncate(g*green);
                b = truncate(b*blue);
                
                
                // GAMMA CORRECTION
                r = Math.pow(r, 1/gamma);
                g = Math.pow(g, 1/gamma);
                b = Math.pow(b, 1/gamma);
                
                
                
                color = new Color(r, g, b, color.getOpacity());
                
                pixelWriter.setColor(i, j, color);
                
            }
        }

    }

    private double truncateRanged(double low, double high, double value){
        if(value < low)
            return low;
        if(value > high)
            return high;
        return value;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        for(int x = 0; x < 5; x++){
//            for(int y = 0; y < 5; y++){
//                sum+= blurFilter[x][y];
//            }
//        }
//        System.out.println(sum);
    }    
    
}
