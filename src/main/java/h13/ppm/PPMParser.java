package h13.ppm;

import h13.ppm.image.PPMHeader;
import h13.ppm.image.PPMImage;
import h13.ppm.image.PixelColor;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.io.IOException;
import java.io.InputStream;

/**
 * Parses a PPM encoded image from a given {@link InputStream}.
 */
public class PPMParser implements AutoCloseable {

    private final InputStream input;

    /**
     * Creates a new {@link PPMParser} that will read an image from the given {@link InputStream}
     *
     * @param input the {@link InputStream} to read from
     */
    @DoNotTouch
    public PPMParser(InputStream input) {
        this.input = input;
    }

    /**
     * Parses one complete image from the input.
     *
     * @return the parsed image
     * @throws IOException if an I/O error occurs
     */
    public PPMImage parse() throws IOException {
        // TODO: H13.1.1
        try{
            PPMHeader header = parseHeader();
            PixelColor[][] pixelData = parsePixelData(header.width(),  header.height());
            return new PPMImage(header, pixelData);
        }
        catch(IOException e){
            throw e;
        }
    }

    /**
     * Reads as many bytes as expected according to the image's width and height.
     *
     * @param width  the image's expected width
     * @param height the image's expected height
     * @return a 2-dimensional array containing pixel rows of the whole image
     * @throws IOException if an I/O error occurs
     */
    public PixelColor[][] parsePixelData(int width, int height) throws IOException {
        // TODO: H13.1.1
        try{
            PixelColor[][] pixelData = new PixelColor[height][width];
            for(int h = 0; h < height; h++){
                for(int w = 0; w < width; w++){
                    int r = input.read();
                    int g = input.read();
                    int b = input.read();
                    if(r == -1 || g == -1 || b == -1){
                        throw new PPMFormatException("Invalid pixel format : expected 3 bytes");
                    }
                    pixelData[h][w] = new PixelColor((byte) r, (byte) g, (byte) b);
                }
            }
            if(input.read() != -1){
                throw new PPMFormatException("Invalid pixel format : stream expected to end");
            }
            return pixelData;
        }
        catch(IOException e){
            throw new IOException("Error while reading pixel data");
        }


    }

    /**
     * Parses the header from the input and returns its relevant information.
     * Only max color values of 255 are allowed.
     *
     * @return the parsed header
     * @throws IOException if an I/O error occur
     */
    public PPMHeader parseHeader() throws IOException {
        // TODO: H13.1.1
        try{
            int p = input.read();
            int six = input.read();
            if(p !='P' || six != '6'){
                throw new PPMFormatException("Invalid header format : magic bytes different then P6");
            }
            int space = input.read();
            if(!Character.isWhitespace(space)){
                throw new PPMFormatException("Invalid header format : no white space found after magic bytes");
            }
            int width = parseWhitespaceTerminatedDecimal();
            int height = parseWhitespaceTerminatedDecimal();
            int maxValue = parseWhitespaceTerminatedDecimal();
            if(maxValue !=255){
                throw new PPMFormatException("Invalid header format : maximum value is not 255");
            }
            return new PPMHeader(width,height);

        }
        catch(IOException e){
            throw new IOException("Cannot read header");
        }



    }

    /**
     * Parses an integer encoded in ASCII decimal as per PPM spec.
     * The decimals end is detected if there is a whitespace character.
     * The terminating whitespace character is also consumed from the stream
     *
     * @return the integer value of the parsed decimal
     * @throws IOException if an I/O error occur
     */
    @DoNotTouch
    private int parseWhitespaceTerminatedDecimal() throws IOException {
        StringBuilder decimal = new StringBuilder();

        int b;
        while (true) {
            b = input.read();
            if (b >= '0' && b <= '9') {
                int digit = b - '0';
                decimal.append(digit);
            } else if (Character.isWhitespace(b)) {
                break;
            } else {
                throw new PPMFormatException("Expected digit or whitespace");
            }
        }

        if (decimal.isEmpty()) {
            throw new PPMFormatException("Expected at least one decimal digit");
        }

        return Integer.parseInt(decimal.toString());
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        input.close(); // close underlying stream
    }
}
